/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package achmad.rifai.apriori.library.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 * @author acmadrifai
 */
public class RandomUtil {
    public static int randomInt(int min, int max) {
        Random r = new Random();
        return (r.nextInt(max - min) + 1) + min;
    }

    public static Timestamp randomTimestamp(Timestamp min, Timestamp max) {
        long waktuUtc = randomLong(min.getTime(), max.getTime());
        return Timestamp.from(Instant.ofEpochMilli(waktuUtc));
    }

    public static long randomLong(long min, long max) {
        return ThreadLocalRandom.current()
                .nextLong(min, max);
    }
}
