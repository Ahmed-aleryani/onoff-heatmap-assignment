DROP TABLE IF EXISTS CALL_LOG CASCADE;
CREATE TABLE IF NOT EXISTS CALL_LOG (
    ID             VARCHAR(45)   PRIMARY KEY,
    USER_ID        VARCHAR(45),
    USERNAME       VARCHAR(255),
    ONOFF_NUMBER   VARCHAR(45),
    CONTACT_NUMBER VARCHAR(45),
    STATUS         VARCHAR(45),
    INCOMING       TINYINT(1),
    DURATION       INT,
    STARTED_AT     TIMESTAMP(3),
    ENDED_AT       TIMESTAMP(3)
    );

-- Index to speed /heatmap api lookups by started_at
CREATE INDEX idx_call_log_started_at
    ON CALL_LOG(STARTED_AT);

-- Index to speed /heatmap api lookups by ended_at
CREATE INDEX idx_call_log_ended_at
    ON CALL_LOG(ENDED_AT);

-- single-column index to speed up status filtering/counting
CREATE INDEX idx_call_log_status
    ON CALL_LOG(STATUS);

CREATE INDEX idx_call_log_status_started_at_and_ended_at
    ON CALL_LOG(STATUS, STARTED_AT, ENDED_AT);
