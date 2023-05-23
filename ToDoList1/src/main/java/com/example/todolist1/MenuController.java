package com.example.todolist1;

import datamodel.ToDoData;
import datamodel.ToDoThing;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class MenuController {

    @FXML
    private TextField descriptionArea;

    @FXML
    private TextArea detailsArea;

    @FXML
    private DatePicker deadlinePicker;

    //gathering user input, creating ToDoThing thingy and adding it to a list
    public ToDoThing processResult(){
        String descArea = descriptionArea.getText().trim();
        String detArea = detailsArea.getText().trim();
        LocalDate deadPick = deadlinePicker.getValue();

        ToDoThing newTask = new ToDoThing(descArea,detArea,deadPick);
        ToDoData.getInstance().addTask(newTask);
        return newTask;
    }


   public ToDoThing editTask (ToDoThing task){

       String descArea = descriptionArea.getText().trim();
     String detArea = detailsArea.getText().trim();
     LocalDate deadPick = deadlinePicker.getValue();

       task.setTaskDescription(descArea);
       task.setTaskDetails(detArea);
       task.setDeadline(deadPick);
       return task;
   }

}
