package datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class ToDoData {

    private static ToDoData instance = new ToDoData();
    private static String filename = "taskList.txt";
//databinding
    private ObservableList<ToDoThing> toDoThingsList;
    private DateTimeFormatter formatter;

    public static ToDoData getInstance() {
        return instance;
    }

    private ToDoData() {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public ObservableList<ToDoThing> getToDoThingsList() {
        return toDoThingsList;
    }

    public void addTask(ToDoThing task) {
        toDoThingsList.add(task);
    }

    public void loadFile() throws IOException {
        toDoThingsList = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);
        String input;

        try {
            while((input = br.readLine()) != null){
                String[] itemPieces = input.split("\t"); //splits data read from file, delimiter = tab
                //converting data to array
                String taskDescription = itemPieces[0];
                String taskDetails = itemPieces[1];
                String deadline = itemPieces[2];

                LocalDate date = LocalDate.parse(deadline, formatter);
                ToDoThing toDoThings = new ToDoThing(taskDescription, taskDetails, date);
                toDoThingsList.add(toDoThings);
            }
        } finally {
            if(br != null){
                br.close();
            }
        }
    }

    public void saveFile() throws IOException{
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try{
//iterating through list and saving
        Iterator<ToDoThing> iter = toDoThingsList.iterator();
        while(iter.hasNext()){
            ToDoThing item = iter.next();
            bw.write(String.format("%s\t%s\t%s",
                    item.getTaskDescription(),
                    item.getTaskDetails(),
                    item.getDeadline().format(formatter)));
            bw.newLine();
        }

        }finally {
            //testing if object if valid before trying to close it, so there is no other exception
            if(bw != null){
                bw.close();
            }
        }
    }
    public void deleteToDoThing(ToDoThing task) {
        toDoThingsList.remove(task);
    }
    //poprawić
    public void setToDoThingsList(){

    }
}
