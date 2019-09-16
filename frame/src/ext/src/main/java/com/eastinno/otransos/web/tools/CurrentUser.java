package com.eastinno.otransos.web.tools;

import java.security.Principal;

public class CurrentUser {
    private static ThreadLocal currentUser = new CurrentUserThreadLocal();

    public static void setCurrnetUser(Principal user) {
        currentUser.set(user);
    }

    public static Principal getCurrentUser() {
        Principal user = (Principal) currentUser.get();
        return user;
    }

    private static class CurrentUserThreadLocal extends ThreadLocal {
        protected synchronized Object initialValue() {
            return null;
        }
    }
}
