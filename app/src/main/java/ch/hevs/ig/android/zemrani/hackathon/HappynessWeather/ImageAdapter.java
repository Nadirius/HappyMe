package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyviewHolder> {
    private final int[] imagesIds = new int[] {R.drawable.level_1, R.drawable.level_2,R.drawable.level_3,R.drawable.level_4,R.drawable.level_5};
    
    public ImageAdapter() {
    }
    
    
    
    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder( @NonNull ViewGroup parent , int viewType ) {
        return new MyviewHolder( LayoutInflater.from( parent.getContext() ).inflate( R.layout.slider_item , parent , false ) );
        
    }
    
    @Override
    public void onBindViewHolder( @NonNull MyviewHolder holder , int position ) {
        holder.view.setBackgroundResource( imagesIds[position] );
    }
    
    @Override
    public int getItemCount() {
        return imagesIds.length;
    }
    
    
    public static class MyviewHolder extends RecyclerView.ViewHolder{
        View view;
        public MyviewHolder( @NonNull View itemView ) {
            super( itemView );
            view = itemView.findViewById( R.id.view );
        }
    }
}
