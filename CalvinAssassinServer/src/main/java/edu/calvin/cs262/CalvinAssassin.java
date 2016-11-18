package edu.calvin.cs262;

import com.google.gson.Gson;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
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


    // GET /profile/query?id=1
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
        return "{err:\"Unable to get profile.\"}";
    }

//    // POST /profile/create
//    // Retrieves data from the POST request and creates a new user in the database
//    // @author: cdh24
//    // @date: 11-16-16
//    @POST
//    @Path("/profile/create")
//    @Produces("application/json")
//    public String createProfile(@Context UriInfo info) {
//        try {
//            return new Gson().toJson(info.getQueryParameters().getFirst("id"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }









    // GET /game/{id}
    // Queries the database for information about the specified game and
    // returns it as JSON.
    // @author: cdh24
    // @date: 11-18-16
    @GET
    @Path("/game/{id}")
    @Produces("application/json")
    public String getGame(@PathParam("id") int id) {

        // Create a new game object
        Game game= new Game();

        try {
            game.loadFromDataBase(id);
            return game.getJSON();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{err:\"Unable to get game.\"}";
    }




    /**
     * Run this main method to fire up the service.
     *
     * @param args command-line arguments (ignored)
     * @throws IOException
     */
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServerFactory.create("http://localhost:9998/");
        server.start();

        System.out.println("Server running...");
        System.out.println("Web clients should visit: http://localhost:9998/api");
        System.out.println("Android emulators should visit: http://LOCAL_IP_ADDRESS:9998/api");

        Game test = new Game();
        test.ID = 1;
        try {
            test.getPlayers();
            for (PlayerProfile e : test.players) {
                System.out.println(e.getJSON());
            }
        }
        catch (Exception e) {
            throw(e);
        }

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