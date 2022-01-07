package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.R;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.models.HappinessModel;

public class InstantRecyclerView  extends RecyclerView.Adapter< InstantRecyclerView.InstantHolder > {
    private final int[] imagesIds = new int[] {R.drawable.level_1, R.drawable.level_2,R.drawable.level_3,R.drawable.level_4,R.drawable.level_5};
    
    private List< HappinessModel > instants;
    private final Context ctx;
    private final int maskId;
    private final OnHappyInstantClickListener listener;
    public interface OnHappyInstantClickListener {
        void onViewClick(HappinessModel instant);
        void onRegistrationActionClick(HappinessModel instant);
    }
    
    
    public InstantRecyclerView( List< HappinessModel > instants , Context ctx , int maskId , OnHappyInstantClickListener listener ) {
        this.instants = instants;
        this.ctx = ctx;
        this.maskId = maskId;
        this.listener = listener;
    }
    
    
    @NonNull
    @Override
    public InstantHolder onCreateViewHolder( @NonNull ViewGroup parent , int viewType ) {
        View view = LayoutInflater
                .from( parent.getContext() )
                .inflate( maskId , parent, false );
        return new InstantHolder( view );
    }
    
    @SuppressLint( "UseCompatLoadingForDrawables" )
    @Override
    public void onBindViewHolder( @NonNull InstantHolder holder , int position ) {
 
        HappinessModel happy = instants.get( position );
        PictureHandler.loadPicture( ctx, happy.getImage(),  holder.instant_image);
        holder.instant_happiness_image.setImageResource( imagesIds[happy.getLevel()] );
        holder.rating.setRating( happy.getRating() );
        holder.title.setText( happy.getTitle() );
        holder.location.setText( getLocation(happy.getLocation()) );
        holder.btnView.setOnClickListener( v -> listener.onViewClick( happy ) );

    }
    
    private String getLocation( String location ) {
        String[] temp = location.split( ",");
        return temp[temp.length-1].trim()+ ", " + temp[temp.length - 2].trim().split( " " )[1];
    }
    
    @Override
    public int getItemCount() {
        return ((instants != null) && (instants.size() !=0) ? instants.size() : 0);
    }
    
    
    static public class InstantHolder extends RecyclerView.ViewHolder {
        ImageView instant_image = null;
        TextView title = null;
        TextView location = null;
        ImageView instant_happiness_image = null;
        RatingBar rating = null;
        ImageButton btnView = null;
    
    
        public InstantHolder( @NonNull View itemView ) {
            super( itemView );
            this.instant_image = (ImageView ) itemView.findViewById( R.id.iv_item_image );
            this.title = (TextView ) itemView.findViewById( R.id.tv_item_name );
            this.location = (TextView) itemView.findViewById( R.id.tv_item_info );
            this.instant_happiness_image = (ImageView ) itemView.findViewById( R.id.iv_instant_happiness_image );
            this.rating = (RatingBar ) itemView.findViewById( R.id.rat_hapinness_instant );
            this.btnView = (ImageButton ) itemView.findViewById( R.id.ib_view);
        }
    }
}

