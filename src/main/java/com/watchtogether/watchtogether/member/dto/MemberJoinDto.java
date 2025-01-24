package com.watchtogether.watchtogether.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberJoinDto {

  private String memberId;
  private String password;
  private String name;
  private String email;
  private String tel;
  private boolean partner;

}
