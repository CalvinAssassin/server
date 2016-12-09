package edu.calvin.cs262;

import com.google.gson.Gson;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by cdh24 on 11/16/16.
 *
 * This class models a game object from the database, and provides methods for interacting with the game, getting data,
 * retrieving from and storing to the database.
 */
public class Game {

    // Instance variables for storing all the data associated with this game
    public Integer                  ID;
    public String                   gameName;
    public Boolean                  inPlay;
    public String                   startDate;
    public ArrayList<PlayerProfile> players = new ArrayList<PlayerProfile>();


    // Default constructor
    public Game() {
        this(null, null, null);
    }

    // Parametrized constructor without ID number (eg. for creating a game object in the server)
    public Game(String gameName, Boolean inPlay, String startDate) {
        this(null, gameName, inPlay, startDate);
    }

    // Full value constructor
    public Game(Integer ID, String gameName, Boolean inPlay, String startDate) {
        this.ID = ID;
        this.gameName = gameName;
        this.inPlay = inPlay;
        this.startDate = startDate;
    }

    // This method is for convenience--it sets the ID, and the loads data from the DB
    public void loadFromDataBase(int gameID) throws Exception {
        // Set the game ID of the class to gameID parameter
        this.ID = gameID;

        // Call the load function
        this.loadFromDataBase();
    }

    // This method goes to the DB and populates the class with data from the DB, based on the gameID
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
            gameName        = rs.getString(2);
            inPlay          = rs.getBoolean(3);
            startDate       = rs.getString(4);
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

    // This method retrieves the data for and creates the objects of the players in the game, then puts them in the
    // players array
    public void getPlayers() throws Exception {
        // Create connection to the database
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;

        // Try connecting and retrieving data
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(CalvinAssassin.DB_URI, CalvinAssassin.DB_LOGIN_ID, CalvinAssassin.DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery(this.generateQueryString("getPlayers"));

            while(rs.next()) {
                // Set the instance variables to the returned values
                PlayerProfile curPlayer = new PlayerProfile(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getFloat(6),
                        rs.getFloat(7),
                        rs.getString(8),
                        rs.getInt(9),
                        rs.getBoolean(10)
                );
                this.players.add(curPlayer);
            }
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

    // This method creates a record for the object in the database and returns the id of the created record
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

    // This method saves the data in the object to the database
    public void saveToDataBase() throws Exception {

//        // Make sure the update request has an ID number
//        if (this.ID == null) {
//            throw new IllegalArgumentException("Updating a record requires an ID number.  To create a new object, use POST.");
//        }

        // Create connection to the database
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;


        // Try connecting and inserting data
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(CalvinAssassin.DB_URI, CalvinAssassin.DB_LOGIN_ID, CalvinAssassin.DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery(this.generateQueryString("update"));
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

    // This method deletes the game from the database
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

    // Output a JSON-formatted representation of this class
    public String getJSON() {
        return new Gson().toJson(this);
    }

    // Generate a query string for this class
    public String generateQueryString(String queryType) {

        switch (queryType) {
            case "read":
                return ("SELECT gameID, gameName, inPlay, startDate FROM Game WHERE Game.gameID = " +
                        Integer.toString(this.ID));
            case "insert":
                return ("INSERT INTO game(gameName, inPlay, startDate) VALUES('" +
                        gameName + "', " +
                        inPlay + ", '" +
                        startDate + "') RETURNING gameID;"
                );
            case "update":
                return ("UPDATE Game SET " +
                        "gameID='" + ID +
                        "', gameName='" + gameName +
                        "', inPlay='" + inPlay +
                        "', startDate='" + startDate +
                        "' WHERE game.gameID = " + Integer.toString(this.ID) + " RETURNING gameID;"
                );
            case "delete":
                return ("DELETE FROM Game WHERE gameID = " + Integer.toString(this.ID) + " RETURNING 1;");
            case "getPlayers":
                return ("SELECT playerID, firstName, lastName, residence, major, latitude, longitude, locUpdateTime, " +
                        "gameID, alive FROM Player WHERE Player.gameID = " + Integer.toString(this.ID));
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