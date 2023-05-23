package datamodel;

import java.time.LocalDate;

public class ToDoThing {
    private String taskDescription;
    private String taskDetails;
    private LocalDate deadline;

    public ToDoThing(String taskDescription, String taskDetails, LocalDate deadline) {
        this.taskDescription = taskDescription;
        this.taskDetails = taskDetails;
        this.deadline = deadline;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(String taskDetails) {
        this.taskDetails = taskDetails;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

}
