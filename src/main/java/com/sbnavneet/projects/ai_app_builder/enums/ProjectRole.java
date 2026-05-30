package com.sbnavneet.projects.ai_app_builder.enums;

import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import static com.sbnavneet.projects.ai_app_builder.enums.ProjectPermission.*;

@RequiredArgsConstructor
@Getter
public enum ProjectRole {
    OWNER(Set.of(DELETE, VIEW, EDIT, EDIT_FILES, MANAGE_MEMBERS, VIEW_MEMBERS)),
    EDITOR(Set.of(VIEW, VIEW_MEMBERS, EDIT_FILES)),
    VIEWER(Set.of(VIEW, VIEW_MEMBERS));

    private final Set<ProjectPermission> permissions;
}
