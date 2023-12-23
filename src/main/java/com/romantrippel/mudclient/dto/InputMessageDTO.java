package com.romantrippel.mudclient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InputMessageDTO {
    private String from;
    private String text;
}
