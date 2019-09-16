package com.eastinno.otransos.core.service.impl;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.eastinno.otransos.core.dao.IPersonalityDAO;
import com.eastinno.otransos.core.domain.Personality;
import com.eastinno.otransos.core.service.IPersonalityService;
import com.eastinno.otransos.core.support.query.IQueryObject;
import com.eastinno.otransos.core.support.query.QueryUtil;
import com.eastinno.otransos.security.domain.User;
import com.eastinno.otransos.web.tools.IPageList;

@Service
public class PersonalityServiceImpl implements IPersonalityService {
    @Resource
    private IPersonalityDAO personalityDao;
    private String defaultPortals = "id:currentUser,col:0,row:0@@id:announce,col:0,row:1@@id:workItem,col:1,row:0@@id:news,col:1,row:1@@id:links,col:2,row:0@@id:plan,col:2,row:1";

    public void setPersonalityDao(IPersonalityDAO personalityDao) {
        this.personalityDao = personalityDao;
    }

    public void setDefaultPortals(String defaultPortals) {
        this.defaultPortals = defaultPortals;
    }

    public Long addPersonality(Personality personality) {
        this.personalityDao.save(personality);
        if ((personality != null) && (personality.getId() != null)) {
            return personality.getId();
        }
        return null;
    }

    public Personality getPersonality(Long id) {
        Personality personality = (Personality) this.personalityDao.get(id);
        return personality;
    }

    public boolean delPersonality(Long id) {
        Personality personality = getPersonality(id);
        if (personality != null) {
            this.personalityDao.remove(id);
            return true;
        }
        return false;
    }

    public boolean batchDelPersonalitys(List<Serializable> personalityIds) {
        for (Serializable id : personalityIds) {
            delPersonality((Long) id);
        }
        return true;
    }

    public IPageList getPersonalityBy(IQueryObject queryObject) {
        return QueryUtil.query(queryObject, Personality.class, this.personalityDao);
    }

    public boolean updatePersonality(Long id, Personality personality) {
        if (personality.getUser() == null) {
            return false;
        }
        Personality p = getPersonality(personality.getUser());
        if (p != null) {
            p.setIframe(personality.getIframe());
            p.setMaxTabs(personality.getMaxTabs());
            p.setSingleTabMode(personality.getSingleTabMode());
            p.setCommonFunctions(personality.getCommonFunctions());
            p.setBackgroundImg(personality.getBackgroundImg());
            p.setLang(personality.getLang());
            p.setViewMode(personality.getViewMode());
            if (StringUtils.hasLength(personality.getPortals())) {
                p.setPortalConfig(personality.getPortalConfig());
            }
            p.setEnableAnimate(personality.getEnableAnimate());
            p.setHomePage(personality.getHomePage());
            p.setStyle(personality.getStyle());
            this.personalityDao.update(p);
        } else {
            p = personality;
            addPersonality(p);
        }
        return true;
    }

    public Personality getPersonality(User user) {
        Personality obj = (Personality) this.personalityDao.getBy("user", user);
        if ((obj == null) && (user != null) && (user.getId().longValue() > 0L)) {
            obj = new Personality();
            obj.setUser(user);
            addPersonality(obj);
        }
        return obj;
    }

    public Personality getDefaultPersonality() {
        Personality p = new Personality();
        p.setPortals(this.defaultPortals);
        return p;
    }
}
