package com.onoffapp.heatmap.service;

import com.onoffapp.heatmap.dto.AnswerRateDto;
import com.onoffapp.heatmap.model.CallLog;
import com.onoffapp.heatmap.repository.CallLogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class HeatmapServiceTest {

    @Mock
    private CallLogRepository callLogRepository;

    @InjectMocks
    private HeatmapService heatmapService;

    private LocalDate testDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        testDate = LocalDate.now();
        startTime = testDate.atTime(0, 0);
        endTime = testDate.atTime(0, 59, 59);
    }

    @Test
    void getAnswerRates_WithValidData_ReturnsCorrectRates() {
        // Arrange
        List<CallLog> mockLogs = Arrays.asList(
            createCallLog("ANSWER"),
            createCallLog("ANSWER"),
            createCallLog("MISSED"),
            createCallLog("ERROR")
        );
        
        // Use the startTime and endTime variables to match the service's behavior
        when(callLogRepository.findAllByStartedAtBetween(eq(startTime), eq(endTime))).thenReturn(mockLogs);

        // Act
        List<AnswerRateDto> result = heatmapService.getAnswerRates(testDate, 5, 0, 0);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(1, result.size(), "Should return exactly one hour of data");
        
        AnswerRateDto firstHour = result.get(0);
        assertEquals(0, firstHour.getHour(), "Hour should be 0 for first hour");
        assertEquals(2, firstHour.getAnsweredCalls(), 
            "Should have 2 answered calls (ANSWER status)");
        assertEquals(4, firstHour.getTotalCalls(), 
            "Should have 4 total calls (2 ANSWER, 1 MISSED, 1 ERROR)");
        assertEquals(50.0, firstHour.getRate(), 
            "Answer rate should be 50% (2 answered out of 4 total)");
        assertTrue(firstHour.getShade().startsWith("Shade"), 
            "Shade string should start with 'Shade'");
    }

    @Test
    void getAnswerRates_WithNoData_ReturnsZeroRates() {
        // Arrange
        when(callLogRepository.findAllByStartedAtBetween(eq(startTime), eq(endTime))).thenReturn(Collections.emptyList());

        // Act
        List<AnswerRateDto> result = heatmapService.getAnswerRates(testDate, 5, 0, 0);

        // Assert
        assertNotNull(result, "Result should not be null even with no data");
        assertEquals(1, result.size(), "Should return exactly one hour of data");
        
        AnswerRateDto firstHour = result.get(0);
        assertEquals(0, firstHour.getHour(), "Hour should be 0 for first hour");
        assertEquals(0, firstHour.getAnsweredCalls(), 
            "Should have 0 answered calls when no data exists");
        assertEquals(0, firstHour.getTotalCalls(), 
            "Should have 0 total calls when no data exists");
        assertEquals(0.0, firstHour.getRate(), 
            "Answer rate should be 0% when no data exists");
        assertEquals("Shade1", firstHour.getShade(), 
            "Empty data should result in Shade1");
    }

    @Test
    void getAnswerRates_WithMultipleHours_ReturnsCorrectData() {
        // Arrange
        List<CallLog> hour0Logs = Arrays.asList(
            createCallLog("ANSWER"),
            createCallLog("ANSWER")
        );
        List<CallLog> hour1Logs = Arrays.asList(
            createCallLog("ANSWER"),
            createCallLog("MISSED"),
            createCallLog("ERROR")
        );

        // Use the startTime and endTime variables for the first hour
        when(callLogRepository.findAllByStartedAtBetween(eq(startTime), eq(endTime)))
            .thenReturn(hour0Logs);
            
        // For the second hour, create new time range
        LocalDateTime hour1Start = testDate.atTime(1, 0);
        LocalDateTime hour1End = testDate.atTime(1, 59, 59);
        when(callLogRepository.findAllByStartedAtBetween(eq(hour1Start), eq(hour1End)))
            .thenReturn(hour1Logs);

        // Act
        List<AnswerRateDto> result = heatmapService.getAnswerRates(testDate, 5, 0, 1);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals(2, result.size(), "Should return exactly two hours of data");
        
        // Check first hour
        AnswerRateDto hour0 = result.get(0);
        assertEquals(0, hour0.getHour(), "First entry should be hour 0");
        assertEquals(2, hour0.getAnsweredCalls(), 
            "Hour 0 should have 2 answered calls (all ANSWER)");
        assertEquals(2, hour0.getTotalCalls(), 
            "Hour 0 should have 2 total calls");
        assertEquals(100.0, hour0.getRate(), 
            "Hour 0 should have 100% answer rate (all calls answered)");
        
        // Check second hour
        AnswerRateDto hour1 = result.get(1);
        assertEquals(1, hour1.getHour(), "Second entry should be hour 1");
        assertEquals(1, hour1.getAnsweredCalls(), 
            "Hour 1 should have 1 answered call (1 ANSWER)");
        assertEquals(3, hour1.getTotalCalls(), 
            "Hour 1 should have 3 total calls (1 ANSWER, 1 MISSED, 1 ERROR)");
        assertEquals(33.33, hour1.getRate(), 0.01, 
            "Hour 1 should have ~33.33% answer rate (1 answered out of 3)");
    }

    private CallLog createCallLog(String status) {
        CallLog log = new CallLog();
        log.setStatus(status);
        return log;
    }
} 