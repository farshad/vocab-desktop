package com.vocab.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vocab.enums.SettingType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Farshad Ahangari - farshad.ahg@gmail.com
 * @since 8/10/25 - 8:18 AM
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Setting {
    private String id;
    private SettingType key;
    private String value;
}
