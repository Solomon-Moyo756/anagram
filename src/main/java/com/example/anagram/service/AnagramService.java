package com.example.anagram.service;

import com.example.anagram.repository.WordRepository;
import org.springframework.stereotype.Service;

/**
 * Service for handling most business logic for the CRUD operations on anagram system.
 *
 * @author Solomon Moyo
 *
 */
@Service
public class AnagramService {

    private final WordRepository wordRepository;

    public AnagramService(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }



}
