package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.models;

import java.io.Serializable;

public class HappinessModel implements Serializable {

    int id;
    int level;
    String title;
    String ownerId;
    String image;
    String Description;
    String date;
    String location;
    Double latitude;
    Double longitude;
    int numberStars;
    float rating;
    
    
    public int getNumberStars() {
        return numberStars;
    }
    
    public void setNumberStars( int numberStars ) {
        this.numberStars = numberStars;
    }
    
    public float getRating() {
        return rating;
    }
    
    public void setRating( float rating ) {
        this.rating = rating;
    }
    

    
    
    public HappinessModel() {
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle( String title ) {
        this.title = title;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId( int id ) {
        this.id = id;
    }
    
    public int getLevel() {
        return level;
    }
    
    public void setLevel( int level ) {
        this.level = level;
    }
    
    public String getOwnerId() {
        return ownerId;
    }
    
    public void setOwnerId( String ownerId ) {
        this.ownerId = ownerId;
    }
    
    public String getImage() {
        return image;
    }
    
    public void setImage( String image ) {
        this.image = image;
    }
    
    public String getDescription() {
        return Description;
    }
    
    public void setDescription( String description ) {
        Description = description;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate( String date ) {
        this.date = date;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation( String location ) {
        this.location = location;
    }
    
    public Double getLatitude() {
        return latitude;
    }
    
    public void setLatitude( Double latitude ) {
        this.latitude = latitude;
    }
    
    public Double getLongitude() {
        return longitude;
    }
    
    public void setLongitude( Double longitude ) {
        this.longitude = longitude;
    }
}
