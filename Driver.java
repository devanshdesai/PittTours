import java.sql.*;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
						}
						else if ((response.toUpperCase()).equals("C")) {
							System.out.println("Enter the departure city");
							String departureCity = scan.nextLine();
							System.out.println("Enter the arrival city");
							String arrivalCity = scan.nextLine();
							System.out.println("Enter the new high price");
							int highPrice = scan.nextInt();
							System.out.println("Enter the new low price");
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
					+ "[2] Show customer info, given customer name \n"
					+ "[3] Find price for flights between two cities \n"
					+ "[4] Find all routes between two cities \n"
					+ "[5] Find all routes between two cities of a given airline \n"
					+ "[6] Find all routes with available seats between two cities on given day \n"
					+ "[7] For a given airline, find all routes with available seats between two cities on given day \n"
					+ "[8] Add reservation \n"
					+ "[9] Show reservation info, given reservation number \n"
					+ "[10] Buy ticket from existing reservation\n"
					+ "[11] Exit");
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
				}
			}
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

	private void changePrice(String departure, String arrival, int high, int low) {
		try {
			Statement s = connection.createStatement();
			String sql = "UPDATE Price SET High_Price = " + high + ", Low_Price = " + low + " WHERE Airline_ID = (SELECT Airline_ID FROM Price WHERE Departure_City = '" + departure + "'" + "AND Arrival_City = '" + arrival + "')";
			s.executeUpdate(sql);
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
		catch(SQLException e) {
			System.out.println("SQL Error: " + e.getMessage());
		}
	}

	private void passengerManifest(String flightNumber, String date) {
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT Salution, First_Name, Last_Name FROM Customer c INNER JOIN Reservation r ON c.CID = r.CID INNER JOIN Reservation_Detail rd ON r.Reservation_Number = rd.Reservation_Number WHERE rd.Flight_Number = '" + flightNumber + "' AND Flight_Date = TO_DATE('" + date + "','MM-DD-YYYY')";
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
			r.next();
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
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT Flight_number, Airline_ID, Departure_city, Arrival_City, Departure_time, Arrival_time" +
				"FROM Flight WHERE departure_city = '" + cityA + "' AND arrival_city = '" + cityB + "';";
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
				"(capacity(Flight_number) - reserved(Flight_number, TO_DATE('" + date + "', 'YYYY-MM-DD'))) > 0;";
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
				"AND (capacity(f1.Flight_number) - reserved(Flight_number, TO_DATE('" + date + "', 'YYYY-MM-DD'))) > 0" +
				"AND (capacity(f2.Flight_number) - reserved(Flight_number, TO_DATE('" + date + "', 'YYYY-MM-DD'))) > 0" +
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

	private void availableSeats(String cityA, String cityB, String date, String airline) {
		try {
			Statement s = connection.createStatement();
			String sql = "SELECT Flight_number, Airline_ID, Departure_city, Arrival_City, Departure_time, Arrival_time" +
				"FROM Flight WHERE departure_city = '" + cityA + "' AND arrival_city = '" + cityB + "' AND Airline_ID = '" + airline + "'" +
				"(capacity(Flight_number) - reserved(Flight_number, TO_DATE('" + date + "', 'YYYY-MM-DD'))) > 0;";
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
				"AND (capacity(f1.Flight_number) - reserved(Flight_number, TO_DATE('" + date + "', 'YYYY-MM-DD'))) > 0" +
				"AND (capacity(f2.Flight_number) - reserved(Flight_number, TO_DATE('" + date + "', 'YYYY-MM-DD'))) > 0" +
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

	private void addReservation(String flights[], String dates[]) {

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