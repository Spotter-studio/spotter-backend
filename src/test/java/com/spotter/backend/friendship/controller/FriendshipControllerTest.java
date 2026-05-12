package com.spotter.backend.friendship.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotter.backend.auth.JwtAuthenticationFilter;
import com.spotter.backend.auth.SecurityConfig;
import com.spotter.backend.common.enums.FriendshipStatus;
import com.spotter.backend.common.exception.BusinessException;
import com.spotter.backend.common.exception.ErrorCode;
import com.spotter.backend.friendship.dto.FriendshipDTO;
import com.spotter.backend.friendship.service.FriendshipService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.securityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
	value = FriendshipController.class,
	excludeAutoConfiguration = SecurityAutoConfiguration.class,
	excludeFilters = @Filter(
		type = FilterType.ASSIGNABLE_TYPE,
		classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
	)
)
class FriendshipControllerTest {

	@TestConfiguration
	@EnableWebSecurity
	static class TestSecurityConfig {
		@Bean
		public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
			return http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
				.build();
		}
	}

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private FriendshipService friendshipService;

	private static RequestPostProcessor auth(Long userId) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(new UsernamePasswordAuthenticationToken(userId, null, List.of()));
		return securityContext(context);
	}

	private FriendshipDTO.Response pendingResponse() {
		return new FriendshipDTO.Response(1L, 1L, 2L, FriendshipStatus.PENDING, LocalDateTime.now(), null);
	}

	@Test
	@DisplayName("POST /api/friends/requests - 친구 요청 성공 시 201을 반환한다")
	void sendRequest_success() throws Exception {
		when(friendshipService.sendRequest(1L, 2L)).thenReturn(pendingResponse());

		mockMvc.perform(post("/api/friends/requests")
				.with(auth(1L))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(Map.of("friendId", 2))))
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.result").value("SUCCESS"))
			.andExpect(jsonPath("$.data.status").value("PENDING"));
	}

	@Test
	@DisplayName("POST /api/friends/requests - friendId 없이 요청하면 400을 반환한다")
	void sendRequest_missingFriendId() throws Exception {
		mockMvc.perform(post("/api/friends/requests")
				.with(auth(1L))
				.contentType(MediaType.APPLICATION_JSON)
				.content("{}"))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("POST /api/friends/requests - 자기 자신에게 요청하면 400을 반환한다")
	void sendRequest_selfRequest() throws Exception {
		when(friendshipService.sendRequest(1L, 1L))
			.thenThrow(new BusinessException(ErrorCode.SELF_FRIEND_REQUEST));

		mockMvc.perform(post("/api/friends/requests")
				.with(auth(1L))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(Map.of("friendId", 1))))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.code").value("F003"));
	}

	@Test
	@DisplayName("POST /api/friends/requests - 이미 친구 관계이면 409를 반환한다")
	void sendRequest_alreadyFriend() throws Exception {
		when(friendshipService.sendRequest(1L, 2L))
			.thenThrow(new BusinessException(ErrorCode.ALREADY_FRIEND));

		mockMvc.perform(post("/api/friends/requests")
				.with(auth(1L))
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(Map.of("friendId", 2))))
			.andExpect(status().isConflict())
			.andExpect(jsonPath("$.code").value("F005"));
	}

	@Test
	@DisplayName("GET /api/friends/requests/incoming - 받은 요청 목록을 반환한다")
	void getIncomingRequests_success() throws Exception {
		when(friendshipService.getIncomingRequests(2L)).thenReturn(List.of(pendingResponse()));

		mockMvc.perform(get("/api/friends/requests/incoming")
				.with(auth(2L)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.result").value("SUCCESS"))
			.andExpect(jsonPath("$.data.length()").value(1));
	}

	@Test
	@DisplayName("POST /api/friends/requests/{requestId}/accept - 수락 성공 시 200을 반환한다")
	void acceptRequest_success() throws Exception {
		FriendshipDTO.Response accepted = new FriendshipDTO.Response(
			1L, 1L, 2L, FriendshipStatus.ACCEPTED, LocalDateTime.now(), LocalDateTime.now());
		when(friendshipService.acceptRequest(2L, 1L)).thenReturn(accepted);

		mockMvc.perform(post("/api/friends/requests/1/accept")
				.with(auth(2L)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.status").value("ACCEPTED"));
	}

	@Test
	@DisplayName("POST /api/friends/requests/{requestId}/accept - 요청을 받은 사람이 아니면 403을 반환한다")
	void acceptRequest_forbidden() throws Exception {
		when(friendshipService.acceptRequest(3L, 1L))
			.thenThrow(new BusinessException(ErrorCode.FORBIDDEN));

		mockMvc.perform(post("/api/friends/requests/1/accept")
				.with(auth(3L)))
			.andExpect(status().isForbidden())
			.andExpect(jsonPath("$.code").value("A002"));
	}

	@Test
	@DisplayName("POST /api/friends/requests/{requestId}/accept - 존재하지 않는 요청이면 404를 반환한다")
	void acceptRequest_notFound() throws Exception {
		when(friendshipService.acceptRequest(2L, 99L))
			.thenThrow(new BusinessException(ErrorCode.FRIEND_REQUEST_NOT_FOUND));

		mockMvc.perform(post("/api/friends/requests/99/accept")
				.with(auth(2L)))
			.andExpect(status().isNotFound())
			.andExpect(jsonPath("$.code").value("F001"));
	}

	@Test
	@DisplayName("POST /api/friends/requests/{requestId}/reject - 거절 성공 시 200을 반환한다")
	void rejectRequest_success() throws Exception {
		FriendshipDTO.Response rejected = new FriendshipDTO.Response(
			1L, 1L, 2L, FriendshipStatus.REJECTED, LocalDateTime.now(), null);
		when(friendshipService.rejectRequest(2L, 1L)).thenReturn(rejected);

		mockMvc.perform(post("/api/friends/requests/1/reject")
				.with(auth(2L)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.status").value("REJECTED"));
	}

	@Test
	@DisplayName("GET /api/friends - 친구 목록을 반환한다")
	void getFriends_success() throws Exception {
		FriendshipDTO.FriendResponse friend = new FriendshipDTO.FriendResponse(
			1L, 2L, "Bob", LocalDateTime.now());
		when(friendshipService.getFriends(1L)).thenReturn(List.of(friend));

		mockMvc.perform(get("/api/friends")
				.with(auth(1L)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data[0].friendName").value("Bob"))
			.andExpect(jsonPath("$.data[0].friendId").value(2));
	}
}