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
 * This module implements a RESTful service for the player table of the monopoly database.
 * Only the player relation is supported, not the game or playergame objects.
 * The server requires Java 1.7 (not 1.8).
 *
 * I tested these services using IDEA's REST Client test tool. Run the server and open
 * Tools-TestRESTService and set the appropriate HTTP method, host/port, path and request body and then press
 * the green arrow (submit request).
 *
 * @author The B Team
 * @version fall 2016

 */
@Path("/api")
public class CalvinAssassin {


    // Database connection constants
    public static final String DB_URI = "jdbc:postgresql://localhost:5432/calvinassassin";
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
    @Path("/profile/query")
    @Produces("application/json")
    public String getProfile(@Context UriInfo info) {

        // Create a new profile object
        PlayerProfile player = new PlayerProfile();

        try {
            player.loadFromDataBase(Integer.parseInt(info.getQueryParameters().getFirst("id")));
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



    /**
     * Run this main method to fire up the service.
     *
     * @param args command-line arguments (ignored)
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServerFactory.create("http://localhost:9998/");
        server.start();

        System.out.println("Server running...");
        System.out.println("Web clients should visit: http://localhost:9998/api");
        System.out.println("Android emulators should visit: http://LOCAL_IP_ADDRESS:9998/api");
        System.out.println("Hit return to stop...");
        //noinspection ResultOfMethodCallIgnored
        System.in.read();
        System.out.println("Stopping server...");
        server.stop(0);
        System.out.println("Server stopped...");
    }

}
