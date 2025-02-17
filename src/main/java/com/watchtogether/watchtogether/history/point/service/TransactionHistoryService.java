package com.watchtogether.watchtogether.history.point.service;

import com.watchtogether.watchtogether.history.point.entity.PointTransactionHistory;
import com.watchtogether.watchtogether.history.point.entity.TransactionDetail;
import com.watchtogether.watchtogether.history.point.repository.TransactionHistoryRepository;
import com.watchtogether.watchtogether.member.entity.Member;
import com.watchtogether.watchtogether.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionHistoryService {

  private final TransactionHistoryRepository transactionHistoryRepository;
  private final MemberRepository memberRepository;

  /**
   * 포인트 사용하는 메서드 이 메서드를 호출함으로써 포인트 차감 / 증가가 된다.
   *
   * @param member 사용하려는 Member
   * @param amount 사용하려는 포인트 양 (양수는 충전 , 음수는 사용)
   * @param detail 사용하는 사용처
   */
  @Transactional
  public void usePoint(Member member, int amount, TransactionDetail detail) {
    // 거래정보를 담은 history 데이터 생성
    PointTransactionHistory history = PointTransactionHistory.builder()
        .transactionAmount(amount)
        .afterTransactionBalance(member.getPoint() + amount)
        .transactionDetail(detail)
        .member(member)
        .build();

    // history 저장
    PointTransactionHistory savedHistory = transactionHistoryRepository.save(history);
    // 사용자의 포인트 업데이트
    memberRepository.updateMemberPoint(member.getMemberId(), savedHistory.getAfterTransactionBalance());
  }
}
