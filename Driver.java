import java.sql.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
//import java.text.ParseException;

public class Driver {
	private static Connection connection;
    private Statement statement;
    private PreparedStatement prepStatement;
    private ResultSet resultSet;
    private String query;

	public Driver() {
		System.out.println("Hello, if you would like the administrator interface, press [1] and [return]. For the user interface, press [2] and [return].");
		Scanner scan = new Scanner(System.in);
		int mode = scan.nextInt();
		int operation = 0;
		String response;

		if (mode == 1) {
			while (true) {
				System.out.println("Choose an operation: \n"
					+ "[1] Erase the database \n"
					+ "[2] Load airline information \n"
					+ "[3] Load schedule information \n"
					+ "[4] Load pricing information \n"
					+ "[5] Load plane information \n"
					+ "[6] Generate passenger manifest for specific flight on given day \n"
					+ "[7] Exit");
				operation = scan.nextInt();
				switch (operation) {
					case 1:
						System.out.println("Are you sure you want to erase all tuples? [y/n]");
						scan.skip("\n");
						response = scan.nextLine();
						if (response.equals("y")) {
							eraseDatabase();
						}
						else {
							System.out.println("The database was unchanged.\n");
							break;
						}
					case 2:
						System.out.println("Enter file name");
						scan.skip("\n");
						response = scan.nextLine();
						loadAirline(response);
						break;
					case 3:
						System.out.println("Enter file name");
						scan.skip("\n");
						response = scan.nextLine();
						loadSchedule(response);
						break;
					case 4:
						System.out.println("Enter file name");
						scan.skip("\n");
						response = scan.nextLine();
						loadSchedule(response);
						break;
					case 5:
						System.out.println("Enter file name");
						scan.skip("\n");
						response = scan.nextLine();
						loadPlane(response);
						break;
					case 6:
						System.out.println("Enter flight number");
						scan.skip("\n");
						String flight = scan.nextLine();
						System.out.println("Enter date [MM-DD-YYYY]");
						scan.skip("\n");
						String date = scan.nextLine();
						passengerManifest(flight, date);
						break;
					case 7:
						System.exit(0);
					default:
						System.out.println("Not a valid operation code");
				}
			}
		}
		else {
			while (true) {
				System.out.println("Choose an operation: \n"
					+ "[1] Add customer \n"
					+ "[[2] Show customer info, given customer name \n"
					+ "[[3] Find price for flights between two cities \n"
					+ "[[4] Find all routes between two cities \n"
					+ "[[5] Find all routes between two cities of a given airline \n"
					+ "[[6] Find all routes with available seats between two cities on given day \n"
					+ "[[7] For a given airline, find all routes with available seats between two cities on given day \n"
					+ "[[8] Add reservation \n"
					+ "[[9] Show reservation info, given reservation number \n"
					+ "[[10] Buy ticket from existing reservation\n"
					+ "[[11] Exit");
				operation = scan.nextInt();
				String cityA;
				String cityB;
				String date;
				String airline;
				String reservation;

				switch (operation) {
					case 1:
						addCustomer();
						break;
					case 2:
						System.out.println("Enter customer name");
						scan.skip("\n");
						String name = scan.nextLine();
						showCustomer(name);
						break;
					case 3:
						System.out.println("Enter start city (eg. PIT)");
						scan.skip("\n");
						cityA = scan.nextLine();
						System.out.println("Enter end city");
						scan.skip("\n");
						cityB = scan.nextLine();
						findPrice(cityA, cityB);
						break;
					case 4:
						System.out.println("Enter start city (eg. PIT)");
						scan.skip("\n");
						cityA = scan.nextLine();
						System.out.println("Enter end city");
						scan.skip("\n");
						cityB = scan.nextLine();
						routesBetweenCities(cityA, cityB);
						break;
					case 5:
						System.out.println("Enter start city (eg. PIT)");
						scan.skip("\n");
						cityA = scan.nextLine();
						System.out.println("Enter end city");
						scan.skip("\n");
						cityB = scan.nextLine();
						System.out.println("Enter airline code (eg. AAL)");
						scan.skip("\n");
						airline = scan.nextLine();
						routesBetweenCitiesOnAirline(cityA, cityB, airline);
						break;
					case 6:
						System.out.println("Enter start city (eg. PIT)");
						scan.skip("\n");
						cityA = scan.nextLine();
						System.out.println("Enter end city");
						scan.skip("\n");
						cityB = scan.nextLine();
						System.out.println("Enter date [MM-DD-YYYY]");
						scan.skip("\n");
						date = scan.nextLine();
						availableSeats(cityA, cityB, date);
						break;
					case 7:
						System.out.println("Enter start city (eg. PIT)");
						scan.skip("\n");
						cityA = scan.nextLine();
						System.out.println("Enter end city");
						scan.skip("\n");
						cityB = scan.nextLine();
						System.out.println("Enter date [MM-DD-YYYY]");
						scan.skip("\n");
						date = scan.nextLine();
						System.out.println("Enter airline code (eg. AAL)");
						scan.skip("\n");
						airline = scan.nextLine();
						availableSeats(cityA, cityB, date, airline);
						break;
					case 8:
						int leg = 0;
						String flights[] = new String[4];
						String dates[] = new String[4];

						while (leg < 4) {
							System.out.println("Enter flight number or [0] if you are finished");
							scan.skip("\n");
							String flightNumber = scan.nextLine();

							if (flightNumber.equals("0")) {
								break;
							}

							flights[leg] = flightNumber;
							System.out.println("Enter date [MM-DD-YYYY]");
							scan.skip("\n");
							date = scan.nextLine();
							dates[leg] = date;
							leg++;
						}
						addReservation(flights,dates);
						break;
					case 9:
						System.out.println("Enter reservation number");
						scan.skip("\n");
						reservation = scan.nextLine();
						showReservation(reservation);
						break;
					case 10:
						System.out.println("Enter reservation number");
						scan.skip("\n");
						reservation = scan.nextLine();
						buyTickets(reservation);
						break;
					case 11:
						System.exit(0);
					default:
						System.out.println("Not a valid operation code");
						scan.skip("\n");
				}
			}
		}
	}

	private void eraseDatabase() {
		try {
			Statement s = connection.createStatement();
			String sql = "DELETE * FROM Reservation_Detail;DELETE * FROM Reservation;DELETE * FROM Price;DELETE * FROM Customer;DELETE * FROM Flight;DELETE * FROM Plane;DELETE * FROM Airline;";
			s.executeUpdate(sql);

			System.out.println("Database was deleted.");
		}
		catch (Exception e) {
			System.out.println("Error deleting the database. " + e.toString());
		}
	}
	private void loadAirline(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			Statement s;
			String sql;
			String[] airline;

			while ((line = br.readLine()) != null) {
				airline = line.split(",");
				sql = "INSERT INTO Airline VALUES('" + airline[0] + "', '" + airline[1] + "', '" + airline[2] + "', " + airline[3] + ");";
				s.executeUpdate(sql);
			}

			System.out.println("Airlines were loaded from " + filename);
		}
		catch (FileNotFoundException e) {
			System.out.println("The file was not found.\n");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void loadSchedule(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			Statement s;
			String sql;
			String[] flight;

			while ((line = br.readLine()) != null) {
				flight = line.split(",");
				sql = "INSERT INTO Flight VALUES('" + flight[0] + "', '" + flight[1] + "', '" + flight[2] + "', '" + flight[3] + "', '" + flight[4] + "', '" + flight[5] + "', '" + flight[6] + "', '" + flight[7] + "');";
				s.executeUpdate(sql);
			}

			System.out.println("Flights were loaded from " + filename);
		}
		catch (FileNotFoundException e) {
			System.out.println("The file was not found.\n");
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void loadPrice(String filename) {

	}
	private void loadPlane(String filename) {

	}
	private void passengerManifest(String flightNumber, String date) {

	}
	private void addCustomer() {

	}
	private void showCustomer(String name) {

	}
	private void findPrice(String cityA,String cityB) {

	}
	private void routesBetweenCities(String cityA,String cityB) {

	}
	private void routesBetweenCitiesOnAirline(String cityA,String cityB, String airline) {

	}
	private void availableSeats(String cityA,String cityB, String date) {

	}
	private void availableSeats(String cityA,String cityB, String date, String airline) {

	}
	private void addReservation(String flights[], String dates[]) {

	}
	private void showReservation(String reservation) {

	}
	private void buyTickets(String reservation) {

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
