/*
Name: Carson Reilly
Course: CNT 4714 Spring 2024
Assignment title: Project 3 – A Two-tier Client-Server Application
Date: March 10, 2024
Class: CNT4714
*/

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
import javafx.scene.control.TextArea; //Lines 10-31 imports all of the native helper classes

public class SampleController {
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
	public Alert alert=new Alert(AlertType.NONE); //Lines 34-67 create all of the variables that are needed for the gui connections and text 
	public void initialize() { //This runs when the gui is created/initialized
		urlProperties.getItems().addAll("project3.properties","bikedb.properties"); //Adds the database properties files to the first drop down menu
		userProperties.getItems().addAll("root.properties","client1.properties","client2.properties"); //Adds the porperties files for the accounts to the second drop down menu
		connectionLine.setText("NO CONNECTION ESTABLISHED"); //Sets the text that displays connection status to "No connection established"
		disconnect.setDisable(true); //Sets the disconnect button to be disabled
		table.getItems().clear(); //Clears the table items to display as blank to start
		table.getColumns().clear(); //Clears the table columns to display no columns to start
		clear.setDisable(true); //Sets the clear button to disabled
		execute.setDisable(true); //Sets the execute button to disabled
		table.setPlaceholder(new Label("")); //Sets the placeholder value for the table to blank
	}
	public void connect(ActionEvent e) throws IOException, ClassNotFoundException, SQLException { //This runs when the connect button is clicked
		dbPath=urlProperties.getValue(); //Gets the value that is selected in the first drop down menu
		String userPath=userProperties.getValue(); //Gets the value that is selected in the second drop down menu
		BufferedReader reader = new BufferedReader(new FileReader(dbPath)); //Creates a reader for the file that is in the first drop down menu
		String currentLine=""; 
		String driver=""; //Lines 83-84 set strings that will be used later to blank
		while((currentLine=reader.readLine())!=null) { //Reads from the first properties file until there is a null line
			if(currentLine.contains("MYSQL_DB_DRIVER_CLASS=")) { //Checks for formatting that will be trimmed/replaced
				currentLine=currentLine.replaceAll("MYSQL_DB_DRIVER_CLASS=", ""); //Gets the altered version of the line (replaces indicator with blank space)
				driver=currentLine; //Sets the driver string to the altered current line
			}
			else if(currentLine.contains("MYSQL_DB_URL=")) { //Checks for second formatting that will be trimmed/replaced for second line
				currentLine=currentLine.replaceAll("MYSQL_DB_URL=", ""); //Gets the altered version of the line (replaces indicator with blank space)
				dbPath=currentLine; //Sets the database path string to the altered current line
			}
		}
		currentLine=""; //Set current line to blank value
		reader.close(); //Close the buffered reader
		BufferedReader reader2=new BufferedReader(new FileReader(userPath)); //Create second buffered reader that will read from second (account) properties file
		while((currentLine=reader2.readLine())!=null){ //Reads from the second properties file until there is a null value
			if(currentLine.contains("MYSQL_DB_USERNAME=")) { //Checks for first formatting that will be trimmed/replaced
				currentLine=currentLine.replaceAll("MYSQL_DB_USERNAME=", ""); //Replaces the indicator in the line with blank value
				usernameRead=currentLine; //Sets the username string to the altered line
			}
			else if(currentLine.contains("MYSQL_DB_PASSWORD=")) { //Checks for second formatting that will be trimmed/replaced
				currentLine=currentLine.replaceAll("MYSQL_DB_PASSWORD=", ""); //Replaces the indicator in the line with blank value
				passwordRead=currentLine; //Sets the password string to the altered line
			}
		}
		usernameInput=usernameBox.getText();
		passwordInput=passwordBox.getText(); //Lines 108-109 gets the text in the input fields and sets the corresponding strings to it
		usernameInput=usernameInput.trim();
		passwordInput=passwordInput.trim();
		usernameRead=usernameRead.trim();
		passwordRead=passwordRead.trim(); //Lines 110-113 trim the blank spaces on the strings set earlier
		int usernameCompare=usernameRead.compareTo(usernameInput); //Compares username inputted to the username read from the properties file
		int passwordCompare=passwordRead.compareTo(passwordInput); //Compares password inputted to the password read from the properties file
		if(usernameCompare==0&&passwordCompare==0) { //If the username and password match execute this chunk of code
		    Class.forName("com.mysql.cj.jdbc.Driver");//Sets driver for connecting
		    connection = DriverManager.getConnection(dbPath, usernameInput, passwordInput); //Sets connection given the database path, username inputted, and password inputted
		    dbMetaData = connection.getMetaData(); //Gets the metadata
		    current=dbMetaData.getUserName(); //Gets the username from the metadata
		    disconnect.setDisable(false); //Enables the disconnect button
		    connectionLine.setText("CONNECTED TO: "+dbPath); //Sets the text that shows the connection to show the database connected to 
		    clear.setDisable(false); //Enables the clear button
		    execute.setDisable(false); //Enables the execute button
		}
		else {
			connectionLine.setText("NOT CONNECTED - User Credentials Do Not Match Property File!"); //If the username or password don't match set the connetion line to show that the credentials don't match
		}
	}
	public void disconnect(ActionEvent e) throws SQLException { //Runs when the disconnect button is clicked
		connection.close(); //Closes the connection
		connectionLine.setText("NO CONNECTION ESTABLISHED"); //Sets the conenction text to show there is no current conection
		disconnect.setDisable(true); //Disables the disconnect button
		clear.setDisable(true); //Disables the clear button
		execute.setDisable(true); //Disables the execute button
		usernameRead="";
		usernameInput="";
		passwordRead="";
		passwordInput="";
		current=""; //Lines 136-140 set the global strings used to blank for reuse
	}
	public void clear(ActionEvent e) throws SQLException, IOException{ //Runs when the clear button is clicked
		commandEntry.setText(""); //Sets the entry field for the command to blank
		table.getItems().clear(); //Clears the table items 
		table.getColumns().clear(); //Clears the table columns
	}
	public void execute(ActionEvent e) throws SQLException, IOException { //Runs when the execute button is clicked
	    String st = commandEntry.getText(); //Creates and sets a string 
	    try {
	        Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); //Creates a statement on the current connection 
	        if (st.contains("select")) { //Checks to see if it is a select command
	            ResultSet resultSet = statement.executeQuery(st); //Creates resultSet that will execute the statement
	            table.getColumns().clear();
	            table.getItems().clear(); //Lines 153-154 clears the table contents before we set the new tables
	            metadata = resultSet.getMetaData(); //Gets the metadata of the statement
	            int colCount = metadata.getColumnCount(); //Gets the amount of columns
	            for (int i = 1; i <= colCount; i++) { //For loop used to create columns
	                final int columnIndex = i - 1; //Gets index prior to i variable for table usage
	                TableColumn<ObservableList<String>, String> column = new TableColumn<>(metadata.getColumnName(i)); //Creates new column
	                column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(columnIndex))); //Sets column so data can be inputted into cell
	                table.getColumns().add(column); //Adds column to table
	            }
	            while (resultSet.next()) { //Runs while there is more data in the resultSet
	                ObservableList<String> row = FXCollections.observableArrayList(); //Creates observableList object for rows
	                for (int i = 1; i <= colCount; i++) { //Loops through columns
	                    row.add(resultSet.getString(i)); //Adds the data from resultSet cast as a string
	                }
	                table.getItems().add(row); //Adds the row to the table
	            }
	            pathToOperations="jdbc:mysql://localhost:3306/operationslog"; //Creates path to operationslog database
	            connection2 = DriverManager.getConnection(pathToOperations,"project3app" ,"project3app" ); //Sets connection to operationslog database
	            DatabaseMetaData metadata = connection2.getMetaData(); //Gets metadata from connection
	            String query="select * from operationscount where login_username = \""+current+"\";"; //Creates string that will be used to query a select statement to see if the current user is in the database
	            Statement st2=connection2.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); //Creates the statement 
	            ResultSet rs2=st2.executeQuery(query); //Executes the statement
	            if(rs2.absolute(1)) { //If the user is already in the operationslog database run this code
	            	String query2="update operationscount set num_queries=num_queries+1 where login_username=\""+current+"\";"; //Set string to query that will update the number of queries by 1
	            	int rows2=st2.executeUpdate(query2); //Execute the query above
	            }
	            else {
	            	String query2="insert into operationscount (login_username,num_queries,num_updates) VALUES(\""+current+"\",1,0);"; //Set string to query to insert username into operationslog database if it doesn't exist already
	            	int rows=st2.executeUpdate(query2); //Execute the above query
	            }
	            connection2.close(); //Close the connection
	        } else { //Runs if not a select statement, but still a valid statement
	        	int rows=statement.executeUpdate(st); //Executes the statement
	        	alert.setAlertType(AlertType.CONFIRMATION);//Creates an error 
				alert.setTitle("Successful Update"); //Sets title of the error
				alert.setHeaderText(""); //Deletes error header
				alert.setContentText("Successful Update... "+rows+" rows updated"); //Sets the text inside of the error
				alert.show(); //Displays the error alert
				pathToOperations="jdbc:mysql://localhost:3306/operationslog"; //Sets path to operationslog database
	            connection2 = DriverManager.getConnection(pathToOperations,"project3app" ,"project3app" ); //Connects to operationslog database
	            DatabaseMetaData metadata = connection2.getMetaData(); //Gets metadata from connection
	            String query="select * from operationscount where login_username = \""+current+"\";"; //Creates string that will be used to query a select statement to see if the current user is in the database
	            Statement st2=connection2.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); //Creates statemetn
	            ResultSet rs2=st2.executeQuery(query); //Executes query 
	            if(rs2.absolute(1)) { //If username exists in operationslog database run this code chunk
	            	String query2="update operationscount set num_updates=num_updates+1 where login_username=\""+current+"\";"; //Creates string for query to update the num_updates column in operations log database associated with username
	            	int rows2=st2.executeUpdate(query2); //Executes query above
	            }
	            else {
	            	String query2="insert into operationscount (login_username,num_queries,num_updates) VALUES(\""+current+"\",0,1);"; //Creates string for query that inserts username into operationslog database
	            	int rows2=st2.executeUpdate(query2); //Executes query above
	            }
	        }
	    } catch (SQLException ex) { //Catches errors for any input that isn't a valid SQL command
	        ex.printStackTrace(); //Prints stack trace
	        alert.setAlertType(AlertType.ERROR);//Creates an error 
			alert.setTitle("Database Error"); //Sets title of the error
			alert.setHeaderText(""); //Deletes error header
			alert.setContentText(ex.getMessage()); //Sets the text inside of the error
			alert.show(); //Displays the error alert
	    }
	}
}

