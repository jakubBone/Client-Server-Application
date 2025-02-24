package com.jakub.bone.command.common;

import java.util.HashMap;
import java.util.Map;

public class CommandDTO {
    private String commandType;
    private Map<String, String> payload;

    private CommandDTO(Builder builder) {
        this.commandType = builder.commandType;
        this.payload = builder.payload;
    }

    public String getCommandType() {
        return commandType;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public static class Builder {
        private String commandType;
        private Map<String, String> payload = new HashMap<>();

        public Builder commandType(String commandType) {
            this.commandType = commandType;
            return this;
        }

        public Builder addPayload(String key, String value) {
            this.payload.put(key, value);
            return this;
        }

        public CommandDTO build() {
            return new CommandDTO(this);
        }
    }
}
