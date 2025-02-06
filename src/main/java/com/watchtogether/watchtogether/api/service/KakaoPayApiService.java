package com.watchtogether.watchtogether.api.service;

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
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class KakaoPayApiService {

  private final RestTemplate restTemplate;
  private final String API_KEY;
  private final String CID_KEY;
  private final String rootUri = "https://open-api.kakaopay.com";

  // API KEY 를 application.properties 에서 가져온다.
  public KakaoPayApiService(@Value("${kakaoPay.apiKey}") String key,
      @Value("${kakaoPay.cidKey}") String cidKey, RestTemplateBuilder builder) {
    this.API_KEY = key;
    this.CID_KEY = cidKey;
    this.restTemplate = builder.build();
  }

  public ResponseEntity<?> chargePoint(String memberId, int amount) {
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

    HttpEntity<KakaoPayReqeustDto > entity = new HttpEntity<>(request, headers);

    ResponseEntity<KakaoPayResponseDto> kakaoPayReqeustDtoResponseEntity = restTemplate.postForEntity(
        uri, request, KakaoPayResponseDto.class);
  }


}
