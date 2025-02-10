package com.watchtogether.watchtogether.meeting.controller;

import com.watchtogether.watchtogether.history.meeting.dto.MeetingHistoryDto;
import com.watchtogether.watchtogether.meeting.dto.MeetingCreateDto;
import com.watchtogether.watchtogether.meeting.dto.MeetingJoinResponseDto;
import com.watchtogether.watchtogether.meeting.entity.WatchMeeting;
import com.watchtogether.watchtogether.meeting.service.MeetingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meeting")
@Tag(name = "같이볼까요?", description = "같이볼까요? 관련 기능입니다.")
@RequiredArgsConstructor
public class MeetingController {

  private final MeetingService meetingService;

  @PostMapping("/create")
  @PreAuthorize("hasRole('ROLE_USER')")
  @Operation(summary = "같이볼까요 생성", description = "해당 시간대에 같이볼까요가 존재하지 않다면 새로운 같이볼까요를 생성합니다."
      + "생성시 15000 포인트가 필요하고, 매 정시에만 생성 가능합니다.")
  public ResponseEntity<WatchMeeting> createWatchMeeting(@RequestBody MeetingCreateDto dto) {
    // SecurityContextHolder 에서 인증된 사용자의 ID 를 가져온다.
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

    WatchMeeting watchMeeting = meetingService.createWatchMeeting(memberId, dto);
    return ResponseEntity.ok(watchMeeting);
  }

  @PostMapping("/join")
  @PreAuthorize("hasRole('ROLE_USER')")
  @Operation(summary = "같이볼까요 참여", description = "같이볼까요에 참가합니다. 포인트가 15000 포인트 차감됩니다.")
  public ResponseEntity<MeetingJoinResponseDto> joinMeeting(@RequestParam Long meetingId) {
    // SecurityContextHolder 에서 인증된 사용자의 ID 를 가져온다.
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

    MeetingJoinResponseDto response = meetingService.joinMeeting(meetingId, memberId);
    return ResponseEntity.ok(response);
  }

  @GetMapping()
  @PreAuthorize("hasRole('ROLE_USER')")
  @Operation(summary = "같이볼까요 조회", description = "본인의 같이볼까요 기록을 조회합니다.")
  public ResponseEntity<List<MeetingHistoryDto>> meetingHistory(@RequestParam int page, @RequestParam int size) {
    // SecurityContextHolder 에서 인증된 사용자의 ID 를 가져온다.
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();

    List<MeetingHistoryDto> result = meetingService.meetinghistory(page, size, memberId);
    return ResponseEntity.ok().body(result);
  }

}
