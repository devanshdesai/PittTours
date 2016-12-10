import java.sql.*;
import java.util.Scanner;
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
		while(true){
			System.out.println("\nAdministrator interface: press [1] and [return] \nUser interface: press [2] and [return].");
			Scanner scan = new Scanner(System.in);
			int mode = scan.nextInt();

			if (mode == 1) admin();
			else user();
		}
	}

	void admin(){
		Scanner scan = new Scanner(System.in);
		int operation = 0;
		String response;
		while(true){
			System.out.println("\nChoose an operation: \n"
					+ "[1] Erase the database \n"
					+ "[2] Load airline information \n"
					+ "[3] Load schedule information \n"
					+ "[4] Load pricing information \n"
					+ "[5] Load plane information \n"
					+ "[6] Generate passenger manifest for specific flight on given day \n"
					+ "[7] Return to interface menu \n"
					+ "[8] Exit");
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
						System.out.println("Would you like to load pricing information [L] or change the price of an existing flight [C]? [L/C]");
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
							System.out.println("Enter the airline of the flight");
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
						System.out.println("Enter flight number");
						scan.skip("\n");
						String flight = scan.nextLine();
						System.out.println("Enter date [MM-DD-YYYY]");
						String date = scan.nextLine();
						passengerManifest(flight, date);
						break;
					case 7:
						return;
					case 8:
						System.exit(0);
					case 100:
						// This hidden key will load all the tuples into the database.
						loadAll();
						break;
					default:
						System.out.println("Not a valid operation code");
			}
		}
	}
	void user(){
		Scanner scan = new Scanner(System.in);
		int operation = 0;
		String response;
		while (true) {
				System.out.println("\nChoose an operation: \n"
					+ "[1] Add customer \n"
					+ "[2] Show customer info, given customer name \n"
					+ "[3] Find price for flights between two cities \n"
					+ "[4] Find all routes between two cities \n"
					+ "[5] Find all routes between two cities of a given airline \n"
					+ "[6] Find all routes with available seats between two cities on given day \n"
					+ "[7] For a given airline, find all routes with available seats between two cities on given day \n"
					+ "[8] Add reservation \n"
					+ "[9] Show reservation info, given reservation number \n"
					+ "[10] Buy ticket from existing reservation\n"
					+ "[11] Return to interface menu\n"
					+ "[12] Exit");
				operation = scan.nextInt();
				String cityA;
				String cityB;
				String date;
				String airline;
				String reservation;
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
						System.out.println(last);
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
							System.out.println("Enter flight number or [0] if you are finished");
							
							String flightNumber = scan.nextLine();
							if (flightNumber.equals("0")) {
								break;
							}

							flights[leg] = flightNumber;
							System.out.println("Enter date [MM-DD-YYYY]");
							date = scan.nextLine();
							if(full(flightNumber,date)) {
								System.out.println("This flight is full, please try again");
								break;
							}
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
						return;
					case 12:
						System.exit(0);
					default:
						System.out.println("Not a valid operation code");
			}
		}
	}

	private boolean full(String flight, String date){
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT capacity('"+flight+"') FROM flight WHERE flight_number = " + flight; 
			ResultSet r = s.executeQuery(sql);
			r.next();
			int capacity = r.getInt(1);
			sql = "SELECT reserved('"+flight+"', TO_DATE('"+date+"', 'MM-DD-YYYY')) FROM flight WHERE flight_number = " + flight;
			r = s.executeQuery(sql);
			r.next()
			int reserved = r.getInt(1);
			if(capacity == reserved) return true;
			return false;
		}
		catch (Exception e) {
			System.out.println("Error fetching the specified flight. " + e.toString());
			return;
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
		catch(SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
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
				sql = "INSERT INTO Flight VALUES('" + flight[0] + "', '" + flight[1] + "', '" + flight[2] + "', '" + flight[3] + "', '" + flight[4] + "', '" + flight[5] + "', '" + flight[6] + "', '" + flight[7] + "')";
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
		catch(SQLException e) {
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
		catch(SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		}
	}

	private void changePrice(String departure, String arrival, String airline, int high, int low) {
		try {
			Statement s = connection.createStatement();
			String sql = "UPDATE Price SET High_Price = " + high + ", Low_Price = " + low + " WHERE Airline_ID = (SELECT p.Airline_ID FROM Price p LEFT JOIN Airline a ON p.Airline_ID = a.Airline_ID WHERE p.Departure_City = '" + departure + "'" + "AND p.Arrival_City = '" + arrival + "' AND a.Airline_Name = '" + airline + "')";
			s.executeUpdate(sql);
			System.out.println("\nThe price of the " + airline + " flight from " + departure + " to " + arrival + " was changed to " + high + "|" + low + ".\n");
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
		catch(SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		}
	}

	private void passengerManifest(String flightNumber, String date) {
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT Salutation, First_Name, Last_Name FROM Customer c INNER JOIN Reservation r ON c.CID = r.CID INNER JOIN Reservation_Detail rd ON r.Reservation_Number = rd.Reservation_Number WHERE rd.Flight_Number = '" + flightNumber + "' AND Flight_Date = TO_DATE('" + date + "','MM-DD-YYYY')";
			ResultSet r = s.executeQuery(sql);

			while (r.next()) {
				System.out.println(r.getString(1) + ". " + r.getString(2) + " " + r.getString(3));
			}
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
			System.out.println("\n\nOne-way flights from " + cityA + " to " + cityB);
			while (r.next()) {
		    	System.out.println("  Depart: " + r.getString("Departure_City") + "  Arrive: " + r.getString("Arrival_City") + "  Airline: " + r.getString("Airline_Name") + " \n"
			      + "    High Price: " + r.getLong("High_Price") + "  Low Price: " + r.getLong("Low_Price"));
		    }

		    sql = "SELECT * FROM Price p INNER JOIN Airline a ON p.Airline_ID = a.Airline_ID WHERE p.Departure_City = '" + cityB + "' AND p.Arrival_City = '" + cityA + "'";
			r = s.executeQuery(sql);
			System.out.println("\n\nOne-way flights from " + cityB + " to " + cityA);
			while (r.next()) {
		    	System.out.println("  Depart: " + r.getString("Departure_City") + "  Arrive: " + r.getString("Arrival_City") + "  Airline: " + r.getString("Airline_Name") + " \n"
			      + "    High Price: " + r.getLong("High_Price") + "  Low Price: " + r.getLong("Low_Price"));
		    }

			sql = "SELECT * FROM Airline a INNER JOIN (SELECT Airline_ID, SUM(High_Price) AS High, SUM(Low_Price) AS Low FROM Price p WHERE Departure_City = '" + cityA + "' AND Arrival_City = '" + cityB + "' OR Departure_City = '" + cityB + "' AND Arrival_City = '" + cityA + "' GROUP BY Airline_ID) p ON a.Airline_ID = p.Airline_ID";
			r = s.executeQuery(sql);
			System.out.println("\n\nRound-trip flights between " + cityA + " to " + cityB);
			while (r.next()) {
				System.out.println("  Airline: " + r.getString("Airline_Name") + " \n" + "    High Price: " + r.getLong("High") + "  Low Price: " + r.getLong("Low"));
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
			String sql = "SELECT Flight_number, Airline_ID, Departure_city, Arrival_City, Departure_time, Arrival_time" +
				"FROM Flight WHERE departure_city = '" + cityA + "' AND arrival_city = '" + cityB + "';";
			ResultSet r = s.executeQuery(sql);

			System.out.println("\nDirect flights from " + cityA + " to " + cityB);

			while (r.next()) {
		    	System.out.println("  Flight Number: " + r.getString(1) + " Airline: " + r.getString(2) + " \n"
			      + "    Depart: " + r.getString(3) + " Arrive " + r.getString(4) + " Depart Time: " + r.getString(5) + " Arrive Time: " + r.getString(6));
		    }

		    System.out.println();
			System.out.println("Flights with one connection from " + cityA + " to " + cityB);
			System.out.println();

		    sql = "SELECT f1.Flight_number, f1.Airline_ID, f1.Departure_city, f1.Arrival_City, f1.Departure_time, f1.Arrival_time, "+
		    			"f2.Flight_number, f2.Airline_ID, f2.Departure_city, f2.Arrival_City, f2.Departure_time, f2.Arrival_time "+
				"FROM FLIGHT f1, FLIGHT f2 "+
				"WHERE f1.Arrival_City = f2.Departure_city"+
				"AND f1.Departure_city = '" + cityA + "'"+
				"AND f2.Arrival_City = '" + cityB + "'"+
				"AND (TO_NUMBER(f2.Departure_Time) - TO_NUMBER(f1.Arrival_time) > 100)"+
				"AND ((SUBSTR(f1.weekly_schedule, 1, 1) = 'S' AND SUBSTR(f2.weekly_schedule, 1, 1) = 'S')"+
					"OR (SUBSTR(f1.weekly_schedule, 2, 1) = 'M' AND SUBSTR(f2.weekly_schedule, 2, 1) = 'M')"+
					"OR (SUBSTR(f1.weekly_schedule, 3, 1) = 'T' AND SUBSTR(f2.weekly_schedule, 3, 1) = 'T')"+
					"OR (SUBSTR(f1.weekly_schedule, 4, 1) = 'W' AND SUBSTR(f2.weekly_schedule, 4, 1) = 'W')"+
					"OR (SUBSTR(f1.weekly_schedule, 5, 1) = 'T' AND SUBSTR(f2.weekly_schedule, 5, 1) = 'T')"+
					"OR (SUBSTR(f1.weekly_schedule, 6, 1) = 'F' AND SUBSTR(f2.weekly_schedule, 6, 1) = 'F')"+
					"OR (SUBSTR(f1.weekly_schedule, 7, 1) = 'S' AND SUBSTR(f2.weekly_schedule, 7, 1) = 'S'));";
			r = s.executeQuery(sql);

			while (r.next()) {
		    	System.out.println( r.getString(1) + " " + r.getString(2) + r.getString(3) + " " +r.getString(4) + " " +r.getString(5) + " " +r.getString(6) + "\n" +
			      r.getString(7) + " " + r.getString(8) + r.getString(9) + " " +r.getString(10) + " " +r.getString(11) + " " +r.getString(12));
		    	System.out.println();
		    }

		    r.close();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void routesBetweenCitiesOnAirline(String cityA, String cityB, String airline) {
		try {

		Statement s = connection.createStatement();
			String sql = "SELECT Flight_number, Airline_ID, Departure_city, Arrival_City, Departure_time, Arrival_time" +
				"FROM Flight " +
				"WHERE departure_city = '" + cityA + "' " +
				"AND arrival_city = '" + cityB + "'" +
				"AND Airline_ID = '" + airline + "';";
			ResultSet r = s.executeQuery(sql);

			System.out.println();
			System.out.println("Direct flights from " + cityA + " to " + cityB);
			System.out.println();

			while (r.next()) {
		    	System.out.println("Flight Number: " + r.getString(1) + " Airline: " + r.getString(2) + " \n"
			      + "Depart: " + r.getString(3) + " Arrive " + r.getString(4) + " Depart Time: " + r.getString(5) + " Arrive Time: " + r.getString(6));
		    }

		    System.out.println();
			System.out.println("Flights with one connection from " + cityA + " to " + cityB);
			System.out.println();

		    sql = "SELECT f1.Flight_number, f1.Airline_ID, f1.Departure_city, f1.Arrival_City, f1.Departure_time, f1.Arrival_time, "+
		    			"f2.Flight_number, f2.Airline_ID, f2.Departure_city, f2.Arrival_City, f2.Departure_time, f2.Arrival_time "+
				"FROM FLIGHT f1, FLIGHT f2 "+
				"WHERE f1.Arrival_City = f2.Departure_city"+
				"AND f1.Departure_city = '" + cityA + "'"+
				"AND f2.Arrival_City = '" + cityB + "'"+
				"AND f1.Airline_ID = '" + airline + "'" +
				"AND f2.Airline_ID = '" + airline + "'" +
				"AND (TO_NUMBER(f2.Departure_Time) - TO_NUMBER(f1.Arrival_time) > 100)"+
				"AND ((SUBSTR(f1.weekly_schedule, 1, 1) = 'S' AND SUBSTR(f2.weekly_schedule, 1, 1) = 'S')"+
					"OR (SUBSTR(f1.weekly_schedule, 2, 1) = 'M' AND SUBSTR(f2.weekly_schedule, 2, 1) = 'M')"+
					"OR (SUBSTR(f1.weekly_schedule, 3, 1) = 'T' AND SUBSTR(f2.weekly_schedule, 3, 1) = 'T')"+
					"OR (SUBSTR(f1.weekly_schedule, 4, 1) = 'W' AND SUBSTR(f2.weekly_schedule, 4, 1) = 'W')"+
					"OR (SUBSTR(f1.weekly_schedule, 5, 1) = 'T' AND SUBSTR(f2.weekly_schedule, 5, 1) = 'T')"+
					"OR (SUBSTR(f1.weekly_schedule, 6, 1) = 'F' AND SUBSTR(f2.weekly_schedule, 6, 1) = 'F')"+
					"OR (SUBSTR(f1.weekly_schedule, 7, 1) = 'S' AND SUBSTR(f2.weekly_schedule, 7, 1) = 'S'));";
			r = s.executeQuery(sql);

			while (r.next()) {
		    	System.out.println( r.getString(1) + " " + r.getString(2) + r.getString(3) + " " +r.getString(4) + " " +r.getString(5) + " " +r.getString(6) + "\n" +
			      r.getString(7) + " " + r.getString(8) + r.getString(9) + " " +r.getString(10) + " " +r.getString(11) + " " +r.getString(12));
		    	System.out.println();
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
			String sql = "SELECT Flight_number, Airline_ID, Departure_city, Arrival_City, Departure_time, Arrival_time" +
				"FROM Flight WHERE departure_city = '" + cityA + "' AND arrival_city = '" + cityB + "' AND " +
				"(capacity(Flight_number) - reserved(Flight_number, TO_DATE('" + date + "', 'MM-DD-YYYY'))) > 0;";
			ResultSet r = s.executeQuery(sql);

			System.out.println();
			System.out.println("Direct flights from " + cityA + " to " + cityB);
			System.out.println();

			while (r.next()) {
		    	System.out.println("Flight Number: " + r.getString(1) + " Airline: " + r.getString(2) + " \n"
			      + "Depart: " + r.getString(3) + " Arrive " + r.getString(4) + " Depart Time: " + r.getString(5) + " Arrive Time: " + r.getString(6));
		    }

		    System.out.println();
			System.out.println("Flights with one connection from " + cityA + " to " + cityB);
			System.out.println();

		    sql = "SELECT f1.Flight_number, f1.Airline_ID, f1.Departure_city, f1.Arrival_City, f1.Departure_time, f1.Arrival_time, "+
		    			"f2.Flight_number, f2.Airline_ID, f2.Departure_city, f2.Arrival_City, f2.Departure_time, f2.Arrival_time "+
				"FROM FLIGHT f1, FLIGHT f2 "+
				"WHERE f1.Arrival_City = f2.Departure_city"+
				"AND f1.Departure_city = '" + cityA + "'"+
				"AND f2.Arrival_City = '" + cityB + "'"+
				"AND (TO_NUMBER(f2.Departure_Time) - TO_NUMBER(f1.Arrival_time) > 100)"+
				"AND (capacity(f1.Flight_number) - reserved(Flight_number, TO_DATE('" + date + "', 'MM-DD-YYYY'))) > 0" +
				"AND (capacity(f2.Flight_number) - reserved(Flight_number, TO_DATE('" + date + "', 'MM-DD-YYYY'))) > 0" +
				"AND ((SUBSTR(f1.weekly_schedule, 1, 1) = 'S' AND SUBSTR(f2.weekly_schedule, 1, 1) = 'S')"+
					"OR (SUBSTR(f1.weekly_schedule, 2, 1) = 'M' AND SUBSTR(f2.weekly_schedule, 2, 1) = 'M')"+
					"OR (SUBSTR(f1.weekly_schedule, 3, 1) = 'T' AND SUBSTR(f2.weekly_schedule, 3, 1) = 'T')"+
					"OR (SUBSTR(f1.weekly_schedule, 4, 1) = 'W' AND SUBSTR(f2.weekly_schedule, 4, 1) = 'W')"+
					"OR (SUBSTR(f1.weekly_schedule, 5, 1) = 'T' AND SUBSTR(f2.weekly_schedule, 5, 1) = 'T')"+
					"OR (SUBSTR(f1.weekly_schedule, 6, 1) = 'F' AND SUBSTR(f2.weekly_schedule, 6, 1) = 'F')"+
					"OR (SUBSTR(f1.weekly_schedule, 7, 1) = 'S' AND SUBSTR(f2.weekly_schedule, 7, 1) = 'S'))";
			r = s.executeQuery(sql);

			while (r.next()) {
		    	System.out.println( r.getString(1) + " " + r.getString(2) + r.getString(3) + " " +r.getString(4) + " " +r.getString(5) + " " +r.getString(6) + "\n" +
			      r.getString(7) + " " + r.getString(8) + r.getString(9) + " " +r.getString(10) + " " +r.getString(11) + " " +r.getString(12));
		    	System.out.println();
		    }

		    r.close();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void availableSeats(String cityA, String cityB, String date, String airline) {
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT Flight_number, Airline_ID, Departure_city, Arrival_City, Departure_time, Arrival_time" +
				"FROM Flight WHERE departure_city = '" + cityA + "' AND arrival_city = '" + cityB + "' AND Airline_ID = '" + airline + "'" +
				"(capacity(Flight_number) - reserved(Flight_number, TO_DATE('" + date + "', 'MM-DD-YYYY'))) > 0;";
			ResultSet r = s.executeQuery(sql);

			System.out.println();
			System.out.println("Direct flights from " + cityA + " to " + cityB);
			System.out.println();

			while (r.next()) {
		    	System.out.println("Flight Number: " + r.getString(1) + " Airline: " + r.getString(2) + " \n"
			      + "Depart: " + r.getString(3) + " Arrive " + r.getString(4) + " Depart Time: " + r.getString(5) + " Arrive Time: " + r.getString(6));
		    }

		    System.out.println();
			System.out.println("Flights with one connection from " + cityA + " to " + cityB);
			System.out.println();

		    sql = "SELECT f1.Flight_number, f1.Airline_ID, f1.Departure_city, f1.Arrival_City, f1.Departure_time, f1.Arrival_time, "+
		    			"f2.Flight_number, f2.Airline_ID, f2.Departure_city, f2.Arrival_City, f2.Departure_time, f2.Arrival_time "+
				"FROM FLIGHT f1, FLIGHT f2 "+
				"WHERE f1.Arrival_City = f2.Departure_city"+
				"AND f1.Departure_city = '" + cityA + "'"+
				"AND f2.Arrival_City = '" + cityB + "'"+
				"AND f1.Airline_ID = '" + airline + "'" +
				"AND f2.Airline_ID = '" + airline + "'" +
				"AND (TO_NUMBER(f2.Departure_Time) - TO_NUMBER(f1.Arrival_time) > 100)"+
				"AND (capacity(f1.Flight_number) - reserved(Flight_number, TO_DATE('" + date + "', 'MM-DD-YYYY'))) > 0" +
				"AND (capacity(f2.Flight_number) - reserved(Flight_number, TO_DATE('" + date + "', 'MM-DD-YYYY'))) > 0" +
				"AND ((SUBSTR(f1.weekly_schedule, 1, 1) = 'S' AND SUBSTR(f2.weekly_schedule, 1, 1) = 'S')"+
					"OR (SUBSTR(f1.weekly_schedule, 2, 1) = 'M' AND SUBSTR(f2.weekly_schedule, 2, 1) = 'M')"+
					"OR (SUBSTR(f1.weekly_schedule, 3, 1) = 'T' AND SUBSTR(f2.weekly_schedule, 3, 1) = 'T')"+
					"OR (SUBSTR(f1.weekly_schedule, 4, 1) = 'W' AND SUBSTR(f2.weekly_schedule, 4, 1) = 'W')"+
					"OR (SUBSTR(f1.weekly_schedule, 5, 1) = 'T' AND SUBSTR(f2.weekly_schedule, 5, 1) = 'T')"+
					"OR (SUBSTR(f1.weekly_schedule, 6, 1) = 'F' AND SUBSTR(f2.weekly_schedule, 6, 1) = 'F')"+
					"OR (SUBSTR(f1.weekly_schedule, 7, 1) = 'S' AND SUBSTR(f2.weekly_schedule, 7, 1) = 'S'))";
			r = s.executeQuery(sql);

			while (r.next()) {
		    	System.out.println( r.getString(1) + " " + r.getString(2) + r.getString(3) + " " +r.getString(4) + " " +r.getString(5) + " " +r.getString(6) + "\n" +
			      r.getString(7) + " " + r.getString(8) + r.getString(9) + " " +r.getString(10) + " " +r.getString(11) + " " +r.getString(12));
		    	System.out.println();
		    }

		    r.close();
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	private void addReservation(String flights[], String dates[]) { 
	//asks the user for name and populates the reservation info with the customer's information
	//determines high and low prices based on today's date: reservation date is today
		try {
			//get name
			Scanner scan = new Scanner(System.in);
		    System.out.println("Enter first name");
		    String firstName = scan.nextLine();
		    System.out.println("Enter last name");
		    String lastName = scan.nextLine();

			Statement s = connection.createStatement();
			String sql = "SELECT CID, Credit_Card_Num, Frequent_Miles FROM Customer WHERE First_Name = '" + firstName + "' AND Last_Name = '" + lastName + "'";
			ResultSet r = s.executeQuery(sql);

			//check if customer exists
			if (!r.isBeforeFirst() ) {    
    			System.out.println("Customer not found. Please sign up as a customer"); 
    			return;
			}

			//get customer info
			r.next();
			String cid = r.getString("CID");
			String credit = r.getString("Credit_Card_Num");
			String freq = r.getString("Frequent_Miles");

			if(credit = null) credit = "";
			if(freq = null) freq = "";


			//get start and end cities for the whole reservation
			sql = "SELECT Departure_city FROM Flight WHERE Flight_Number = '" + flights[0] + "'";
			r = s.executeQuery(sql);
			r.next();
			String departure_city = r.getString("Departure_city");

			sql = "SELECT Arrival_City FROM Flight WHERE Flight_Number = '" + flights[flights.length-1] + "'";
			r = s.executeQuery(sql);
			r.next();
			String arrival_city = r.getString("Arrival_City");

			sql = "SELECT TO_CHAR (SYSDATE, 'MM-DD-YYYY') "NOW" FROM DUAL";
			r = s.executeQuery(sql);
			r.next();
			String today = r.getString("NOW");

			//calculate totalPrice
			int totalPrice = 0;
			for(int i = 0; i < flights.length; i++){
				sql = "SELECT Departure_City, Arrival_City, Airline_ID FROM Flight WHERE Flight_Number = '" + flights[i] + "'";
				r = s.executeQuery(sql);
				r.next();
				String a_city = r.getString("Arrival_City");
				String d_city = r.getString("Departure_City");
				String airline = r.getString("Airline_ID");

				//if same day flight, add high price, else add low price
				if(dates[i].equals(today)
					sql = "SELECT High_Price FROM Price WHERE Departure_city = '" + d_city + "' AND Arrival_City = '" + a_city + "' AND Airline_ID = '"+ airline + "'";
				else
					sql = "SELECT Low_Price FROM Price WHERE Departure_city = '" + d_city + "' AND Arrival_City = '" + a_city + "' AND Airline_ID = '"+ airline + "'";
				
				r = s.executeQuery(sql);
				r.next();
				int priceOfFlight = r.getInt("1");

				//check if customer is frequent miles member of airline
				if(freq.equals(airline))
					priceOfFlight *= 9;
					priceOfFlight /= 10;

				totalPrice += priceOfFlight;
			}

			//generate random reservation number
			String res_num;
			Random r = new Random();
			while(true){
				int random = r.nextInt(100000)
				res_num = String.format("%05d", String.parseInt(random));
				sql = "SELECT * FROM Reservation WHERE Reservation_Number = '" + res_num + "'";
				r = s.executeQuery(sql);
				//check if resultSet is empty, if so, we've found a new reservation number
				if (!r.isBeforeFirst()) break;
			}

			//insert flights into reservation_detail
			for(int i = 0; i < flights.length; i++){
				sql = "INSERT INTO Reservation_Detail VALUES('" + res_num + "', '"+ flights[i] + "', '" + dates[i] + "', " + Integer.toString(i) + ")";
				r.executeQuery(sql);
			}
			
			sql = "INSERT INTO Reservation VALUES('" + res_num + "', " + cid + "', " + Integer.toString(totalPrice) + ", '" + credit + "', TO_DATE('"+ today +"', 'MM-DD-YYYY')," + 
				"'N', '" + departure_city + "', '"+ arrival_city + "')";

			System.out.println();
			System.out.println("Direct flights from " + cityA + " to " + cityB);
			System.out.println();

			while (r.next()) {
		    	System.out.println("Flight Number: " + r.getString(1) + " Airline: " + r.getString(2) + " \n"
			      + "Depart: " + r.getString(3) + " Arrive " + r.getString(4) + " Depart Time: " + r.getString(5) + " Arrive Time: " + r.getString(6));
		    }

		    System.out.println("Reservation number " + res_num + "confirmed");

		    r.close();
		}
		catch (Exception e) {
			System.out.println("Error creating reservation" + e.toString());
		}
	}

	private void showReservation(String reservation) {

	}

	private void buyTickets(String reservation) {

	}


	public static void main(String args[]) throws SQLException {
		String username = "dmd113";
		String password = "1234";

		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
		  	String url = "jdbc:oracle:thin:@class3.cs.pitt.edu:1521:dbclass";

			connection = DriverManager.getConnection(url, username, password);
			Driver driver = new Driver();
		}
		catch(Exception Ex) {
			System.out.println("Error connecting to database.  Machine Error: " + Ex.toString());
		}
		finally {
			connection.close();
		}
	}
}
