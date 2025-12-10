package com.cgpi.jewelryInventory.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

public class SecurityUtil {

	private SecurityUtil() {
	}

	public static String getRole() {

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if (auth == null || !auth.isAuthenticated()) {
			return "SYSTEM";
		}

		for (GrantedAuthority authority : auth.getAuthorities()) {
			return authority.getAuthority();
		}

		return "SYSTEM";
	}
}
