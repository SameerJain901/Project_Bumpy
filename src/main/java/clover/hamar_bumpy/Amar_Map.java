package clover.hamar_bumpy;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Cap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import java.util.Random;
import java.util.Stack;

public class Amar_Map extends FragmentActivity implements OnMapReadyCallback{

    public static GoogleMap mMap;
    public static Geo_Location geo_location;
    public static Stack<Marker> markers = new Stack<Marker>();
    public static MyBackHurts mySensor;
    public static boolean startAlgo;
    public static RoadTest roadTest;
    private String logger = "MyApplication";
    private boolean options;
    private int c=0;

//    private FusedLP_Location fusedLP_location=new FusedLP_Location(this);
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amar__map);
//        Toolbar toolbar=findViewById(R.id.myid);
//        setActionBar(toolbar);
        options=false;
        startAlgo=false;
        roadTest=new RoadTest(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mySensor=new MyBackHurts(context);
                startAlgo=true;
                Toast.makeText(context,"Starting Accelorometer ",Toast.LENGTH_SHORT).show();
            }
        },20000);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        geo_location = new Geo_Location(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        final FloatingActionButton fb=findViewById(R.id.myMenu);
        ImageView imageView=findViewById(R.id.settings);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(options==false)
                {
                    fb.animate().rotationBy(90);
                    findViewById(R.id.containerOptions).setVisibility(View.VISIBLE);
                    findViewById(R.id.containerOptions).animate().rotationBy(360);
                    options=true;
                }
                else
                {
                    fb.animate().rotationBy(-90);
                    findViewById(R.id.containerOptions).setVisibility(View.INVISIBLE);
                    options=false;
                }

            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c++;
                switch (c%6 + 1 )
                {
                    case 1 : try {
                        // Customise the styling of the base map using a JSON object defined
                        // in a raw resource file.
                        boolean success = mMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        getApplicationContext(),R.raw.mapstyle_dark));

                        if (!success) {
                            Log.e("", "Style parsing failed.");
                        }
                    } catch (Resources.NotFoundException e) {
                        Log.e("", "Can't find style. Error: ", e);
                    }

                            break;

                    case 2 : try {
                        // Customise the styling of the base map using a JSON object defined
                        // in a raw resource file.
                        boolean success = mMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        getApplicationContext(),R.raw.blastpink));

                        if (!success) {
                            Log.e("", "Style parsing failed.");
                        }
                    } catch (Resources.NotFoundException e) {
                        Log.e("", "Can't find style. Error: ", e);
                    }
                        break;

                    case 3 : try {
                        // Customise the styling of the base map using a JSON object defined
                        // in a raw resource file.
                        boolean success = mMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        getApplicationContext(),R.raw.mapstyle_retro));

                        if (!success) {
                            Log.e("", "Style parsing failed.");
                        }
                    } catch (Resources.NotFoundException e) {
                        Log.e("", "Can't find style. Error: ", e);
                    }
                        break;

                    case 4 : try {
                        // Customise the styling of the base map using a JSON object defined
                        // in a raw resource file.
                        boolean success = mMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        getApplicationContext(),R.raw.mapstyle_grayscale));

                        if (!success) {
                            Log.e("", "Style parsing failed.");
                        }
                    } catch (Resources.NotFoundException e) {
                        Log.e("", "Can't find style. Error: ", e);
                    }
                        break;

                    case 5 : try {
                        // Customise the styling of the base map using a JSON object defined
                        // in a raw resource file.
                        boolean success = mMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        getApplicationContext(),R.raw.stylemap));

                        if (!success) {
                            Log.e("", "Style parsing failed.");
                        }
                    } catch (Resources.NotFoundException e) {
                        Log.e("", "Can't find style. Error: ", e);
                    }
                        break;


                    case 6 : try {
                        // Customise the styling of the base map using a JSON object defined
                        // in a raw resource file.
                        boolean success = mMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        getApplicationContext(),R.raw.mapstylecoolblue));

                        if (!success) {
                            Log.e("", "Style parsing failed.");
                        }
                    } catch (Resources.NotFoundException e) {
                        Log.e("", "Can't find style. Error: ", e);
                    }
                        break;

                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(startAlgo)
            if(!mySensor.isListenerRegistered)
                mySensor.registerAccel();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySensor.unregisterAccel();
        mySensor.isListenerRegistered=false;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setLocationSource(geo_location);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        final Location myLocation=geo_location.getCurrLocation(this);
        final Location cry=myLocation;

        Log.d(logger,">>>>>>>>>>>>>>>current location"+myLocation.toString());
        LatLng sydney = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        markers.push(mMap.addMarker(new MarkerOptions().position(sydney).title(geo_location.getLocationAddress(myLocation,context))));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,mMap.getMaxZoomLevel()));
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                LatLng sydney = new LatLng(latLng.latitude, latLng.longitude);
                Location temp = cry;
                temp.setLatitude(latLng.latitude);
                temp.setLongitude(latLng.longitude);
                markers.push(mMap.addMarker(new MarkerOptions().position(sydney).title(geo_location.getLocationAddress(temp,context))));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        markers.pop().remove();
                    }
                },5000);
            }
        });


    }




//    @Override
//    public void onLocationChanged(Location location) {
//        final Location myLocation=geo_location.getCurrLocation(this);
//        LatLng sydney = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
//        markers.pop().remove();
//        markers.push(mMap.addMarker(new MarkerOptions().position(sydney).title(geo_location.getLocationAddress(myLocation,context))));
//    }
}
