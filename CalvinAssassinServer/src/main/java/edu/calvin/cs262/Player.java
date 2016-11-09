package edu.calvin.cs262;

/**
 * A Player class (POJO) for the player relation
 *
 * @author efb4
 * @version fall, 2016
 */
public class Player {

    private int playerId;
    private String name, userName, password, firstName, lastName, year, major;

    Player() { /* a default constructor, required by Gson */  }

    Player(int playerId, String name, String userName, String firstName, String lastName, String year, String major ) {
        this.playerId = playerId;
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.year = year;
        this.major = major;
    }

    public int getplayerId() {
        return playerId;
    }
    public String getUserName() {
        return userName;
    }
    public String getPassword() {
        return password;
    }
    public String getName() {
        return name;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getYear() {
        return year;
    }
    public String getMajor() {
        return major;
    }


    public void setPlayerId(int newId) {
        this.playerId = newId;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPassword(String password) {this.password = password; }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public void setMajor(String year) {
        this.major = major;
    }


}
