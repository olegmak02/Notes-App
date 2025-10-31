package com.notes.app.utils;

import com.notes.app.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class Utils {
    public static String getCurrentUserId() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId().toString();
    }
}
