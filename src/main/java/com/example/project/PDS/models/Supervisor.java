package com.example.project.PDS.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Document(collection = "supervisors")
@Data
@NoArgsConstructor
public class Supervisor {

    @Id
    private String id;

    private String name;
    private String email;

    @DBRef(lazy = false)
    private List<Project> projects;


    public Supervisor(String name, String email) {
        this.name = name;
        this.email = email;
        this.projects = new ArrayList<>();
    }

    public boolean checkProjectId(String Id){
        for(Project project : this.projects){
            if (project.getId().equals(Id))
                return true;
        }
        return false;
    }
    public void removeProject(String Id){
        this.projects.removeIf(task -> task.getId().equals(Id));
    }

    public boolean checkForStage(String stageId){
        for(Project project : this.projects){
            for (Stage stage : project.getStages()){
                if (stage.getId().equals(stageId))
                    return true;
            }
        }
        return false;
    }


}
