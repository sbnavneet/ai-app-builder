package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sbnavneet.projects.ai_app_builder.dto.project.FileContentResponse;
import com.sbnavneet.projects.ai_app_builder.dto.project.FileNode;
import com.sbnavneet.projects.ai_app_builder.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImpl implements FileService{

    @Override
    public FileContentResponse getFile(Long projectId, String path, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFile'");
    }

    @Override
    public List<FileNode> getFileTree(Long projectId, Long userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFileTree'");
    }

    @Override
    public void saveFile(Long projectId, String filePath, String fileContent) {
        log.info("Saving file: {}", filePath);
        
    }

}
