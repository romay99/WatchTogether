package com.watchtogether.watchtogether.member.service;

import com.watchtogether.watchtogether.exception.custom.MemberIdAlreadyUseException;
import com.watchtogether.watchtogether.member.dto.MemberJoinDto;
import com.watchtogether.watchtogether.member.entity.Member;
import com.watchtogether.watchtogether.member.entity.Role;
import com.watchtogether.watchtogether.member.repository.MemberRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  public Member joinMember(MemberJoinDto dto) {

    // 이미 사용중인 ID 라면 예외 발생
    if (memberRepository.findByMemberId(dto.getMemberId()).isPresent()) {
      throw new MemberIdAlreadyUseException("이미 사용중인 ID 입니다.");
    }

    // 저장을 위한 Member 객체 초기화
    Member member = Member.builder()
        .memberId(dto.getMemberId())
        // 비밀번호 암호화
        .password(passwordEncoder.encode(dto.getPassword()))
        // 초기 포인트는 0으로 초기화
        .point(0)
        .name(dto.getName())
        .email(dto.getEmail())
        .tel(dto.getTel())
        .role(Role.ROLE_USER)
        // 가입일 초기화
        .registerDate(LocalDate.now())
        .build();

    // 파트너 가입일 땐 파트너 권한을 준다.
    if (dto.isPartner()) {
      member.setRole(Role.ROLE_PARTNER);
    }

    return memberRepository.save(member);
  }
}
