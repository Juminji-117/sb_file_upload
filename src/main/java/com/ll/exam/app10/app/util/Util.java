package com.ll.exam.app10.app.util;

import groovy.grape.IvyGrabRecord;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

public class Util {
    public static class date {

        // https://www.digitalocean.com/community/tutorials/java-simpledateformat-java-date-format 참고
        public static String getCurrentDateFormatted(String pattern) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.format(new Date());
        }
    }

    public static class file {

        // Baeldung 사이트 참고
        public static String getExt(String filename) {
            return Optional.ofNullable(filename)
                    .filter(f -> f.contains("."))
                    .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                    .orElse("");
        }
    }
}
