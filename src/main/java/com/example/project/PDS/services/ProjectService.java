package com.example.project.PDS.services;

import com.example.project.PDS.DTO.projectDTO;
import com.example.project.PDS.models.*;
import com.example.project.PDS.repository.ProjectRepo;
import com.example.project.PDS.repository.SupervisorRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class ProjectService {
    private ProjectRepo projectRepo;
    private final SupervisorRepo supervisorRepo;
    private final StagesService stagesService;

    //Create a project
    public String createProject(projectDTO projectDto, String supervisorId){

        Supervisor supervisor = supervisorRepo.findById(supervisorId)
                .orElseThrow(() -> new RuntimeException("Supervisor not found"));

        Team newTeam = new Team(
                "myTeam"
        );
        Member member = new Member(
                supervisor.getName(),supervisor.getEmail(),"supervisor"
        );
        newTeam.getMembers().add(member);


        Project newProject = new Project(
                projectDto.title,projectDto.description,projectDto.timeline,newTeam
        );
        newProject.setTeam(newTeam);

        newProject.setSupervisorId(supervisor);
        Project project = projectRepo.save(newProject);
        supervisor.getProjects().add(project);
        supervisorRepo.save(supervisor);

        return project.getId();
    }
    //Update a project

    //Add stages to the project

    // View Project
    public Project getProject(String Id){
        return projectRepo.findById(Id)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    public List<Project> getAllProjects(){
        return projectRepo.findAll();
    }

    // View Project stages
    public List<Stage> getProjectStages(String Id){
        return projectRepo.findById(Id).get().getStages();
    }

    public List<Stage> getStageByProject(String projectId) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return project.getStages();
    }

    public List<Task> getTasksByStage(String stageId) {
        return stagesService.getTaskByStage(stageId);

    }

    public List<Comment> getCommentsByStage(String stageId) {
        return stagesService.getCommentByStage(stageId);
    }

    public String uploadSpec(String projectId) {
        return "uploaded spec";
    }

    public String uploadClassD(String projectId) {
        return "uploaded Class Diagram";
    }

    public String uploadUseCaseD(String projectId) {
        return "uploaded Use Case Diagram";
    }


    //update a project

    //Delete project - delete stages - delete tasks
}
