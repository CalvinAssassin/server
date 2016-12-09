package edu.calvin.cs262;

import com.google.gson.Gson;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * This module implements the server for the Calvin-Assassin game.
 * The server requires Java 1.7 (not 1.8).
 *
 *
 * @author The B Team
 * @version v0.0

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
    // @date: 11-16-16
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

    // POST /profile
    // Adds a profile to the database with the information supplied.
    // @author: cdh24
    // @date: 11-20-16
    @POST
    @Consumes("application/json")
    @Path("/profile")
    @Produces("application/json")
    public String createProfile(String data) {

        try {
            // Create new object from json string
            Gson gson = new Gson();
            PlayerProfile player = gson.fromJson(data, PlayerProfile.class);

            // Insert into DB and get new ID number
            player.insertIntoDataBase();

            // Send the profile back to the browser with the ID for the new user
            return getProfile(player.ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{\"err\":\"Unable to create profile.\"}";
    }

    // PUT /profile/{id}
    // Updates a profile in the database with the information supplied.
    // @author: cdh24
    // @date: 11-20-16
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
    // Updates a profile in the database with the information supplied.
    // @author: cdh24
    // @date: 11-20-16
    @DELETE
    @Consumes("application/json")
    @Path("/profile/{id}")
    @Produces("application/json")
    public String deleteProfile(@PathParam("id") int id) {

        try {
            // Create player object
            PlayerProfile player = new PlayerProfile();
            player.ID = id;

            // Update DB
            player.deleteFromDataBase();

            // Send the profile back to the browser the way it appears in the DB
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
    // @date: 11-20-16
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

//    // GET /game/{id}/players
//    // Queries the database for the players associated with the game
//    // @author: cdh24
//    // @date: 11-20-16
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