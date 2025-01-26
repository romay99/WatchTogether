package com.watchtogether.watchtogether.movie.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@ToString
public class Movie {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonProperty("id")
  private long code;

  @Column(nullable = false)
  private String title;

  @JsonProperty("overview")
  private String plot;

  @JsonProperty("poster_path")
  private String posterUrl;

  private boolean adult;

  //  @JsonProperty("release_date")
//  private LocalDate releaseDate;

  @JsonProperty("runtime")
  private int runtime;

  @Column(nullable = false)
  private boolean screenAble;
}
