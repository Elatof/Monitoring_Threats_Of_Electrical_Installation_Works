package com.korbiak.service.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ServiceUtils {
    public static Timestamp calculateTargetTime(List<Timestamp> allDateTimes, int jumpTime) {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        List<Timestamp> sorted = allDateTimes.stream().sorted(Timestamp::compareTo).collect(Collectors.toList());
        if (sorted.isEmpty()) {
            return null;
        }
        Timestamp closest = Collections.min(sorted, (d1, d2) -> {
            long diff1 = Math.abs(d1.getTime() - currentTime.getTime());
            long diff2 = Math.abs(d2.getTime() - currentTime.getTime());
            return Long.compare(diff1, diff2);
        });
        int index = sorted.indexOf(closest);
        try {
            return sorted.get(index + jumpTime);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }
}
