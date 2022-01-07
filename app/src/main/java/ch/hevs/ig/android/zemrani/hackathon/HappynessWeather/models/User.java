package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.models;


import java.io.Serializable;

public class User implements Serializable {
    private String id = "";
    private String firstName = "";
    private String lastName = "";
    private String email = "";
    private String img = "";
    private long mobile = 0;
    private String gender = "";
    private int profileCompleted = 0;
    
    public User(  ) {
    }
    
    
    
    public User setId( String id ) {
        this.id = id;
        return this;
    }
    
    public String getId() {
        return id;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getImg() {
        return img;
    }
    
    public long getMobile() {
        return mobile;
    }
    
    public String getGender() {
        return gender;
    }
    
    public int getProfileCompleted() {
        return profileCompleted;
    }
    
    
    
    public User setFirstName( String firstName ) {
        this.firstName = firstName;
        return this;
    }
    
    public User setLastName( String lastName ) {
        this.lastName = lastName;
        return this;
    }
    
    public User setEmail( String email ) {
        this.email = email;
        return this;
    }
    
    public User setImg( String img ) {
        this.img = img;
        return this;
    }
    
    public User setMobile( long mobile ) {
        this.mobile = mobile;
        return this;
    }
    
    public User setGender( String gender ) {
        this.gender = gender;
        return this;
    }
    
    public User setProfileCompleted( int profileCompleted ) {
        this.profileCompleted = profileCompleted;
        return this;
    }
    
}
