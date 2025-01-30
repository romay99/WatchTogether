package com.watchtogether.watchtogether.cinema.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.watchtogether.watchtogether.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
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
  @Lob
  private String description;

  @Column(nullable = false)
  @JsonIgnore
  private Point coordinates;

  @Column(nullable = false)
  private LocalDate registerDate;

  private String profilePhotoUrl;

  @OneToOne
  private Member member;

  @JsonProperty("위도")
  public double getLongitude(){
    return this.coordinates.getX();
  }

  @JsonProperty("경도")
  public double getLatitude(){
    return this.coordinates.getY();
  }

  // DB 저장될때 날짜/시간 생성
  @PrePersist
  private void initTransactionDateTime() {
    this.registerDate = LocalDate.now();
  }
}
