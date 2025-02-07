package com.watchtogether.watchtogether.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KakaoPayReqeustDto {

  private String cid;
  private String partner_order_id; // 가맹점 주문번호, 최대 100자
  private String partner_user_id; // 가맹점 회원 id, 최대 100자
  // (실명, ID와 같은 개인정보가 포함되지 않도록 유의)
  private String item_name; // 상품명 최대 100자
  private int quantity; // 상품 수량
  private int total_amount; // 상품 총액
  private int tax_free_amount; // 상품 비과세 금액
  private String approval_url; // 결제 성공시 url
  private String cancel_url; // 결제 취소시 url
  private String fail_url; // 결제 실패시 url

  public static KakaoPayReqeustDto makeRequest(
      String cid,
      String orderId,
      String memberId,
      int amount) {
    return KakaoPayReqeustDto.builder()
        .cid(cid)
        .partner_order_id(orderId)
        .partner_user_id(memberId)
        .item_name("포인트 충전")
        .quantity(1)
        .total_amount(amount)
        .tax_free_amount(0)
        .approval_url(
            "http://localhost:8080/kp/success?member_id=" + memberId + "&amount=" + amount)
        .cancel_url("http://localhost:8080/kp/cancel")
        .fail_url("http://localhost:8080/kp/fail")
        .build();
  }
}
