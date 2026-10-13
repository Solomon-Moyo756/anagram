package com.example.anagram.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Dto is used for inbound and outbound communication with front-end
 *
 * @author soolomon
 *
 */
@Getter
@AllArgsConstructor
public class WordDto {
    private String value;
}
