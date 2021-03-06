import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import users.Flight;

public class MainMenuCustTest extends Database implements Initializable {
	@FXML private javafx.scene.control.Button add;
	@FXML private javafx.scene.control.Button delete;
	@FXML private javafx.scene.control.TextField searchField;
	
	
	@FXML private TableView<Flight> availableFlights;
	
	@FXML private TableColumn<Flight, Integer> flightNumberColumn;
	@FXML private TableColumn<Flight, String> fromColumn;
	@FXML private TableColumn<Flight, String> toColumn;
	@FXML private TableColumn<Flight, SimpleDateFormat> departureDateColumn;
	@FXML private TableColumn<Flight, SimpleDateFormat> arrivalDateColumn;
	@FXML private TableColumn<Flight, String> numsOfPassengersColumn;
	
	ObservableList<Flight> listOfFlights = FXCollections.observableArrayList();
	
	@Override
	public void initialize (URL Location, ResourceBundle resources) {
		
		try {
			Connection connection = getConnection();
			
			ResultSet results = connection.createStatement().executeQuery("SELECT flightnumber, cityfrom, cityto, DATE_FORMAT(departure,'%M %e, %Y at %r'), DATE_FORMAT(arrival,'%M %e, %Y at %r'), numberofpassengers FROM Flight;");
			
			while (results.next()) {
				listOfFlights.add(new Flight(
						Integer.parseInt(results.getString("flightnumber")), 
						results.getString("cityfrom"), 
						results.getString("cityto"), 
						results.getString("DATE_FORMAT(departure,'%M %e, %Y at %r')"), 
						results.getString("DATE_FORMAT(arrival,'%M %e, %Y at %r')"), 
						Integer.parseInt(results.getString("numberofpassengers"))));
			}
		}
		catch (Exception e) {
			System.out.print(e);
		}
		
		flightNumberColumn.setCellValueFactory(new PropertyValueFactory<>("flightNumber")); // these are the variables from the class
		fromColumn.setCellValueFactory(new PropertyValueFactory<>("cityFrom"));
		toColumn.setCellValueFactory(new PropertyValueFactory<>("cityTo"));
		departureDateColumn.setCellValueFactory(new PropertyValueFactory<>("departure"));
		arrivalDateColumn.setCellValueFactory(new PropertyValueFactory<>("arrival"));
		numsOfPassengersColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfPassengers"));
		
		availableFlights.setItems(listOfFlights);
		
	}
	
	@FXML
	private void switchToSignIn(ActionEvent event) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("UI.fxml")); //get FMXL file
		
		Scene scene = new Scene(root);
		Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(scene);
		window.show();
	}
	
	
	@Override
	protected void insertStatement(String table, String query, ActionEvent event) throws Exception {
		Connection connection = getConnection();
		
		try {
			PreparedStatement insertStatement = connection.prepareStatement(String.format("INSERT INTO %s VALUES (%s);", table, query));
			insertStatement.executeUpdate();
			
			// if successful
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Success");
			alert.setHeaderText("Table added!");
			
			alert.showAndWait();
			
			
		}
		catch (Exception e) { // if not successful
			Alert alert = new Alert(Alert.AlertType.ERROR);
			
			alert.setTitle("Unsuccessful");
			alert.setHeaderText("Information rejected.");
			alert.setContentText("Expand the dialog below to see why...");
			
			TextArea area = new TextArea(e.toString());
			alert.getDialogPane().setExpandableContent(area);
			alert.showAndWait();
			
		}
		finally {
			connection.close();
		}
	}
	
	private static String parseStringDateIntoDBDate(String str) { // this has to be called when making a query from the table, because the table has a different format than the database
		String[] splittedString = str.split(" ");
		
		String year = splittedString[2];
		String month = null;
		
		switch(splittedString[0]) {
		case "January":
			month = "1"; break;
		case "Feburary":
			month = "2"; break;
		case "March":
			month = "3"; break;
		case "April":
			month = "4"; break;
		case "May":
			month = "5"; break;
		case "June":
			month = "6"; break;
		case "July":
			month = "7"; break;
		case "August":
			month = "8"; break;
		case "September":
			month = "9"; break;
		case "October":
			month = "10"; break;
		case "November":
			month = "11"; break;
		case "December":
			month = "12"; break;
		}
		
		String day = splittedString[1].replaceAll("[^0-9]", "");
		
		String time = null;
		
		if (splittedString[5].equals("PM")) {
			String[] splittedTime = splittedString[4].split(":");
			if (Integer.parseInt(splittedTime[0]) >= 1) {
				int temp = Integer.parseInt(splittedTime[0]) + 12;
				
				time = Integer.toString(temp) + ":" + splittedTime[1] + ":" + splittedTime[2];
			}
			else {
				time = splittedString[4];
			}
		}
		else {
			time = splittedString[4];
		}
		
		return String.format("%s-%s-%s %s", year, month, day, time);
	}
	
	private void addFlight() {
		 
	}
}
