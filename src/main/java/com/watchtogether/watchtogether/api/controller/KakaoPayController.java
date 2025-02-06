package com.watchtogether.watchtogether.api.controller;

import com.watchtogether.watchtogether.api.dto.KakaoPayApproveResponseDto;
import com.watchtogether.watchtogether.api.dto.KakaoPayResponseDto;
import com.watchtogether.watchtogether.api.service.KakaoPayApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/kp")
public class KakaoPayController {

  private final KakaoPayApiService kakaoPayApiService;

  /**
   * 카카오페이 결제를 위한 준비 API 입니다. TID 를 발급받습니다.
   *
   * @param amount 결제할 금액
   * @return Redirect URL , TID 를 포함한 response return
   */
  @PostMapping("/prepare")
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<?> prepareChargePoint(@RequestParam int amount) {
    // SecurityContextHolder 에서 인증된 사용자의 ID 를 가져온다.
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
    ResponseEntity<KakaoPayResponseDto> result = kakaoPayApiService
        .prepareChargePoint(memberId, amount);

    return ResponseEntity.ok(result);
  }

  /**
   * 카카오페이 요청이 성공했을때 실행되는 메서드
   *
   * @param pgToken  카카오 서버에서 발급한 pgToken
   * @param memberId 거래중인 사용자 ID
   */
  @GetMapping("/success")
  public ResponseEntity<KakaoPayApproveResponseDto> approvedPointCharge(@RequestParam("pg_token") String pgToken,
      @RequestParam("member_id") String memberId) {
    KakaoPayApproveResponseDto response = kakaoPayApiService
        .approvedPointCharge(pgToken, memberId);
    return ResponseEntity.ok(response);
  }

  /**
   * 카카오페이 거래가 취소되었을때 실행되는 메서드
   */
  @GetMapping("/cancel")
  public ResponseEntity<String> cancelChargePoint() {
    return ResponseEntity.ok().body("충전이 취소되었습니다.");
  }

  /**
   * 카카오페이 거래가 실패했을때 실행되는 메서드
   */
  @GetMapping("/fail")
  public ResponseEntity<String> failChargePoint() {
    return ResponseEntity.badRequest().body("충전에 실패했습니다.");
  }
}
