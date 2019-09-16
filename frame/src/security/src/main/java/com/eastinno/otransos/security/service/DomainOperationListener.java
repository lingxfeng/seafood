package com.eastinno.otransos.security.service;

import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;

import com.eastinno.otransos.security.UserContext;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.web.core.FrameworkEngine;

public class DomainOperationListener implements PostInsertEventListener, PostUpdateEventListener, PostDeleteEventListener {
    private IDomainOperationService operationService;

    @Override
    public boolean requiresPostCommitHanding(EntityPersister persister) {
        return false;
    }

    private void loadService() {
        if (this.operationService != null)
            return;
        if (FrameworkEngine.getContainer() != null)
            this.operationService = ((IDomainOperationService) FrameworkEngine.getContainer().getBean("domainOperationLogServiceService"));
    }

    public void onPostInsert(PostInsertEvent event) {
        Thread r = new Thread(new Recorder(event.getEntity(), Integer.valueOf(0), UserContext.getUser()));
        r.start();
    }

    public void onPostUpdate(PostUpdateEvent event) {
        event.getSession().beginTransaction();
        Thread r = new Thread(new Recorder(event.getEntity(), Integer.valueOf(1), UserContext.getUser()));
        r.start();
    }

    public void onPostDelete(PostDeleteEvent event) {
        Thread r = new Thread(new Recorder(event.getEntity(), Integer.valueOf(2), UserContext.getUser()));
        r.start();
    }

    private class Recorder implements Runnable {
        private User user;
        private Object entity;
        private Integer action;

        Recorder(Object entity, Integer action, User user) {
            this.entity = entity;
            this.user = user;
            this.action = action;
        }

        public void run() {
            DomainOperationListener.this.loadService();
            if (DomainOperationListener.this.operationService == null) {
                return;
            }

            DomainOperationListener.this.operationService.record(this.entity, this.action, this.user);
        }
    }
}
