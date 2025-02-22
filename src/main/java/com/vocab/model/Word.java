package com.vocab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/3/25 - 11:00 AM
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Word {
    private String id;
    private String chapterId;
    private String title;
    private String translate;
    private String example;
    private Boolean fav;
}
