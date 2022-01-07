package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager2.widget.ViewPager2;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.databinding.ActivityShareHappinessStateBinding;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.firebase.Somewhere;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.AppConst;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.AppPermissions;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.GetAddressFromLatLng;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.ImageUploader;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.PictureHandler;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.models.HappinessModel;

public class ShareHappinessStateActivity extends BaseActivity implements ImageUploader {
    
    private ActivityShareHappinessStateBinding binding;
    TextView[] dots = new TextView[5];
    private final int[] colors = new int[]{R.color.level_1 , R.color.level_2 , R.color.level_3 , R.color.level_4 , R.color.level_5};
    private Uri selectedImageFileUri = null;
    private String selectedImageFileUrl = "";
    
    double latitude = 0.0;
    double longitude = 0.0;
    ImageAdapter adapter;
    
    private HappinessModel happy = null;
    
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
                    binding.etLocation.setText( address );
                }
    
                @Override
                public void onError() {
                    Log.e("Get Address ::", "Something is wrong...");
                }
            } );
            
            addressTask.getAddress();
        }
        
    };
    private int position = 1;
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        binding = ActivityShareHappinessStateBinding.inflate( getLayoutInflater() );
        root = binding.getRoot();
        setContentView( binding.getRoot() );
        setToolBar();
        setImageAdapter();
        setHappyImageListener();
        setLocationRegime();
        
        binding.btnSave.setOnClickListener( v -> {
            if ( validateRegisterDetails() ) {
                showProgressDialog( getString( R.string.please_wait ) );
                if ( null != selectedImageFileUri ) {
                    Somewhere.uploadImageToFirebase( this , selectedImageFileUri );
                    System.out.println(selectedImageFileUri);
                } else {
                    storeInstant();
                }
            }
        } );
    
     
    }
    
    private void storeInstant() {
        happy = new HappinessModel();
        happy.setTitle(  binding.etTitle.getText().toString().trim() );
        happy.setDescription( binding.etDescription.getText().toString().trim() );
        happy.setLocation(  binding.etLocation.getText().toString().trim() );
        happy.setLatitude( latitude );
        happy.setLongitude( longitude );
        happy.setImage( selectedImageFileUrl );
        happy.setLevel( position );
        happy.setOwnerId( Somewhere.getCurrentUserId() );
        Somewhere.storeHappinessInstant(happy, this);
    }
    
    private Boolean validateRegisterDetails() {
        
        if ( TextUtils.isEmpty( binding.etLocation.getText().toString().trim() ) ) {
            showErrorSnackBar( "Please authorize location"  , true );
            return false;
        }
        
        if ( TextUtils.isEmpty( binding.etTitle.getText().toString().trim() ) ) {
            showErrorSnackBar( "Please provide title" , true );
            return false;
        }
        
        if ( TextUtils.isEmpty( binding.etDescription.getText().toString().trim() ) ) {
            showErrorSnackBar( "please provide description"  , true );
            return false;
        }
        
        if ( selectedImageFileUri == null && selectedImageFileUrl == null ) {
            showErrorSnackBar( "Please add image." , true );
            return false;
        }
        
        return true;
    }
    
    private void setLocationRegime() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient( this );
    
        if ( !Places.isInitialized() ) {
            Places.initialize( this , getString( R.string.google_maps_api_key ) );
        }
    
//        binding.etLocation.setOnClickListener( v -> {
//            try {
//                List< Place.Field > fields = Arrays.asList( Place.Field.ID , Place.Field.NAME , Place.Field.LAT_LNG , Place.Field.ADDRESS );
//                Intent openMap = new Autocomplete.IntentBuilder( AutocompleteActivityMode.FULLSCREEN , fields ).build( this );
//                this.startActivityForResult( openMap , AppPermissions.PLACE_AUTOCOMPLETE_REQUEST_CODE );
//            } catch ( Exception e ) {
//                e.printStackTrace();
//            }
//        } );
    
    
        //binding.tvSelectCurrentLocation.setOnClickListener( v -> {
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
       // } );
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
    
    
    private void takePhotoFromCamera() {
        Activity act = this;
        Dexter.withActivity( this ).withPermissions( Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE , Manifest.permission.CAMERA ).withListener( new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked( MultiplePermissionsReport report ) {
                if ( report.areAllPermissionsGranted() ) {
                    AppPermissions.showCamera( act );
            
                }
            }
    
            @Override
            public void onPermissionRationaleShouldBeShown( List< PermissionRequest > permissions , PermissionToken token ) {
                showRationalDialogForPermissions();
            }
        } ).onSameThread().check();
    
    }
    
    private void choosePhotoFromGallery() {
        Activity act = this;
        Dexter.withActivity( this )
                .withPermissions( Manifest.permission.READ_EXTERNAL_STORAGE , Manifest.permission.WRITE_EXTERNAL_STORAGE )
                .withListener( new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked( MultiplePermissionsReport report ) {
                if ( report.areAllPermissionsGranted() ) {
                    AppPermissions.showImageChooser( act );
                }
            }
            @Override
            public void onPermissionRationaleShouldBeShown( List< PermissionRequest > permissions , PermissionToken token ) {
                showRationalDialogForPermissions();
            }
        } ).onSameThread().check();
        
    }
    
    @Override
    protected void onActivityResult( int requestCode , int resultCode , @Nullable Intent data ) {
        super.onActivityResult( requestCode , resultCode , data );
        if ( resultCode == Activity.RESULT_OK && data != null ) {
            switch ( requestCode ) {
                case AppPermissions.PICK_IMAGE_REQUEST_CODE:
                    try {
                        selectedImageFileUri = data.getData();
                        PictureHandler.loadPicture( getBaseContext() , selectedImageFileUri , binding.ivPlaceImage );
                    } catch ( Exception e ) {
                        e.printStackTrace();
                        Toast.makeText( this , getString( R.string.image_selection_failed ) , Toast.LENGTH_SHORT ).show();
                    }

                    break;
    
                case AppPermissions.CAMERA_PERMISSION_CODE:
                    Bitmap thumbnail = ( Bitmap ) data.getExtras().get( "data" );
                    selectedImageFileUri = saveImageToInternalStorage( thumbnail );
                    PictureHandler.loadPicture( getBaseContext() , selectedImageFileUri , binding.ivPlaceImage );
                    //PictureHandler.loadPicture( getBaseContext() , selectedImageFileUri , binding.ivPlaceImage );
                    break;
                    
                case AppPermissions.PLACE_AUTOCOMPLETE_REQUEST_CODE:
                    
                    Place place = Autocomplete.getPlaceFromIntent( data );
                    
                    binding.etLocation.setText( place.getAddress() );
                    
                    latitude = Objects.requireNonNull( place.getLatLng() ).latitude;
                    longitude = place.getLatLng().longitude;
                    break;
            }
        } else if ( resultCode == Activity.RESULT_CANCELED ) {
            Log.e( "Request Cancelled" , "data is null " );
        }
    }
    
    private Uri saveImageToInternalStorage( Bitmap thumbnail ) {
        
        Context wrapper = new ContextWrapper(getApplicationContext());
        
        File file = wrapper.getDir( AppConst.IMAGE_DIRECTORY,   Context.MODE_PRIVATE);
        
        file = new File(file, UUID.randomUUID()+".jpg" );
        
        try {
            OutputStream stream = new FileOutputStream( file );
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
            
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return Uri.parse( file.getAbsolutePath());
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
    
    @RequiresApi( api = Build.VERSION_CODES.M )
    private void selectedIndicator( int position ) {
        for ( int i = 0; i < dots.length; i++ ) {
            if ( i == position ) {
                this.position = i;
                dots[i].setTextColor( getColor( colors[i] ) );
            } else {
                dots[i].setTextColor( getColor( R.color.black ) );
            }
        }
    }
    
    private void dotsIndicator() {
        for ( int i = 0; i < dots.length; i++ ) {
            dots[i] = new TextView( this );
            dots[i].setText( Html.fromHtml( "&#9679;" ) );
            dots[i].setTextSize( 18 );
            binding.dotsContainer.addView( dots[i] );
        }
    }
    
    

    
    private void setHappyImageListener() {
        binding.tvAddImage.setOnClickListener( v -> new AlertDialog.Builder( this ).setTitle( "Select Action" ).setItems( new CharSequence[]{"Select photo from gallery" , "Capture photo from camera"} , ( dialog , which ) -> {
            switch ( which ) {
                case 0:
                    choosePhotoFromGallery();
                    break;
                case 1:
                    takePhotoFromCamera();
                    break;
            }
        } ).show() );
    }
    
    private void setImageAdapter() {
        adapter = new ImageAdapter();
        binding.viewPager.setAdapter( adapter );
        dotsIndicator();
        binding.viewPager.registerOnPageChangeCallback( new ViewPager2.OnPageChangeCallback() {
            @RequiresApi( api = Build.VERSION_CODES.M )
            @Override
            public void onPageSelected( int position ) {
                selectedIndicator( position );
                super.onPageSelected( position );
            }
        } );
    }
    
    private void setToolBar() {
        setSupportActionBar( binding.toolbarAddPlace );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        binding.toolbarAddPlace.setNavigationOnClickListener( v -> onBackPressed() );
    }
    

    public void imageUploadSuccess( String url ) {
        selectedImageFileUrl = url;
        storeInstant();
    }
    
    public void registrationSucess() {
        hideProgressDialog();
        onBackPressed();
    }
    
    public void registationFailure( String message ){
        hideProgressDialog();
    
    }
    
}