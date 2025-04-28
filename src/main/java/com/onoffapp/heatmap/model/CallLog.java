package com.onoffapp.heatmap.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "CALL_LOG")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CallLog {

    @Id
    private String id;
    private String userId;
    private String username;
    private String onoffNumber;
    private String contactNumber;
    private String status;
    private boolean incoming;
    private int duration;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

}