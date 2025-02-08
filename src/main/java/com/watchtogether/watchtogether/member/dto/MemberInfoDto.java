package com.watchtogether.watchtogether.member.dto;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberInfoDto {
  private String memberId;
  private String memberName;
  private LocalDate registerDate;

}
