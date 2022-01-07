package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.databinding.ActivityMapBinding;
import ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.models.HappinessModel;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {
    
    private HappinessModel happyInstant;
    private ActivityMapBinding binding;
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
       binding = ActivityMapBinding.inflate( getLayoutInflater() );
       root = binding.getRoot();
       
//        if(getIntent().hasExtra( (MainActivity.HAPPINESS_LOCATION_DETAILS) )){
//            happyInstant = (HappinessModel ) getIntent().getSerializableExtra( MainActivity.HAPPINESS_LOCATION_DETAILS );
//        }
        
        if(null != happyInstant){
            setSupportActionBar( binding.toolbarMap );
            Objects.requireNonNull( getSupportActionBar() ).setDisplayHomeAsUpEnabled( true );
            getSupportActionBar().setTitle( happyInstant.getTitle() );
        
            binding.toolbarMap.setOnClickListener( v -> {
                onBackPressed();
            } );
    
            SupportMapFragment supportMapFragment = ( SupportMapFragment ) getSupportFragmentManager().findFragmentById( R.id.map );
            Objects.requireNonNull( supportMapFragment ).getMapAsync( this );
        }
    }
    
    @Override
    public void onMapReady( @NonNull GoogleMap googleMap ) {
        LatLng position = new LatLng( happyInstant.getLatitude(), happyInstant.getLongitude() );
        googleMap.addMarker( new MarkerOptions().position( position ).title( happyInstant.getLocation() ) );
        CameraUpdate newLatLngZoom = CameraUpdateFactory.newLatLngZoom( position, 20f );
        googleMap.animateCamera( newLatLngZoom );
        
    }
}