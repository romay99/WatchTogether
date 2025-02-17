package com.watchtogether.watchtogether.api.controller;

import com.watchtogether.watchtogether.api.dto.KakaoPayApproveResponseDto;
import com.watchtogether.watchtogether.api.dto.KakaoPayResponseDto;
import com.watchtogether.watchtogether.api.service.KakaoPayApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "카카오페이 API",description = "카카오페이 결제 API 관련 기능입니다.")
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
  @Operation(summary = "카카오페이 결제 준비", description = "카카오페이를 이용한 결제 준비단계 입니다. 사용자 인증을 거쳐"
      + "인증 성공시 /kb/pay 로 redirect 됩니다.")
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
  @Operation(summary = "카카오페이 결제 승인", description = " 결제 준비 API 에서 redirect 로 넘어온 데이터로 결제를 처리합니다.")
  public ResponseEntity<KakaoPayApproveResponseDto> approvedPointCharge(
      @RequestParam("pg_token") String pgToken,
      @RequestParam("member_id") String memberId, @RequestParam int amount) {
    // api 요청을 보낸 후 받은 응답
    KakaoPayApproveResponseDto response = kakaoPayApiService
        .approvedPointCharge(pgToken, memberId);
    // api 요청이 정상적으로 끝났다면 DB 에 저장
    kakaoPayApiService.saveTransactionDataIntoDB(memberId, amount);
    return ResponseEntity.ok(response);
  }

  /**
   * 카카오페이 거래가 취소되었을때 실행되는 메서드
   */
  @GetMapping("/cancel")
  @Operation(summary = "카카오페이 결제 취소", description = "카카오페이 결제 준비중 취소되면 실행되는 메서드입니다.")
  public ResponseEntity<String> cancelChargePoint() {
    return ResponseEntity.ok().body("충전이 취소되었습니다.");
  }

  /**
   * 카카오페이 거래가 실패했을때 실행되는 메서드
   */
  @GetMapping("/fail")
  @Operation(summary = "카카오페이 결제 실패", description = "카카오페이 결제가 실패하면 실행되는 메서드입니다.")
  public ResponseEntity<String> failChargePoint() {
    return ResponseEntity.badRequest().body("충전에 실패했습니다.");
  }
}
