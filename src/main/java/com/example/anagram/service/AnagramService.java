package com.example.anagram.service;

import com.example.anagram.components.DictionaryDataLoader;
import com.example.anagram.dto.WordDto;
import com.example.anagram.entity.Word;
import com.example.anagram.repository.WordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.example.anagram.components.DictionaryDataLoader.normalize;

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

    @Cacheable("words")
    public List<WordDto> getAllWords() {
        return wordRepository.findAll().stream().map(word -> new WordDto(word.getValue())).toList();
    }

    @CacheEvict(value = {"words","stats"}, allEntries = true)
    public Boolean addWord(String value) {
        value = normalize(value);
        List<Word> currentWord = wordRepository.findByValue(value);
        if (!currentWord.isEmpty()) {
            log.info("word: {} failed to be added, it might exist", value);
            return false;
        }
        Word newWord = new Word();
        newWord.setValue(value);
        wordRepository.save(newWord);
        //update hash map
        log.info("word: {} successfully added", value);
        dictionaryDataLoader.addWordToAnagramMap(value);
        return true;
    }

    @CacheEvict(value = {"words","stats"}, allEntries = true)
    public Boolean deleteWord(String value) {
        value = normalize(value);
        List<Word> wordToDElete = wordRepository.findByValue(value);
        if (wordToDElete.isEmpty()) {
            log.error("failed to delete word because word: {} was not found", value);
            return false;
        }
        wordRepository.deleteAll(wordToDElete);
        //remove from in memery anagram hash map
        dictionaryDataLoader.removeWordFromAnagramMap(wordToDElete);
        log.info("word {} was successfully deleted.", wordToDElete.get(0).getValue());
        return true;
    }

    public List<WordDto> getAnagramsForWord(String value) {
        return dictionaryDataLoader.getAnagramsForWord(value).stream().map(WordDto::new).toList();
    }

    @Cacheable("stats")
    public Map<Integer, Integer> getLengthToAnagramsStats() {
        return dictionaryDataLoader.getLengthToAnagramsStats();
    }
}
