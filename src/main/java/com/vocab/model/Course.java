package com.vocab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Locale;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 2/3/25 - 11:00 AM
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {
    private String id;
    private String title;
    private List<Chapter> chapters;
    private String locale;

    public void setLocale(Locale locale) {
        this.locale = (locale != null) ? locale.toString() : null;
    }

    public Locale getLocale() {
        return (locale != null) ? Locale.forLanguageTag(locale) : null;
    }
}
