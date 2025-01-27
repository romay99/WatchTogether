package com.watchtogether.watchtogether.member.service;

import com.watchtogether.watchtogether.exception.custom.MemberNotFoundException;
import com.watchtogether.watchtogether.member.entity.CustomUserDetail;
import com.watchtogether.watchtogether.member.entity.Member;
import com.watchtogether.watchtogether.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Member member = memberRepository.findByMemberId(username).orElseThrow(
        () -> new MemberNotFoundException("존재하지 않는 사용자입니다."));

    return CustomUserDetail.builder()
        .memberId(member.getMemberId())
        .role(member.getRole())
        .password(member.getPassword())
        .build();
  }
}
