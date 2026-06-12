package com.sbnavneet.projects.ai_app_builder.dto.project;


public record FileNode(
    String path
) {

    @Override
    public String toString() {
        return path;
    }

}
