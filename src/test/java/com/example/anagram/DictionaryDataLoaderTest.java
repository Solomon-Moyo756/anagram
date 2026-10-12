package com.example.anagram;

import com.example.anagram.components.DictionaryDataLoader;
import com.example.anagram.entity.Word;
import com.example.anagram.repository.WordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

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
}
