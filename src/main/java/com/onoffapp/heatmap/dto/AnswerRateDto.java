package com.onoffapp.heatmap.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AnswerRateDto {
    private int hour;
    private long answeredCalls;
    private long totalCalls;
    private double rate;
    private String shade;
}