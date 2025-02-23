package com.jakub.bone.command.common;

import java.util.HashMap;
import java.util.Map;

public class CommandDTO {
    private String commandType;
    private Map<String, Object> payload;

    private CommandDTO(Builder builder) {
        this.commandType = builder.commandType;
        this.payload = builder.payload;
    }

    public String getCommandType() {
        return commandType;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public static class Builder {
        private String commandType;
        private Map<String, Object> payload = new HashMap<>();

        public Builder commandType(String commandType) {
            this.commandType = commandType;
            return this;
        }

        public Builder addPayload(String key, Object value) {
            this.payload.put(key, value);
            return this;
        }

        public CommandDTO build() {
            return new CommandDTO(this);
        }
    }
}
