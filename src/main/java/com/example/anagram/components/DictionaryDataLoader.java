package com.example.anagram.components;

import com.example.anagram.entity.Word;
import com.example.anagram.repository.WordRepository;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * This Component is the one that does the main work of identifying anagrams, intializing and keeping them in mmemory.
 *
 * @author Solomon
 */
@Component
@Slf4j
public class DictionaryDataLoader {

    private final WordRepository wordRepository;
    private String DICT_PATH = "src/main/resources/dict/Dictionary.txt";
    @Getter
    private HashMap<String, ArrayList<String>> anagramsMap = new HashMap<>();

    public DictionaryDataLoader(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    /**
     * Seeding the databse so we can use it as source of truth
     *
     */
    @EventListener(ApplicationReadyEvent.class)
    public void seedDatabase() {
        try {
            if (wordRepository.count() == 0) {
                log.info("seeding database with new data");
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

    /**
     * Initializing the in memory anagram map, which is used to store the anagrams relationship
     */
    public void initializeAnagramMap() {
        List<Word> words = wordRepository.findAll();
        for (Word word : words) {
            String value = word.getValue().toLowerCase();
            String orderedWord = sortString(value);
            if (anagramsMap.containsKey(orderedWord)) {
                anagramsMap.get(orderedWord).add(value);
            } else {
                //adding a new key value pair
                ArrayList<String> listOfWords = new ArrayList<>();
                listOfWords.add(value);
                anagramsMap.put(orderedWord, listOfWords);
            }
        }
        log.info("hash map for anagrams has been successfully created");
    }

    public void addWordToAnagramMap(String newWord) {
        //including a new word to the anagrams hash map
        String anagramKey = sortString(newWord);
        if (anagramsMap.containsKey(anagramKey)) {
            anagramsMap.get(anagramKey).add(newWord.toLowerCase());
        } else {
            ArrayList<String> listOfWords = new ArrayList<>();
            listOfWords.add(newWord.toLowerCase());
            anagramsMap.put(anagramKey, listOfWords);
        }
    }

    public void removeWordFromAnagramMap(String wordToRemove) {
        //including a new word to the anagrams hash map
        String anagramKey = sortString(wordToRemove);
        if (anagramsMap.containsKey(anagramKey)) {
            anagramsMap.get(anagramKey).remove(wordToRemove.toLowerCase());
            log.info("word {} removed from anagrams map", wordToRemove);
        }
    }

    /**
     * defines how correlation between length of word and number of anagrams
     */
    public HashMap<Integer, Integer> getLengthToAnagramsStats() {
        //looping through key set and check the size, after that add (size of list - 1) of that key,
        log.info("generating stats for all anagrams of all lengths");
        HashMap<Integer, Integer> statsMap = new HashMap<>();
        for (String key : anagramsMap.keySet()) {
            int keySize = key.length();
            if (statsMap.containsKey(keySize)) {
                int numberOfAnagrams = statsMap.get(keySize);
                numberOfAnagrams += anagramsMap.get(key).size() - 1; //subtracting 'one' because a word cannot be its own anagram
                statsMap.put(keySize, numberOfAnagrams);
            } else {
                int newNumberOfAnagrams = anagramsMap.get(key).size() - 1;
                statsMap.put(keySize, newNumberOfAnagrams);
            }
        }
        log.info("generating stats complete");
        return statsMap;
    }

    /**
     * determines the anagrams for a specific word, while making sure that itself is not counted as its own anagram
     */
    public List<String> getAnagramsForWord(String value) {
        log.info("computing anagrams for word: {}", value);
        String anagramKey = sortString(value);
        if (anagramsMap.containsKey(anagramKey)) {
            List<String> anagramsToReturn = anagramsMap.get(anagramKey);
            anagramsToReturn.remove(value.toLowerCase()); //removing value since not part of its own anagram
            return anagramsToReturn;
        }
        return Collections.emptyList();
    }

    public String sortString(String input) {
        char[] chars = input.toLowerCase().toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

}
