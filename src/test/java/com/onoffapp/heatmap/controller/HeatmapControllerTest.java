package com.onoffapp.heatmap.controller;

import com.onoffapp.heatmap.dto.AnswerRateDto;
import com.onoffapp.heatmap.service.HeatmapService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
class HeatmapControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Mock
    private HeatmapService heatmapService;

    private MockMvc mockMvc;
    private List<AnswerRateDto> mockResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .alwaysDo(print()) // This will print detailed request/response info for all tests
            .build();

        mockResponse = Arrays.asList(
            new AnswerRateDto(0, 10, 20, 50.0, "Shade2"),
            new AnswerRateDto(1, 15, 25, 60.0, "Shade3")
        );
    }

    @Test
    void getAnswerRate_WithValidInput_ReturnsOkResponse() throws Exception {
        when(heatmapService.getAnswerRates(
            any(LocalDate.class),
            any(Integer.class),
            any(Integer.class),
            any(Integer.class)
        )).thenReturn(mockResponse);

        mockMvc.perform(get("/api/heatmap/answer-rate")
                .param("dateInput", LocalDate.now().toString())
                .param("numberOfShades", "5")
                .param("startHour", "0")
                .param("endHour", "23"))
                .andDo(result -> {
                    if (result.getResponse().getStatus() != 200) {
                        System.err.println("Failed with status: " + result.getResponse().getStatus());
                        System.err.println("Response body: " + result.getResponse().getContentAsString());
                    }
                })
                .andExpect(status().isOk());
    }

    @Test
    void getAnswerRate_WithInvalidHours_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/heatmap/answer-rate")
                .param("dateInput", LocalDate.now().toString())
                .param("numberOfShades", "5")
                .param("startHour", "25")  // Invalid hour
                .param("endHour", "23"))
                .andDo(result -> {
                    if (result.getResponse().getStatus() != 400) {
                        System.err.println("Expected 400 Bad Request but got: " + result.getResponse().getStatus());
                        System.err.println("Response body: " + result.getResponse().getContentAsString());
                    }
                })
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAnswerRate_WithEndHourBeforeStartHour_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/heatmap/answer-rate")
                .param("dateInput", LocalDate.now().toString())
                .param("numberOfShades", "5")
                .param("startHour", "10")
                .param("endHour", "5"))  // End hour before start hour
                .andDo(result -> {
                    if (result.getResponse().getStatus() != 400) {
                        System.err.println("Expected 400 Bad Request but got: " + result.getResponse().getStatus());
                        System.err.println("Response body: " + result.getResponse().getContentAsString());
                    }
                })
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAnswerRate_WithInvalidShades_ReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/heatmap/answer-rate")
                .param("dateInput", LocalDate.now().toString())
                .param("numberOfShades", "0")  // Invalid number of shades
                .param("startHour", "0")
                .param("endHour", "23"))
                .andDo(result -> {
                    if (result.getResponse().getStatus() != 400) {
                        System.err.println("Expected 400 Bad Request but got: " + result.getResponse().getStatus());
                        System.err.println("Response body: " + result.getResponse().getContentAsString());
                    }
                })
                .andExpect(status().isBadRequest());
    }
} 