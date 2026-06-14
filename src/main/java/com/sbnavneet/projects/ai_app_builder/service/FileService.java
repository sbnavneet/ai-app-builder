package com.sbnavneet.projects.ai_app_builder.service;

import java.util.List;


import com.sbnavneet.projects.ai_app_builder.dto.project.FileContentResponse;
import com.sbnavneet.projects.ai_app_builder.dto.project.FileNode;

public interface FileService {

    List<FileNode> getFileTree(Long projectId);

    FileContentResponse getFile(Long projectId, String path);

    void saveFile(Long projectId, String filePath, String fileContent);
}
