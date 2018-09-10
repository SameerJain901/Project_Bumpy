/*
        Test Runs : 47
        Success : 18
        Failures : 29
        Code Debugs : 19

        Update : Still not capable to tell the speed of the user
        Update : New Method applied ot set Location Source
        Update : Removed Location Listener
        Update : Using GMaps API -> Location Source
        Update : Implementing Location Listener Directly
        Update : Changed OnLocationChanged Logic a bit. Now not calling the last Known Location\
        Update : Changed OnLocationChanged Logic again cause it was returning null location to API
        Update : Sends proper location and Speed of user
 */
package clover.hamar_bumpy;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;


public class Geo_Location implements LocationSource, LocationListener{
    private LocationManager userLocation;           //Location Manager
    private Context activityContext;
    private String location_context;
    private Criteria crta = new Criteria();
    private String mProvider;
    private Location currLocation,oldLocation;
    private int REQUEST_FINE_LOCATION;              //Application Created
    private int REQUEST_COARSE_LOCATION;            //Application Created
    private OnLocationChangedListener mListener;
    private Geocoder geocoder;

    Geo_Location(final Context context) {
        activityContext = context;
        location_context = Context.LOCATION_SERVICE;
        userLocation = (LocationManager) context.getSystemService(location_context);
        crta.setAccuracy(Criteria.ACCURACY_FINE);
        crta.setAltitudeRequired(false);
        crta.setSpeedAccuracy(Criteria.ACCURACY_HIGH);
        crta.setPowerRequirement(Criteria.POWER_LOW);
        geocoder = new Geocoder(context, Locale.getDefault());
        mProvider = userLocation.getBestProvider(crta, true);

    }

    Location getCurrLocation(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_COARSE_LOCATION);

        }
            do {
                currLocation = userLocation.getLastKnownLocation(mProvider);
                Log.d("", ">>>>>>>No Location found");
            }while(currLocation==null);

        return currLocation;
    }


    String getLocationAddress(Location currLocation, Context context) {
        try {
            List<Address> addresses = geocoder.getFromLocation(currLocation.getLatitude(),
                    currLocation.getLongitude(), 1);
            StringBuilder sb = new StringBuilder();
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                sb.append(address.getAddressLine(0)).append("\n");
                sb.append(address.getLocality()).append("\n");
                sb.append(address.getPostalCode()).append("\n");
                sb.append(address.getCountryName());
            }
            return sb.toString();
        } catch (Exception e) {
            return "Exception";
        }
//        return "Not Found";
    }

    public float getUserSpeed() {
        return currLocation.getSpeed();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(mListener!=null) {
            oldLocation=currLocation;
            currLocation=location;
            LatLng s=new LatLng(currLocation.getLatitude(),currLocation.getLongitude());
            if(!Amar_Map.markers.empty())Amar_Map.markers.pop().remove();
            Amar_Map.markers.push(Amar_Map.mMap.addMarker(new MarkerOptions().position(s).title(this.getLocationAddress(currLocation,activityContext))));
            mListener.onLocationChanged(currLocation);

            //            Toast.makeText(activityContext,"Current Speed"+this.getUserSpeed(),Toast.LENGTH_SHORT).show();
            if(Amar_Map.startAlgo) {
                if (getUserSpeed()>3.0) {
                    //All accelorometer logic here
                    Amar_Map.roadTest.drawDanger(oldLocation,currLocation);
                    Amar_Map.roadTest.refreshRoadDanger();
                    //Clear data no need
                    Amar_Map.mySensor.pushZero();
                }
                else
                {
                    Toast.makeText(activityContext,"User is not Moving Faster",Toast.LENGTH_SHORT).show();
                }
            }

        }
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mProvider != null) {
            if (ActivityCompat.checkSelfPermission(activityContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activityContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
//            Toast.makeText(activityContext,"Its Normal Provider"+mProvider,Toast.LENGTH_LONG).show();
            userLocation.requestLocationUpdates(mProvider, 100, 1, this);

        }
        LocationProvider networkProvider = userLocation.getProvider(LocationManager.NETWORK_PROVIDER);
        if(networkProvider != null) {
            userLocation.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10, 1, this);
//            Toast.makeText(activityContext,"Its Network Provider"+LocationManager.NETWORK_PROVIDER,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void deactivate() {
        userLocation.removeUpdates(this);
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // Not to be used
    }

    @Override
    public void onProviderEnabled(String provider) {
        // Not to be used
    }

    @Override
    public void onProviderDisabled(String provider) {
        // Not to be used
    }
}
