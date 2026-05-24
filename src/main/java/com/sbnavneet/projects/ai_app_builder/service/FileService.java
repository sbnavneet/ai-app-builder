package com.sbnavneet.projects.ai_app_builder.service;

import java.util.List;


import com.sbnavneet.projects.ai_app_builder.dto.project.FileContentResponse;
import com.sbnavneet.projects.ai_app_builder.dto.project.FileNode;

public interface FileService {

    List<FileNode> getFileTree(Long projectId, Long userId);

    FileContentResponse getFile(Long projectId, String path, Long userId);


}
