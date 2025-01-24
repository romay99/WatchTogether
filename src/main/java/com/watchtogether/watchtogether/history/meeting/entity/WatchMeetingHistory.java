package com.watchtogether.watchtogether.history.meeting.entity;

import com.watchtogether.watchtogether.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
public class WatchMeetingHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long code;

  @Column(nullable = false)
  private LocalDateTime joinDateTime;

  private LocalDateTime cancelDateTime;

  @ManyToOne
  private Member member;

  // DB 저장될때 거래 날짜/시간 생성
  @PrePersist
  private void initTransactionDateTime() {
    this.joinDateTime = LocalDateTime.now();
  }

  /**
   * 이 Entity 가 Update 될 때 실행. 이 Entity 가 Update 되는 경우는 신청이 취소 되었을때 밖에 없기 때문에 아래 @PreUpdate 로 작업함.
   */
  @PreUpdate
  private void initCancelDateTime() {
    this.cancelDateTime = LocalDateTime.now();
  }

}
