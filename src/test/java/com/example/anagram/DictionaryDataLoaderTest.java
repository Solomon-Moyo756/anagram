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

import static com.example.anagram.components.DictionaryDataLoader.normalize;
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
        assertTrue(anagrams.contains(normalize("listen")));
        assertTrue(anagrams.contains(normalize("silent")));
        assertTrue(anagrams.contains(normalize("enlist")));
        assertFalse(anagrams.contains(normalize("why")));

    }

    @Test
    void testSortString() {
        String result = dataLoader.sortString("banana");
        assertEquals(normalize("aaabnn"), result);
    }

    @Test
    void testAddWordToAnagramMap() {
        dataLoader.addWordToAnagramMap("listen");
        dataLoader.addWordToAnagramMap("silent");

        String key = dataLoader.sortString("listen");
        List<String> group = dataLoader.getAnagramsMap().get(key);

        assertEquals(2, group.size());
        assertTrue(group.contains(normalize("listen")));
        assertTrue(group.contains(normalize("silent")));
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
        assertTrue(group.contains(normalize("listen")));
        assertFalse(group.contains(normalize("silent")));
    }

    @Test
    void testGetLengthToAnagramsStats() {
        dataLoader.addWordToAnagramMap(normalize("listen"));
        dataLoader.addWordToAnagramMap(normalize("silent"));
        dataLoader.addWordToAnagramMap(normalize("enlist"));

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
        assertTrue(anagrams.contains(normalize("silent")));
        assertTrue(anagrams.contains(normalize("enlist")));
        assertFalse(anagrams.contains(normalize("listen"))); // original word excluded
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
        assertFalse(anagrams.contains(normalize("silent")));
        assertTrue(anagrams.contains(normalize("enlist")));
        assertFalse(anagrams.contains(normalize("listen")));
    }

    @Test
    void testAddWordAndGetItsAnagram() {
        dataLoader.addWordToAnagramMap("listen");
        dataLoader.addWordToAnagramMap("silent");
        dataLoader.addWordToAnagramMap("enlist");

        dataLoader.addWordToAnagramMap("selint");

        List<String> anagrams = dataLoader.getAnagramsForWord("listen");

        assertEquals(3, anagrams.size());
        assertTrue(anagrams.contains(normalize("silent")));
        assertTrue(anagrams.contains(normalize("enlist")));
        assertTrue(anagrams.contains(normalize("selint")));
        assertFalse(anagrams.contains(normalize("listen")));
    }
}
