package cz.marek_b.edp.retry_topics_dlq.event;

import java.time.Instant;

public record RetryMeta(
        int attempt,
        Instant firstFailureAt,
        String lastErrorType
) {}
