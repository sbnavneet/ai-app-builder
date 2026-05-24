package com.sbnavneet.projects.ai_app_builder.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sbnavneet.projects.ai_app_builder.dto.project.FileContentResponse;
import com.sbnavneet.projects.ai_app_builder.dto.project.FileNode;
import com.sbnavneet.projects.ai_app_builder.service.FileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/{projectId}/files")
public class FileController {

    private final FileService fileService;

    @GetMapping
    public ResponseEntity<List<FileNode>> getFileTree(@PathVariable Long projectId){
        Long userId = 1L;
        return ResponseEntity.ok(fileService.getFileTree(projectId, userId));    
    }

    @GetMapping("/{*path}")
    public ResponseEntity<FileContentResponse> getFile(@PathVariable Long projectId, @PathVariable String path){
        Long userId = 1L;
        return ResponseEntity.ok(fileService.getFile(projectId, path, userId));
    }

}
