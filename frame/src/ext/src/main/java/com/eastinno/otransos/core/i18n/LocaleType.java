package com.eastinno.otransos.core.i18n;

public enum LocaleType {
    Locale_ZH("ZH"), Locale_EN("EN"), Locale_JP("JP");

    LocaleType(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }

}
