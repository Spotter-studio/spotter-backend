package com.spotter.backend.meetup.controller.docs;

import com.spotter.backend.common.response.ApiResponse;
import com.spotter.backend.meetup.dto.MeetupsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Meetups", description = "밋업 관련 API")
@SecurityRequirement(name = "bearerAuth")
public interface MeetupsControllerDocs {

	@Operation(summary = "밋업 생성")
	ResponseEntity<ApiResponse<MeetupsDTO.Response>> create(Authentication authentication, @Valid @RequestBody MeetupsDTO.CreateRequest request);

	@Operation(summary = "밋업 목록 조회")
	ResponseEntity<ApiResponse<List<MeetupsDTO.Response>>> list(Authentication authentication);

	@Operation(summary = "밋업 상세 조회")
	ResponseEntity<ApiResponse<MeetupsDTO.Response>> get(Authentication authentication, Long meetupId);

	@Operation(summary = "밋업 참여")
	ResponseEntity<ApiResponse<MeetupsDTO.Response>> join(Authentication authentication, Long meetupId);

	@Operation(summary = "밋업 취소")
	ResponseEntity<Void> cancel(Authentication authentication, Long meetupId);

	@Operation(summary = "밋업 나가기")
	ResponseEntity<Void> leave(Authentication authentication, Long meetupId);
}
