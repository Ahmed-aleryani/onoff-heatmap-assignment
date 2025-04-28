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

CREATE INDEX idx_call_log_started_at_status
    ON CALL_LOG(STARTED_AT, STATUS);

CREATE INDEX idx_call_log_status
    ON CALL_LOG(STATUS);

CREATE INDEX idx_call_log_ended_at_status
    ON CALL_LOG(ENDED_AT, STATUS);

-- In case we need to filter by user_id
CREATE INDEX idx_call_log_user_id
    ON CALL_LOG(USER_ID);

    