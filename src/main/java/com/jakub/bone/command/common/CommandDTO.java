package com.jakub.bone.command.common;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
public class CommandDTO {
    private String commandType;

    // @Builder.Default ensures payload is never null
    // Setting payload as empty Map to avoid NPE when payload is not set
    @Builder.Default
    private Map<String, String> payload = new HashMap<>();
}
