package com.example.project.PDS.controllers;

import com.example.project.PDS.DTO.projectDTO;
import com.example.project.PDS.DTO.stageDTO;
import com.example.project.PDS.DTO.taskDTO;
import com.example.project.PDS.DTO.userDTO;
import com.example.project.PDS.models.*;
import com.example.project.PDS.services.ProjectService;
import com.example.project.PDS.services.StagesService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final StagesService stagesService;

    @Operation(summary = "Get All Projects ")
    @GetMapping()
    public List<Project> getProjects(){return projectService.getAllProjects();}

    @Operation(summary = "Get Project By Id")
    @GetMapping(value ="/{projectId}")
    public Project getProject(@PathVariable String projectId){return projectService.getProject(projectId);}

    //get stages by project
    @Operation(summary = "Get Stages by Project Id")
    @GetMapping(value ="/{projectId}/stages")
    public List<Stage> getStagesProject(@PathVariable String projectId){return projectService.getStageByProject(projectId);}

    @Operation(summary = "Get Stages by Stage Id")
    @GetMapping(value ="/stages/{stageId}")
    public Stage getStage(@PathVariable String stageId){return projectService.getStage(stageId);}


    //get tasks by stage
    @Operation(summary = "Get Tasks by Stage Id")
    @GetMapping(value ="/stages/{stageId}/tasks")
    public List<Task> getTasksStage(@PathVariable String stageId){return projectService.getTasksByStage(stageId);}

    //get tasks by project
    @Operation(summary = "Get Tasks by Project Id")
    @GetMapping(value ="/{projectId}/tasks")
    public List<Task> getTasksProject(@PathVariable String projectId){return projectService.getTasksByproject(projectId);}


    @Operation(summary = "Get Comments by Stage Id")
    @GetMapping(value ="/stages/{stageId}/comments")
    public List<Comment> getCommentsStage(@PathVariable String stageId){return projectService.getCommentsByStage(stageId);}


    // Add stage to project
    @Operation(summary = "Add stage to a Project")
    @PostMapping(value = "/{projectId}/stages")
    public String addStage(@PathVariable String projectId,@RequestBody stageDTO stageDto){return stagesService.createStage(projectId,stageDto);}


    //Add a document
    @Operation(summary = "Upload specification book PDF")
    @PostMapping(value ="/{projectId}/document")
    public String uploadDoc(@PathVariable String projectId, @RequestParam("pdf") MultipartFile pdf) throws IOException {return projectService.uploadDoc(projectId,pdf.getOriginalFilename(),pdf);}

    @Operation(summary = "download specification book PDF")
    @GetMapping(value ="/{projectId}/document")
    public ResponseEntity<Resource> downloadDoc(@PathVariable String projectId) throws IOException {
        Doccument document = projectService.getDocument(projectId);
        Resource resoure = new ByteArrayResource(document.getDoc().getData());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename\""+ document.getTitle() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resoure);

    }

    @Operation(summary = "Delete Stage")
    @DeleteMapping (value = "/stages/{stageId}")
    public String deleteStage(@PathVariable String stageId){return stagesService.deleteStage(stageId);}

    // remove task by stageId
    @Operation(summary = "Remove a task with stageId ")
    @DeleteMapping(value ="/stages/{stageId}/tasks/{taskId}")
    public String removeTaskStageById(@PathVariable String stageId,@PathVariable String taskId){return projectService.removeTaskById(stageId,taskId);}

    // remove task by projectId
    @Operation(summary = "Remove a task with projectId ")
    @DeleteMapping(value ="/{projectId}/tasks/{taskId}")
    public String removeTaskProjectById(@PathVariable String projectId,@PathVariable String taskId){return projectService.removeTaskById2(projectId,taskId);}

    @Operation(summary = "Update task state")
    @PutMapping(value ="/{Id}/tasks/{taskId}")
    public String updateTaskState(@PathVariable String Id,@PathVariable String taskId){return projectService.updateTask(Id,taskId);}


    // Delete a project
    @Operation(summary = "Delete Project")
    @DeleteMapping (value = "/{projectId}")
    public String deleteProject(@PathVariable String projectId){return projectService.deleteProject(projectId);}


}
