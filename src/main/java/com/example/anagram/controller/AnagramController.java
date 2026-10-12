package com.example.anagram.controller;

import com.example.anagram.dto.WordDto;
import com.example.anagram.service.AnagramService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for CRUD operations on anagram system.
 *
 * @author Solomon Moyo
 */
@RestController
@RequestMapping("anagrams")
public class AnagramController {
    private final AnagramService anagramService;

    public AnagramController(AnagramService anagramService) {
        this.anagramService = anagramService;
    }

    @GetMapping("/anagram-for-word")
    public ResponseEntity<Map<String, Object>> getAnagramForWord(@RequestParam String word) {
        Map<String, Object> response = new HashMap<>();
        long start = System.currentTimeMillis();
        List<WordDto> anagrams = anagramService.getAnagramsForWord(word);
        long end = System.currentTimeMillis();
        response.put("data", anagrams);
        response.put("executionTimeMs", end - start);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/words")
    public ResponseEntity<Map<String, Object>> getAllWords() {
        Map<String, Object> response = new HashMap<>();
        long start = System.currentTimeMillis();
        List<WordDto> words = anagramService.getAllWords();
        long end = System.currentTimeMillis();
        response.put("data", words);
        response.put("executionTimeMs", end - start);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAnagramStats() {
        Map<String, Object> response = new HashMap<>();
        long start = System.currentTimeMillis();
        Map<Integer, Integer> stats = anagramService.getLengthToAnagramsStats();
        long end = System.currentTimeMillis();
        response.put("data", stats);
        response.put("executionTimeMs", end - start);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-word")
    public void addNewWord(@RequestParam String value) {
        anagramService.addWord(value);
    }

    @DeleteMapping("/delete-word")
    public void deleteWord(@RequestParam String value) {
        anagramService.deleteWord(value);
    }
}
