package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.auth_activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.Map;

import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.BaseActivity;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.MainActivity;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.R;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.databinding.ActivityUserProfileBinding;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.firebase.Somewhere;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.AppConst;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.AppFirebase;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.AppPermissions;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.Gender;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.ImageUploader;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.PictureHandler;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.models.User;

public class UserProfileActivity extends BaseActivity implements ImageUploader {
    
    private ActivityUserProfileBinding binding;
    private User user;
    private Uri selectedImageFileUri = null;
    private String selectedImageFileUrl = "";
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        binding = ActivityUserProfileBinding.inflate( getLayoutInflater() );
        root = binding.getRoot();
        setContentView( root );
        user = ( getIntent().hasExtra( AppConst.CURRENT_USER_COMPLETE_PROFILE ) ) ? ( User ) getIntent().getSerializableExtra( AppConst.CURRENT_USER_COMPLETE_PROFILE ) : new User();
        
        initUserForm();
        setListeners();
    }
    
    private void setListeners() {
        binding.ivUserPhoto.setOnClickListener( v -> {
            if ( ContextCompat.checkSelfPermission( this , Manifest.permission.READ_EXTERNAL_STORAGE ) == PackageManager.PERMISSION_GRANTED ) {
                //showErrorSnackBar( "You have already the storage permission" , false );
                AppPermissions.showImageChooser( this );
            } else {
                ActivityCompat.requestPermissions( this , new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , AppPermissions.READ_STORAGE_PERMISSION_CODE );
            }
        } );
        
        binding.btnSubmit.setOnClickListener( v -> {
            
            if ( validateUserProfileDetails() ) {
                showProgressDialog( getString( R.string.please_wait ) );
                if ( null != selectedImageFileUri ) {
                    Somewhere.uploadImageToFirebase( this , selectedImageFileUri );
                    
                } else {
                    updateUserProfile();
                }
            }
        } );
    }
    
    private void updateUserProfile() {
        Map< String, Object > userMap = new HashMap<>();
        long userMobileNumber = Long.parseLong( binding.etMobileNumber.getText().toString().trim() );
        String userGender = ( binding.rbMale.isChecked() ) ? Gender.MALE.label : ( binding.rbFemale.isChecked() ) ? Gender.FEMALE.label : Gender.NON_BINARY.label;
        userMap.put( AppFirebase.User.GENDER , userGender );
        userMap.put( AppFirebase.User.MOBILE , userMobileNumber );
        if ( !selectedImageFileUrl.isEmpty() ) {
            userMap.put( AppFirebase.User.PROFILE_IMAGE , selectedImageFileUrl );
        }
        userMap.put( AppFirebase.User.PROFILE_COMPLETED , 1 );
        Somewhere.updateUserData( this , userMap );
    }
    
    private void initUserForm() {
        binding.etFirstName.setEnabled( false );
        binding.etFirstName.setText( user.getFirstName() );
        
        binding.etLastName.setEnabled( false );
        binding.etLastName.setText( user.getLastName() );
        
        binding.etEmail.setEnabled( false );
        binding.etEmail.setText( user.getEmail() );
    }
    
    @Override
    public void onRequestPermissionsResult( int requestCode , @NonNull String[] permissions , @NonNull int[] grantResults ) {
        super.onRequestPermissionsResult( requestCode , permissions , grantResults );
        if ( requestCode == AppPermissions.READ_STORAGE_PERMISSION_CODE ) {
            if ( grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                AppPermissions.showImageChooser( this );
            } else {
                Toast.makeText( this , getString( R.string.read_stotage_permission_denied ) , Toast.LENGTH_LONG ).show();
            }
        }
    }
    
    @Override
    protected void onActivityResult( int requestCode , int resultCode , @Nullable Intent data ) {
        super.onActivityResult( requestCode , resultCode , data );
        if ( resultCode == Activity.RESULT_OK ) {
            if ( requestCode == AppPermissions.PICK_IMAGE_REQUEST_CODE ) {
                if ( null != data ) {
                    try {
                        selectedImageFileUri = data.getData();
                        PictureHandler.loadUserPicture( getBaseContext() , selectedImageFileUri , binding.ivUserPhoto );
                    } catch ( Exception e ) {
                        e.printStackTrace();
                        Toast.makeText( this , getString( R.string.image_selection_failed ) , Toast.LENGTH_SHORT ).show();
                    }
                }
            }
        } else if ( resultCode == Activity.RESULT_CANCELED ) {
            Log.e( "Request Cancelled" , "Image selection cancelled " );
        }
    }
    
    private boolean validateUserProfileDetails() {
        if ( binding.etMobileNumber.getText() != null && binding.etMobileNumber.getText().toString().trim().isEmpty() ) {
            showErrorSnackBar( getString( R.string.err_msg_enter_mobile_number ) , true );
            return false;
        }
        return true;
    }
    
    public void userProfileUpdateSuccess() {
        hideProgressDialog();
        Toast.makeText( this , getString( R.string.msg_profile_updtae_sucess ) , Toast.LENGTH_SHORT ).show();
        startActivity( new Intent( getBaseContext() , MainActivity.class ) );
        finish();
    }
    
    public void imageUploadSuccess( String url ) {
        selectedImageFileUrl = url;
        updateUserProfile();
    }
}