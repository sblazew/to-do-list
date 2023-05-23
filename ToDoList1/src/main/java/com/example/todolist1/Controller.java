package com.example.todolist1;

import datamodel.ToDoData;
import datamodel.ToDoThing;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

public class Controller {


    @FXML
    private ListView<ToDoThing> todoListView;
    @FXML
    private TextArea taskDetailsTextArea;
    @FXML
    private Label deadlineLabel;

    //for deleting
    @FXML
    private ContextMenu listContextMenu;

    @FXML
    private BorderPane mainBorderPane;

    public void initialize(){

        listContextMenu = new ContextMenu();
        MenuItem deleteMenuTask = new MenuItem("Delete");

        deleteMenuTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToDoThing task = todoListView.getSelectionModel().getSelectedItem();
                deleteTask(task);
            }
        });
// poprawić bo nie działa
        MenuItem deleteMenuTask2 = new MenuItem("Edit");
        deleteMenuTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToDoThing task = todoListView.getSelectionModel().getSelectedItem();
                handleEdit();
            }
        });
        listContextMenu.getItems().addAll(deleteMenuTask);
        //displays task details automatically on start or on mouseclick
        todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoThing>() {
            @Override
            public void changed(ObservableValue<? extends ToDoThing> observableValue, ToDoThing toDoThing, ToDoThing t1) {
                if(t1 != null) {
                    ToDoThing task = todoListView.getSelectionModel().getSelectedItem();
                    taskDetailsTextArea.setText(task.getTaskDetails()); //task description
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd MM yyyy");
                    deadlineLabel.setText(dtf.format(task.getDeadline())); //task deadline
                }
            }
        });

        //Sorted list comparator by deadline
        SortedList<ToDoThing> sortedList = new SortedList<ToDoThing>(ToDoData.getInstance().getToDoThingsList(), new Comparator<ToDoThing>() {
            @Override
            public int compare(ToDoThing o1, ToDoThing o2) {
                return o1.getDeadline().compareTo(o2.getDeadline()); //-1 if lesser, 0 if equal, 1 if greater
            }
        });
        //populating list
        todoListView.setItems(sortedList);
        todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //select first as default
        todoListView.getSelectionModel().selectFirst();

        //coloring red stuff after deadline
        todoListView.setCellFactory(new Callback<ListView<ToDoThing>, ListCell<ToDoThing>>() {
            @Override
            public ListCell<ToDoThing> call(ListView<ToDoThing> toDoThingListView) {
                ListCell<ToDoThing> cell = new ListCell<ToDoThing>() {
                    @Override
                    protected void updateItem(ToDoThing toDoThing, boolean empty) {
                        super.updateItem(toDoThing, empty);
                        if(empty){
                            setText(null);
                        } else {
                            setText(toDoThing.getTaskDescription());
                            if(toDoThing.getDeadline().isBefore(LocalDate.now().plusDays(1))){
                                setTextFill(Color.RED);
                            }
                        }
                    }
                };
                //using cell factory to autoupdate cells (displayed data)
                //updating list so removed items are no longer displayed - used lambda expression in addlistener
                cell.emptyProperty().addListener(
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty) {
                                cell.setContextMenu(null);
                            } else{
                                cell.setContextMenu(listContextMenu);
                            }
                        }
                );
                return cell;
            }
        });
    }

    public void showNewItemDialog(){
        //creating instance of dialog class
        Dialog<ButtonType> dialog = new Dialog<>();
        //locking window (making modal) until decision is made;
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add new task to do");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoMenu.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch(IOException e){
            System.out.println("Loading dialog impossible.");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            MenuController controller = fxmlLoader.getController();
            ToDoThing newTask = controller.processResult();

            todoListView.getSelectionModel().select(newTask); //automatically selects new task

        }
    }
    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        ToDoThing selectedTask = todoListView.getSelectionModel().getSelectedItem();
        if(selectedTask != null){
            if(keyEvent.getCode().equals(KeyCode.DELETE)){
                deleteTask(selectedTask);
            }
        }
    }

    //displaying data in center window after clicking description
//    @FXML
//    public void handleClickListView(){
//        ToDoThing task = todoListView.getSelectionModel().getSelectedItem();
////adding date
//        taskDetailsTextArea.setText((task.getTaskDetails()));
//        deadlineLabel.setText(task.getDeadline().toString());
//    }

    public void deleteTask(ToDoThing task){
        //confirmation dialog
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete task");
        alert.setHeaderText("delete: " + task.getTaskDescription());
        alert. setContentText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && (result.get() == ButtonType.OK)){
            ToDoData.getInstance().deleteToDoThing(task);
        }
    }
    //deleting from menu
    @FXML
    public void handleDelete() {
        ToDoThing selectedTask = todoListView.getSelectionModel().getSelectedItem();
        if(selectedTask != null){
            deleteTask(selectedTask);
        }
    }
    @FXML
    public void handleEdit(){
        ToDoThing selectedTask = todoListView.getSelectionModel().getSelectedItem();
        if(selectedTask != null){
            Dialog<ButtonType> dialog = new Dialog<>();
            //locking window (making modal) until decision is made;
            dialog.initOwner(mainBorderPane.getScene().getWindow());
            dialog.setTitle("Edit task");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("todoMenu.fxml"));
            try{
                dialog.getDialogPane().setContent(fxmlLoader.load());
            }catch(IOException e){
                System.out.println("Couldn't load the Edit dialog.");
                e.printStackTrace();
                return;
            }
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
//something is missing



            Optional<ButtonType> result = dialog.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                MenuController controller = fxmlLoader.getController();
                controller.editTask(selectedTask);

                todoListView.getSelectionModel().select(selectedTask); //automatically selects edited task
            }
        }
    }

    @FXML
    public void handleExit() {
        Platform.exit();
    }


}