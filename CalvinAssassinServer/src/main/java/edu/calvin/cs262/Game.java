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


    /**
     * This is the default constructor for the object
     * @author: Christiaan Hazlett
     */
    public Game() {
        this(null, null, null);
    }

    /**
     * This is the paramatarized constructor for the game, without the ID value (eg. for inserting a game)
     * @param gameName, the name of the game
     * @param inPlay, whether the game is in play
     * @param startDate, the starting date and time of the game
     * @author: Christiaan Hazlett
     */
    public Game(String gameName, Boolean inPlay, String startDate) {
        this(null, gameName, inPlay, startDate);
    }

    /**
     * This is the paramatarized constructor for the game
     * @param ID,
     * @param gameName, the name of the game
     * @param inPlay, whether the game is in play
     * @param startDate, the starting date and time of the game
     * @author: Christiaan Hazlett
     */
    public Game(Integer ID, String gameName, Boolean inPlay, String startDate) {
        this.ID = ID;
        this.gameName = gameName;
        this.inPlay = inPlay;
        this.startDate = startDate;
    }

    /**
     * This method sets the ID of the game, and then loads the game's info from the DB
     * @param gameID, the ID number of the game
     * @author: Christiaan Hazlett
     */
    public void loadFromDataBase(int gameID) throws Exception {
        // Set the game ID of the class to gameID parameter
        this.ID = gameID;

        // Call the load function
        this.loadFromDataBase();
    }

    /**
     * Retrieve informationa about the game from the DB
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

    /**
     * This method loads all the player in the game, and stores them in this class
     * @author: Christiaan Hazlett
     */
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
                        rs.getDouble(6),
                        rs.getDouble(7),
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

    /**
     * This method inserts the game into the database, and returns the game's ID number
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
     * This method updates the DB with info from this class
     * @author: Christiaan Hazlett
     */
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

    /**
     * This method deletes the game from the database
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
     * This method returns a JSON representation of this class
     * @return string, a JSON string of the class
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