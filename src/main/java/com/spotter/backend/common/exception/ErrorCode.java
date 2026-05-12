package com.spotter.backend.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Global (공통, G-xxx)
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G001", "예상치 못한 서버 오류입니다. 관리자에게 문의해주세요."),
    EXTERNAL_SERVER_ERROR(HttpStatus.BAD_GATEWAY, "G002", "외부 서비스와의 통신 중 오류가 발생했습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "G003", "잘못된 요청입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "G004", "잘못된 입력입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "G005", "허용되지 않은 HTTP 메소드입니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "G006", "요청하신 리소스를 찾을 수 없습니다."),

    // Auth (인증/인가, A-xxx)
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "A002", "접근 권한이 없습니다."),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A003", "이메일 또는 비밀번호가 올바르지 않습니다."),

    // User (유저, U-xxx)
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "U001", "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "U002", "이미 사용 중인 이메일입니다."),

    // Friendship (친구, F-xxx)
    FRIENDSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "F001", "친구 관계를 찾을 수 없습니다."),
    ALREADY_FRIEND(HttpStatus.CONFLICT, "F002", "이미 친구 관계입니다."),
    NOT_FRIENDS(HttpStatus.BAD_REQUEST, "F003", "친구 관계가 아닙니다."),

    // Location (장소, L-xxx)
    LOCATION_NOT_FOUND(HttpStatus.NOT_FOUND, "L001", "장소를 찾을 수 없습니다."),
    LOCATION_INVALID_INPUT(HttpStatus.BAD_REQUEST, "L002", "신규 장소 등록에 필요한 정보가 부족합니다."),

    // Meetup (밋업, M-xxx)
    MEETUP_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "밋업을 찾을 수 없습니다."),
    MEETUP_ALREADY_JOINED(HttpStatus.CONFLICT, "M002", "이미 참가한 모임입니다."),
    MEETUP_FULL(HttpStatus.CONFLICT, "M003", "모임 인원이 가득 찼습니다."),
    MEETUP_NOT_RECRUITING(HttpStatus.BAD_REQUEST, "M004", "모집 중인 모임이 아닙니다."),
    MEETUP_ACCESS_DENIED(HttpStatus.FORBIDDEN, "M005", "접근 권한이 없는 모임입니다."),
    MEETUP_HOST_CANNOT_LEAVE(HttpStatus.BAD_REQUEST, "M006", "주최자는 모임을 나갈 수 없습니다. 취소를 이용해주세요."),
    MEETUP_NOT_HOST(HttpStatus.FORBIDDEN, "M007", "모임 주최자만 수행할 수 있는 작업입니다."),
    MEETUP_NOT_PARTICIPANT(HttpStatus.BAD_REQUEST, "M008", "참가하지 않은 모임입니다."),
    INVITATION_NOT_FOUND(HttpStatus.NOT_FOUND, "M009", "초대를 찾을 수 없습니다."),
    INVITATION_ALREADY_SENT(HttpStatus.CONFLICT, "M010", "이미 초대를 보낸 사용자입니다."),

    // Category (카테고리, C-xxx)
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "C001", "카테고리를 찾을 수 없습니다."),

    // SharedPost (공유 게시글, SP-xxx)
    SHARED_POST_NOT_FOUND(HttpStatus.NOT_FOUND, "SP001", "공유 게시글을 찾을 수 없습니다."),
    SHARED_POST_FORBIDDEN(HttpStatus.FORBIDDEN, "SP002", "해당 게시글에 대한 권한이 없습니다."),
    SHARED_POST_INVALID_STATUS(HttpStatus.CONFLICT, "SP003", "게시글의 상태가 올바르지 않습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
