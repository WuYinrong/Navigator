package com.org.navigator;

public class User {
    private double user_timestamp;
    private String user_account;
    /**
     * Static function, an instance
     * @return new instance
     */
    private String user_password;


    public double getUser_timestamp() {
        return user_timestamp;
    }

    /**
     * Static function, an instance
     * @return new instance
     */
    public void setUser_timestamp(double user_timestamp) {
        this.user_timestamp = user_timestamp;
    }

    // the total number
    public String getUser_account() {
        return user_account;
    }
    /**
     * Static function, an instance
     * @return new instance
     */

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public String getUser_password() {
        return user_password;
    }

    /**
     * Static function, an instance
     * @return new instance
     */
    public void setUser_password(String user_password) {
        this.user_password = user_password;
    }

}
