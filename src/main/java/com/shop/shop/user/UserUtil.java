package com.shop.shop.user;

import com.shop.shop.user.entity.User;
import com.shop.shop.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public final class UserUtil {
    private UserUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static User getLoggedInUser(UserRepository userRepository) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            return null;
        }
        String username = userDetails.getUsername();
        return userRepository.findByEmail(username).orElse(null);
    }
}
