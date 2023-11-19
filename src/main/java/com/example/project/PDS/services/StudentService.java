package com.example.project.PDS.services;

import com.example.project.PDS.DTO.taskDTO;
import com.example.project.PDS.DTO.userDTO;
import com.example.project.PDS.Exceptions.ObjectNotFoundException;
import com.example.project.PDS.models.*;
import com.example.project.PDS.repository.StudentRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {
    private final StudentRepo studentRepo;
    private final ProjectService projectService;
    private final StagesService stagesService;

    // Create a student
    public String addStudent(userDTO user){
        Student student = new Student(
                user.name, user.email
        );
        return studentRepo.save(student).getId();
    }
    public Student getStudent(String Id) {
        return studentRepo.findById(Id)
                .orElseThrow(() -> new ObjectNotFoundException("Student not found"));
    }
    public List<Student> getAllStudent() {
        return studentRepo.findAll();
    }
    // Update a student
    public String UpdateStudent(String Id, String Name, String Email){
        Student student = getStudent(Id);
        student.setName(Name);
        student.setEmail(Email);
        return studentRepo.save(student).getId();
    }
    // Delete a student
    public String deleteStudent(String Id){
        Student student = getStudent(Id);
        studentRepo.delete(student);
        return "student "+" "+student.getName()+" "+"is deleted";
    }

    // Select project - Add student to team
    public String EnrollProject(String studentId, String projectId){
        Student student = getStudent(studentId);
        Project project = projectService.getProject(projectId);
        Team team = project.getTeam();

        Member member = new Member(student.getName(),student.getEmail());
        team.getMembers().add(member);

        student.setTeam(team);
        project.setTeam(team);
        student.setProject(project);
        studentRepo.save(student);
        projectService.saveProject(project);
        return student.getName() + "Enrolled in project" + project.getTitle();
    }

    public String leaveProject(String Id, String projectId) {
        Student student = getStudent(Id);
        Project project = projectService.getProject(projectId);
        if (!student.getProject().getId().equals(projectId))
            throw new ObjectNotFoundException("Student not enrolled in this project");

        student.setTeam(null);
        student.setProject(null);
        Team team = project.getTeam();
        team.removeMember(student.getEmail());
        project.setTeam(team);

        studentRepo.save(student);
        projectService.saveProject(project);
        return "student "+ student.getName() + "has left project:" +project.getTitle();
    }

    public String createTask(String Id,String stageId,taskDTO taskDto){
        return stagesService.addTask(Id,stageId,taskDto);
    }

    public List<Stage> getStages(String Id){
        Project myProject = getMyProject(Id);
        return myProject.getStages();
    }
    public List<Task> getMyTasks(String Id){
        Student student = getStudent(Id);
        List<Stage> myStages = getStages(Id);
        List<Task> MyTasks = new ArrayList<>();
        for (Stage stage : myStages){
            for (Task task : stage.getTasks()){
                if (task.getCreatedby().equals(student.getName()))
                    MyTasks.add(task);
            }
        }
        return MyTasks;
    }

    public String removeTaskByTitle(String stageId, String taskTitle) {
        Stage stage = stagesService.getStage(stageId);
        if (!stage.checkTask(taskTitle))
            throw new ObjectNotFoundException("Task with title " + taskTitle + " not found in stage " + stageId);

        stage.removeTask(taskTitle);
        stagesService.saveStage(stage);
        return "Taks:" +" "+"'"+ taskTitle + "'"+" " + "is removed";
    }

    public Project getMyProject(String studentId) {
        Student student = getStudent(studentId);
        return student.getProject();
    }

    public Team getMyTeam(String studentId) {
        Student student = getStudent(studentId);
        return student.getTeam();
    }

}
