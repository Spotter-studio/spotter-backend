package com.spotter.backend.meetup.controller.docs;

import com.spotter.backend.meetup.dto.MeetupInvitationsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Meetup Invitations", description = "밋업 초대 관련 API")
@SecurityRequirement(name = "bearerAuth")
public interface MeetupInvitationsControllerDocs {

	@Operation(summary = "밋업 초대")
	ResponseEntity<MeetupInvitationsDTO.Response> invite(Authentication authentication, Long meetupId, @Valid @RequestBody MeetupInvitationsDTO.CreateRequest request);

	@Operation(summary = "받은 초대 목록 조회")
	ResponseEntity<List<MeetupInvitationsDTO.Response>> getIncoming(Authentication authentication);

	@Operation(summary = "초대 수락")
	ResponseEntity<MeetupInvitationsDTO.Response> accept(Authentication authentication, Long invitationId);

	@Operation(summary = "초대 거절")
	ResponseEntity<MeetupInvitationsDTO.Response> reject(Authentication authentication, Long invitationId);
}
