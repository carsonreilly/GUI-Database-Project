/*
Name: Carson Reilly
Course: CNT 4714 Spring 2024
Assignment title: Project 3 – A Two-tier Client-Server Application - Main 
Date: March 10, 2024
Class: CNT4714
*/


package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("Sample.fxml")); //Creates borderpane for client gui and connects it to controller java file
			Scene scene = new Scene(root,700,600); //Creates scene for client gui
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); //Adds style sheet for client gui
			primaryStage.setTitle("SQL Client Application - (CR - CNT 4714 - Spring 2024 - Project 3)"); //Sets the name of the GUI for client gui
			primaryStage.setScene(scene); //Sets the current stage with the scene for client gui
			primaryStage.show(); //Displays the client gui
			Stage stage2=new Stage(); //Creates scene for accountant gui
			BorderPane root2 = FXMLLoader.load(getClass().getResource("Sample2.fxml")); //Connects borderpane for accountant gui to second controller java file
			Scene scene2 = new Scene(root2,700,600); //Creates scene for accountant gui
			scene2.getStylesheets().add(getClass().getResource("application.css").toExternalForm()); //Adds style sheet for accountant gui
			stage2.setTitle("SPECIALIZED ACCOUTANT APPLICATION - (CR - CNT 4714 - Spring 2024 - Project 3)"); //Sets the name of the GUI
			stage2.setScene(scene2); //Sets the current stage with scene for accountant gui
			stage2.show(); //Displays the accountant gui

		} catch(Exception e) {
			e.printStackTrace(); //Prints error if there is any
		}
	}
	
	public static void main(String[] args) {
		launch(args); //Runs start 
	}
}
