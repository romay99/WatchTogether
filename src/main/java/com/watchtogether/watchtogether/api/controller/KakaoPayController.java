package com.watchtogether.watchtogether.api.controller;

import com.watchtogether.watchtogether.api.service.KakaoPayApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/kp")
public class KakaoPayController {
  private final KakaoPayApiService kakaoPayApiService;

  @GetMapping("/success")
  public ResponseEntity<?> approvedPointCharge() {
    // 요청 성공


  }



}
