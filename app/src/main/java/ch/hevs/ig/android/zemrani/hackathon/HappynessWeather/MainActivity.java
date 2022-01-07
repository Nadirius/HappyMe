package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;
import java.util.Objects;

import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.databinding.ActivityMainBinding;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.AppPermissions;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.GetAddressFromLatLng;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.InstantRecyclerView;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.models.HappinessModel;

public class MainActivity extends BaseActivity {
    
    private ActivityMainBinding binding;
    
    private InstantRecyclerView hapinessAdapter;
    private List< HappinessModel > instants;
    
    double latitude = 0.0;
    double longitude = 0.0;
    
    private FusedLocationProviderClient fusedLocationProviderClient;
    Activity act = this;
    private final LocationCallback locationCallback =
            new LocationCallback() {
                
                @Override
                public void onLocationResult( @NonNull LocationResult locationResult ) {
                    Location lastLocation = locationResult.getLastLocation();
                    latitude = lastLocation.getLatitude();
                    longitude = lastLocation.getLongitude();
                    GetAddressFromLatLng addressTask = new GetAddressFromLatLng( act , latitude , longitude );
                    addressTask.setAddressListener( new GetAddressFromLatLng.AddressListener() {
                        @Override
                        public void onAddressFound( String address ) {
                            getLocationInstants( address );
                        }
                        
                        @Override
                        public void onError() {
                            Log.e("Get Address ::", "Something is wrong...");
                        }
                    } );
                    
                    addressTask.getAddress();
                }
                
            };
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        binding = ActivityMainBinding.inflate( getLayoutInflater() );
        setContentView( binding.getRoot() );
        
        binding.fabAddMyHappy.setOnClickListener( v -> this.startActivityForResult( new Intent(this, ShareHappinessStateActivity.class) , AppPermissions.ADD_PLACE_ACTIVITY_REQUEST_CODE ));
        setToolBar();
        getHappyInstant();
        
    }
    
    private void getHappyInstant() {
      setLocationRegime();
//        String collection =
    }
    
    private String[] getLocationInstants( String address ) {
       String[] temp = address.split( ",");
        binding.toolbarAddPlace.setTitle( temp[temp.length-1].trim()+ ", " + temp[temp.length - 2].trim().split( " " )[1] );
        return null;
    }
    
    private void setToolBar() {
        setSupportActionBar( binding.toolbarAddPlace );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        binding.toolbarAddPlace.setNavigationOnClickListener( v -> onBackPressed() );
    }
    
    
    
    
    //############################# Location
    private void setLocationRegime() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient( this );
    
        if ( !Places.isInitialized() ) {
            Places.initialize( this , getString( R.string.google_maps_api_key ) );
        }
        
        if ( !isLocationEnabled() ) {
            Toast.makeText( this , "Your location provider is turned off. Please turn it on." , Toast.LENGTH_SHORT ).show();
            startActivity( new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS ) );
        } else {
            Dexter.withActivity( this ).withPermissions( Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION )
                    .withListener( new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked( MultiplePermissionsReport report ) {
                            if ( report.areAllPermissionsGranted() ) {
                                requestNewLocationData();
                            }
                        }
                    
                        @Override
                        public void onPermissionRationaleShouldBeShown( List< PermissionRequest > permissions , PermissionToken token ) {
                            showRationalDialogForPermissions();
                        }
                    } ).onSameThread().check();
        }
    }
    
    @SuppressLint( "MissingPermission" )
    private void requestNewLocationData() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority( LocationRequest.PRIORITY_HIGH_ACCURACY );
        locationRequest.setInterval( 1000 );
        locationRequest.setFastestInterval( 100 );
        locationRequest.setNumUpdates( 1 );
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient( this );
        fusedLocationProviderClient.requestLocationUpdates( locationRequest , locationCallback , Looper.myLooper() );
        
    }
    
    private boolean isLocationEnabled() {
        LocationManager locMan = (LocationManager ) getSystemService( Context.LOCATION_SERVICE );
        return locMan.isProviderEnabled( LocationManager.GPS_PROVIDER )
                || locMan.isProviderEnabled( LocationManager.NETWORK_PROVIDER );
    }
    
    @Override
    protected void onActivityResult( int requestCode , int resultCode , @Nullable Intent data ) {
        super.onActivityResult( requestCode , resultCode , data );
        if ( resultCode == Activity.RESULT_OK && data != null ) {
            switch ( requestCode ) {
                case AppPermissions.CAMERA_PERMISSION_CODE:
                    getHappyInstant();
                    break;
                
                case AppPermissions.PLACE_AUTOCOMPLETE_REQUEST_CODE:
                    
                    Place place = Autocomplete.getPlaceFromIntent( data );
    
                    getLocationInstants( place.getAddress() );
                    
                    latitude = Objects.requireNonNull( place.getLatLng() ).latitude;
                    longitude = place.getLatLng().longitude;
                    break;
            }
        } else if ( resultCode == Activity.RESULT_CANCELED ) {
            Log.e( "Request Cancelled" , "data is null " );
        }
    }
    
    private void showRationalDialogForPermissions() {
        new AlertDialog.Builder( this ).setMessage( "It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings" ).setPositiveButton( "GO TO SETTINGS" , ( dialog , b ) -> {
            try {
                Intent intent = new Intent( Settings.ACTION_APPLICATION_DETAILS_SETTINGS );
                Uri uri = Uri.fromParts( "package" , getPackageName() , null );
                intent.setData( uri );
                startActivity( intent );
            } catch ( ActivityNotFoundException e ) {
                e.printStackTrace();
            }
        } ).setNegativeButton( "Cancel" , ( dialog , x ) -> dialog.dismiss() ).show();
    }
}