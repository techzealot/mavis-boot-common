package com.mavis.boot.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DatetimeUtil {
  public static DateTimeFormatter standardFormatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
  public static LocalDateTime DateToLocalDateTime(Date source) {
    Instant instant = source.toInstant();
    ZoneId zone = ZoneId.systemDefault();
    return LocalDateTime.ofInstant(instant, zone);
  }

  public static Date LocalDateTimeToDate(LocalDateTime source) {
    ZoneId zone = ZoneId.systemDefault();
    Instant instant = source.atZone(zone).toInstant();
    return Date.from(instant);
  }
  public static void main(String[] args) {
    LocalDateTime queryDatetime= DatetimeUtil.DateToLocalDateTime(new Date());
    LocalDateTime startTime=queryDatetime.minusMinutes(30);
    LocalDateTime endTime=queryDatetime.plusMinutes(30);
    System.out.println(startTime.format(standardFormatter)+"==="+endTime.format(standardFormatter));
  }
}
