package com.watchtogether.watchtogether.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KakaoPayReqeustDto {

  private String cid;
  @JsonProperty("partner_order_id")
  private String partnerOrderId;// 가맹점 주문번호, 최대 100자
  @JsonProperty("partner_user_id")
  private String partnerUserId; // 가맹점 회원 id, 최대 100자
  // (실명, ID와 같은 개인정보가 포함되지 않도록 유의)
  @JsonProperty("item_name")
  private String itemName; // 상품명 최대 100자
  private int quantity; // 상품 수량
  @JsonProperty("total_amount")
  private int totalAmount; // 상품 총액
  @JsonProperty("tax_free_amount")
  private int taxFreeAmount; // 상품 비과세 금액
  @JsonProperty("approval_url")
  private String approvalUrl; // 결제 성공시 url
  @JsonProperty("cancel_url")
  private String cancelUrl; // 결제 취소시 url
  @JsonProperty("fail_url")
  private String failUrl; // 결제 실패시 url

  public static KakaoPayReqeustDto makeRequest(
      String cid,
      String orderId,
      String memberId,
      int amount) {
    return KakaoPayReqeustDto.builder()
        .cid(cid)
        .partnerOrderId(orderId)
        .partnerUserId(memberId)
        .itemName("포인트 충전")
        .quantity(1)
        .totalAmount(amount)
        .taxFreeAmount(0)
        .approvalUrl(
            "http://localhost:8080/kp/success?member_id=" + memberId + "&amount=" + amount)
        .cancelUrl("http://localhost:8080/kp/cancel")
        .failUrl("http://localhost:8080/kp/fail")
        .build();
  }
}
