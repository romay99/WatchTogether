package com.watchtogether.watchtogether.meeting.entity;

import com.watchtogether.watchtogether.cinema.entity.Cinema;
import com.watchtogether.watchtogether.member.entity.Member;
import com.watchtogether.watchtogether.movie.entity.Movie;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
public class WatchMeeting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long code;

  @Column(nullable = false)
  private LocalDateTime dateTime;

  @Column(nullable = false)
  private int maxPeople;

  @ManyToOne
  private Movie movie;

  @ManyToOne
  private Cinema cinema;

  @ManyToOne
  private Member member;
}
