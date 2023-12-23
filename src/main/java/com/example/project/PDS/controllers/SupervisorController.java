package com.example.project.PDS.controllers;

import com.example.project.PDS.DTO.TeamDTO;
import com.example.project.PDS.DTO.commentDTO;
import com.example.project.PDS.DTO.projectDTO;
import com.example.project.PDS.DTO.userDTO;
import com.example.project.PDS.models.*;
import com.example.project.PDS.services.SupervisorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping(value = "api/v1/supervisors")
public class SupervisorController {
    private final SupervisorService supervisorService;

    @Operation(summary = "Get All Supervisors")
    @GetMapping()
    public List<Supervisor> getAllSupervisors(){return supervisorService.getAllSupervisor();}

    @Operation(summary = "Get Supervisor By Id")
    @GetMapping(value ="/{Id}")
    public Supervisor getSupervisor(@PathVariable String Id){return supervisorService.getSupervisor(Id);}


    @Operation(summary = "Get Supervisor By Name")
    @GetMapping(value ="/{name}/name")
    public Supervisor getSupervisorByName(@PathVariable String name){return supervisorService.getSupervisorByName(name);}

    @Operation(summary = "Add Supervisor")
    @PostMapping()
    public String addSupervisor(@RequestBody userDTO userDto){return supervisorService.createSupervisor(userDto);}

    @Operation(summary = "Update Supervisor")
    @PutMapping(value = "/{supervisorId}")
    public String updateSupervisor(@PathVariable String supervisorId,@RequestBody userDTO userDto){return supervisorService.updateSupervisor(supervisorId,userDto);}


    @Operation(summary = "Delete this Supervisor By Id")
    @DeleteMapping(value ="/{Id}")
    public String removeSupervisor(@PathVariable String Id){return supervisorService.deleteSupervisor(Id);}

    @Operation(summary = "Delete this Supervisor By Name")
    @DeleteMapping(value ="/{name}")
    public String removeSupervisorByName(@PathVariable String name){return supervisorService.deleteSupervisorByName(name);}


    //create project
    @Operation(summary = "Add Project")
    @PostMapping(value ="/{Id}/projects")
    public String addProject(@PathVariable String Id,@RequestBody projectDTO projectDto){return supervisorService.AddProject(Id,projectDto);}

    // add comment to stage /only a supervisor can add comment
    @Operation(summary = "Add comment to stage / only a supervisor can add comments")
    @PostMapping(value ="/{supervisorId}/stages/{stageId}/comments")
    public String addComment(@PathVariable String supervisorId,@PathVariable String stageId, @RequestBody commentDTO commentDto){return supervisorService.addComment(supervisorId,stageId,commentDto);}

    // view his teams
    @Operation(summary = "View My teams")
    @GetMapping(value ="/{Id}/teams")
    public List<TeamDTO> getSupervisorTeams(@PathVariable String Id){return supervisorService.getMyTeams(Id);}

    // view his projects
    @Operation(summary = "View My Projects")
    @GetMapping(value ="/{Id}/projects")
    public List<Project> getSupervisorProjects(@PathVariable String Id){return supervisorService.getMyProjects(Id);}

    // Delete comment
    @Operation(summary = "Delete comment with Id")
    @DeleteMapping(value ="/stages/{stageId}/comments/{commentId}")
    public String removeComment(@PathVariable String stageId, @PathVariable String commentId){return supervisorService.removeComment(stageId,commentId);}

    @Operation(summary = "Delete project with Id")
    @DeleteMapping(value ="/{supervisorId}/projects/{projectId}")
    public String removeProject(@PathVariable String supervisorId, @PathVariable String projectId){return supervisorService.removeProject(supervisorId,projectId);}

}
