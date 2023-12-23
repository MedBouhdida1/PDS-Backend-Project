package com.example.project.PDS.services;


import com.example.project.PDS.DTO.TeamDTO;
import com.example.project.PDS.DTO.commentDTO;
import com.example.project.PDS.DTO.projectDTO;
import com.example.project.PDS.DTO.userDTO;
import com.example.project.PDS.Exceptions.ObjectNotFoundException;
import com.example.project.PDS.models.*;
import com.example.project.PDS.repository.SupervisorRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class SupervisorService {
    private final SupervisorRepo supervisorRepo;
    private final ProjectService projectService;
    private final StagesService stagesService;

    // create supervisor
    public String createSupervisor(userDTO user){
        Supervisor supervisor = new Supervisor(
                user.name, user.email
        );
        return supervisorRepo.save(supervisor).getId();
    }
    // get supervisor
    public Supervisor getSupervisor(String Id){
        return supervisorRepo.findById(Id)
                .orElseThrow(() -> new ObjectNotFoundException("Supervisor not found"));
    }

    public Supervisor getSupervisorByName(String name){
        Supervisor supervisor=supervisorRepo.findByName(name);
        if (supervisor == null)
            throw new ObjectNotFoundException("Supervisor not found");
        return supervisor;
    }
    // update supervisor

    public String updateSupervisor(String supervisorId, userDTO userDto) {
        Supervisor updatedSup = getSupervisor(supervisorId);
        updatedSup.setEmail(userDto.email);
        updatedSup.setName(userDto.name);
        return supervisorRepo.save(updatedSup).getId();
    }
    // delete account
    public String deleteSupervisor(String Id){ //to Modify (delete all entities that he has relation with)
        Supervisor supervisor = getSupervisor(Id);
        supervisorRepo.delete(supervisor);
        return "supervisor "+supervisor.getName()+" is deleted";
    }
    public String deleteSupervisorByName(String name) {
        Supervisor supervisor = supervisorRepo.findByName(name);
        if (supervisor == null)
            throw new ObjectNotFoundException("Supervisor not found");
        supervisorRepo.delete(supervisor);
        return "supervisor "+supervisor.getName()+" is deleted";
    }
    public List<Supervisor> getAllSupervisor(){
        return supervisorRepo.findAll();
    }



    // Add projects
    public String AddProject(String supervisorId, projectDTO project){
        return projectService.createProject(project ,supervisorId);
    }
    //// View project details
        // View/download document
        // view his projects / Teams
        // view project stages / tasks
    public List<Project> getMyProjects (String Id){
        Supervisor supervisor = getSupervisor(Id);
        return supervisor.getProjects();
    }


    // Add comments on each stage
    public String addComment(String Id,String stageId, commentDTO commentDto){
        Supervisor supervisor = getSupervisor(Id);
        if (!supervisor.checkForStage(stageId))
            throw new ObjectNotFoundException("Stage not found");
        return stagesService.addComment(stageId,commentDto).getId();
    }

    public List<TeamDTO> getMyTeams(String Id) {
        Supervisor supervisor = getSupervisor(Id);
        List<Project> myProjects = supervisor.getProjects();
        List<TeamDTO> myTeams = new ArrayList<>();
        for(Project project : myProjects){
            TeamDTO teamDto = new TeamDTO(
                    project.getTeam(),
                    project.getTitle()
            );
            myTeams.add(teamDto);
        }
        return myTeams;
    }

    public String removeComment(String stageId, String commentId) {
        Stage stage = stagesService.getStage(stageId);
        if (!stage.checkCommentId(commentId))
            throw new IllegalArgumentException("Comment with Id " + commentId + " not found in stage " + stageId);

        stage.removeCommentById(commentId);
        stagesService.saveStage(stage);
        return "Comment:" +" "+"'"+ commentId + "'"+" " + "is removed";
    }

    public String removeProject(String Id,String projectId){
        Supervisor supervisor = getSupervisor(Id);
        for (Project project : supervisor.getProjects()){
            if (project.getId().equals(projectId)){
                supervisor.removeProject(projectId);
                supervisorRepo.save(supervisor);
                projectService.deleteProject(projectId);
                return "Project:" +" "+"'"+ projectId + "'"+" " + "is removed";
            }
        }
        throw new ObjectNotFoundException("Project not found");

    }




    // Delete comments

    // Query / search / insights
}
