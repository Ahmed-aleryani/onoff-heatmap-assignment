package com.onoffapp.heatmap.controller;

import com.onoffapp.heatmap.dto.AnswerRateDto;
import com.onoffapp.heatmap.service.HeatmapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import com.onoffapp.heatmap.dto.requests.AnswerRateInputDto;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;

@RestController
@Validated
public class HeatmapController {

    private static final Logger logger = LoggerFactory.getLogger(HeatmapController.class);
    private final HeatmapService service;

    public HeatmapController(HeatmapService service) {
        this.service = service;
        logger.debug("HeatmapController initialized");
    }

    @GetMapping("/api/heatmap/answer-rate")
    public ResponseEntity<List<AnswerRateDto>> getAnswerRate(@Valid @ModelAttribute AnswerRateInputDto inputDto) {
        logger.info("Processing request with parameters: date={}, numberOfShades={}, startHour={}, endHour={}",
                inputDto.getDateInput(), inputDto.getNumberOfShades(), inputDto.getStartHour(), inputDto.getEndHour());
        
        List<AnswerRateDto> data = service.getAnswerRates(
                inputDto.getDateInput(),
                inputDto.getNumberOfShades(),
                inputDto.getStartHour(),
                inputDto.getEndHour()
        );
        
        logger.info("Successfully retrieved {} answer rate entries", data.size());
        return ResponseEntity.ok(data);
    }
}
