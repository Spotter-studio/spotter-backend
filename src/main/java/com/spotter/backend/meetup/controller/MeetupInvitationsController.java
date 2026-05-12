package com.spotter.backend.meetup.controller;

import com.spotter.backend.auth.AuthenticatedUser;
import com.spotter.backend.meetup.dto.MeetupInvitationsDTO;
import com.spotter.backend.meetup.service.MeetupInvitationsCommandService;
import com.spotter.backend.meetup.service.MeetupInvitationsQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/meetup-invitations")
@RequiredArgsConstructor
public class MeetupInvitationsController {

	private final MeetupInvitationsQueryService meetupInvitationsQueryService;
	private final MeetupInvitationsCommandService meetupInvitationsCommandService;

	@PostMapping("/{meetupId}/invitations")
	public ResponseEntity<MeetupInvitationsDTO.Response> invite(
		Authentication authentication,
		@PathVariable Long meetupId,
		@Valid @RequestBody MeetupInvitationsDTO.CreateRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(meetupInvitationsCommandService.invite(AuthenticatedUser.id(authentication), meetupId, request));
	}

	@GetMapping("/incoming")
	public ResponseEntity<List<MeetupInvitationsDTO.Response>> getIncoming(Authentication authentication) {
		return ResponseEntity.ok(meetupInvitationsQueryService.getIncoming(AuthenticatedUser.id(authentication)));
	}

	@PostMapping("/{invitationId}/accept")
	public ResponseEntity<MeetupInvitationsDTO.Response> accept(
		Authentication authentication,
		@PathVariable Long invitationId
	) {
		return ResponseEntity.ok(meetupInvitationsCommandService.accept(AuthenticatedUser.id(authentication), invitationId));
	}

	@PostMapping("/{invitationId}/reject")
	public ResponseEntity<MeetupInvitationsDTO.Response> reject(
		Authentication authentication,
		@PathVariable Long invitationId
	) {
		return ResponseEntity.ok(meetupInvitationsCommandService.reject(AuthenticatedUser.id(authentication), invitationId));
	}
}
