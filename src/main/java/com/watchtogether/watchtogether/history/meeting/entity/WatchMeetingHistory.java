package com.watchtogether.watchtogether.history.meeting.entity;

import com.watchtogether.watchtogether.meeting.entity.WatchMeeting;
import com.watchtogether.watchtogether.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WatchMeetingHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long code;

  @Column(nullable = false)
  private LocalDateTime joinDateTime;

  private LocalDateTime cancelDateTime;

  @ManyToOne
  private Member member;

  @ManyToOne
  @OnDelete(action = OnDeleteAction.SET_NULL)  // 부모 삭제 시 자식의 외래 키를 NULL로 설정
  private WatchMeeting meeting;

  // DB 저장될때 거래 날짜/시간 생성
  @PrePersist
  private void initTransactionDateTime() {
    this.joinDateTime = LocalDateTime.now();
  }
}
