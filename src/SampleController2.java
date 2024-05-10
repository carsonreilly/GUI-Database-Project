package application;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewFocusModel;
import javafx.scene.control.TextArea;

/*
Name: Carson Reilly
Course: CNT 4714 Spring 2024
Assignment title: Project 3 – A Specialized Accountant Application
Date: March 10, 2024
Class: CNT4714
*/

public class SampleController2 {
	@FXML 
	private ComboBox<String> urlProperties;
	@FXML
	private ComboBox<String> userProperties;
	@FXML
	private Button connect;
	@FXML
	private Button disconnect;
	@FXML
	private TextField usernameBox;
	@FXML
	private TextField passwordBox;
	private Connection connection;
	private Connection connection2;
	@FXML
	private Label connectionLine;
	@FXML
	private Button clear;
	@FXML
	private Button execute;
	@FXML
	private TextArea commandEntry;
	@FXML
	private TableView table;
	private String dbPath="";
	private String pathToOperations="";
	private ResultSetMetaData metadata;
	private String usernameRead="";
	private String passwordRead="";
	private String usernameInput="";
	private String passwordInput="";
	private DatabaseMetaData dbMetaData;
	private String current;
	public Alert alert=new Alert(AlertType.NONE); //Lines 3-66 initialize the elements of the gui and variables used later
	public void initialize() {  //Runs when program starts and gui is initialized
		urlProperties.getItems().addAll("operationslog.properties"); //Adds database properties file to first drop down 
		userProperties.getItems().addAll("theaccountant.properties");	//Adds user propreties file to second drop down
		disconnect.setDisable(true); //Disables disconnect button
		table.getItems().clear(); //Clears the items of the table
		table.getColumns().clear(); //Clears the columns of the table
		clear.setDisable(true); //Disables the clear button
		execute.setDisable(true); //Disables the execute button
		table.setPlaceholder(new Label("")); //Sets the placeholder value of the table to blank
	}
	public void connect(ActionEvent e) throws IOException, ClassNotFoundException, SQLException { //Runs when connect button is pressed
		dbPath=urlProperties.getValue(); //Sets string to value selected in first drop down
		String userPath=userProperties.getValue(); //Sets path string to value of second drop down
		BufferedReader reader = new BufferedReader(new FileReader(dbPath)); //Creates BufferedReader that will read from the database properties file
		String currentLine=""; 
		String driver=""; //Lines 81-82 create strings that will be used for later
		while((currentLine=reader.readLine())!=null) { //Reads from file as long as the line isn't null
			if(currentLine.contains("MYSQL_DB_DRIVER_CLASS=")) { //If the line contains the first indicator run this code
				currentLine=currentLine.replaceAll("MYSQL_DB_DRIVER_CLASS=", ""); //Replace the indicator with a blank space
				driver=currentLine; //Set the driver to the altered line
			}
			else if(currentLine.contains("MYSQL_DB_URL=")) { //If the line contains the second indicator run this code
				currentLine=currentLine.replaceAll("MYSQL_DB_URL=", ""); //Replace the indicator with a blank space
				dbPath=currentLine; //Set the database path to the altered line
			}
		}
		currentLine=""; //Reset the current line string to blank
		reader.close(); //Close the reader
		BufferedReader reader2=new BufferedReader(new FileReader(userPath)); //Create second buffered reader that reads in user properties file
		while((currentLine=reader2.readLine())!=null){ //Reads from user property file until there is a null line
			if(currentLine.contains("MYSQL_DB_USERNAME=")) { //If the line contains the first indicator run this code
				currentLine=currentLine.replaceAll("MYSQL_DB_USERNAME=", ""); //Replaces the indicator with a blank space
				usernameRead=currentLine; //Sets the username read to the altered line
			}
			else if(currentLine.contains("MYSQL_DB_PASSWORD=")) { //If the line contains the second indicator run this code
				currentLine=currentLine.replaceAll("MYSQL_DB_PASSWORD=", ""); //Replaces the indicator with a blank space
				passwordRead=currentLine; //Sets teh password read to the altered line
			}
		}
		usernameInput=usernameBox.getText();
		passwordInput=passwordBox.getText(); //Lines 106-107 sets the username and password input equal to strings
		usernameInput=usernameInput.trim();
		passwordInput=passwordInput.trim();
		usernameRead=usernameRead.trim();
		passwordRead=passwordRead.trim(); //Lines 108-111 trim all of the input and read strings
		int usernameCompare=usernameRead.compareTo(usernameInput); //Compares the username read to the username inputted
		int passwordCompare=passwordRead.compareTo(passwordInput);  //Compares the password read to the password inputted
		if(usernameCompare==0&&passwordCompare==0) { //If the username and password compares are equal to each other run this code
		    Class.forName("com.mysql.cj.jdbc.Driver"); //Sets driver for connection
		    connection = DriverManager.getConnection(dbPath, usernameInput, passwordInput); //Creates connection with database path, username, and password
		    dbMetaData = connection.getMetaData(); //Gets the metadata for the connection
		    current=dbMetaData.getUserName(); //Gets the username of the user connected to the connection
		    disconnect.setDisable(false); //Enables the disconnect button
		    connectionLine.setText("CONNECTED TO: "+dbPath); //Sets text for connection to show the current connection
		    clear.setDisable(false); //Enables the clear button
		    execute.setDisable(false); //Enables the execute button
		}
		else {
			connectionLine.setText("NOT CONNECTED - User Credentials Do Not Match Property File!"); //If the username or password don't match then set the connection text to show the credentials don't match
		}
	}
	public void disconnect(ActionEvent e) throws SQLException { //Runs when the disconnect button is pressed
		connection.close(); //Closes the connection
		connectionLine.setText("NO CONNECTION ESTABLISHED"); //Sets the connection text to show there is no current connection
		disconnect.setDisable(true); //Disables the disconnect button 
		clear.setDisable(true); //Disables the clear button
		execute.setDisable(true); //Disables the execute button
		usernameRead=""; 
		usernameInput="";
		passwordRead="";
		passwordInput="";
		current=""; //Lines 134-137 Resets all of the strings used to blank values
	}
	public void clear(ActionEvent e) throws SQLException, IOException{ //Runs when the clear button is clicked
		commandEntry.setText(""); //Sets the text in the sql command entry field to blank value
		table.getItems().clear(); //Clears the table items
		table.getColumns().clear(); //Clears the table columns
	}
	public void execute(ActionEvent e) throws SQLException, IOException { //Runs when the execute button is clicked
		String st = commandEntry.getText();  //Creates a string that is set to the text in the sql command entry field
	    try {
	        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); //Creates statement for the connection
	        if (st.contains("select")) { //Checks to see if the command string contains select
	            ResultSet resultSet = statement.executeQuery(st); //Executes the query
	            table.getColumns().clear();
	            table.getItems().clear(); //Lines 151-152 clear the table contents
	            metadata = resultSet.getMetaData(); //Gets the metadata from the query
	            int colCount = metadata.getColumnCount(); //Gets the amount of columns
	            for (int i = 1; i <= colCount; i++) { //Loops through columns
	                final int columnIndex = i - 1; //Gets the previous column
	                TableColumn<ObservableList<String>, String> column = new TableColumn<>(metadata.getColumnName(i)); //Creates a column with column name
	                column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(columnIndex))); //Sets cell to allow for data to be inputted
	                table.getColumns().add(column); //Adds the column to the table
	            }
	            while (resultSet.next()) { //Runs while the resultSet line isn't blank
	                ObservableList<String> row = FXCollections.observableArrayList(); //Creates observable list for row data
	                for (int i = 1; i <= colCount; i++) { //Loops through columns
	                    row.add(resultSet.getString(i)); //Adds the row with data from result set in the i'th column
	                }
	                table.getItems().add(row); //Add the row to the table
	            }
	            connection2.close(); //Close the connection
	        } else { //Runs if sql command string doesn't contain select
	        	int rows=statement.executeUpdate(st); //Executes the query
	        	alert.setAlertType(AlertType.CONFIRMATION);//Creates an error 
				alert.setTitle("Successful Update"); //Sets title of the error
				alert.setHeaderText(""); //Deletes error header
				alert.setContentText("Successful Update... "+rows+" rows updated"); //Sets the text inside of the error
				alert.show(); //Displays the error alert
	        }
	    } catch (SQLException ex) { //Runs if text is entered that isn't a valid sql command
	        ex.printStackTrace(); //Prints stack trace
	        alert.setAlertType(AlertType.ERROR);//Creates an error 
			alert.setTitle("Database Error"); //Sets title of the error
			alert.setHeaderText(""); //Deletes error header
			alert.setContentText(ex.getMessage()); //Sets the text inside of the error
			alert.show(); //Displays the error alert
	    }
	}
}
