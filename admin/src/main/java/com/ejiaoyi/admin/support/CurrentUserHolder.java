package com.ejiaoyi.admin.support;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 当前用户
 *
 * @author Z0001
 * @since 2020-03-17
 */
public class CurrentUserHolder {

    /**
     * 通过spring security 获取当前用户信息
     *
     * @return spring security 认证用户信息
     */
    public static AuthUser getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof AuthUser) {
            return (AuthUser) principal;
        }

        return null;
    }

}
