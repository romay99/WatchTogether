package com.watchtogether.watchtogether.history.point.entity;

import com.watchtogether.watchtogether.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointTransactionHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long code;

  @Column(nullable = false)
  private LocalDateTime transactionDateTime;

  @Column(nullable = false)
  private int transactionAmount;

  @Column(nullable = false)
  private int afterTransactionBalance;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TransactionDetail transactionDetail;

  @ManyToOne
  private Member member;

  // DB 저장될때 거래 날짜/시간 생성
  @PrePersist
  private void initTransactionDateTime() {
    this.transactionDateTime = LocalDateTime.now();
  }
}
