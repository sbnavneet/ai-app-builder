
package com.sbnavneet.projects.ai_app_builder.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProjectPermission {
    VIEW("project:view"),
    EDIT("project:edit"),
    EDIT_FILES("project:edit_files"),
    DELETE("project:delete"),
    VIEW_MEMBERS("project_members:view"),
    MANAGE_MEMBERS("project_members:manage");

    private final String value ;
}
