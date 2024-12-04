package com.movierecommender.project;

public class Sigin {
    private String username;
    private String password;

    // Constructors
    public Sigin() {
    }

    /**
     * Constructs a new Sigin object with the specified username and password.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    public Sigin(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getters and Setters

    /**
     * Retrieves the username associated with this Sigin object.
     *
     * @return the username as a String
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the Sigin object.
     *
     * @param username the new username to be set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Retrieves the password associated with this Sigin object.
     *
     * @return the password as a String
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the Sigin object.
     *
     * @param password the new password to be set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
