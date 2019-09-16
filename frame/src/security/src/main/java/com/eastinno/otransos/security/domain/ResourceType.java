package com.eastinno.otransos.security.domain;

public enum ResourceType {
    URL, METHOD, ACL, MODULE;

    public String toString() {
        if (this == URL)
            return "URL限制";
        if (this == METHOD)
            return "方法限制";
        if (this == ACL)
            return "ACL限制";
        if (this == MODULE)
            return "模块限制";
        return super.toString();
    }
}
