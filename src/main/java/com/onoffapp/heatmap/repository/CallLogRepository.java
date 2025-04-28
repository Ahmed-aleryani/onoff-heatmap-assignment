package com.onoffapp.heatmap.repository;

import com.onoffapp.heatmap.model.CallLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface CallLogRepository extends JpaRepository<CallLog, String> {

    @Query("SELECT c FROM CallLog c WHERE c.startedAt BETWEEN :start AND :end")
    List<CallLog> findAllByStartedAtBetween(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}