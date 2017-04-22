package demo.wunder.org.WunderOnlineTestDemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import demo.wunder.org.Controller.CarDetails;
import demo.wunder.org.Controller.Util.FileSharedPreference;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by subha on 4/19/2017.
 */

public class CarMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener,GoogleMap.OnInfoWindowCloseListener,
        GoogleMap.OnInfoWindowClickListener,OnMapReadyCallback {

    final int REQUEST_CODE_LOCATION = 1001;
    MapView mapView = null;
    GoogleMap map = null;
    FileSharedPreference fsp = new FileSharedPreference();
    List<CarDetails> cd = null;
    double userlat = 0.0;
    double userlongi = 0.0;
    private Marker lastClicked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_maps, container, false);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);


        // Gets to GoogleMap from the MapView and does initialization stuff
        mapView.getMapAsync(this);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls

        MapsInitializer.initialize(this.getActivity());


        return v;
    }

    private void checkPermission()
    {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;

        if (ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED)
        {
            doIfLocationPermissionGranted();
        }

        //show explanation
        else  if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission))
        {
            requestPermissions(new String[]{permission}, REQUEST_CODE_LOCATION);
        }
        else
        {
            requestPermissions(new String[]{permission}, REQUEST_CODE_LOCATION);

        }

    }

    private void doIfLocationPermissionGranted() {

        //already permission granted
        map.setMyLocationEnabled(true);
        //get user location and zoom into it
        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);


        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            //for testing purpose hardcode hamburg location
            //LatLng coordinate = new LatLng(latitude, longitude);
            userlat = 53.551086;
            userlongi =  9.993682;
            LatLng coordinate = new LatLng( userlat,userlongi);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinate,17.0f));
            // map.animateCamera(CameraUpdateFactory.zoomTo(19), 5000, null);


            //place coordinates
            placeCoordinates();

        }
    }

    private void placeCoordinates() {

        for (int i = 0; i < cd.size(); i++) {
            CarDetails entry = cd.get(i);
            addMarker(entry.getCoordinates()[1], entry.getCoordinates()[0],entry.getName(),false);
        }
        addUserMarker( userlat,userlongi,false);

    }

    @Override
    public void onResume() {
        mapView.onResume();
        if(map != null)
        {
            cd = fsp.loadJsonLocation(getActivity().getApplicationContext());
            checkPermission();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }




    private void addMarker(double lat, double longi,String title, boolean makeVisible)
    {
        LatLng place = new LatLng(lat,longi);
        if(makeVisible)
            map.addMarker(new MarkerOptions().position(place).title(title).icon(BitmapDescriptorFactory.
                    fromResource(R.drawable.pink))).showInfoWindow();
        else
            map.addMarker(new MarkerOptions().position(place).title(title).icon(BitmapDescriptorFactory.
                    fromResource(R.drawable.pink))).hideInfoWindow();
        // map.moveCamera(CameraUpdateFactory.newLatLng(place));
    }

    private void addUserMarker(double lat, double longi,boolean makeVisible)
    {
        LatLng place = new LatLng(lat,longi);
        if(makeVisible)
            map.addMarker(new MarkerOptions().position(place).title("user").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))).showInfoWindow();
        else

            map.addMarker(new MarkerOptions().position(place).title("user").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))).hideInfoWindow();


    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        //No case to handle for user car
        if(marker.getTitle().equals("user"))
            return true;

        //display the name
        if (lastClicked != null)
        {

            map.clear();
            placeCoordinates();
            lastClicked = null;
            return true;
        }
        else
        {

            map.clear();
            addMarker(marker.getPosition().latitude,marker.getPosition().longitude,marker.getTitle(),true);
            addUserMarker( userlat,userlongi,false);
            lastClicked = marker;
            return false;
        }



    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_CODE_LOCATION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission granted.
                    doIfLocationPermissionGranted();


                } else {

                    // permission denied
                    Toast.makeText(this.getContext(),"Location Access Denied. Cannot display cars. Please grant the permission and try again",
                            Toast.LENGTH_LONG).show();
                }
                return;

        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {

        Log.d("WUNDER info click",marker.getTitle());
       /* marker.hideInfoWindow();
        map.clear();
        placeCoordinates();*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map  = googleMap;
        map.setOnInfoWindowClickListener(this);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setOnMarkerClickListener(this);
        cd = fsp.loadJsonLocation(getActivity().getApplicationContext());
        checkPermission();



    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        Log.d("WUNDER info close click",marker.getTitle());
       /* marker.hideInfoWindow();
        map.clear();
        placeCoordinates();*/
    }
}
