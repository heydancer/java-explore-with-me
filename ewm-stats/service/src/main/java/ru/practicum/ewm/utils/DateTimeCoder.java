package ru.practicum.ewm.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeCoder {
    public static LocalDateTime[] decoder(String encodeStart, String encodeEnd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String decodedStart = URLDecoder.decode(encodeStart, StandardCharsets.UTF_8);
        String decodedEnd = URLDecoder.decode(encodeEnd, StandardCharsets.UTF_8);

        LocalDateTime start = LocalDateTime.parse(decodedStart, formatter);
        LocalDateTime end = LocalDateTime.parse(decodedEnd, formatter);

        return new LocalDateTime[]{start, end};
    }

    public static String[] encoder(String decodeStart, String decodeEnd) {
        String encodedStart = URLEncoder.encode(decodeStart, StandardCharsets.UTF_8);
        String encodedEnd = URLEncoder.encode(decodeEnd, StandardCharsets.UTF_8);

        return new String[]{encodedStart, encodedEnd};
    }
}
