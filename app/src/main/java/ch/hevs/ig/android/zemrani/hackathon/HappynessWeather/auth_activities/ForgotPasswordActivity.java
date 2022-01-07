package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.auth_activities;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.BaseActivity;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.R;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends BaseActivity {
        
        private ActivityForgotPasswordBinding binding;
        
        @Override
        protected void onCreate( Bundle savedInstanceState ) {
            super.onCreate( savedInstanceState );
            binding = ActivityForgotPasswordBinding.inflate( getLayoutInflater() );
            root = binding.getRoot();
            setContentView( root );
            
            setupActionBarBack( binding.toolbarForgotPasswordActivity );
            
            binding.btnSubmit.setOnClickListener( v -> resetPassword());
        }
        
        
        private void resetPassword() {
            String mail =  binding.etEmail.getText().toString().trim();
            if ( TextUtils.isEmpty(mail) ) {
                showErrorSnackBar( getString( R.string.err_msg_enter_email ) , true );
            }else{
                showProgressDialog( null );
                FirebaseAuth.getInstance().sendPasswordResetEmail( mail ).addOnCompleteListener( this , new OnCompleteListener< Void >() {
                    @Override
                    public void onComplete( @NonNull Task< Void > task ) {
                        hideProgressDialog();
                        if(task.isSuccessful()){
                            showErrorSnackBar( getString( R.string.reset_password_mail_sent_success ), false );
                            new Handler().postDelayed( ( () -> {
                                finish();
                            }),1000);
                        }else{
                            String message = task.getException().getMessage();
                            showErrorSnackBar( message , true );
                        }
                    }
                } );
            }
        }
    }