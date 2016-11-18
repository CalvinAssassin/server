package edu.calvin.cs262;

import com.google.gson.Gson;
import java.sql.*;

/**
 * Created by cdh24 on 11/16/16.
 *
 * This class models a player as it appears in our server's database.  It provides methods for creating, storing, and
 * retrieving data from the database.
 */
public class PlayerProfile {

    // Instance variables for storing all the data associated with this player
    public Integer ID;
    public String firstName;
    public String lastName;
    public String residence;
    public String major;


    // Default constructor
    public PlayerProfile() {
        this("", "", "", "");
    }

    // Parametrized constructor without ID number (eg. for creating a player in the server)
    public PlayerProfile(String firstName, String lastName, String residence, String major) {
        this(null, firstName, lastName, residence, major);
    }

    // Full value constructor
    public PlayerProfile(Integer ID, String firstName, String lastName, String residence, String major) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.residence = residence;
        this.major = major;
    }

    // This method is for convenience--it sets the ID, and the loads data from the DB
    public void loadFromDataBase(int playerID) throws Exception {
        // Set the player ID of the class to playerID parameter
        this.ID = playerID;

        // Call the load function
        this.loadFromDataBase();
    }

    // This method goes to the DB and populates the class with data from the DB, based on the playerID
    public void loadFromDataBase() throws Exception {
        // Create connection to the database
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;


        // Try connecting and retrieving data
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(CalvinAssassin.DB_URI, CalvinAssassin.DB_LOGIN_ID, CalvinAssassin.DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery(this.generateQueryString("read"));
            rs.next();

            // Set the instance variables to the returned values
            ID              = rs.getInt(1);
            firstName       = rs.getString(2);
            lastName        = rs.getString(3);
            residence       = rs.getString(4);
            major           = rs.getString(5);
        }
        catch (SQLException e) {
            throw (e);
        }
        catch (ClassNotFoundException e) {
            throw (e);
        }
        finally {
            // Close all residual connection stuff
            rs.close();
            statement.close();
            connection.close();
        }
    }

    // Output a JSON-formatted representation of this class
    public String getJSON() {
        return new Gson().toJson(this);
    }

    // Generate a query string for this class
    public String generateQueryString(String queryType) {

        switch (queryType) {
            case "read":
                return ("SELECT * FROM Player WHERE Player.playerID = " + Integer.toString(this.ID));
            case "insert":
                // return an SQL query here
                break;
            case "update":
                // return an SQL query here
                break;
            case "delete":
                // return an SQL query here
                break;
            default:
                return "";
        }
        return "";
    }
}
