package com.example.security.config;

import java.util.ResourceBundle;

import com.example.common.util.UTF8Control;

public class Messages {
  private static final String BUNDLE_NAME = "i18n.security-messages";

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, new UTF8Control());

  private Messages() {
  }
  
  public static String getString(String key, String defaultMessage) {
    try {
      if (RESOURCE_BUNDLE.containsKey(key)) {
        return RESOURCE_BUNDLE.getString(key);
      }
    } catch (Exception e) {
    }
    if (defaultMessage!= null) {
      return defaultMessage;
    }
    return '!' + key + '!';
  }

  public static String getString(String key) {
    try {
      if (RESOURCE_BUNDLE.containsKey(key)) {
        return RESOURCE_BUNDLE.getString(key);
      }
    } catch (Exception e) {
    }
    return '!' + key + '!';
  }
}
