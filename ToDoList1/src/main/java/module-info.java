module com.example.todolist1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.todolist1 to javafx.fxml;
    exports com.example.todolist1;
}