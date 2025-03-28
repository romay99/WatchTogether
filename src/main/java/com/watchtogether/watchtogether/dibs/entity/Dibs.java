package com.watchtogether.watchtogether.dibs.entity;

import com.watchtogether.watchtogether.member.entity.Member;
import com.watchtogether.watchtogether.movie.entity.Movie;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class Dibs {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long code;

  @Column(nullable = false)
  private LocalDateTime dateTime;

  @ManyToOne
  private Movie movie;

  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  // DB 저장될때 거래 날짜/시간 생성
  @PrePersist
  private void initDibsDateTime() {
    this.dateTime = LocalDateTime.now();
  }
}
