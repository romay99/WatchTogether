package com.watchtogether.watchtogether.member.controller;

import com.watchtogether.watchtogether.member.dto.MemberJoinDto;
import com.watchtogether.watchtogether.member.dto.MemberLoginDto;
import com.watchtogether.watchtogether.member.dto.MemberUpdateDto;
import com.watchtogether.watchtogether.member.entity.Member;
import com.watchtogether.watchtogether.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
@Tag(name = "사용자", description = "사용자 관련 API 입니다.")
public class MemberController {

  private final MemberService memberService;

  /**
   * 회원가입 메서드
   */
  @PostMapping("/join")
  @Operation(summary = "회원가입", description = "회원가입하는 기능입니다. "
      + "일반유저와 파트너유저 두가지 권한이 존재합니다.")
  public ResponseEntity<Member> joinMember(@RequestBody MemberJoinDto dto) {
    Member member = memberService.joinMember(dto);
    log.info("{}님 회원가입 완료", dto.getName());
    return ResponseEntity.ok(member);
  }

  /**
   * 로그인 하는 메서드
   */
  @PostMapping("/login")
  @Operation(summary = "로그인", description = "로그인 기능입니다. 로그인 성공시 JWT 를 return 합니다.",
      responses = {
          @ApiResponse(responseCode = "200", content = @Content(mediaType = "text/plain;charset=UTF-8"))})
  public ResponseEntity<String> memberLogin(@RequestBody MemberLoginDto dto) {
    String token = memberService.loginMember(dto);
    log.info("{}님 로그인 완료", dto.getUsername());
    return ResponseEntity.ok().body(token);
  }

  /**
   * 회원 정보 수정하는 메서드
   *
   * @param dto 수정할 회원정보가 담긴 DTO
   */
  @PutMapping()
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_PARTNER')")
  @Operation(summary = "회원 정보 수정", description = "회원정보 수정기능입니다.", responses = {
      @ApiResponse(responseCode = "200", content = @Content(mediaType = "text/plain;charset=UTF-8"))})
  public ResponseEntity<String> updateMember(@RequestBody MemberUpdateDto dto) {
    // SecurityContextHolder 에서 인증된 사용자의 ID 를 가져온다.
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
    memberService.updateMember(dto, memberId);
    return ResponseEntity.ok("회원정보가 수정되었습니다.");
  }

  /**
   * 회원 탈퇴하는 메서드
   */
  @DeleteMapping()
  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_PARTNER')")
  @Operation(summary = "회원탈퇴", description = "회원탈퇴 기능입니다.", responses = {
      @ApiResponse(responseCode = "200", content = @Content(mediaType = "text/plain;charset=UTF-8"))})
  public ResponseEntity<String> deleteMember() {
    // SecurityContextHolder 에서 인증된 사용자의 ID 를 가져온다.
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
    memberService.deleteMember(memberId);
    log.info("{}님의 정보가 삭제되었습니다.", memberId);
    return ResponseEntity.ok("회원탈퇴가 완료되었습니다.");
  }
}
