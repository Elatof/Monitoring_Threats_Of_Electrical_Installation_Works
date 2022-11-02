package com.korbiak.service.utils;

import com.korbiak.service.security.jwt.JwtUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

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

    public static String getAuthority() {
        JwtUser user = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) user.getAuthorities();
        GrantedAuthority grantedAuthority = authorities.get(0);
        return grantedAuthority.getAuthority();
    }
}
