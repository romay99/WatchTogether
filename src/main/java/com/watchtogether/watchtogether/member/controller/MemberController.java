package com.watchtogether.watchtogether.member.controller;

import com.watchtogether.watchtogether.member.dto.MemberJoinDto;
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
   * @param dto 회원가입을 위한 DTO
   * @return 저장 성공한 Member 객체를 return
   */
  @PostMapping("/join")
  public ResponseEntity<Member> joinMember(@RequestBody MemberJoinDto dto) {
    Member member = memberService.joinMember(dto);
    log.info("{}님 회원가입 완료", dto.getName());
    return ResponseEntity.ok(member);
  }
}
