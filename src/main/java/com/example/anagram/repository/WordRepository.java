package com.example.anagram.repository;

import com.example.anagram.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Solomon Moyo
 */
@Repository
public interface WordRepository extends JpaRepository<Word, Long> {
    List<Word> findByValue(String value);
}
