package com.demo.demo.error;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

public class ErrorResponse implements Serializable {

    private final Instant timestamp;
    private final int status;
    private final String error;
    private final String message;
    private final String userMessage;
    private final String path;

    private ErrorResponse(Builder builder) {
        this.timestamp = builder.timestamp;
        this.status = builder.status;
        this.error = builder.error;
        this.message = builder.message;
        this.userMessage = builder.userMessage;
        this.path = builder.path;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public String getPath() {
        return path;
    }

    public static class Builder {
        private Instant timestamp;
        private String message;

        //optional
        private int status;
        private String error;
        private String userMessage;
        private String path;

        public Builder(Instant timestamp, String message) {
            this.timestamp = timestamp;
            this.message = message;
        }

        public Builder userMessage(String userMessage) {
            this.userMessage = userMessage;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder error(String error) {
            this.error = error;
            return this;
        }

        public Builder status(int status) {
            this.status = status;
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
        return status == that.status && Objects.equals(timestamp, that.timestamp) && Objects.equals(error, that.error) && Objects.equals(message, that.message) && Objects.equals(userMessage, that.userMessage) && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(timestamp);
        result = 31 * result + status;
        result = 31 * result + Objects.hashCode(error);
        result = 31 * result + Objects.hashCode(message);
        result = 31 * result + Objects.hashCode(userMessage);
        result = 31 * result + Objects.hashCode(path);
        return result;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", userMessage='" + userMessage + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
