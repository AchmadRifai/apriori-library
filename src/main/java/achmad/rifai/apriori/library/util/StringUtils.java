/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package achmad.rifai.apriori.library.util;

import achmad.rifai.apriori.library.constants.CsvContants;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author acmadrifai
 */
public class StringUtils {
    public static boolean nonBlank(String s) {
        if (Objects.isNull(s)) 
            return false;
        String s2 = buildWhitespace(' ', s);
        String s3 = buildWhitespace('-', s);
        String s4 = buildWhitespace('_', s);
        return !s.isEmpty() && 
                !s.equalsIgnoreCase(s2) && 
                !s.equalsIgnoreCase(s3) && 
                !s.equalsIgnoreCase(s4);
    }

    public static String cleanUpCsv(String s1) {
        String s = s1.startsWith("\"") && s1.endsWith("\"") ? s1.substring(1, s1.length() - 1) : s1;
        return s.replace("\\\"", "\"")
                .replace("|", ",")
                .replace("\\'", "'");
    }

    public static String combineToString(String[] s) {
        return Stream.of(s)
                .filter(Objects::nonNull)
                .map(StringUtils::escapeSpecialChar)
                .collect(Collectors.joining(CsvContants.CSV_DELIMITER));
    }

    public static String escapeSpecialChar(String s1) {
        String s = s1.replaceAll("\\R", " ");
        if (s.contains(",") || s.contains("\"") || s.contains("'")) {
            s = s.replace("\"", "\\\"").replace(',', '|').replace("'", "\\'");
            s = "\"" + s + "\"";
        }
        return s;
    }

    private static String buildWhitespace(char c, String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) 
            sb.append(c);
        return sb.toString();
    }
}
