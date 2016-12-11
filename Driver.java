import java.sql.*;
import java.util.Scanner;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FilenameFilter;
//import java.text.ParseException;

public class Driver {
	private static Connection connection;
    private Statement statement;
    private PreparedStatement prepStatement;
    private ResultSet resultSet;
    private String query;

	public Driver() {
		while (true) {
			System.out.println("\nAdministrator interface: press [1] and [return] \nUser interface: press [2] and [return].");
			Scanner scan = new Scanner(System.in);
			int mode = scan.nextInt();

			if (mode == 1) admin();
			else user();
		}
	}

	void admin() {
		Scanner scan = new Scanner(System.in);
		int operation = 0;
		String response;
		while (true) {
			System.out.println("\nChoose an operation: \n"
					+ "[1] Erase the database \n"
					+ "[2] Load airline information \n"
					+ "[3] Load schedule information \n"
					+ "[4] Load pricing information \n"
					+ "[5] Load plane information \n"
					+ "[6] Load all CSV files \n"
					+ "[7] Generate passenger manifest for specific Flight on given day \n"
					+ "[8] Run tests of all tasks \n"
					+ "[9] Return to interface menu \n"
					+ "[10] Exit");
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
						}
						break;
					case 2:
						System.out.println("Here");
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
						System.out.println("Would you like to load pricing information [L] or change the price of an existing Flight [C]? [L/C]");
						scan.skip("\n");
						response = scan.nextLine();
						if ((response.toUpperCase()).equals("L")) {
							System.out.println("Enter file name");
							response = scan.nextLine();
							loadSchedule(response);
						}
						else if ((response.toUpperCase()).equals("C")) {
							System.out.println("Enter the departure city");
							String departureCity = scan.nextLine();
							System.out.println("Enter the arrival city");
							String arrivalCity = scan.nextLine();
							System.out.println("Enter the airline of the Flight");
							String airline = scan.nextLine();
							System.out.println("Enter the new high price");
							int highPrice = scan.nextInt();
							System.out.println("Enter the new low price");
							int lowPrice = scan.nextInt();
							changePrice(departureCity, arrivalCity, airline, highPrice, lowPrice);
						}
						else {
							System.out.println("\nPricing was unchanged.\n");
						}
						break;
					case 5:
						System.out.println("Enter file name");
						scan.skip("\n");
						response = scan.nextLine();
						loadPlane(response);
						break;
					case 6:
						loadAll();
						break;
					case 7:
						System.out.println("Enter Flight number");
						scan.skip("\n");
						String Flight = scan.nextLine();
						System.out.println("Enter date [MM-DD-YYYY]");
						String date = scan.nextLine();
						passengerManifest(Flight, date);
						break;
					case 8:
						testAllFunctions();
						break;
					case 9:
						return;
					case 10:
						System.exit(0);
					default:
						System.out.println("Not a valid operation code");
			}
		}
	}
	void user() {
		Scanner scan = new Scanner(System.in);
		int operation = 0;
		String response;
		while (true) {
				System.out.println("\nChoose an operation: \n"
					+ "[1] Add customer \n"
					+ "[2] Show customer info, given customer name \n"
					+ "[3] Find price for Flights between two cities \n"
					+ "[4] Find all routes between two cities \n"
					+ "[5] Find all routes between two cities of a given airline \n"
					+ "[6] Find all routes with available seats between two cities on given day \n"
					+ "[7] For a given airline, find all routes with available seats between two cities on given day \n"
					+ "[8] Add Reservation \n"
					+ "[9] Show Reservation info, given Reservation number \n"
					+ "[10] Buy ticket from existing Reservation\n"
					+ "[11] Return to interface menu\n"
					+ "[12] Exit");
				operation = scan.nextInt();
				String cityA;
				String cityB;
				String date;
				String airline;
				String Reservation;
				String first;
				String last;

				switch (operation) {
					case 1:
						System.out.println("Enter customer's salutation (Format: 'Mr', 'Mrs', or 'Ms')");
						scan.skip("\n");
						String salutation = scan.nextLine();
						System.out.println("Enter customer's first name");
						first = scan.nextLine();
						System.out.println("Enter customer's last name");
						last = scan.nextLine();
						System.out.println("Enter customer's credit card number (Format: 16 digits)");
						String cc = scan.nextLine();
						System.out.println("Enter customer's credit card expiration date (Format: 'MM-DD-YYYY')");
						String ccExpire = scan.nextLine();
						System.out.println("Enter customer's street address");
						String street = scan.nextLine();
						System.out.println("Enter customer's city");
						String city = scan.nextLine();
						System.out.println("Enter customer's state (Format: PA)");
						String state = scan.nextLine();
						System.out.println("Enter customer's phone number (Format: 10 digits)");
						String phone = scan.nextLine();
						System.out.println("Enter customer's email address");
						String email = scan.nextLine();
						System.out.println("Enter customer's frequent miles number (if none, press [Enter])");
						String freqMiles = scan.nextLine();

						addCustomer(salutation, first, last, cc, ccExpire, street, city, state, phone, email, freqMiles);
						break;
					case 2:
						System.out.println("Enter customer first name");
						scan.skip("\n");
						first = scan.nextLine();
						System.out.println("Enter customer last name");
						last = scan.nextLine();
						showCustomer(first.trim(), last.trim());
						break;
					case 3:
						System.out.println("Enter start city (eg. PIT)");
						scan.skip("\n");
						cityA = scan.nextLine();
						System.out.println("Enter end city");
						cityB = scan.nextLine();
						findPrice(cityA, cityB);
						break;
					case 4:
						System.out.println("Enter start city (eg. PIT)");
						scan.skip("\n");
						cityA = scan.nextLine();
						System.out.println("Enter end city");
						cityB = scan.nextLine();
						routesBetweenCities(cityA, cityB);
						break;
					case 5:
						System.out.println("Enter start city (eg. PIT)");
						scan.skip("\n");
						cityA = scan.nextLine();
						System.out.println("Enter end city");
						cityB = scan.nextLine();
						System.out.println("Enter airline code (eg. AAL)");
						airline = scan.nextLine();
						routesBetweenCitiesOnAirline(cityA, cityB, airline);
						break;
					case 6:
						System.out.println("Enter start city (eg. PIT)");
						scan.skip("\n");
						cityA = scan.nextLine();
						System.out.println("Enter end city");
						cityB = scan.nextLine();
						System.out.println("Enter date [MM-DD-YYYY]");
						date = scan.nextLine();
						availableSeats(cityA, cityB, date);
						break;
					case 7:
						System.out.println("Enter start city (eg. PIT)");
						scan.skip("\n");
						cityA = scan.nextLine();
						System.out.println("Enter end city");
						cityB = scan.nextLine();
						System.out.println("Enter date [MM-DD-YYYY]");
						date = scan.nextLine();
						System.out.println("Enter airline code (eg. AAL)");
						airline = scan.nextLine();
						availableSeats(cityA, cityB, date, airline);
						break;
					case 8:
						int leg = 0;
						String flights[] = new String[4];
						String dates[] = new String[4];
						scan.skip("\n");
						while (leg < 4) {
							System.out.println("Enter Flight number or [0] if you are finished");

							String FlightNumber = scan.nextLine();
							if (FlightNumber.equals("0")) {
								break;
							}

							flights[leg] = FlightNumber;
							System.out.println("Enter date [MM-DD-YYYY]");
							date = scan.nextLine();

							if (full(FlightNumber, date)) {
								break;
							}

							dates[leg] = date;
							leg++;
						}
						addReservation(flights, dates, "", "");
						break;
					case 9:
						System.out.println("Enter Reservation number");
						scan.skip("\n");
						Reservation = scan.nextLine();
						showReservation(Reservation);
						break;
					case 10:
						System.out.println("Enter Reservation number");
						scan.skip("\n");
						Reservation = scan.nextLine();
						buyTickets(Reservation);
						break;
					case 11:
						return;
					case 12:
						System.exit(0);
					default:
						System.out.println("Not a valid operation code");
			}
		}
	}

	private boolean full(String Flight, String date) {
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT capacity('" + Flight + "') FROM Flight WHERE Flight_Number = " + Flight;
			ResultSet r = s.executeQuery(sql);
			if (r.next()) {
				int capacity = r.getInt(1);
				sql = "SELECT reserved('" + Flight + "', TO_DATE('" +date+ "', 'MM-DD-YYYY')) FROM Flight WHERE Flight_Number = " + Flight;
				r = s.executeQuery(sql);
				r.next();
				int reserved = r.getInt(1);
				if (capacity == reserved) {
					System.out.println("This Flight is full");
					return true;
				}
			}
			else {
				System.out.println("Invalid Flight number");
				return true;
			}

			return false;
		}
		catch (Exception e) {
			System.out.println("Error fetching the specified Flight. " + e.toString());
			return true;
		}
	}


	private void eraseDatabase() {
		try {
			Statement s = connection.createStatement();
			String sql = "DELETE FROM Reservation_Detail";
			s.executeUpdate(sql);
			sql = "DELETE FROM Reservation";
			s.executeUpdate(sql);
			sql = "DELETE FROM Price";
			s.executeUpdate(sql);
			sql = "DELETE FROM Customer";
			s.executeUpdate(sql);
			sql = "DELETE FROM Flight";
			s.executeUpdate(sql);
			sql = "DELETE FROM Plane";
			s.executeUpdate(sql);
			sql = "DELETE FROM Airline";
			s.executeUpdate(sql);
			sql = "DELETE FROM System_Date";
			s.executeUpdate(sql);

			System.out.println("Database was deleted.");
		}
		catch (Exception e) {
			System.out.println("Error deleting the database. " + e.toString());
		}
	}

	private void loadAll() {
		try {
			String directory = new java.io.File(".").getCanonicalPath();
			File dir = new File(directory);
			File[] allCSV = dir.listFiles(new FilenameFilter() {
	                 public boolean accept(File dir, String filename)
	                      { return filename.endsWith(".csv"); }
	        } );

			for (int i = 0; i < allCSV.length; i++) {
				String name = allCSV[i].getName();
				if (name.equals("01airline.csv")) {
					loadAirline(name);
				}
				else if (name.equals("02plane.csv")) {
					loadPlane(name);
				}
				else if (name.equals("03flight.csv")) {
					loadSchedule(name);
				}
				else if (name.equals("04price.csv")) {
					loadPrice(name);
				}
			}
		}
		catch (IOException e) {
			System.out.println(e.toString());
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
				sql = "INSERT INTO Airline VALUES('" + airline[0] + "', '" + airline[1] + "', '" + airline[2] + "', " + airline[3] + ")";
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
		catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		}
	}

	private void loadSchedule(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			Statement s = connection.createStatement();
			String line;
			String sql;
			String[] Flight;

			while ((line = br.readLine()) != null) {
				Flight = line.split(",");
				sql = "INSERT INTO Flight VALUES('" + Flight[0] + "', '" + Flight[1] + "', '" + Flight[2] + "', '" + Flight[3] + "', '" + Flight[4] + "', '" + Flight[5] + "', '" + Flight[6] + "', '" + Flight[7] + "')";
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
		catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		}
	}

	private void loadPrice(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			Statement s = connection.createStatement();
			String line;
			String sql;
			String[] price;

			while ((line = br.readLine()) != null) {
				price = line.split(",");
				sql = "INSERT INTO Price VALUES('" + price[0] + "', '" + price[1] + "', '" + price[2] + "', " + price[3] + ", " + price[4]  + ")";
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
		catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		}
	}

	private void changePrice(String departure, String arrival, String airline, int high, int low) {
		try {
			Statement s = connection.createStatement();
			String sql = "UPDATE Price SET High_Price = " + high + ", Low_Price = " + low + " WHERE Airline_ID = (SELECT p.Airline_ID FROM Price p LEFT JOIN Airline a ON p.Airline_ID = a.Airline_ID WHERE p.Departure_City = '" + departure + "'" + "AND p.Arrival_City = '" + arrival + "' AND a.Airline_Name = '" + airline + "')";
			s.executeUpdate(sql);
			System.out.println("\nThe price of the " + airline + " Flight from " + departure + " to " + arrival + " was changed to " + high + "|" + low + ".\n");
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
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
				sql = "INSERT INTO Plane VALUES('" + plane[0] + "', '" + plane[1] + "', " + plane[2] + ", TO_DATE('" + plane[3] + "', 'MM-DD-YYYY'), " + plane[4]  + ", '" + plane[5] + "')";
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
		catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		}
	}

	private void passengerManifest(String FlightNumber, String date) {
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT Salutation, First_Name, Last_Name FROM Customer c INNER JOIN Reservation r ON c.CID = r.CID INNER JOIN Reservation_Detail rd ON r.Reservation_Number = rd.Reservation_Number WHERE rd.Flight_Number = '" + FlightNumber + "' AND Flight_Date = TO_DATE('" + date + "','MM-DD-YYYY')";
			ResultSet r = s.executeQuery(sql);

			while (r.next()) {
				System.out.println(r.getString(1) + ". " + r.getString(2) + " " + r.getString(3));
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void testAllFunctions() {
		try {
			System.out.println("\nDeleteing database...");
			eraseDatabase();
			System.out.println("\nFilling database tables...\n");
			loadAll();
			System.out.println("\nAdding customer to database...\n");
			addCustomer("Mr", "Devansh", "Desai", "1234567890123456", "12-31-2020", "4200 Fifth Ave", "Pittsburgh", "PA", "4125551234", "dmd113@pitt.edu", "00001");
			System.out.println("Displaying customer...");
			showCustomer("Devansh", "Desai");
			System.out.println("\nFinding prices for flights between SFO and PIT...");
			findPrice("SFO", "PIT");
			System.out.println("\nFinding all routes for flights between SFO and PIT...");
			routesBetweenCities("SFO", "PIT");
			System.out.println("\nFinding all routes for flights between SFO and PIT on Delta Airlines...");
			routesBetweenCitiesOnAirline("SFO", "PIT", "DAL");
			System.out.println("\nFinding all available seats for flights between SFO and PIT on 11-23-2016...");
			availableSeats("SFO", "PIT", "11-23-2016");
			System.out.println("\nFinding all available seats for flights between SFO and PIT on 11-23-2016 on Delta Airines...");
			availableSeats("SFO", "PIT", "11-23-2016", "DAL");
			System.out.println("\nAdding reservation for flights #039 and #139 on 11-23-2016 for customer...");
			String[] flights = new String[2];
			String[] dates = new String[2];
			flights[0] = "039";
			flights[1] ="139";
			dates[0] = "11-23-2016";
			dates[1] = "11-23-2016";
			String reservationNumber = addReservation(flights, dates, "Devansh", "Desai");
			System.out.println("\nCost should be $336 at a high price...");
			System.out.println("\nShowing reservation #" + reservationNumber + "...");
			showReservation(reservationNumber);
			System.out.println("\nEnter [y] to buy at ticket and complete the test...");
			buyTickets(reservationNumber);
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void addCustomer(String salutation, String first, String last, String credit, String creditExpire, String street, String city, String state, String phone, String email, String freqFlyer) {
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT COUNT(*) FROM Customer WHERE First_Name = '" + first + "' AND Last_Name = '" + last + "'";
			ResultSet r = s.executeQuery(sql);
			r.next();
			int count = r.getInt(1);
			int currentCID;

			if (count > 0) {
				System.out.println("Customer was not added. There is already a person with the same first and last name in the database.\n");
			}
			else {
				sql = "SELECT MAX(CID) FROM Customer";
				r = s.executeQuery(sql);
				r.next();
				String lastCID = r.getString(1);

				if (lastCID == null) {
					currentCID = 1;
				}
				else {
					currentCID = Integer.parseInt(lastCID) + 1;
				}

				sql = "INSERT INTO CUSTOMER VALUES('" + currentCID + "', '" + salutation + "', '" + first + "', '" + last + "', '" + credit + "', TO_DATE('"
				+ creditExpire + "','MM-DD-YYYY'), '" + street + "', '" + city  + "', '" + state + "', '" + phone + "', '" + email + "', '" + freqFlyer + "')";
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
			String sql = "SELECT * FROM Customer WHERE First_Name = '" + first + "' AND Last_Name = '" + last + "'";
			ResultSet r = s.executeQuery(sql);
			if (r.next()) {
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

				System.out.println("\n" + salutation + ". " + first + " " + last + "\n" + email + "\n" + phone + "\n" + street + "\n" + city + ", " + state
				+ "\n" + credit + "\n" + creditExpireStr + "\n" + "PittRewards #: " + cid + "\nFrequent Flyer #: " + freq + "\n");
			}
			else {
				System.out.println("\n" + first + " " + last + " was not found.");
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void findPrice(String cityA, String cityB) {
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT * FROM Price p INNER JOIN Airline a ON p.Airline_ID = a.Airline_ID WHERE p.Departure_City = '" + cityA + "' AND p.Arrival_City = '" + cityB + "'";
			ResultSet r = s.executeQuery(sql);
			System.out.println("\n\nOne-way Flights from " + cityA + " to " + cityB + ":");
			while (r.next()) {
		    	System.out.println("  Departure City: " + r.getString("Departure_City") + "  Arrival City: " + r.getString("Arrival_City") + "  Airline: " + r.getString("Airline_Name") + " \n"
			      + "    High Price: $" + r.getLong("High_Price") + "  Low Price: $" + r.getLong("Low_Price"));
		    }

		    sql = "SELECT * FROM Price p INNER JOIN Airline a ON p.Airline_ID = a.Airline_ID WHERE p.Departure_City = '" + cityB + "' AND p.Arrival_City = '" + cityA + "'";
			r = s.executeQuery(sql);
			System.out.println("\n\nOne-way Flights from " + cityB + " to " + cityA + ":");
			while (r.next()) {
		    	System.out.println("  Departure City: " + r.getString("Departure_City") + "  Arrival City: " + r.getString("Arrival_City") + "  Airline: " + r.getString("Airline_Name") + " \n"
			      + "    High Price: $" + r.getLong("High_Price") + "  Low Price: $" + r.getLong("Low_Price"));
		    }

			sql = "SELECT * FROM Airline a INNER JOIN (SELECT Airline_ID, SUM(High_Price) AS High, SUM(Low_Price) AS Low FROM Price p WHERE Departure_City = '" + cityA + "' AND Arrival_City = '" + cityB + "' OR Departure_City = '" + cityB + "' AND Arrival_City = '" + cityA + "' GROUP BY Airline_ID) p ON a.Airline_ID = p.Airline_ID";
			r = s.executeQuery(sql);
			System.out.println("\n\nRound-trip Flights between " + cityA + " to " + cityB + ":");
			while (r.next()) {
				System.out.println("  Airline: " + r.getString("Airline_Name") + " \n" + "    High Price: $" + r.getLong("High") + "  Low Price: $" + r.getLong("Low"));
			}

		    r.close();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void routesBetweenCities(String cityA, String cityB) {
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT Flight_Number, Airline_ID, Departure_City, Arrival_City, Departure_time, Arrival_Time " +
				"FROM Flight WHERE Departure_City = '" + cityA + "' AND Arrival_City = '" + cityB + "'";

			ResultSet r = s.executeQuery(sql);

			System.out.println("\nDirect Flights from " + cityA + " to " + cityB + ":");
			while (r.next()) {
		    	System.out.println("  Flight Number: " + r.getString(1) + " Airline: " + r.getString(2) + " \n"
			      + "    Depart: " + r.getString(3) + " Arrive " + r.getString(4) + " Depart Time: " + r.getString(5) + " Arrive Time: " + r.getString(6));
		    }

			System.out.println("\nFlights with one connection from " + cityA + " to " + cityB + ":");

		    sql = "SELECT f1.Flight_Number, f1.Airline_ID, f1.Departure_City, f1.Arrival_City, f1.Departure_time, f1.Arrival_Time, " +
		    			"f2.Flight_Number, f2.Airline_ID, f2.Departure_City, f2.Arrival_City, f2.Departure_time, f2.Arrival_Time " +
				"FROM FLIGHT f1, FLIGHT f2 " +
				"WHERE f1.Arrival_City = f2.Departure_City " +
				"AND f1.Departure_City = '" + cityA + "' " +
				"AND f2.Arrival_City = '" + cityB + "' " +
				"AND (TO_NUMBER(f2.Departure_Time) - TO_NUMBER(f1.Arrival_Time) > 100) " +
				"AND ((SUBSTR(f1.Weekly_Schedule, 1, 1) = 'S' AND SUBSTR(f2.Weekly_Schedule, 1, 1) = 'S') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 2, 1) = 'M' AND SUBSTR(f2.Weekly_Schedule, 2, 1) = 'M') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 3, 1) = 'T' AND SUBSTR(f2.Weekly_Schedule, 3, 1) = 'T') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 4, 1) = 'W' AND SUBSTR(f2.Weekly_Schedule, 4, 1) = 'W') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 5, 1) = 'T' AND SUBSTR(f2.Weekly_Schedule, 5, 1) = 'T') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 6, 1) = 'F' AND SUBSTR(f2.Weekly_Schedule, 6, 1) = 'F') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 7, 1) = 'S' AND SUBSTR(f2.Weekly_Schedule, 7, 1) = 'S'))";
			r = s.executeQuery(sql);

			while (r.next()) {
				System.out.println("  Flight No: " + r.getString(1) + "  Airline ID: " + r.getString(2) + "  Departure City: " + r.getString(3) + "  Arrival City: " + r.getString(4) + "  Departure Time: " + r.getString(5) + "  Arrival Time: " + r.getString(6) + "\n" +
			      "  Flight No: " + r.getString(7) + "  Airline ID: " + r.getString(8) + "  Departure City: " + r.getString(9) + "  Arrival City: " + r.getString(10) + "  Departure Time: " + r.getString(11) + "  Arrival Time: " + r.getString(12) + "\n");
			}

		    r.close();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void routesBetweenCitiesOnAirline(String cityA, String cityB, String airline_abbreviation) {
		try {

			Statement s = connection.createStatement();
			String sql = "SELECT Airline_ID FROM Airline WHERE Airline_Abbreviation = '" + airline_abbreviation + "'";
			ResultSet r = s.executeQuery(sql);

			if (!r.isBeforeFirst()) {
    			System.out.println("Airline not found");
    			return;
			}

			r.next();
			String airline_id = r.getString("Airline_ID");


			sql = "SELECT Flight_Number, Airline_ID, Departure_City, Arrival_City, Departure_time, Arrival_Time " +
				"FROM Flight " +
				"WHERE Departure_City = '" + cityA + "' " +
				"AND Arrival_City = '" + cityB + "' " +
				"AND airline_id = '" + airline_id + "'";
			r = s.executeQuery(sql);

			System.out.println("\nDirect Flights from " + cityA + " to " + cityB + ":");

			while (r.next()) {
		    	System.out.println("  Flight No: " + r.getString(1) + "  Airline ID: " + r.getString(2) + " \n"
			      + "     Departure City: " + r.getString(3) + "  Arrival City: " + r.getString(4) + "  Departaure Time: " + r.getString(5) + "  Arrival Time: " + r.getString(6));
		    }

			System.out.println("\nFlights with one connection from " + cityA + " to " + cityB + ":");

		    sql = "SELECT f1.Flight_Number, f1.Airline_ID, f1.Departure_City, f1.Arrival_City, f1.Departure_Time, f1.Arrival_Time, " +
		    			"f2.Flight_Number, f2.Airline_ID, f2.Departure_City, f2.Arrival_City, f2.Departure_Time, f2.Arrival_Time " +
				"FROM FLIGHT f1, FLIGHT f2 " +
				"WHERE f1.Arrival_City = f2.Departure_City " +
				"AND f1.Departure_City = '" + cityA + "' " +
				"AND f2.Arrival_City = '" + cityB + "' " +
				"AND f1.airline_id = '" + airline_id + "' " +
				"AND f2.airline_id = '" + airline_id + "' " +
				"AND (TO_NUMBER(f2.Departure_Time) - TO_NUMBER(f1.Arrival_Time) > 100) " +
				"AND ((SUBSTR(f1.Weekly_Schedule, 1, 1) = 'S' AND SUBSTR(f2.Weekly_Schedule, 1, 1) = 'S') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 2, 1) = 'M' AND SUBSTR(f2.Weekly_Schedule, 2, 1) = 'M') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 3, 1) = 'T' AND SUBSTR(f2.Weekly_Schedule, 3, 1) = 'T') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 4, 1) = 'W' AND SUBSTR(f2.Weekly_Schedule, 4, 1) = 'W') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 5, 1) = 'T' AND SUBSTR(f2.Weekly_Schedule, 5, 1) = 'T') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 6, 1) = 'F' AND SUBSTR(f2.Weekly_Schedule, 6, 1) = 'F') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 7, 1) = 'S' AND SUBSTR(f2.Weekly_Schedule, 7, 1) = 'S'))";
			r = s.executeQuery(sql);

			while (r.next()) {
				System.out.println("  Flight No: " + r.getString(1) + "  Airline ID: " + r.getString(2) + "  Departure City: " + r.getString(3) + "  Arrival City: " + r.getString(4) + "  Departure Time: " + r.getString(5) + "  Arrival Time: " + r.getString(6) + "\n" +
			      "  Flight No: " + r.getString(7) + "  Airline ID: " + r.getString(8) + "  Departure City: " + r.getString(9) + "  Arrival City: " + r.getString(10) + "  Departure Time: " + r.getString(11) + "  Arrival Time: " + r.getString(12) + "\n");
			}

		    r.close();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void availableSeats(String cityA, String cityB, String date) {
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT Flight_Number, Airline_ID, Departure_City, Arrival_City, Departure_time, Arrival_Time " +
				"FROM Flight WHERE Departure_City = '" + cityA + "' AND Arrival_City = '" + cityB + "' AND " +
				"(capacity(Flight_Number) - reserved(Flight_Number, TO_DATE('" + date + "', 'MM-DD-YYYY'))) > 0";
			ResultSet r = s.executeQuery(sql);

			System.out.println("\nDirect Flights from " + cityA + " to " + cityB + ":");
			while (r.next()) {
		    	System.out.println("  Flight No: " + r.getString(1) + "  Airline ID: " + r.getString(2) + " \n"
			      + "     Departure City: " + r.getString(3) + "  Arrival City: " + r.getString(4) + "  Departaure Time: " + r.getString(5) + "  Arrival Time: " + r.getString(6));
		    }

		    System.out.println("\nFlights with one connection from " + cityA + " to " + cityB + ":");
		    sql = "SELECT f1.Flight_Number, f1.Airline_ID, f1.Departure_City, f1.Arrival_City, f1.Departure_time, f1.Arrival_Time, " +
		    			"f2.Flight_Number, f2.Airline_ID, f2.Departure_City, f2.Arrival_City, f2.Departure_time, f2.Arrival_Time " +
				"FROM FLIGHT f1, FLIGHT f2 " +
				"WHERE f1.Arrival_City = f2.Departure_City " +
				"AND f1.Departure_City = '" + cityA + "' " +
				"AND f2.Arrival_City = '" + cityB + "' " +
				"AND (TO_NUMBER(f2.Departure_Time) - TO_NUMBER(f1.Arrival_Time) > 100) " +
				"AND (capacity(f1.Flight_Number) - reserved(f1.Flight_Number, TO_DATE('" + date + "', 'MM-DD-YYYY'))) > 0 " +
				"AND (capacity(f2.Flight_Number) - reserved(f2.Flight_Number, TO_DATE('" + date + "', 'MM-DD-YYYY'))) > 0 " +
				"AND ((SUBSTR(f1.Weekly_Schedule, 1, 1) = 'S' AND SUBSTR(f2.Weekly_Schedule, 1, 1) = 'S') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 2, 1) = 'M' AND SUBSTR(f2.Weekly_Schedule, 2, 1) = 'M') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 3, 1) = 'T' AND SUBSTR(f2.Weekly_Schedule, 3, 1) = 'T') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 4, 1) = 'W' AND SUBSTR(f2.Weekly_Schedule, 4, 1) = 'W') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 5, 1) = 'T' AND SUBSTR(f2.Weekly_Schedule, 5, 1) = 'T') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 6, 1) = 'F' AND SUBSTR(f2.Weekly_Schedule, 6, 1) = 'F') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 7, 1) = 'S' AND SUBSTR(f2.Weekly_Schedule, 7, 1) = 'S'))";
			r = s.executeQuery(sql);
			while (r.next()) {
				System.out.println("  Flight No: " + r.getString(1) + "  Airline ID: " + r.getString(2) + "  Departure City: " + r.getString(3) + "  Arrival City: " + r.getString(4) + "  Departure Time: " + r.getString(5) + "  Arrival Time: " + r.getString(6) + "\n" +
			      "  Flight No: " + r.getString(7) + "  Airline ID: " + r.getString(8) + "  Departure City: " + r.getString(9) + "  Arrival City: " + r.getString(10) + "  Departure Time: " + r.getString(11) + "  Arrival Time: " + r.getString(12) + "\n");
			}

		    r.close();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void availableSeats(String cityA, String cityB, String date, String airline_abbreviation) {
		try {
			Statement s = connection.createStatement();
			//get id from abbreviation
			String sql = "SELECT Airline_ID FROM Airline WHERE Airline_Abbreviation = '" + airline_abbreviation + "'";
			ResultSet r = s.executeQuery(sql);

			if (!r.isBeforeFirst()) {
    			System.out.println("Airline not found");
    			return;
			}

			r.next();
			String airline_id = r.getString("Airline_ID");

			sql = "SELECT Flight_Number, Airline_ID, Departure_City, Arrival_City, Departure_time, Arrival_Time " +
				"FROM Flight WHERE Departure_City = '" + cityA + "' AND Arrival_City = '" + cityB + "' AND airline_id = '" + airline_id + "' " +
				"AND (capacity(Flight_Number) - reserved(Flight_Number, TO_DATE('" + date + "', 'MM-DD-YYYY'))) > 0";
			r = s.executeQuery(sql);

			System.out.println("\nDirect Flights from " + cityA + " to " + cityB + ":");

			while (r.next()) {
		    	System.out.println("  Flight No: " + r.getString(1) + "  Airline ID: " + r.getString(2) + " \n"
			      + "     Departure City: " + r.getString(3) + "  Arrival City: " + r.getString(4) + "  Departaure Time: " + r.getString(5) + "  Arrival Time: " + r.getString(6));
		    }

		    System.out.println("\nFlights with one connection from " + cityA + " to " + cityB + ":");
		    sql = "SELECT f1.Flight_Number, f1.Airline_ID, f1.Departure_City, f1.Arrival_City, f1.Departure_time, f1.Arrival_Time, " +
		    			"f2.Flight_Number, f2.Airline_ID, f2.Departure_City, f2.Arrival_City, f2.Departure_time, f2.Arrival_Time " +
				"FROM FLIGHT f1, FLIGHT f2 " +
				"WHERE f1.Arrival_City = f2.Departure_City " +
				"AND f1.Departure_City = '" + cityA + "' " +
				"AND f2.Arrival_City = '" + cityB + "' " +
				"AND f1.airline_id = '" + airline_id + "' " +
				"AND f2.airline_id = '" + airline_id + "' " +
				"AND (TO_NUMBER(f2.Departure_Time) - TO_NUMBER(f1.Arrival_Time) > 100) " +
				"AND (capacity(f1.Flight_Number) - reserved(f1.Flight_Number, TO_DATE('" + date + "', 'MM-DD-YYYY'))) > 0 " +
				"AND (capacity(f2.Flight_Number) - reserved(f2.Flight_Number, TO_DATE('" + date + "', 'MM-DD-YYYY'))) > 0" +
				"AND ((SUBSTR(f1.Weekly_Schedule, 1, 1) = 'S' AND SUBSTR(f2.Weekly_Schedule, 1, 1) = 'S') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 2, 1) = 'M' AND SUBSTR(f2.Weekly_Schedule, 2, 1) = 'M') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 3, 1) = 'T' AND SUBSTR(f2.Weekly_Schedule, 3, 1) = 'T') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 4, 1) = 'W' AND SUBSTR(f2.Weekly_Schedule, 4, 1) = 'W') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 5, 1) = 'T' AND SUBSTR(f2.Weekly_Schedule, 5, 1) = 'T') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 6, 1) = 'F' AND SUBSTR(f2.Weekly_Schedule, 6, 1) = 'F') " +
					"OR (SUBSTR(f1.Weekly_Schedule, 7, 1) = 'S' AND SUBSTR(f2.Weekly_Schedule, 7, 1) = 'S'))";
			r = s.executeQuery(sql);
			while (r.next()) {
		    	System.out.println( r.getString(1) + " " + r.getString(2) + " " + r.getString(3) + " " +r.getString(4) + " " +r.getString(5) + " " +r.getString(6) + "\n" +
			      r.getString(7) + " " + r.getString(8) + " " + r.getString(9) + " " +r.getString(10) + " " +r.getString(11) + " " +r.getString(12));
		    	System.out.println();
		    }

		    r.close();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private String addReservation(String flights[], String dates[], String firstName, String lastName) {

	//all flight numbers in flights[] can be assume to be valid
	//asks the user for name and populates the reservation info with the customer's information
	//determines high and low prices based on today's date: reservation date is today
		try {
			if (firstName.equals("") && lastName.equals("")) {
				// get name
				Scanner scan = new Scanner(System.in);
			    System.out.println("Enter first name");
			    firstName = scan.nextLine();
			    System.out.println("Enter last name");
			    lastName = scan.nextLine();
			}

			Statement s = connection.createStatement();
			String sql = "SELECT CID, Credit_Card_Num, Frequent_Miles FROM Customer WHERE First_Name = '" + firstName + "' AND Last_Name = '" + lastName + "'";
			ResultSet r = s.executeQuery(sql);

			String cid = "";
			String credit = "";
			String freq = "";
			//if customer exists
			if (r.next()) {
				cid = r.getString("CID");
				credit = r.getString("Credit_Card_Num");
				freq = r.getString("Frequent_Miles");
			}
			else {
    			System.out.println("Customer not found. Please sign up as a customer.");
    			return null;
			}

			if (credit == null) credit = "";
			if (freq == null) freq = "";

			// determine how many flights there are
			int flightCount = 0;
			for (int i = 0; i < flights.length; i++) {
				if (flights[i] != null) flightCount++;
			}

			//get start and end cities for the whole Reservation
			sql = "SELECT Departure_City FROM Flight WHERE Flight_Number = '" + flights[0] + "'";
			r = s.executeQuery(sql);
			r.next();
			String departureCity = r.getString("Departure_City");

			sql = "SELECT Arrival_City FROM Flight WHERE Flight_Number = '" + flights[flightCount-1] + "'";
			r = s.executeQuery(sql);
			r.next();
			String arrivalCity = r.getString("Arrival_City");

			sql = "SELECT TO_CHAR(SYSDATE, 'MM-DD-YYYY') NOW FROM DUAL";
			r = s.executeQuery(sql);
			r.next();
			String today = r.getString("NOW");

			//calculate totalPrice
			int totalPrice = 0;

			for(int i = 0; i < flightCount; i++){
				sql = "SELECT Departure_City, Arrival_City, Airline_ID FROM Flight WHERE Flight_Number = '" + flights[i] + "'";
				r = s.executeQuery(sql);
				r.next();
				String a_city = r.getString("Arrival_City");
				String d_city = r.getString("Departure_City");
				String airline = r.getString("Airline_ID");

				//if same day Flight, add high price, else add low price
				if (dates[i].equals(today)) {
					sql = "SELECT High_Price FROM Price WHERE Departure_City = '" + d_city + "' AND Arrival_City = '" + a_city + "' AND Airline_ID = '" + airline + "'";
				}
				else {
					sql = "SELECT Low_Price FROM Price WHERE Departure_City = '" + d_city + "' AND Arrival_City = '" + a_city + "' AND Airline_ID = '" + airline + "'";
				}
				r = s.executeQuery(sql);
				r.next();
				int priceOfFlight = r.getInt(1);

				//check if customer is frequent miles member of airline
				if (freq.equals(airline)) {
					priceOfFlight *= 9;
					priceOfFlight /= 10;
				}
				totalPrice += priceOfFlight;
			}

			//generate random Reservation number
			String reservationNumber;
			Random ran = new Random();
			while (true) {
				int random = ran.nextInt(100000);
				reservationNumber = String.format("%05d", random);
				sql = "SELECT * FROM Reservation WHERE Reservation_Number = '" + reservationNumber + "'";
				r = s.executeQuery(sql);
				//check if resultSet is empty, if so, we've found a new Reservation number
				if (!r.isBeforeFirst()) break;
			}

			//make new reservation
			sql = "INSERT INTO Reservation VALUES('" + reservationNumber + "', '" + cid + "', " + Integer.toString(totalPrice) + ", '" + credit + "', TO_DATE('"+ today +"', 'MM-DD-YYYY')," +
				"'N', '" + departureCity + "', '"+ arrivalCity + "')";
			s.executeUpdate(sql);

			//insert flights into reservation_detail
			for(int i = 0; i < flightCount; i++){
				sql = "INSERT INTO Reservation_Detail VALUES('" + reservationNumber + "', '"+ flights[i] + "', TO_DATE('" + dates[i] + "', 'MM-DD-YYYY'), " + Integer.toString(i) + ")";
				s.executeUpdate(sql);
			}

		    System.out.println("Reservation number " + reservationNumber + " confirmed");

		    r.close();
		    return reservationNumber;
		}
		catch (Exception e) {
			System.out.println("Error creating Reservation" + e.toString());
		}

		return null;
	}

	private void showReservation(String reservationNumber) {
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT * FROM Reservation WHERE Reservation_Number = '" + reservationNumber + "'";
			ResultSet r = s.executeQuery(sql);

			//check if Reservation exists
			if (!r.isBeforeFirst() ) {
    			System.out.println("Reservation number not found");
    			return;
			}

			r.next();
			String res_num = r.getString("Reservation_Number");
			String cid = r.getString("CID");
			String credit = r.getString("Credit_Card_Num");
			int cost = r.getInt("Cost");
			Date dt = r.getDate("Reservation_Date");
			String ticket = r.getString("Ticketed");
			String start = r.getString("Start_city");
			String end = r.getString("End_city");

			DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
			String res_date = df.format(dt);

			System.out.println("\n  Reservation #: " + res_num + "\n  Customer ID: " + cid + "\n  Credit Card: " + credit + "\n  Cost: $" + Integer.toString(cost) + "\n  Reservation Date: " + res_date + "\n" +
			"\n  Ticketed: " + ticket + "\n  " + start + " to " + end);
		}
		catch (Exception e) {
			System.out.println("Error finding Reservation" + e.toString());
		}
	}

	private void buyTickets(String reservationNumber) {
		try {
			Scanner scan = new Scanner(System.in);
			Statement s = connection.createStatement();
			String sql = "SELECT Credit_Card_Num FROM Reservation WHERE Reservation_Number = '" + reservationNumber + "'";
			ResultSet r = s.executeQuery(sql);

			//check if Reservation exists
			if (!r.isBeforeFirst()) {
    			System.out.println("Reservation #" + reservationNumber + " not found.");
    			return;
			}

			r.next();
			String ccn = r.getString("Credit_Card_Num");
			boolean newCCN = false;
			if (ccn != null && ccn.length() == 16) {
    			System.out.println("\nWould you like to use the credit card associated with your account? [y/n]");
    			String response = scan.nextLine();
    			if (response.equals("y")) newCCN = true;
			}
			//change credit card number
			if (!newCCN) {
				String newNumber;
				while (true) {
					System.out.println("\nInput your 16 digit credit card number.");
    				newNumber = scan.nextLine();
    				if (newNumber.length() == 16) break;
					System.out.println("Invalid format");
				}
				sql = "UPDATE Reservation Set Credit_Card_Num = '" + newNumber + "' WHERE Reservation_Number = '" + reservationNumber + "'";
				s.executeUpdate(sql);
				System.out.println("Updated credit card number.");
			}

			sql = "UPDATE Reservation Set Ticketed = 'Y' WHERE Reservation_Number = '" + reservationNumber + "'";
			s.executeUpdate(sql);

			System.out.println("\nYour ticket was purchased.");
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}


	public static void main(String args[]) throws SQLException {
		String username = "kwz5";
		String password = "asdfj";

		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		  	String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";

			connection = DriverManager.getConnection(url, username, password);
			Driver driver = new Driver();
		}
		catch (Exception Ex) {
			System.out.println("Error connecting to database.  Machine Error: " + Ex.toString());
		}
		finally {
			connection.close();
		}
	}
}
