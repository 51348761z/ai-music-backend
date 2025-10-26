package org.demo.aimusic.common.enums;

/**
 * Compile-time date/time format pattern constants for use in annotations like
 * {@code @JsonFormat(pattern = DatePattern.YYYY_MM_DD_HH_MM_SS)}.
 *
 * This was an enum previously, but since annotation attributes require
 * compile-time constants, and you said the patterns are only used in
 * annotations, a constants-only final class is simpler and avoids confusion.
 */
public final class DatePattern {

  private DatePattern() { /* prevent instantiation */ }

  public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
  public static final String YYYY_MM_DD = "yyyy-MM-dd";
  public static final String HH_MM_SS = "HH:mm:ss";
  public static final String ISO_DATETIME_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

}
