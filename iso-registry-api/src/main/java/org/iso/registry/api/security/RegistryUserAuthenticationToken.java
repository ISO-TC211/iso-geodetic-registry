package org.iso.registry.api.security;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class RegistryUserAuthenticationToken extends UsernamePasswordAuthenticationToken
{
	private final String displayName;

	public RegistryUserAuthenticationToken(Object principal, Object credentials, String displayName) {
		super(principal, credentials);
		
		this.displayName = displayName;
	}

	public RegistryUserAuthenticationToken(Object principal, Object credentials, String displayName,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
		
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return this.displayName;
	}
}
