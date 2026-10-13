package com.example.anagram;

import com.example.anagram.components.DictionaryDataLoader;
import com.example.anagram.entity.Word;
import com.example.anagram.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DictionaryDataLoaderTest {

    private WordRepository wordRepository;
    private DictionaryDataLoader dataLoader;

    @BeforeEach
    void setUp() {
        wordRepository = Mockito.mock(WordRepository.class);
        dataLoader = new DictionaryDataLoader(wordRepository);
    }

    @Test
    void testInitializeAnagramMap() {
        // Arrange: mock repository to return some words
        Word word1 = new Word();
        word1.setValue("listen");
        Word word2 = new Word();
        word2.setValue("silent");
        Word word3 = new Word();
        word3.setValue("enlist");
        Word word4 = new Word();
        word4.setValue("why");

        when(wordRepository.findAll()).thenReturn(Arrays.asList(word1, word2, word3));

        // Act
        dataLoader.initializeAnagramMap();

        // Assert: all should map to the same sorted key
        String sortedKey = dataLoader.sortString("listen");
        List<String> anagrams = dataLoader.getAnagramsMap().get(sortedKey);

        assertNotNull(anagrams);
        assertEquals(3, anagrams.size());
        assertTrue(anagrams.contains("listen"));
        assertTrue(anagrams.contains("silent"));
        assertTrue(anagrams.contains("enlist"));
        assertFalse(anagrams.contains("why"));

    }

    @Test
    void testSortString() {
        String result = dataLoader.sortString("banana");
        assertEquals("aaabnn", result);
    }

    @Test
    void testAddWordToAnagramMap() {
        dataLoader.addWordToAnagramMap("listen");
        dataLoader.addWordToAnagramMap("silent");

        String key = dataLoader.sortString("listen");
        List<String> group = dataLoader.getAnagramsMap().get(key);

        assertEquals(2, group.size());
        assertTrue(group.contains("listen"));
        assertTrue(group.contains("silent"));
    }

    @Test
    void testRemoveWordFromAnagramMap() {
        dataLoader.addWordToAnagramMap("listen");
        dataLoader.addWordToAnagramMap("silent");
        Word word = new Word();
        word.setValue("silent");
        dataLoader.removeWordFromAnagramMap(List.of(word));

        String key = dataLoader.sortString("listen");
        List<String> group = dataLoader.getAnagramsMap().get(key);

        assertEquals(1, group.size());
        assertTrue(group.contains("listen"));
        assertFalse(group.contains("silent"));
    }

    @Test
    void testGetLengthToAnagramsStats() {
        dataLoader.addWordToAnagramMap("listen");
        dataLoader.addWordToAnagramMap("silent");
        dataLoader.addWordToAnagramMap("enlist");

        Map<Integer, Integer> stats = dataLoader.getLengthToAnagramsStats();

        // length 6 words: 3 words, so 2 anagrams per word group
        assertTrue(stats.containsKey(6));
        assertEquals(2, stats.get(6));
    }

    @Test
    void testGetAnagramsForWord() {
        dataLoader.addWordToAnagramMap("listen");
        dataLoader.addWordToAnagramMap("silent");
        dataLoader.addWordToAnagramMap("enlist");

        List<String> anagrams = dataLoader.getAnagramsForWord("listen");

        assertEquals(2, anagrams.size());
        assertTrue(anagrams.contains("silent"));
        assertTrue(anagrams.contains("enlist"));
        assertFalse(anagrams.contains("listen")); // original word excluded
    }

    @Test
    void testRemoveWordAndGetItsAnagram() {
        dataLoader.addWordToAnagramMap("listen");
        dataLoader.addWordToAnagramMap("silent");
        dataLoader.addWordToAnagramMap("enlist");
        Word word = new Word();
        word.setValue("silent");
        dataLoader.removeWordFromAnagramMap(List.of(word));

        List<String> anagrams = dataLoader.getAnagramsForWord("listen");

        assertEquals(1, anagrams.size());
        assertFalse(anagrams.contains("silent"));
        assertTrue(anagrams.contains("enlist"));
        assertFalse(anagrams.contains("listen"));
    }

    @Test
    void testAddWordAndGetItsAnagram() {
        dataLoader.addWordToAnagramMap("listen");
        dataLoader.addWordToAnagramMap("silent");
        dataLoader.addWordToAnagramMap("enlist");

        dataLoader.addWordToAnagramMap("selint");

        List<String> anagrams = dataLoader.getAnagramsForWord("listen");

        assertEquals(3, anagrams.size());
        assertTrue(anagrams.contains("silent"));
        assertTrue(anagrams.contains("enlist"));
        assertTrue(anagrams.contains("selint"));
        assertFalse(anagrams.contains("listen"));
    }
}
