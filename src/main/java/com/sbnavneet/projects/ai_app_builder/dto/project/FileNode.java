package com.sbnavneet.projects.ai_app_builder.dto.project;

import java.time.Instant;

public record FileNode(
    String path,
    Instant modifiedAt,
    Long size,
    String type
) {

}
