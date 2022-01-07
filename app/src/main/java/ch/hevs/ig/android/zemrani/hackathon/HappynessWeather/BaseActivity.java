package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.customed_widgets.MontSerratTextViewRegular;


public abstract class BaseActivity extends AppCompatActivity {
    
    private boolean doubleBackToExitPressedOnce = false;
    private Dialog msgOnProgressDialog;
    protected View root;
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN , WindowManager.LayoutParams.FLAG_FULLSCREEN );
    }
    
    public void showErrorSnackBar( String msg, Boolean errorMsg){
        Snackbar snackbar = Snackbar.make( root, msg, Snackbar.LENGTH_LONG );
        View snackbarView = snackbar.getView();
    
        int errorColorId =
                (errorMsg)
                ? ContextCompat.getColor( getBaseContext(), R.color.colorSnackBarError )
                : ContextCompat.getColor( getBaseContext(), R.color.colorSnackBarSuccess );
        
        snackbarView.setBackgroundColor( errorColorId );
        snackbar.show();
    }
    
    public void showProgressDialog(String...dialogs ){
        String dialog = (null == dialogs)
                ? getString( R.string.please_wait )
                : dialogs[0];
        
        msgOnProgressDialog = new Dialog(root.getContext());
        
        msgOnProgressDialog.setContentView( R.layout.dialog_progress );
        
        (( MontSerratTextViewRegular ) msgOnProgressDialog
                .findViewById( R.id.txt_progress_text ))
                .setText( dialog );
        
        msgOnProgressDialog.setCancelable( false );
        msgOnProgressDialog.setCanceledOnTouchOutside( false );
        
        msgOnProgressDialog.show();
    
    }
    
    public void hideProgressDialog(){
        msgOnProgressDialog.dismiss();
    }
    
    protected void setupActionBarBack( Toolbar toolbar) {
        setSupportActionBar( toolbar );
        if ( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setHomeAsUpIndicator( R.drawable.ic_black_color_back_24dp );
        }
        toolbar.setNavigationOnClickListener( v -> doubleBackToExit() );
    }
    
    protected String formatString(String str){
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    
    public void doubleBackToExit(){
        if(doubleBackToExitPressedOnce){
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText( this , getString( R.string.click_back_again_to_exit ) , Toast.LENGTH_SHORT ).show();
        
        new Handler().postDelayed( ( () -> {
            doubleBackToExitPressedOnce = false;
        }),2000);
    }
    

}