package com.watchtogether.watchtogether.dibs.controller;

import com.watchtogether.watchtogether.dibs.dto.DibsResponseDto;
import com.watchtogether.watchtogether.dibs.service.DibsService;
import com.watchtogether.watchtogether.movie.dto.MovieListPageDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/dibs")
@Tag(name = "찜하기", description = "영화 \"찜하기\" 관련 기능입니다.")
public class DibsController {

  private final DibsService dibsService;

  @PostMapping()
  @PreAuthorize("hasRole('ROLE_USER')")
  @Operation(summary = "찜 하기", description = "영화를 \"찜 목록\" 에 추가합니다. "
      + "상영 가능 상태인 영화는 추가할 수 없습니다.")
  public ResponseEntity<DibsResponseDto> dibsOnAMovie(@RequestParam Long movieId) {
    // SecurityContextHolder 에서 인증된 사용자의 ID 를 가져온다.
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.ok(dibsService.divsOnAMovie(movieId, memberId));
  }

  @GetMapping()
  @PreAuthorize("hasRole('ROLE_USER')")
  @Operation(summary = "내가 찜한 영화 조회", description = "본인이 찜한 영화를 조회합니다.")
  public ResponseEntity<MovieListPageDto> getDibsMovieList(
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "20") int size) {
    // SecurityContextHolder 에서 인증된 사용자의 ID 를 가져온다.
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
    MovieListPageDto dibsMovieList = dibsService.getDibsMovieList(memberId, page, size);

    return ResponseEntity.ok(dibsMovieList);
  }

  @DeleteMapping()
  @PreAuthorize("hasRole('ROLE_USER')")
  @Operation(summary = "찜 삭제하기", description = "찜 목록에서 영화를 삭제합니다",
      responses = {
          @ApiResponse(responseCode = "200", content = @Content(mediaType = "text/plain;charset=UTF-8"))})
  public ResponseEntity<String> deleteMemberDibs(@RequestParam Long movieId) {
    // SecurityContextHolder 에서 인증된 사용자의 ID 를 가져온다.
    String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
    boolean result = dibsService.deleteMemberDibs(memberId, movieId);

    return ResponseEntity.ok(result ? "정상적으로 삭제되었습니다" : "삭제가 실패하였습니다.");
  }
}
