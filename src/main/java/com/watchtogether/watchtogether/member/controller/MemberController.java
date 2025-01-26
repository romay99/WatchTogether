package com.watchtogether.watchtogether.member.controller;

import com.watchtogether.watchtogether.member.dto.MemberJoinDto;
import com.watchtogether.watchtogether.member.dto.MemberLoginDto;
import com.watchtogether.watchtogether.member.entity.Member;
import com.watchtogether.watchtogether.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
public class MemberController {

  private final MemberService memberService;

  /**
   * 회원가입 메서드
   */
  @PostMapping("/join")
  public ResponseEntity<Member> joinMember(@RequestBody MemberJoinDto dto) {
    Member member = memberService.joinMember(dto);
    log.info("{}님 회원가입 완료", dto.getName());
    return ResponseEntity.ok(member);
  }

  /**
   * 로그인 하는 메서드
   */
  @PostMapping("/login")
  public ResponseEntity<String> memberLogin(@RequestBody MemberLoginDto dto) {
    String token = memberService.loginMember(dto);
    return ResponseEntity.ok().body(token);
  }
}
