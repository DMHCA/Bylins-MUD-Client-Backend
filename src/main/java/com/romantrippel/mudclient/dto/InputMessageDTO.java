package com.romantrippel.mudclient.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OutputMessageDTO {
    private String from;
    private String text;
    private String time;
}
