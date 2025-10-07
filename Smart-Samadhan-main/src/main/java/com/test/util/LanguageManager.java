package com.test.util;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager {

    private static LanguageManager instance;
    private ResourceBundle resourceBundle;
    private String currentLanguageCode;

    private static final String BUNDLE_NAME = "com/test/strings/strings";

    private LanguageManager() {
        setLanguage("en");
    }

    public static synchronized LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public void setLanguage(String languageCode) {
        try {
            this.currentLanguageCode = languageCode;
            Locale locale = new Locale(languageCode);
            resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, locale);
        } catch (Exception e) {
            System.err.println("Cannot find resource bundle for language: " + languageCode + ". Falling back to default (English).");
            this.currentLanguageCode = "en";
            resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME, new Locale("en"));
        }
    }

    public String getString(String key) {
        try {
            return resourceBundle.getString(key);
        } catch (Exception e) {
            System.err.println("Missing translation key: '" + key + "' in bundle for language '" + currentLanguageCode + "'.");
            return key;
        }
    }

    public String getCurrentLanguageCode() {
        return currentLanguageCode;
    }
}