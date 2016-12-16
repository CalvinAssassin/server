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
    public Double latitude;
    public Double longitude;
    public String locUpdateTime;
    public Integer currentGameID;
    public Boolean isAlive;
    public PlayerTargetInfo targetInfo;


    /**
     * This is the default constructor for the object
     * @author: Christiaan Hazlett
     */
    public PlayerProfile() {
        this("", "", "", "", 0.0, 0.0, "", 0, false);
    }

    /**
     * This is the paramatarized constructor, but without an ID number (eg. for new games)
     * @param firstName, the first name of the player
     * @param lastName, the last name of the player
     * @param residence, the living location of the player
     * @param major, the player's major
     * @param latitude, the player's latitude;
     * @param longitude, the player's longitude
     * @param locUpdateTime, the date and time of the last location update
     * @param currentGameID, the game which the player is playing in
     * @param isAlive, whether the player is alive or not
     * @author: Christiaan Hazlett
     */
    public PlayerProfile(String firstName, String lastName, String residence, String major, Double latitude,
                         Double longitude, String locUpdateTime, Integer currentGameID, Boolean isAlive) {
        this(null, firstName, lastName, residence, major, latitude, longitude, locUpdateTime, currentGameID, isAlive);
    }

    /**
     * This is the paramatarized constructor for the player
     * @param ID, the ID number of the player
     * @param firstName, the first name of the player
     * @param lastName, the last name of the player
     * @param residence, the living location of the player
     * @param major, the player's major
     * @param latitude, the player's latitude;
     * @param longitude, the player's longitude
     * @param locUpdateTime, the date and time of the last location update
     * @param currentGameID, the game which the player is playing in
     * @param isAlive, whether the player is alive or not
     * @author: Christiaan Hazlett
     */
    public PlayerProfile(Integer ID, String firstName, String lastName, String residence, String major, Double latitude,
                         Double longitude, String locUpdateTime, Integer currentGameID, Boolean isAlive) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.residence = residence;
        this.major = major;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locUpdateTime = locUpdateTime;
        this.currentGameID = currentGameID;
        this.isAlive = isAlive;
    }

    /**
     * This method sets the ID of the player, and then loads the player's info from the DB
     * @param playerID, the ID number of the player
     * @author: Christiaan Hazlett
     */
    public void loadFromDataBase(int playerID) throws Exception {
        // Set the player ID of the class to playerID parameter
        this.ID = playerID;

        // Call the load function
        this.loadFromDataBase();
    }

    /**
     * Retrieve informationa about the player from the DB
     * @author: Christiaan Hazlett
     */
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
            latitude        = rs.getDouble(6);
            longitude       = rs.getDouble(7);
            locUpdateTime   = rs.getString(8);
            currentGameID   = rs.getInt(9);
            isAlive         = rs.getBoolean(10);
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

    /**
     * This method inserts the player into the DB and returns the new ID number
     * @return int, the ID number of the player
     * @author: Christiaan Hazlett
     */
    public int insertIntoDataBase() throws Exception {
        // Create connection to the database
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;


        // Try connecting and inserting data
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(CalvinAssassin.DB_URI, CalvinAssassin.DB_LOGIN_ID, CalvinAssassin.DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery(this.generateQueryString("insert"));
            rs.next();

            // Set the instance variables to the returned ID
            ID              = rs.getInt(1);

            // Return the ID of the new record
            return ID;
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

    /**
     * This method saves the data in the class to the DB
     * @author: Christiaan Hazlett
     */
    public void saveToDataBase() throws Exception {

//        // Make sure the update request has an ID number
//        if (this.ID == null) {
//            throw new IllegalArgumentException("Updating a record requires an ID number.");
//        }

        // Create connection to the database
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;


        // Try connecting and inserting data
//        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(CalvinAssassin.DB_URI, CalvinAssassin.DB_LOGIN_ID, CalvinAssassin.DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery(this.generateQueryString("update"));
        //}
//        catch (SQLException e) {
//            throw (e);
//        }
//        catch (ClassNotFoundException e) {
//            throw (e);
//        }
//        finally {
//            // Close all residual connection stuff
//            rs.close();
//            statement.close();
//            connection.close();
//        }
    }

    /**
     * This method deletes the player from the database
     * @author: Christiaan Hazlett
     */
    public void deleteFromDataBase() throws Exception {

        // Create connection to the database
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;


        // Try connecting and inserting data
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(CalvinAssassin.DB_URI, CalvinAssassin.DB_LOGIN_ID, CalvinAssassin.DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery(this.generateQueryString("delete"));
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

    /**
     * This class is the data structure that models information about a player's target
     * @author: Christiaan Hazlett
     */
    public class PlayerTargetInfo {
        public Integer matchID;
        public Integer gameID;
        public Integer playerID;
        public Integer targetID;
        public String targetStartTime;
        public String targetTimeoutTime;
        public String targetTimeLeft;
    }

    /**
     * This method loads information about the player's target into the targetinfo object
     * @author: Christiaan Hazlett
     */
    public void getTargetInformationFromDatabase() throws Exception {
        // Create a new targetInformation object
        targetInfo = new PlayerTargetInfo();

        // Create connection to the database
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;


        // Try connecting and retrieving data
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(CalvinAssassin.DB_URI, CalvinAssassin.DB_LOGIN_ID, CalvinAssassin.DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery(this.generateQueryString("readTargetInfo"));
            rs.next();

            // Set the instance variables for the target info object to the returned values
//            targetInfo.matchID               = rs.getInt(1);
            targetInfo.gameID                = rs.getInt(2);
            targetInfo.playerID              = rs.getInt(3);
            targetInfo.targetID              = rs.getInt(4);
            targetInfo.targetStartTime       = rs.getString(5);
            targetInfo.targetTimeoutTime     = rs.getString(6);
            targetInfo.targetTimeLeft        = "10001";

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

    /**
     * This method outputs a JSON string of the target object
     * @return a string of JSON
     * @author: Christiaan Hazlett
     */
    public String getTargetJSON() {
        return new Gson().toJson(this.targetInfo);
    }

    /**
     * This method outputs a JSON string of the player object
     * @return a string of JSON
     * @author: Christiaan Hazlett
     */
    public String getJSON() {
        return new Gson().toJson(this);
    }

    /**
     * This method returns the correct SQL query string for each operation in the class
     * @param queryType, the type of query to be done
     * @return an SQL query string
     * @author: Christiaan Hazlett
     */
    public String generateQueryString(String queryType) {

        switch (queryType) {
            case "read":
                return ("SELECT playerID, firstName, lastName, residence, major, latitude, longitude, locUpdateTime, " +
                        "gameID, alive FROM Player WHERE Player.playerID = " + Integer.toString(this.ID));
            case "insert":
                return ("INSERT INTO Player(firstName, lastName, residence, major, latitude, longitude, " +
                        "locUpdateTime, gameID, alive) VALUES('"+
                        firstName +"', '"+
                        lastName +"', '"+
                        residence +"', '"+
                        major+"', '"+
                        latitude+"', '"+
                        longitude+"', NOW(), '"+
                        currentGameID+"', '"+
                        isAlive+
                        "') RETURNING playerID;"
                );
            case "update":
                return ("UPDATE Player SET" +
                        " firstName ='"+ firstName +
                        "', lastName ='"+ lastName +
                        "', residence ='"+ residence +
                        "', major ='"+ major +
                        "', latitude ='"+ latitude +
                        "', longitude ='"+ longitude +
                        "', locUpdateTime = NOW()" +
                        ", gameID ='"+ currentGameID +
                        "', alive ='"+ isAlive +
                        "' WHERE player.playerID = "+ Integer.toString(this.ID) + " RETURNING playerID;");
            case "delete":
                return ("DELETE FROM Player WHERE playerID = " + Integer.toString(this.ID) + " RETURNING 1;");
            case "readTargetInfo":
                return ("SELECT matchID, gameID, playerID, targetID, targetStartTime, targetTimeoutTime " +
                        "FROM TargetMatches WHERE " +
                        "TargetMatches.playerID = " + Integer.toString(ID) + " AND " +
                        "TargetMatches.gameID = " + Integer.toString(currentGameID) + " " +
                        "LIMIT 1;");
            default:
                return "";
        }
    }
}

//
//   _____      _       _                                        _
//  / ____|    | |     (_)           /\                         (_)
// | |     __ _| |_   ___ _ __      /  \   ___ ___  __ _ ___ ___ _ _ __
// | |    / _` | \ \ / / | '_ \    / /\ \ / __/ __|/ _` / __/ __| | '_ \
// | |___| (_| | |\ V /| | | | |  / ____ \\__ \__ \ (_| \__ \__ \ | | | |
//  \_____\__,_|_| \_/ |_|_| |_| /_/    \_\___/___/\__,_|___/___/_|_| |_|
//