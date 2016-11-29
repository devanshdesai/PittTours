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
						System.out.println("Would you like to load pricing information [L] or change the price of an exisiting flight [C]? [L/C]");
						scan.skip("\n");
						response = scan.nextLine();
						if ((response.toUpperCase()).equals("L")) {
							System.out.println("Enter file name");
							scan.skip("\n");
							response = scan.nextLine();
						}
						else if ((response.toUpperCase()).equals("C")) {
							System.out.println("Enter the departure city");
							scan.skip("\n");
							String departureCity = scan.nextLine();
							System.out.println("Enter the arrival city");
							scan.skip("\n");
							String arrivalCity = scan.nextLine();
							System.out.println("Enter the new high price");
							scan.skip("\n");
							int highPrice = scan.nextInt();
							System.out.println("Enter the new low price");
							scan.skip("\n");
							int lowPrice = scan.nextInt();
							changePrice(departureCity, arrivalCity, highPrice, lowPrice);
						}
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
			Statement s = connection.createStatement();
			String line;
			String sql;
			String[] airline;

			while ((line = br.readLine()) != null) {
				airline = line.split(",");
				sql = "INSERT INTO Airline VALUES('" + airline[0] + "', '" + airline[1] + "', '" + airline[2] + "', " + airline[3] + ");";
				s.executeUpdate(sql);
			}

			br.close();
			System.out.println("Airlines were loaded from " + filename + ".\n");
		}
		catch (FileNotFoundException e) {
			System.out.println("The file was not found.\n");
		}
		catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	private void loadSchedule(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			Statement s = connection.createStatement();
			String line;
			String sql;
			String[] flight;

			while ((line = br.readLine()) != null) {
				flight = line.split(",");
				sql = "INSERT INTO Flight VALUES('" + flight[0] + "', '" + flight[1] + "', '" + flight[2] + "', '" + flight[3] + "', '" + flight[4] + "', '" + flight[5] + "', '" + flight[6] + "', '" + flight[7] + "');";
				s.executeUpdate(sql);
			}

			br.close();
			System.out.println("Flights were loaded from " + filename + ".\n");
		}
		catch (FileNotFoundException e) {
			System.out.println("The file was not found.\n");
		}
		catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	private void loadPrice(String filename) {try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			Statement s = connection.createStatement();
			String line;
			String sql;
			String[] price;

			while ((line = br.readLine()) != null) {
				price = line.split(",");
				sql = "INSERT INTO Price VALUES('" + airline[0] + "', '" + airline[1] + "', '" + airline[2] + "', " + airline[3] + ", " + airline[4]  + ");";
				s.executeUpdate(sql);
			}

			br.close();
			System.out.println("Prices were loaded from " + filename + ".\n");
		}
		catch (FileNotFoundException e) {
			System.out.println("The file was not found.\n");
		}
		catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	private void changePrice(String departure, String arrival, int high, int low) {
		
	}

	private void loadPlane(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			Statement s = connection.createStatement();
			String line;
			String sql;
			String[] plane;

			while ((line = br.readLine()) != null) {
				plane = line.split(",");
				sql = "INSERT INTO Plane VALUES('" + plane[0] + "', '" + plane[1] + "', " + plane[2] + ", TO_DATE('" + plane[3] + "', 'MM_DD-YYY'), " + plane[4]  + ", '" + plane[5] + "');";
				s.executeUpdate(sql);
			}

			br.close();
			System.out.println("Planes were loaded from " + filename + ".\n");
		}
		catch (FileNotFoundException e) {
			System.out.println("The file was not found.\n");
		}
		catch (IOException e) {
			System.out.println(e.toString());
		}
	}

	private void passengerManifest(String flightNumber, String date) {
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT Salution, First_Name, Last_Name FROM Customer c INNER JOIN Reservation r ON c.CID = r.CID INNER JOIN Reservation_Detail rd ON r.Reservation_Number = rd.Reservation_Number WHERE rd.Flight_Number = '" + flightNumber + "' AND Flight_Date = TO_DATE('" + date + "','MM-DD-YYYY');";
			ResultSet r = s.executeQuery(sql);

			do {
				System.out.println(r.getString(1) + ". " + r.getString(2) + " " + r.getString(3));
			} while (r.next());
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void addCustomer(String salutation, String first, String last, String credit, String creditExpire, String street, String city, String state, String phone, String email, String freqFlyer) {
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT COUNT(*) FROM Customer WHERE First_Name = '" + first + "' AND Last_Name = '" + last + "';";
			ResultSet r = s.executeQuery(sql);
			int count = r.getInt(1);

			if (count > 0) {
				System.out.println("Customer was not added. There is already a person with the same first and last name in the database.\n");
			}
			else {
				sql = "SELECT MAX(CID) FROM Customer;";
				r = s.executeQuery(sql);
				String lastCID = r.getString(1);
				if (lastCID == null) {
					int currentCID = 1;
				}
				else {
					int currentCID = Integer.parseInt(lastCID) + 1;
				}

				sql = "INSERT INTO CUSTOMER VALUES('" + currentCID + "', '" + salutation + "', '" + first + "', '" + last + "', '" + credit +  + "', TO_DATE('"
				+ creditExpire + "','MM-DD-YYYY'), '" + street + "', '" + city  + "', '" + state + "', '" + phone + "', '" + email + "', '" + freqMiles;
				s.executeQuery(sql);
				System.out.println("Customer was successfully added.\n");
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void showCustomer(String first, String last) {
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT * FROM Customer WHERE First_Name = '" + first + "' AND Last_Name = '" + last + "';";
			ResultSet r = s.executeQuery(sql);

			if (!r.first()) {
				String cid = r.getString("CID");
				String salutation = r.getString("Salutation");
				String credit = r.getString("Credit_Card_Num");
				Date creditExpire = r.getDate("Credit_Card_Expire");
				String street = r.getString("Street");
				String city = r.getString("City");
				String state = r.getString("State");
				String phone = r.getString("Phone");
				String email = r.getString("Email");
				String freq = r.getString("Frequent_Miles");

				DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
				String creditExpireStr = df.format(creditExpire);

				System.out.println(salutation + ". " + first + " " + last + "\n" + email + "\n" + phone + "\n" + street + "\n" + city + ", " + state
				+ "\n" + credit + "\n" + creditExpireStr + "\n" + "PittRewards #: " + cid + "\nFrequent Flyer #: " + freq + "\n");
			}
			else {
				System.out.println(first + " " + last + "was not found in the database.\n");
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void findPrice(String cityA, String cityB) {
		//need to add round trips
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT * FROM Price WHERE departure_city = '" + cityA + "' AND arrival_city = '" + cityB + "';";
			ResultSet r = s.executeQuery(sql);

			System.out.println();
			System.out.println("Flights from " + cityA + " to " + cityB);
			System.out.println();

			while (r.next()) {

		    	System.out.println("Depart: " + r.getString(1) + " Arrive: " + r.getString(2) + " Airline:" + r.getString(3) + " \n"
			      + "High Price: " + r.getLong(4) + "Low Price: " + r.getLong(5));
		    }

		    sql = "SELECT * FROM Price WHERE departure_city = '" + cityB + "' AND arrival_city = '" + cityA + "';";
			r = s.executeQuery(sql);

			System.out.println();
			System.out.println("Flights from " + cityB + " to " + cityA);
			System.out.println();

			while (r.next()) {
		    	System.out.println("Depart: " + r.getString(1) + " Arrive: " + r.getString(2) + " Airline:" + r.getString(3) + " \n"
			      + "High Price: " + r.getLong(4) + "Low Price: " + r.getLong(5));
		    }

		    r.close();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void routesBetweenCities(String cityA, String cityB) {

	}

	private void routesBetweenCitiesOnAirline(String cityA, String cityB, String airline) {

	}

	private void availableSeats(String cityA, String cityB, String date) {

	}

	private void availableSeats(String cityA, String cityB, String date, String airline) {

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
