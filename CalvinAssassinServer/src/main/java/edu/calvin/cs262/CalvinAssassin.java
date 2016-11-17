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
 * @author efb4
 * @version fall 2016

 */
@Path("/calvinassassin")
public class CalvinAssassin {

    /**
     * a hello-world resource
     *
     * @return a simple string value
     */
    @SuppressWarnings("SameReturnValue")
    @GET
    @Path("/welcome")
    @Produces("text/plain")
    public String getClichedMessage() {
        return "Welcome to Calvin Assassin!";
    }

    /**
     * GET method that returns a list of all monopoly players
     *
     * @return a JSON list representation of the player records
     */
    @GET
    @Path("/players")
    @Produces("application/json")
    public String getPlayers() {
        try {
            // As an example of GSON, we'll hard-code a couple players and return their JSON representation.
            //retrievePlayers();
            return new Gson().toJson(retrievePlayers());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * GET method that returns the playerID of the target given an assassin playerID
     *
     * @return a JSON list representation of the player records
     */
    @GET
    @Path("/targetid")
    @Produces("application/json")
    public String targetID(@Context UriInfo info) {
        try {
            // As an example of GSON, we'll hard-code a couple players and return their JSON representation.
            return new Gson().toJson(getTargetID( info.getQueryParameters().getFirst("assassinID") ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //this should be a post method, but when I change it to @POST, it stops working
    @GET
    @Path("/createuserprofile")
    @Produces("application/json")
    public String createProfile(@Context UriInfo info)
    {
        String username = info.getQueryParameters().getFirst("username");
        String name = info.getQueryParameters().getFirst("name");
        String residence = info.getQueryParameters().getFirst("residence");
        String year = info.getQueryParameters().getFirst("year");
        try {
            return new Gson().toJson( createProfile(username,name,residence,year) );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * GET method that returns the GPS coordinates of a player
     *
     * @return a JSON list representation of the player records
     */
    @GET
    @Path("/location")
    @Produces("application/json")
    public String getLocation(@Context UriInfo info) {
        String playerid = info.getQueryParameters().getFirst("playerid");
        try {
            return new Gson().toJson( retrievePlayerLocation(playerid) );
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


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
        System.out.println("Web clients should visit: http://localhost:9998/calvinassassin");
        System.out.println("Android emulators should visit: http://LOCAL_IP_ADDRESS:9998/calvinassassin");
        System.out.println("Hit return to stop...");
        //noinspection ResultOfMethodCallIgnored
        System.in.read();
        System.out.println("Stopping server...");
        server.stop(0);
        System.out.println("Server stopped...");
    }


    private static final String DB_URI = "jdbc:postgresql://localhost:5432/calvinassassin";
    private static final String DB_LOGIN_ID = "postgres";
    private static final String DB_PASSWORD = "postgres";

    private int[] getTargetID( String assasssinID ) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List players = new ArrayList<>();
        int targetID;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT targetID FROM PlayerMatchUp WHERE assassinID = " + assasssinID);
            rs.next();
            targetID = rs.getInt(1);
        }
        catch (SQLException e) {
            throw (e);
        }
        finally {
            rs.close();
            statement.close();
            connection.close();
        }
        int[] idArray = {targetID};
        return idArray;

    }

    private List retrievePlayers() throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        List players = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            rs = statement.executeQuery("SELECT * FROM Player");
            while (rs.next()) {
                players.add(new Player(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5)
                , rs.getString(6), rs.getString(7)));
            }
        } catch (SQLException e) {
            throw (e);
        } finally {
            rs.close();
            statement.close();
            connection.close();
        }
        return players;
    }

    private int[] createProfile ( String userName, String name, String residence, String year ) throws Exception
    {

        ResultSet rs = null;
        Connection connection = null;
        Statement statement = null;
        String insertTableSQL = "INSERT INTO Player"
            + "(userName, password, firstName, lastName, year, major) " + "VALUES"
            + "(" +  "'" + userName + "'" + " , '123'," + "'"  + name + "'," + "'" + name + "'," + "'" + year + "', 'not given' )";

        int id=0;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            statement.executeUpdate(insertTableSQL);

        }
        catch (SQLException e) {
            throw (e);
        }
        finally {
            statement.close();
            connection.close();
        }

        Connection connection2 = null;
        Statement statementQuery = null;
        try {
            connection2 = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statementQuery = connection2.createStatement();
           rs = statementQuery.executeQuery("SELECT playerID FROM Player ORDER BY playerID DESC LIMIT 1");

            rs.next();
            id = rs.getInt(1);
        }
        catch (SQLException e) {
            throw (e);
        }
        finally {
            rs.close();
            statementQuery.close();
            connection2.close();
        }
        int[] idArray = {id};
        return idArray;
    }



    private double[] retrievePlayerLocation( String id ) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        double[] coordinates = new double[2];

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_URI, DB_LOGIN_ID, DB_PASSWORD);
            statement = connection.createStatement();
            int playerID = Integer.parseInt( id );
            rs = statement.executeQuery("SELECT * FROM PlayerLocation WHERE playerID =" + playerID);
            while (rs.next()) {
                coordinates[0] = rs.getDouble(4);
                coordinates[1] = rs.getDouble(5);
            }
        } catch (SQLException e) {
            throw (e);
        } finally {
            rs.close();
            statement.close();
            connection.close();
        }
        return coordinates;
    }

}
