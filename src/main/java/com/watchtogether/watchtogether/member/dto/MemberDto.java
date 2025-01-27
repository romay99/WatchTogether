package com.watchtogether.watchtogether.member.dto;

import com.watchtogether.watchtogether.member.entity.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MemberDto {

  private String memberId;
  private Role role;
}
