package com.example.anagram.components;

import com.example.anagram.entity.Word;
import com.example.anagram.repository.WordRepository;
import com.example.anagram.service.AnagramService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

@Component
@Slf4j
public class DictionaryDataLoader {

    private final WordRepository wordRepository;
    private String DICT_PATH = "src/main/resources/dict/Dictionary.txt";
    @Getter
    private HashMap<String, List<String>> anagramsMap = new HashMap<>();

    public DictionaryDataLoader(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void seedDatabase() {
        try {
            if (wordRepository.count() == 0) {
                FileInputStream inputStream = new FileInputStream(DICT_PATH);
                Scanner scanner = new Scanner(inputStream);
                List<Word> words = new ArrayList<>();
                while (scanner.hasNext()) {
                    Word word = new Word();
                    String value = scanner.nextLine();
                    word.setValue(value);
                    words.add(word);
                }
                wordRepository.saveAll(words);
            } else {
                log.info("skipping seeding...");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            log.info("initializing anagram hash map");
            initializeAnagramMap();
        }
    }

    public void initializeAnagramMap() {
        List<Word> words = wordRepository.findAll();
        for (Word word : words) {
            String value = word.getValue();
            String orderedWord = sortString(value);
            if (anagramsMap.containsKey(orderedWord)) {
                anagramsMap.get(orderedWord).add(value);
            } else {
                //adding a new key value pair
                List<String> listOfWords = new ArrayList<>();
                listOfWords.add(value);
                anagramsMap.put(orderedWord, listOfWords);
            }
        }
        log.info("hash map for anagrams has been successfully created");
    }

    public String sortString(String input) {
        char[] chars = input.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

}
