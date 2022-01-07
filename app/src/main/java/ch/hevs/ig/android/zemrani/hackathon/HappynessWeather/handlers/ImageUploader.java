package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers;

public interface ImageUploader {
    void imageUploadSuccess( String toString );
    
    String getLocalClassName();
    
    void hideProgressDialog();
}
