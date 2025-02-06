package com.watchtogether.watchtogether.api.service;

import com.watchtogether.watchtogether.api.dto.KakaoPayApproveDto;
import com.watchtogether.watchtogether.api.dto.KakaoPayApproveResponseDto;
import com.watchtogether.watchtogether.api.dto.KakaoPayReqeustDto;
import com.watchtogether.watchtogether.api.dto.KakaoPayResponseDto;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class KakaoPayApiService {

  private final RestTemplate restTemplate;
  private final String API_KEY;
  private final String CID_KEY;
  private final String rootUri = "https://open-api.kakaopay.com";
  private final KakaoPayRedisService kakaoPayRedisService;

  // API KEY 를 application.properties 에서 가져온다.
  public KakaoPayApiService(@Value("${kakaoPay.apiKey}") String key,
      @Value("${kakaoPay.cidKey}") String cidKey, RestTemplateBuilder builder,
      KakaoPayRedisService kakaoPayRedisService) {
    this.API_KEY = key;
    this.CID_KEY = cidKey;
    this.restTemplate = builder.build();
    this.kakaoPayRedisService = kakaoPayRedisService;
  }

  /**
   * 카카오페이 결제를 위한 준비단계 메서드.
   *
   * @param memberId 사용자의 ID
   * @param amount   결제할 금액
   * @return Kakao 서버에서 보내오는 결과값
   */
  public ResponseEntity<KakaoPayResponseDto> prepareChargePoint(String memberId, int amount) {
    // 요청보내는 Uri 생성
    URI uri = UriComponentsBuilder.fromUriString(rootUri)
        .path("/online/v1/payment/ready")
        .encode().build().toUri();

    // Header 생성
    HttpHeaders headers = new HttpHeaders();
    // Header 에 API Key 설정
    headers.set("Authorization", "SECRET_KEY " + API_KEY);
    // Body 생성
    KakaoPayReqeustDto request = KakaoPayReqeustDto
        .makeRequest(CID_KEY, "1", memberId, amount);

    HttpEntity<KakaoPayReqeustDto> entity = new HttpEntity<>(request, headers);

    // 결과를 받아온 response
    ResponseEntity<KakaoPayResponseDto> response = restTemplate.postForEntity(
        uri, entity, KakaoPayResponseDto.class);

    // redis 에 응답 정보 임시저장
    kakaoPayRedisService.saveKakaoPayData(memberId, response.getBody().getTid());

    return response;
  }

  @Transactional
  public KakaoPayApproveResponseDto approvedPointCharge(String pgToken, String memberId) {
    // 요청보내는 Uri 생성
    URI uri = UriComponentsBuilder.fromUriString(rootUri)
        .path("/online/v1/payment/approve")
        .encode().build().toUri();

    // Redis 에서 이전 요청에 대한 응답정보 가져오기
    String tid = kakaoPayRedisService.getKakaoPayData(memberId);

    // TID 와 PgToken 을 담은 새로운 요청 생성
    KakaoPayApproveDto request = KakaoPayApproveDto.builder()
        .cid(CID_KEY)
        .tid(tid)
        .partner_order_id("1")
        .partner_user_id(memberId)
        .pg_token(pgToken)
        .build();

    // Header 생성
    HttpHeaders headers = new HttpHeaders();
    // Header 에 API Key 설정
    headers.set("Authorization", "SECRET_KEY " + API_KEY);

    // 마지막 요청을 담은 HttpEntity 객체 생성
    HttpEntity<KakaoPayApproveDto> entity = new HttpEntity<>(request, headers);

    // 최종 승인 응답을 받은 response 객체
    ResponseEntity<KakaoPayApproveResponseDto> response = restTemplate
        .postForEntity(uri, entity, KakaoPayApproveResponseDto.class);

    //redis 에서 데이터 삭제
    kakaoPayRedisService.deleteKakaoPayData(memberId);

    /*
    TODO : DB에 거래기록 저장하는 기능 구현 必
     */

    return response.getBody();
  }
}
