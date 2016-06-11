package com.techtask.xps.pinsapp.Models;

import com.facebook.AccessToken;

/**
 * Created by XPS on 6/11/2016.
 */
public class User {

    private static User instance;

    private String firstName;
    private String lastName;

    private User(){  }
    public static User getInstance(){
        if(instance == null)
            instance = new User();
        return instance;
    }

    public String getFirstName() {
           return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
