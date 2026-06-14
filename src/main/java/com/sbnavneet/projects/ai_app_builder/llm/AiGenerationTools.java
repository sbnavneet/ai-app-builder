package com.sbnavneet.projects.ai_app_builder.llm;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import com.sbnavneet.projects.ai_app_builder.service.FileService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AiGenerationTools {

    private final FileService projectFileService;
    private final Long projectid;

    @Tool(name ="read_files",
        description = "Read the content of files. Only input the file names present inside the FILE_TREE. DO NOT input any other file outside the FILE_TREE"
    )
    public List<String> readFiles(
        @ToolParam(description = "List of relative path (e.g., ['src/App.tsx])")
        List<String> paths
    ){
        List<String> result = new ArrayList<>();

        for(String path: paths){
            String cleanPath = path.startsWith("/")? path.substring(1):path;
            log.info("Request file {}", cleanPath);
            String content = projectFileService.getFile(projectid, cleanPath).content();
            result.add(String.format(
                "--- START OF FILE: %s ---\n%s\n--- END OF FILE ---", cleanPath, content
            ));
        }
        return result;
    }
}
