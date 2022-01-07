package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.firebase;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;

import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.ShareHappinessStateActivity;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.auth_activities.LoginActivity;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.auth_activities.RegisterActivity;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.auth_activities.UserProfileActivity;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.AppConst;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.AppFirebase;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.ImageUploader;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.models.HappinessModel;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.models.User;


public class Somewhere {
    
    public static String currentUser = ( null != FirebaseAuth.getInstance().getCurrentUser() ) ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "";
    
    public static void storeUser( RegisterActivity registerActivity , User u ) {
        FirebaseFirestore.getInstance().collection( AppFirebase.Collections.USERS ).document( u.getId() ).set( u , SetOptions.merge() ).addOnSuccessListener( t -> registerActivity.registrationSucess() ).addOnFailureListener( e -> registerActivity.registationFailure( e.getMessage() ) );
    }
    
    public static String getCurrentUserId() {
        return currentUser;
    }
    
    public static User getUser( Activity activity ) {
        FirebaseFirestore.getInstance().collection( AppFirebase.Collections.USERS ).document( getCurrentUserId() ).get().addOnSuccessListener( documentSnapshot -> {
        } ).addOnFailureListener( e -> {
        } );
        return null;
    }
    
    public static void getUser( LoginActivity activity ) {
        FirebaseFirestore.getInstance().collection( AppFirebase.Collections.USERS ).document( getCurrentUserId() ).get().addOnSuccessListener( documentSnapshot -> {
            User user = documentSnapshot.toObject( User.class );
            activity.userLoggedInSuccess( user );
            activity.getSharedPreferences( AppConst.HAPPYNESS_PREFERENCES , Context.MODE_PRIVATE ).edit().putString( AppConst.CURRENT_USERNAME , user.getFirstName() + " " + user.getLastName() ).apply();
        } ).addOnFailureListener( e -> activity.hideProgressDialog() );
    }
    
    public static void updateUserData( UserProfileActivity activity , Map< String, Object > userMap ) {
        FirebaseFirestore.getInstance().collection( AppFirebase.Collections.USERS ).document( getCurrentUserId() ).update( userMap ).addOnSuccessListener( t -> activity.userProfileUpdateSuccess() ).addOnFailureListener( e -> {
            activity.hideProgressDialog();
            Log.e( activity.getLocalClassName() , "Error while updating the user details" );
        } );
    }
    
    public static void uploadImageToFirebase( ImageUploader activity , Uri imageFileUri ) {
        FirebaseStorage.getInstance().getReference().child( AppConst.INSTANT_IMAGE + "_" + System.currentTimeMillis()  )
                .putFile( imageFileUri ).addOnSuccessListener( taskSnapshot -> {
            Task< Uri > downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
            Log.e( "Firebase Image URL" , downloadUrl.toString() );
            downloadUrl.addOnSuccessListener( uri -> {
                Log.e( "Downloadable Image URL" , uri.toString() );
                activity.imageUploadSuccess( uri.toString() );
            } );
        } ).addOnFailureListener( e -> {
            activity.hideProgressDialog();
            Log.e( activity.getLocalClassName() , e.getMessage() , e );
        } );
    }
    
    public static void storeHappinessInstant( HappinessModel happy, ShareHappinessStateActivity act ) {
        String[] locationSplit = happy.getLocation().split( "," );
        String pays = locationSplit[locationSplit.length - 1];
        String ville = locationSplit[locationSplit.length - 2 ].split( " " )[2];
        FirebaseFirestore
                .getInstance()
                .collection( pays.trim() )
                .document(ville.trim())
                .collection( "Instants" )
                .document()
                .set( happy , SetOptions.merge() )
                .addOnSuccessListener( t -> act.registrationSucess() )
                .addOnFailureListener( e -> act.registationFailure( e.getMessage() ) );
    }
}