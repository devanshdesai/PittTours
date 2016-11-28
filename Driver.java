import java.sql.*;  //import the file containing definitions for the parts
//import java.text.ParseException;

public class Driver {
	private static Connection connection; //used to hold the jdbc connection to the DB
    private Statement statement; //used to create an instance of the connection
    private PreparedStatement prepStatement; //used to create a prepared statement, that will be later reused
    private ResultSet resultSet; //used to hold the result of your query (if one exists)
    private String query;  //this will hold the query we are using

	public Driver(){
		int counter = 1;
		/*We will now perform a simple query to the database, asking for all the
		records it has.  For your project, performing queries will be similar*/
		try{
		    statement = connection.createStatement(); //create an instance
		    String selectQuery = "SELECT * FROM Airline"; //sample query
		    
		    resultSet = statement.executeQuery(selectQuery); //run the query on the DB table
		    
		    /*the results in resultSet have an odd quality. The first row in result
		      set is not relevant data, but rather a place holder.  This enables us to
		      use a while loop to go through all the records.  We must move the pointer
		      forward once using resultSet.next() or you will get errors*/

		    while (resultSet.next()) //this not only keeps track of if another record
			//exists but moves us forward to the first record
		    {
		    	System.out.println("Record " + counter + ": " +
			      resultSet.getString(1) + ", " + //since the first item was of type
			      //string, we use getString of the
			      //resultSet class to access it.
			      //Notice the one, that is the
			      //position of the answer in the
			      //resulting table
			      resultSet.getLong(2) + ", " +   //since second item was number(10),
			      //we use getLong to access it
			      resultSet.getDate(3)); //since type date, getDate.
		    	counter++;
		    }
		    resultSet.close();
		}
	    catch(SQLException Ex) {
			System.out.println("Error running the sample queries.  Machine Error: " + Ex.toString());
		} 
		/*catch (ParseException e) {
			System.out.println("Error parsing the date. Machine Error: " + e.toString());
		}*/
		finally{
			try {
				if (statement != null) statement.close();
				if (prepStatement != null) prepStatement.close();
			} 
			catch (SQLException e) {
				System.out.println("Cannot close Statement. Machine error: " + e.toString());
			}
		}
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