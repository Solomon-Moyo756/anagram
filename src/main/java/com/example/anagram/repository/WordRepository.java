package com.example.anagram.repository;

import com.example.anagram.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word,Long> {
    Optional<Word> findByValue(String value);
}
