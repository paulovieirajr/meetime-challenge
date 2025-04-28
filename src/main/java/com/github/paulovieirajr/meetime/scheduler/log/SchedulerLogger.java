package com.github.paulovieirajr.meetime.scheduler.log;

public enum SchedulerLogger {
    LOG_REMOVING_EXPIRED_TOKENS("[SCHEDULER] - Tarefa agendada para limpar tokens expirados do HubSpot iniciada");

    private final String message;

    SchedulerLogger(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
