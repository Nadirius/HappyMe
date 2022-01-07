package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.auth_activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.BaseActivity;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.R;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.databinding.ActivityRegisterBinding;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.firebase.Somewhere;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.models.User;


public class RegisterActivity extends BaseActivity {
    
    private ActivityRegisterBinding binding;
    
    private FirebaseAuth auth;
    
    private RegisterActivity thisActivity;
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        thisActivity = this;
        binding = ActivityRegisterBinding.inflate( getLayoutInflater() );
        root =  binding.getRoot();
        setContentView( root );
        
        auth = FirebaseAuth.getInstance();
        
        setupActionBarBack(binding.toolbarSignUpActivity);
        
        binding.txtLogin.setOnClickListener( v -> onBackPressed());
        binding.btnRegister.setOnClickListener( v -> registerUser());
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser thisUser = auth.getCurrentUser();
        if ( null != thisUser ) {
            thisUser.reload();
        }
    }
    
    private Boolean validateRegisterDetails() {
        
        if ( TextUtils.isEmpty( binding.etFirstName.getText().toString().trim() ) ) {
            showErrorSnackBar( getString( R.string.err_msg_enter_first_name ) , true );
            return false;
        }
        
        if ( TextUtils.isEmpty( binding.etLastName.getText().toString().trim() ) ) {
            showErrorSnackBar( getString( R.string.err_msg_enter_first_name ) , true );
            return false;
        }
        
        if ( TextUtils.isEmpty( binding.etEmail.getText().toString().trim() ) ) {
            showErrorSnackBar( getString( R.string.err_msg_enter_email ) , true );
            return false;
        }
        
        if ( TextUtils.isEmpty( binding.etPassword.getText().toString().trim() ) ) {
            showErrorSnackBar( getString( R.string.err_msg_enter_password ) , true );
            return false;
        }
        
        if ( TextUtils.isEmpty( binding.etConfirmPassword.getText().toString().trim() ) ) {
            showErrorSnackBar( getString( R.string.err_msg_enter_confirm_password ) , true );
            return false;
        }
        
        if ( !binding.etPassword.getText().toString().trim().equals( binding.etConfirmPassword.getText().toString().trim() ) ) {
            showErrorSnackBar( getString( R.string.err_msg_password_and_confirm_password_mismatch ) , true );
            return false;
        }
        
        if ( !binding.cbTermsAndCondition.isChecked() ) {
            showErrorSnackBar( getString( R.string.err_msg_agree_terms_and_condition ) , true );
            return false;
        }
        
        return true;
    }
    
    
    private void registerUser() {
        if ( validateRegisterDetails() ) {
            showProgressDialog( null );
            
            String mail = binding.etEmail.getText().toString().trim().toLowerCase();
            String password = binding.etPassword.getText().toString().trim();
            
            auth.createUserWithEmailAndPassword( mail , password )
                    .addOnCompleteListener( new OnCompleteListener< AuthResult >() {
                        
                        @Override
                        public void onComplete( @NonNull Task< AuthResult > task ) {
                            String message;
                            if ( task.isSuccessful() ) {
                                Somewhere.storeUser(thisActivity, bindUser(task.getResult().getUser()));
                            } else {
                                hideProgressDialog();
                                message = task.getException().getMessage().toString();
                                showErrorSnackBar( message , true );
                            }
                        }
                    } );
        }
    }
    
    private User bindUser( FirebaseUser user ) {
        return new User()
                .setId( user.getUid() )
                .setFirstName( formatString(binding.etFirstName.getText().toString().trim()) )
                .setLastName( formatString(binding.etLastName.getText().toString().trim()) )
                .setEmail( binding.etEmail.getText().toString().trim().toLowerCase() );
    }
    
    public void registrationSucess(){
        hideProgressDialog();
        Toast.makeText(getBaseContext(), getString( R.string.registry_successfull ), Toast.LENGTH_LONG).show();
        auth.signOut();
        finish();
    }
    
    public void registationFailure( String errorMsg ) {
        hideProgressDialog();
        Toast.makeText(getBaseContext(), errorMsg , Toast.LENGTH_LONG).show();
    }
}
