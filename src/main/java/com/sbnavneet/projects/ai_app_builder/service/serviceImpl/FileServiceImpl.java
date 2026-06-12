package com.sbnavneet.projects.ai_app_builder.service.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sbnavneet.projects.ai_app_builder.dto.project.FileContentResponse;
import com.sbnavneet.projects.ai_app_builder.dto.project.FileNode;
import com.sbnavneet.projects.ai_app_builder.entity.Project;
import com.sbnavneet.projects.ai_app_builder.entity.ProjectFile;
import com.sbnavneet.projects.ai_app_builder.error.ResourceNotFoundException;
import com.sbnavneet.projects.ai_app_builder.mapper.ProjectFileMapper;
import com.sbnavneet.projects.ai_app_builder.repository.ProjectFileRepository;
import com.sbnavneet.projects.ai_app_builder.repository.ProjectRepository;
import com.sbnavneet.projects.ai_app_builder.service.FileService;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{

    private final ProjectRepository projectRepository;
    private final ProjectFileRepository projectFileRepository;
    private final MinioClient minioClient;
    private final ProjectFileMapper projectFileMapper;
    
    @Value("${minio.project-bucket}")
    private String projectBucket;

    @Override
    public FileContentResponse getFile(Long projectId, String path, Long userId) {
        return null;
    }

    @Override
    public List<FileNode> getFileTree(Long projectId) {
        List<ProjectFile> projectFileList = projectFileRepository.findByProjectId(projectId);
        return projectFileMapper.toListOfFileNode(projectFileList);
    }

    @Override
    public void saveFile(Long projectId, String filePath, String fileContent) {
        log.info("Saving file: {}", filePath);
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project", projectId.toString()));
        String cleanPath = filePath.startsWith("/")?filePath.substring(1):filePath;
        String objectKey = projectId + "/" + cleanPath;
        try{
           byte[] contentBytes = fileContent.getBytes(StandardCharsets.UTF_8);
           InputStream inputStream = new ByteArrayInputStream(contentBytes);
           //Saving actual file
           minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(projectBucket)
                    .object(objectKey)
                    .stream(inputStream, contentBytes.length, -1)
                    .contentType(determineContentType(cleanPath))
                    .build());
            //Saving meta data to DB 
            ProjectFile file = projectFileRepository.findByProjectIdAndPath(projectId, cleanPath)
                .orElseGet(()-> ProjectFile.builder()
                                .project(project)
                                .path(cleanPath)
                                .minioObjectKey(objectKey)
                                .createdAt(Instant.now())
                                .build());
            file.setUpdatedAt(Instant.now());
            projectFileRepository.save(file);
            log.info("Saved file: {}", objectKey);
        }catch(Exception e){
            log.error("Error while saving file {} ", objectKey);
        }
    }

    private String determineContentType(String path){
        String type = URLConnection.guessContentTypeFromName(path);
        if(type != null) return type;
        if(path.endsWith(".jsx") || path.endsWith(".ts") || path.endsWith(".tsx")){
            return "text/javascript";
        }
        if(path.endsWith(".json")) return "application/json";
        if(path.endsWith(".css")) return "text/css";
        return "text/plain";
    }

}
