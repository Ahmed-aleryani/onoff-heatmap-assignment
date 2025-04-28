package com.onoffapp.heatmap.service;


import com.onoffapp.heatmap.dto.AnswerRateDto;
import com.onoffapp.heatmap.repository.CallLogRepository;
import com.onoffapp.heatmap.utils.ShadeCalculator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class HeatmapService {

    private final CallLogRepository repo;

    public HeatmapService(CallLogRepository repo) {
        this.repo = repo;
    }

    public List<AnswerRateDto> getAnswerRates (
            LocalDate date, int numberOfShades, int startHour, int endHour
    ) {
        return IntStream.rangeClosed(startHour, endHour)
                .mapToObj(hour -> {
                    LocalDateTime hStart = date.atTime(hour, 0);
                    LocalDateTime hEnd = date.atTime(hour, 59, 59);
                    long total = repo.findAllByStartedAtBetween(hStart, hEnd).size();
                    long answered = repo.findAllByStartedAtBetween(hStart, hEnd).stream()
                            .filter(c -> "COMPLETED".equals(c.getStatus()))
                            .count();
                    double rate = total == 0 ? 0.0 : (answered * 100.0 / total);
                    int shadeNum = ShadeCalculator.calculateShade(rate, numberOfShades);
                    return new AnswerRateDto(hour, answered, total, rate, "Shade" + shadeNum);
                })
                .collect(Collectors.toList());
    }
}