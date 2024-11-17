package com.lobox.imdb.controller;

import com.lobox.imdb.controller.dto.TitleDTO;
import com.lobox.imdb.domain.TopRating;
import com.lobox.imdb.service.TitleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/titles")
@Tag(name = "title", description = "The Title API")
public class TitleController {

    private final TitleService titleService;

    @GetMapping
    @Operation(description = "List of the titles.")
    public ResponseEntity<List<TitleDTO>> getTitles(
            @RequestParam(value = "same_director_writer", required = false) Boolean sameDirectorTitle,
            @RequestParam(value = "alive", required = false) Boolean alive,
            @RequestParam(value = "actor", required = false) List<String> actorIds,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "start", required = false, defaultValue = "0") int start,
            @RequestParam(value = "length", required = false, defaultValue = "10") int length
    ) {
        return ResponseEntity.ok(
                titleService.getTitles(sameDirectorTitle, alive, actorIds, genre, start, length)
        );
    }

    @GetMapping("/genre/{genre}")
    @Operation(description = "Get list of the top rating by genre")
    public ResponseEntity<List<TopRating>> getByGenre(
            @PathVariable(value = "genre") String genre
    ) {
        return ResponseEntity.ok(
                titleService.getBestTitlesByGenre(genre)
        );
    }

}
