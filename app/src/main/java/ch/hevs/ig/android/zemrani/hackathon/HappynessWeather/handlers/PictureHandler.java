package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.R;


public class PictureHandler {
    
    
    public static void loadUserPicture(Context ctx, Uri uri, ImageView imageView ){
        try {
            Glide
                    .with(ctx)
                    .load( uri )
                    .centerCrop()
                    .placeholder( R.drawable.ic_user_placeholder )
                    .into( imageView );
        } catch ( Exception e ){
            e.printStackTrace();
        }
    }
    
    public static void loadPicture(Context ctx, Uri uri, ImageView imageView ){
        try {
            Glide
                    .with(ctx)
                    .load( uri )
                    .centerCrop()
                    .placeholder( R.drawable.add_screen_image_placeholder )
                    .into( imageView );
        } catch ( Exception e ){
            e.printStackTrace();
        }
    }
    
    public static void loadPicture(Context ctx, String uri, ImageView imageView ){
        try {
            Glide
                    .with(ctx)
                    .load( uri )
                    .centerCrop()
                    .placeholder( R.drawable.add_screen_image_placeholder )
                    .into( imageView );
        } catch ( Exception e ){
            e.printStackTrace();
        }
    }
    
    public static String getFileExtention( Activity activity, Uri uri) {
        
        return MimeTypeMap.getSingleton()
                .getExtensionFromMimeType( activity.getContentResolver().getType( uri ) );
    }
}
