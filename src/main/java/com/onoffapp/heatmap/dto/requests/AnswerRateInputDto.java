package com.onoffapp.heatmap.dto.requests;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AnswerRateInputDto {
    @NotNull(message = "date must be provided")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateInput;

    @NotNull(message = "numberOfShades is required")
    @Min(value = 3, message = "numberOfShades must be at least {value}")
    @Max(value = 10, message = "numberOfShades must be at most {value}")
    private Integer numberOfShades;

    @NotNull
    @Min(value = 0, message = "startHour must be >= {value}")
    @Max(value = 23, message = "startHour must be <= {value}")
    private Integer startHour = 0;

    @NotNull
    @Min(value = 0, message = "endHour must be >= {value}")
    @Max(value = 23, message = "endHour must be <= {value}")
    private Integer endHour = 23;

    @AssertTrue(message = "endHour must be greater than or equal to startHour")
    public boolean isHourRangeValid() {
        if (startHour == null || endHour == null) {
            return true;
        }
        return endHour >= startHour;
    }
}