package com.example.anagram.controller;

import com.example.anagram.dto.WordDto;
import com.example.anagram.service.AnagramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for CRUD operations on anagram system.
 *
 * @author Solomon Moyo
 */
@RestController
@RequestMapping("api/")
public class AnagramController {
    private final AnagramService anagramService;

    public AnagramController(AnagramService anagramService) {
        this.anagramService = anagramService;
    }

    @GetMapping("words")
    public ResponseEntity<List<WordDto>> getAllWords() {
        List<WordDto> words = anagramService.getAllWords();

        return ResponseEntity.ok(words);
    }

    @GetMapping("anagrams")
    public ResponseEntity<List<WordDto>> getAnagramForWord(@RequestParam String word){
        return ResponseEntity.ok(anagramService.getAnagramsForWord(word));
    }
}
