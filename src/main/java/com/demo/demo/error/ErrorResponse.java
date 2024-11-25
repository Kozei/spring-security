package com.demo.demo.error;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class ErrorResponse implements Serializable {

    private final Instant timestamp;
    private final int status;
    private final String statusMessage;

    private final String userMessage;
    private final String path;

    private ErrorResponse(Builder builder) {
        this.timestamp = builder.timestamp;
        this.status = builder.status;
        this.statusMessage = builder.statusMessage;
        this.userMessage = builder.userMessage;
        this.path = builder.path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getPath() {
        return path;
    }

    public static class Builder {
        private final Instant timestamp;
        private final int status;
        private final String statusMessage;

        //optional
        private String userMessage;
        private String path;

        public Builder(Instant timestamp, int status, String statusMessage) {
            this.timestamp = timestamp;
            this.status = status;
            this.statusMessage = statusMessage;
        }

        public Builder userMessage(String userMessage) {
            this.userMessage = userMessage;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ErrorResponse that = (ErrorResponse) o;
        return status == that.status && Objects.equals(timestamp, that.timestamp) && Objects.equals(statusMessage, that.statusMessage) && Objects.equals(userMessage, that.userMessage) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(timestamp);
        result = 31 * result + status;
        result = 31 * result + Objects.hashCode(statusMessage);
        result = 31 * result + Objects.hashCode(userMessage);
        result = 31 * result + Objects.hashCode(path);
        return result;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", statusMessage='" + statusMessage + '\'' +
                ", userMessage='" + userMessage + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
