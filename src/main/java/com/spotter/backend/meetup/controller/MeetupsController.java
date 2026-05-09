package com.spotter.backend.meetup.controller;

import com.spotter.backend.meetup.dto.MeetupsDTO;
import com.spotter.backend.meetup.service.MeetupsCommandService;
import com.spotter.backend.meetup.service.MeetupsQueryService;
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
@RequestMapping("/api/meetups")
@RequiredArgsConstructor
public class MeetupsController {

	private final MeetupsQueryService meetupsQueryService;
	private final MeetupsCommandService meetupsCommandService;

	@PostMapping
	public ResponseEntity<MeetupsDTO.Response> create(
		Authentication authentication,
		@Valid @RequestBody MeetupsDTO.CreateRequest request
	) {
		return ResponseEntity.status(HttpStatus.CREATED).body(meetupsCommandService.create(authentication.getName(), request));
	}

	@GetMapping
	public ResponseEntity<List<MeetupsDTO.Response>> list(Authentication authentication) {
		return ResponseEntity.ok(meetupsQueryService.list(authentication.getName()));
	}

	@GetMapping("/{meetupId}")
	public ResponseEntity<MeetupsDTO.Response> get(
		Authentication authentication,
		@PathVariable Long meetupId
	) {
		return ResponseEntity.ok(meetupsQueryService.get(authentication.getName(), meetupId));
	}

	@PostMapping("/{meetupId}/join")
	public ResponseEntity<MeetupsDTO.Response> join(
		Authentication authentication,
		@PathVariable Long meetupId
	) {
		return ResponseEntity.ok(meetupsCommandService.join(authentication.getName(), meetupId));
	}

	@PostMapping("/{meetupId}/cancel")
	public ResponseEntity<Void> cancel(
		Authentication authentication,
		@PathVariable Long meetupId
	) {
		meetupsCommandService.cancel(authentication.getName(), meetupId);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{meetupId}/leave")
	public ResponseEntity<Void> leave(
		Authentication authentication,
		@PathVariable Long meetupId
	) {
		meetupsCommandService.leave(authentication.getName(), meetupId);
		return ResponseEntity.noContent().build();
	}
}
