package com.watchtogether.watchtogether.api.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoPayApproveResponseDto {
  private String aid; // 요청 고유 번호
  private String tid; // 결제 고유 번호
  private LocalDateTime approved_at; // 결제 승인 시간
}
