package edu.calvin.cs262;

import com.google.gson.Gson;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import javax.ws.rs.*;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.core.UriInfo;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//import java.sql.*;
//import java.util.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This module implements the server for the Calvin-Assassin game.
 * The server requires Java 1.7 (not 1.8).
 *
 *
 * @author cdh24, The B Team
 * @version v -9000.1

 */
@Path("/api")
public class CalvinAssassin {


    // Database connection constants
    public static final String DB_URI = "jdbc:postgresql://localhost:5432/assassin";
    public static final String DB_LOGIN_ID = "postgres";
    public static final String DB_PASSWORD = "postgres";



    //  _____            __ _ _         _____ _          __  __
    // |  __ \          / _(_) |       / ____| |        / _|/ _|
    // | |__) | __ ___ | |_ _| | ___  | (___ | |_ _   _| |_| |_
    // |  ___/ '__/ _ \|  _| | |/ _ \  \___ \| __| | | |  _|  _|
    // | |   | | | (_) | | | | |  __/  ____) | |_| |_| | | | |
    // |_|   |_|  \___/|_| |_|_|\___| |_____/ \__|\__,_|_| |_|


    // GET /profile/{id}
    // Queries the database for information about the specified profile and
    // returns it as JSON.
    // @author: cdh24
    @GET
    @Path("/profile/{id}")
    @Produces("application/json")
    public String getProfile(@PathParam("id") int id) {

        // Create a new profile object
        PlayerProfile player = new PlayerProfile();

        try {
            player.loadFromDataBase(id);
            return player.getJSON();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"err\":\"Unable to get profile.\"}";
    }

    // GET /profile/{id}/target
    // Queries the database for information about the target of the given person, and returns it via JSON
    // @author: cdh24
    @GET
    @Path("/profile/{id}/target")
    @Produces("application/json")
    public String getPlayersTarget(@PathParam("id") int id) {

        // Create a new profile object
        PlayerProfile player = new PlayerProfile();

        try {
            player.loadFromDataBase(id);
            player.getTargetInformationFromDatabase();
            return player.getTargetJSON();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"err\":\"Unable to get target information for player.\"}";
    }

    // POST /profile
    // Adds a profile to the database with the information supplied.
    // @author: cdh24
    @POST
    @Consumes("application/json")
    @Path("/profile")
    @Produces("application/json")
    public String createProfile(String data) throws Exception {

        try {
            // Create new object from json string
            Gson gson = new Gson();
            PlayerProfile player = gson.fromJson(data, PlayerProfile.class);

            // Insert into DB and get new ID number
            player.insertIntoDataBase();

            // Send the profile back to the browser with the ID for the new user
            return getProfile(player.ID);
        } catch (Exception e) {
            //e.printStackTrace();
            throw(e);
        }
        //return "{\"err\":\"Unable to create profile.\"}";
    }

    // PUT /profile/{id}
    // Updates a profile in the database with the information supplied.
    // @author: cdh24
    @PUT
    @Consumes("application/json")
    @Path("/profile/{id}")
    @Produces("application/json")
    public String updateProfile(@PathParam("id") int id, String data) {

        try {
            // Create new object from json string
            Gson gson = new Gson();
            PlayerProfile player = gson.fromJson(data, PlayerProfile.class);

            // Set the new player's ID to the one from the URL
            player.ID = id;

            // Update DB
            player.saveToDataBase();

            // Send the profile back to the browser the way it appears in the DB
            return getProfile(player.ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"err\":\"Unable to update profile.\"}";
    }

    // DELETE /profile/{id}
    // Deletes a profile from the database
    // @author: cdh24
    @DELETE
    @Consumes("application/json")
    @Path("/profile/{id}")
    @Produces("application/json")
    public String deleteProfile(@PathParam("id") int id) {

        try {
            // Create player object
            PlayerProfile player = new PlayerProfile();
            player.ID = id;

            // Delete from DB
            player.deleteFromDataBase();

            // Send a message back to the user
            return "{\"msg\":\"Deleted user from database.\"}";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"err\":\"Unable to delete profile.\"}";
    }




    //   _____                         _____ _          __  __
    //  / ____|                       / ____| |        / _|/ _|
    // | |  __  __ _ _ __ ___   ___  | (___ | |_ _   _| |_| |_
    // | | |_ |/ _` | '_ ` _ \ / _ \  \___ \| __| | | |  _|  _|
    // | |__| | (_| | | | | | |  __/  ____) | |_| |_| | | | |
    //  \_____|\__,_|_| |_| |_|\___| |_____/ \__|\__,_|_| |_|



    // GET /game/{id}
    // Queries the database for information about the specified game and
    // returns it as JSON.
    // @author: cdh24
    @GET
    @Path("/game/{id}")
    @Produces("application/json")
    public String getGame(@PathParam("id") int id) {

        // Create a new game object
        Game game = new Game();

        try {
            game.loadFromDataBase(id);
            game.getPlayers();
            return game.getJSON();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"err\":\"Unable to get game.\"}";
    }


    // GET /games
    // Returns information about all the games in the DB
    // @author: cdh24
    @GET
    @Path("/games")
    @Produces("application/json")
    public String getAllGames() {

        // Create a list to hold the IDs of all the games in the DB
        List<Integer> gameIDs = new ArrayList<Integer>();


        // Create connection to the database
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;


        // Try connecting and retrieving data
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(CalvinAssassin.DB_URI, CalvinAssassin.DB_LOGIN_ID, CalvinAssassin.DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT gameID FROM GAME;");


            // Add the game ids into the list
            while(rs.next()) {
                // Add the ID for each of the games into the list
                gameIDs.add(rs.getInt(1));
            }


            // Close all residual connection stuff
            rs.close();
            statement.close();
            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"err\":\"Unable to query database for IDs of games.\"}";
        }


        try {

            // Create an array of games to return
            List<Game> gameList = new ArrayList<Game>();

            for (Integer ID: gameIDs) {
                // Create a new game object
                Game game = new Game();
                game.loadFromDataBase(ID);
                game.getPlayers();
                gameList.add(game);
            }

            return new Gson().toJson(gameList);

//            return game.getJSON();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return "{\"err\":\"Unable to get the list of games from the database\"}";


    }


    // POST /game
    // Adds a new game object to the database and sends the data back to the browser
    // @author: cdh24
    @POST
    @Consumes("application/json")
    @Path("/game")
    @Produces("application/json")
    public String createGame(String data) {

        try {
            // Create a new game object
            Gson gson = new Gson();
            Game game = gson.fromJson(data, Game.class);

            // Insert into DB and get new ID number
            game.insertIntoDataBase();

            // Send the profile back to the browser with the ID for the new user
            return getGame(game.ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"err\":\"Unable to create game.\"}";
    }


    // PUT /game/{id}
    // Updates a game in the database with the information supplied.
    // @author: cdh24
    @PUT
    @Consumes("application/json")
    @Path("/game/{id}")
    @Produces("application/json")
    public String updateGame(@PathParam("id") int id, String data) {

        try {
            // Create new object from json string
            Gson gson = new Gson();
            Game game = gson.fromJson(data, Game.class);

            // Set the new game's ID to the one from the URL
            game.ID = id;

            // Update DB
            game.saveToDataBase();

            // Send the profile back to the browser the way it appears in the DB
            return getGame(game.ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"err\":\"Unable to update game.\"}";
    }


    // DELETE /game/{id}
    // Deletes the game from the DB
    // @author: cdh24
    @DELETE
    @Consumes("application/json")
    @Path("/game/{id}")
    @Produces("application/json")
    public String deleteGame(@PathParam("id") int id) {

        try {
            // Create game object
            Game game= new Game();
            game.ID = id;

            // Update DB
            game.deleteFromDataBase();

            // Send a message back to the user informing of the delete
            return "{\"msg\":\"Deleted game from database.\"}";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"err\":\"Unable to delete game.\"}";
    }







//    // GET /game/{id}/players
//    // Queries the database for the players associated with the game
//    // @author: cdh24
//    @GET
//    @Path("/game/{id}/players")
//    @Produces("application/json")
//    public String getPlayersInGame(@PathParam("id") int id) {
//
//        // Create a new profile object
//        Game game = new Game();
//
//        try {
//            game.loadFromDataBase(id);
//            game.getPlayers();
//            return game.getJSON();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "{\"err\":\"Unable to get game.\"}";
//    }




    /**
     * Run this main method to fire up the service.
     *
     * @param args command-line arguments (ignored)
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServerFactory.create("http://localhost:8082/");
        server.start();

        System.out.println("Server running...");
        System.out.println("Web clients should visit: http://localhost:8082/api");
        System.out.println("Android emulators should visit: http://LOCAL_IP_ADDRESS:8082/api");

//        Game test = new Game();
//        test.ID = 1;
//        try {
//            test.getPlayers();
//            for (PlayerProfile e : test.players) {
//                System.out.println(e.getJSON());
//            }
//        }
//        catch (Exception e) {
//            throw(e);
//        }

        System.out.println("Hit return to stop...");
        //noinspection ResultOfMethodCallIgnored
        System.in.read();
        System.out.println("Stopping server...");
        server.stop(0);
        System.out.println("Server stopped...");



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