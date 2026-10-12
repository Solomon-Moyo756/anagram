package com.example.anagram.controller;

import com.example.anagram.dto.WordDto;
import com.example.anagram.service.AnagramService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AnagramController {
    private final AnagramService anagramService;

    public AnagramController(AnagramService anagramService) {
        this.anagramService = anagramService;
    }

    @GetMapping("/anagram-for-word")
    public ResponseEntity<Map<String, Object>> getAnagramForWord(@RequestParam String word) {
        log.info("getting anagram for word {}", word);
        Map<String, Object> response = new HashMap<>();
        long start = System.currentTimeMillis();
        List<WordDto> anagrams = anagramService.getAnagramsForWord(word);
        long end = System.currentTimeMillis();
        response.put("data", anagrams);
        response.put("executionTimeMs", end - start);
        log.info("returning anagram result for word: {}", word);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/words")
    public ResponseEntity<Map<String, Object>> getAllWords() {
        log.info("retrieving all words");
        Map<String, Object> response = new HashMap<>();
        long start = System.currentTimeMillis();
        List<WordDto> words = anagramService.getAllWords();
        long end = System.currentTimeMillis();
        response.put("data", words);
        response.put("executionTimeMs", end - start);
        log.info("returning all retrieved words");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getAnagramStats() {
        log.info("getting all anagram statistics (length and quantity)");
        Map<String, Object> response = new HashMap<>();
        long start = System.currentTimeMillis();
        Map<Integer, Integer> stats = anagramService.getLengthToAnagramsStats();
        long end = System.currentTimeMillis();
        response.put("data", stats);
        response.put("executionTimeMs", end - start);
        log.info("returning the anagram stats");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add-word")
    public void addNewWord(@RequestParam String value) {
        log.info("adding new word: {} to DB", value);
        //TODO: I will authenticate for adding a value
        anagramService.addWord(value);
    }

    @DeleteMapping("/delete-word")
    public void deleteWord(@RequestParam String value) {
        log.info("deleting word: {} from DB", value);
        //TODO: authenticate for deleting a value
        anagramService.deleteWord(value);
    }
}
