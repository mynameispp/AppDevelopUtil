package com.ffzxnet.developutil.ui.unlock.code.language;

public enum LanguageType {
    CHINESE("ch"),
    ENGLISH("en");

    private String language;

    LanguageType(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language == null ? "" : language;
    }
}
