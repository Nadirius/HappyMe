package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers;


public enum Gender {
    
    MALE("male"),
    FEMALE("female"),
    NON_BINARY("non_binary");
    public final String label;
    
    Gender( String label ) {
        this.label = label;
    }
}
