package com.example.anagram.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "words")
@Data
@NoArgsConstructor
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "words_seq")
    @SequenceGenerator(name = "words_seq", sequenceName = "words_seq", allocationSize = 50)
    private Long id;

    private String value;
}
