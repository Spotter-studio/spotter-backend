package com.spotter.backend.auth;

import com.spotter.backend.user.entity.User;
import com.spotter.backend.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";

	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		String authorization = request.getHeader(AUTHORIZATION_HEADER);
		if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}

		Long userId = jwtTokenProvider.validateAndGetUserId(authorization.substring(BEARER_PREFIX.length()));
		if (userId != null) {
			userRepository.findById(userId).ifPresent(this::setAuthentication);
		}

		filterChain.doFilter(request, response);
	}

	private void setAuthentication(User user) {
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
			user.getId(),
			null,
			AuthorityUtils.NO_AUTHORITIES
		);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
}
