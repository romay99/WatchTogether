package com.watchtogether.watchtogether.api.service;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoPayRedisService {

  private final StringRedisTemplate redisTemplate;

  // 데이터 저장하기
  public void saveKakaoPayData(String memberId, String tid) {
    String key = "kakao: " + memberId;
    // 1분간 redis 에 저장
    redisTemplate.opsForValue().set(key, tid, 1, TimeUnit.MINUTES);
  }

  // 데이터 가져오기
  public String getKakaoPayData(String memberId) {
    String key = "kakao: " + memberId;
    return redisTemplate.opsForValue().get(key);
  }

  // 데이터 삭제
  public void deleteKakaoPayData(String memberId) {
    String key = "kakao: " + memberId;
    redisTemplate.delete(key);
  }
}
