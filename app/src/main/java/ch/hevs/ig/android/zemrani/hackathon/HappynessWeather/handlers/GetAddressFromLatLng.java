package ch.hevs.ig.android.zemrani.hackathon.HappynessWeather.handlers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GetAddressFromLatLng extends AsyncTask<Void, String, String> {
    
    private final Geocoder geocoder;
    private final double latitude;
    private final double longitude;
    
    public void getAddress() {
        this.execute();
        }
    
    public interface AddressListener {
        void onAddressFound(String address);
        void onError();
    }
    
    private AddressListener addressListener;
    
    public GetAddressFromLatLng( Context ctx , double latitude , double longitude )
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.geocoder = new Geocoder( ctx, Locale.getDefault() );
    }
    
    @Override
    protected String doInBackground( Void... voids ) {
        try{
            List< Address > addressList = geocoder.getFromLocation( latitude, longitude, 1 );
            
            if(addressList != null && !addressList.isEmpty()){
                Address address = addressList.get( 0 );
                StringBuilder sb = new StringBuilder();
                for ( int i = 0; i <= address.getMaxAddressLineIndex(); i++ ) {
                    sb.append(address.getAddressLine(i)).append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                return sb.toString();
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return "";
    }
    
    @Override
    protected void onPostExecute( String s ) {

        if(s == null){
            addressListener.onError();
        }else{
            addressListener.onAddressFound( s );
        }
        super.onPostExecute( s );
    }
    
    public void setAddressListener( AddressListener addressListener ) {
        this.addressListener = addressListener;
    }
}
