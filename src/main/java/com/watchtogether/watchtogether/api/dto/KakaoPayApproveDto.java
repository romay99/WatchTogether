package com.watchtogether.watchtogether.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KakaoPayApproveDto {

  private String cid;
  private String tid;
  private String partner_order_id;
  private String partner_user_id;
  private String pg_token;

}
