package com.watchtogether.watchtogether.member.service;

import com.watchtogether.watchtogether.cinema.repository.CinemaRepository;
import com.watchtogether.watchtogether.exception.custom.MemberIdAlreadyUseException;
import com.watchtogether.watchtogether.exception.custom.MemberNotFoundException;
import com.watchtogether.watchtogether.exception.custom.MemberPasswordNotMatchException;
import com.watchtogether.watchtogether.jwt.JwtProvider;
import com.watchtogether.watchtogether.member.dto.MemberInfoDto;
import com.watchtogether.watchtogether.member.dto.MemberJoinDto;
import com.watchtogether.watchtogether.member.dto.MemberLoginDto;
import com.watchtogether.watchtogether.member.dto.MemberUpdateDto;
import com.watchtogether.watchtogether.member.entity.Member;
import com.watchtogether.watchtogether.member.entity.Role;
import com.watchtogether.watchtogether.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;
  private final CinemaRepository cinemaRepository;

  /**
   * 회원가입 하는 메서드
   *
   * @param dto 회원가입을 위한 DTO
   * @return 저장된 MemberEntity 리턴
   */
  public Member joinMember(MemberJoinDto dto) {

    // 이미 사용중인 ID 라면 예외 발생
    if (memberRepository.existsByMemberId(dto.getMemberId())) {
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
        .role(dto.isPartner() ? Role.ROLE_PARTNER : Role.ROLE_USER)
        .build();

    return memberRepository.save(member);
  }

  /**
   * 로그인 하는 메서드
   *
   * @param dto 회원의 ID 와 비밀번호를 담은 DTO
   * @return 정상적으로 로그인 성공시 JWT 응답
   */
  public String loginMember(MemberLoginDto dto) {
    // ID 값으로 멤버를 찾는다. 존재하지 않는다면 예외 발생
    Member member = memberRepository.findByMemberId(dto.getUsername()).orElseThrow(
        () -> new MemberNotFoundException("존재하지 않는 회원정보 입니다.")
    );

    // 비밀번호가 일치하지 않으면 예외 발생
    if (!passwordEncoder.matches(dto.getPassword(), member.getPassword())) {
      throw new MemberPasswordNotMatchException("유효하지 않은 비밀번호 입니다.");
    }

    // 토큰 생성
    return jwtProvider.createAccessToken(member.getMemberId());
  }

  /**
   * 유저 정보 업데이트하는 메서드
   *
   * @param dto      업데이트할 정보를 담은 DTO
   * @param memberId 메서드를 호출한 멤버의 ID
   */
  public void updateMember(MemberUpdateDto dto, String memberId) {
    // 사용자가 존재 하지 않으면 예외 발생
    Member member = memberRepository.findByMemberId(memberId).orElseThrow(
        () -> new MemberNotFoundException("존재하지 않는 회원정보 입니다.")
    );

    if (dto.getPassword() != null) {
      member.setPassword(passwordEncoder.encode(dto.getPassword()));
    }
    if (dto.getEmail() != null) {
      member.setEmail(dto.getEmail());
    }
    if (dto.getName() != null) {
      member.setName(dto.getName());
    }
    if (dto.getTel() != null) {
      member.setTel(dto.getTel());
    }

    memberRepository.save(member);
  }


  /**
   * 사용자 정보 삭제하는 메서드
   *
   * @param memberId 삭제할 사용자의 ID
   */
  @Transactional
  public void deleteMember(String memberId) {
    // 사용자가 존재 하지 않으면 예외 발생
    Member member = memberRepository.findByMemberId(memberId).orElseThrow(
        () -> new MemberNotFoundException("존재하지 않는 회원정보 입니다.")
    );

    /*
    파트너 계정일 경우 계정에 연동되어 있는 극장정보가 있는지 확인,
    존재한다면 극장 데이터도 삭제한다.
     */
    if ("ROLE_PARTNER".equals(member.getRole().toString())) {
      cinemaRepository.deleteByMemberMemberId(memberId);
    }
    memberRepository.delete(member);
  }

  /**
   * 회원정보 조회하기
   * @param memberId 조회하려는 사용자 ID
   * @return 사용자 정보를 담은 DTO
   */
  public MemberInfoDto getMemberInfo(String memberId) {
    Member member = memberRepository.findByMemberId(memberId)
        .orElseThrow(() -> new MemberNotFoundException("존재하지 않는 사용자입니다."));

    return MemberInfoDto.builder()
        .memberId(member.getMemberId())
        .memberName(member.getName())
        .registerDate(member.getRegisterDate())
        .build();
  }
}
