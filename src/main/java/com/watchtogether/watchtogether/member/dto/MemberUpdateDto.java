package com.watchtogether.watchtogether.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUpdateDto {

  private String password;
  private String email;
  private String name;
  private String tel;
}
