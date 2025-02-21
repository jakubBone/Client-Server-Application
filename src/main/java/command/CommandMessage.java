package command;

import java.util.HashMap;
import java.util.Map;

public class CommandMessage {
    private String commandType;
    private Map<String, Object> payload;

    private CommandMessage(Builder builder) {
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

        public CommandMessage build() {
            return new CommandMessage(this);
        }
    }
}
