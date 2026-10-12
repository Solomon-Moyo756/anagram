package com.example.anagram.service;

import com.example.anagram.components.DictionaryDataLoader;
import com.example.anagram.dto.WordDto;
import com.example.anagram.entity.Word;
import com.example.anagram.repository.WordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for handling most business logic for the CRUD operations on anagram system.
 *
 * @author Solomon Moyo
 *
 */
@Service
@Slf4j
public class AnagramService {

    private final WordRepository wordRepository;
    private final DictionaryDataLoader dictionaryDataLoader;

    public AnagramService(WordRepository wordRepository, DictionaryDataLoader dictionaryDataLoader) {
        this.wordRepository = wordRepository;
        this.dictionaryDataLoader = dictionaryDataLoader;
    }

    public List<WordDto> getAllWords() {
        return wordRepository.findAll().stream().map(word -> new WordDto(word.getValue())).toList();
    }

    public void addWord(String value) {
        Word newWord = new Word();
        newWord.setValue(value);
        wordRepository.save(newWord);
    }

    public void deleteWord(String value) {
        Optional<Word> wordToDElete = wordRepository.findByValue(value);
        if (wordToDElete.isEmpty()) {
            log.error("failed to delete word because word: {} was not found", value);
            return;
        }
        wordRepository.delete(wordToDElete.get());
        log.info("word {} was successfully deleted.", wordToDElete.get().getValue());
    }

    public List<WordDto> getAnagramsForWord(String value) {
        return dictionaryDataLoader.getAnagramsForWord(value).stream().map(WordDto::new).toList();
    }
}
