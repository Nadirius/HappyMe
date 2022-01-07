package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.auth_activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.BaseActivity;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.MainActivity;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.R;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.databinding.ActivityLoginBinding;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.firebase.Somewhere;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers.AppConst;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.models.User;

public class LoginActivity extends BaseActivity {
    
    private ActivityLoginBinding binding;
    private LoginActivity loginActivity;
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        loginActivity = this;
        binding = ActivityLoginBinding.inflate( getLayoutInflater() );
        root =  binding.getRoot();
        setContentView( root );
        
        binding.txtForgotPassword.setOnClickListener( v ->  startActivity( new Intent( getBaseContext() , ForgotPasswordActivity.class ) ));
        binding.txtSignUp.setOnClickListener( v ->
                startActivity( new Intent( getBaseContext() , RegisterActivity.class ) ) );
        binding.btnLogin.setOnClickListener(  v -> logInRegistredUser() );
    }
    
    private boolean validateLoginDetails() {
        if ( TextUtils.isEmpty( binding.etEmail.getText().toString().trim() ) ) {
            showErrorSnackBar(getString( R.string.err_msg_enter_email ) , true );
            return false;
        }
        
        if ( TextUtils.isEmpty( binding.etPassword.getText().toString().trim() ) ) {
            showErrorSnackBar(getString( R.string.err_msg_enter_password ) , true );
            return false;
        }
        showErrorSnackBar( getString( R.string.valid_credentials ) , false );
        return true;
    }
    
    private void logInRegistredUser() {
        
        if ( validateLoginDetails() ) {
            
            showProgressDialog( null );
            
            String mail = binding.etEmail.getText().toString().trim().toLowerCase();
            String password = binding.etPassword.getText().toString().trim();
            
            FirebaseAuth.getInstance().signInWithEmailAndPassword( mail , password )
                    .addOnCompleteListener( new OnCompleteListener< AuthResult >() {
                        @Override
                        public void onComplete( @NonNull Task task ) {
                            if ( task.isSuccessful() ) {
                                Somewhere.getUser( loginActivity );
                            } else {
                                hideProgressDialog();
                                showErrorSnackBar( task.getException().getMessage().toString() , true );
                            }
                        }
                    } );
        }
    }
    
    public void userLoggedInSuccess( User user ) {
        hideProgressDialog();
        
        if(user.getProfileCompleted() == 0){
            startActivity(new Intent(this, UserProfileActivity.class)
                    .putExtra( AppConst.CURRENT_USER_COMPLETE_PROFILE , user ) );
        }else{
            startActivity(new Intent(this, MainActivity.class) );
        }
        finish();
    }
}