package com.vellkare.core

/**
 * Created by roopesh on 15/02/16.
 */
public enum Gender {
  MALE , FEMALE;
  public static Gender fromString(String text) {
    if (text != null) {
      for (Gender b : values()) {
        if (text.equalsIgnoreCase(b.name())) {
          return b;
        }
      }
    }
    return null;
  }
}
