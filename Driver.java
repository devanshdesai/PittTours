import java.sql.*;  //import the file containing definitions for the parts
import java.util.Scanner;
//import java.text.ParseException;

public class Driver {
	private static Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
    private ResultSet resultSet; //used to hold the result of your query (if one exists)
    private String query;  //this will hold the query we are using

	public Driver(){
		System.out.println("Hello, if you would like the administrator interface, press [1] and [return]");
		Scanner scan = new Scanner(System.in);
		int mode = scan.nextInt();
		int operation = 0;
		if(mode == 1) {
			while(){
				System.out.println("Choose an operation: \n 
					[1] Erase the database \n
					[2] Load airline information \n
					[3] Load schedule information \n
					[4] Load pricing information \n
					[5] Load plane information \n
					[6] Generate passenger manifest for specific flight on given day \n
					[7] Exit");
				operation = scan.nextInt();
				switch (operation) {
					case 1:
						System.out.println("Are you sure? [y/n]");
						String response = scan.nextLine();
						if(response.equals("y")) 
						break;
					case 2:
						break;
					case 3:
						break;
					case 4:
						break;
					case 5:
						break;
					case 6:
						break;
					case 7:
						System.exit(0);
					default:
						System.out.println("Not a valid operation code")
				}
			}
		}
		else{
			while(){
				System.out.println("Choose an operation: \n 
					[1] Add customer \n
					[2] Show customer info, given customer name \n
					[3] Find price for flights between two cities \n
					[4] Find all routes between two cities \n
					[5] Find all routes between two cities of a given airline \n
					[6] Find all routes with available seats between two cities on given day \n
					[7] For a given airline, find all routes with available seats between two cities on given day \n
					[8] Add reservation \n
					[9] Show reservation info, given reservation number \n
					[10] Buy ticket from existing reservation\n
					[11] Exit");
				operation = scan.nextInt();
				switch (operation) {
					case 1:
						break;
					case 2:
						break;
					case 3:
						break;
					case 4:
						break;
					case 5:
						break;
					case 6:
						break;
					case 7:
						break;
					case 8:
						break;
					case 9:
						break;
					case 10:
						break;
					case 11:
						System.exit(0);
					default:
						System.out.println("Not a valid operation code")
				}
			}
		}
	}

	private void eraseDatabase(){

	}
	private void loadAirline(){

	}
	private void loadSchedule(){
		
	}
	private void loadPrice(){
		
	}
	private void loadPlane(){
		
	}
	private void passengerManifest(){
		
	}
	private void addCustomer(){
		
	}
	private void showCustomer(){
		
	}
	private void findPrice(){
		
	}
	private void routesBetweenCities(){
		
	}
	private void routesBetweenCitiesOnAirline(){
		
	}
	private void availableSeats(){
		
	}
	private void availableSeatsOnAirline(){
		
	}
	private void addReservation(){
		
	}
	private void showReservation(){
		
	}
	private void buyTickets(){
		
	}


	public static void main(String args[]) throws SQLException {
		String username = "kwz5";
		String password = "asdfj";

		try{
		    
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		  	String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass"; 
			
			connection = DriverManager.getConnection(url, username, password); 
			Driver driver = new Driver();
		}
		catch(Exception Ex)  {
			System.out.println("Error connecting to database.  Machine Error: " +
				Ex.toString());
		}
		finally
		{
			connection.close();
		}
	}
}