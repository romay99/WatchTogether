package com.watchtogether.watchtogether.cinema.entity;

import com.watchtogether.watchtogether.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cinema {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long code;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private Point coordinates;

  @Column(nullable = false)
  private LocalDate registerDate;

  private String profilePhotoUrl;

  @OneToOne
  private Member member;
}
