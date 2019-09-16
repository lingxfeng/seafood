package com.eastinno.otransos.core.service;

public abstract interface IScriptLoader {
    public abstract boolean isOffline();

    public abstract String loadApp(String paramString);

    public abstract void setExpired(boolean paramBoolean);
}
