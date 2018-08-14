package net.myexperiments.gutenberg;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


public class ResultSet {
MariaDBHelper db  = new MariaDBHelper();
String con;
String mysqlpassword;
Connection connection;
String username;
String hostname;
String host;
String user;
String password;
Statement stmt;

	public ResultSet (Properties propfile)  {
		
	}
	boolean createconnection(Properties prop, String host, String sql, String table) {
		username = prop.getProperty("mysqluser");
		mysqlpassword = prop.getProperty("mysqlpassword");
		if (host == "")
			host = prop.getProperty("mysqlhost");
		if (sql == "")
			sql = prop.getProperty("createtable");
		hostname = host;
		try {
			// "jdbc:mariadb://localhost:3306/DB?user=root&password=myPassword");
			String con = "jdbc:mariadb://" + hostname + ":3306/gutenberg?user=" + username + "&password="
					+ mysqlpassword;
			connection = DriverManager.getConnection(con);
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
}
/*
	    public static void main(String[] args) {
	        // The credentials that we need to have available for the connection to the database.
	        String username = "myusername";
	        String password = "mypassword";
	        String databaseName = "albums";

	        Connection connect = null;
	        Statement statement = null;


	        try {
	            // Load the MySQL driver.
	            Class.forName("com.mysql.jdbc.Driver");

	            // Setup the connection to the database.
	            // Take notice at the different variables that are needed here:
	            //		1. The name of the database and its location (currently localhost)
	            //		2. A valid username/password for the connection.
	            connect = DriverManager.getConnection("jdbc:mysql://localhost/"
	                    + databaseName + "?"
	                    + "user=" + username
	                    + "&password=" + password);

	            // Create the statement to be used to get the results.
	            statement = connect.createStatement();

	            // Create a query to use.
	            String query = "SELECT * FROM the_classics ORDER BY year";

	            // Execute the query and get the result set, which contains
	            // all the results returned from the database.
	            java.sql.ResultSet resultSet = statement.executeQuery(query);

	            // We loop through the rows that were returned, and we can access the information
	            // depending on the type of each column. In this case:
	            //		Album: Varchar, so we use getString().
	            //		Artist: Also Varchar, so getString() again.
	            //		Year: Int, so we use getInt().
	            // For other types of columns, such as boolean or Date, we use the appropriate methods.
	            while (resultSet.next()) {
	                System.out.println("Printing result...");

	                // Now we can fetch the data by column name, save and use them!
	                String albumName = resultSet.getString("name");
	                String artist = resultSet.getString("artist");
	                int year = resultSet.getInt("year");

	                System.out.println("\tAlbum: " + albumName + 
	                        ", by Artist: " + artist + 
	                        ", released in: " + year);
	            }

	        } catch (ClassNotFoundException e) {
	            e.printStackTrace();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        } finally {
	            // We have to close the connection and release the resources used.
	            // Closing the statement results in closing the resultSet as well.
	            try {
	                statement.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }

	            try {
	                connect.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    
	   */ 
	    
	}
