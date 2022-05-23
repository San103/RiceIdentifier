package com.san.ripenessidentification.GoogleMaps;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {
    String googlePlacesData;
    GoogleMap googleMap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {
        googleMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();
        try {
            googlePlacesData = downloadUrl.redUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
//        try {
//            JSONObject parentObject = new JSONObject(s);
//            JSONArray resultArray = parentObject.getJSONArray("results");
//
//            for (int i = 0; i < resultArray.length(); i++) {
//                JSONObject jsonObject= resultArray.getJSONObject(i);
//                JSONObject locationObj = jsonObject.getJSONObject("geometry").getJSONObject("location");
//
//                String latitude = locationObj.getString("lat");
//                String longitude= locationObj.getString("lng");
//
//                JSONObject nameObject = resultArray.getJSONObject(i);
//                String name= nameObject.getString("name");
//
//                LatLng latLng= new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
//                MarkerOptions markerOptions = new MarkerOptions();
//                markerOptions.title(name);
//                markerOptions.position(latLng);
//
//                googleMap.addMarker(markerOptions);
//
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        List<HashMap<String, String>> nearbyPlaceList = null;
        DataParser parser= new DataParser();
        nearbyPlaceList = parser.parse(s);
        showNearbyPlaces(nearbyPlaceList);
    }
    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList){
        for(int i=0; i< nearbyPlaceList.size(); i++){
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String,String> googlePlace= nearbyPlaceList.get(i);

            String placename = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            double lat= Double.parseDouble(googlePlace.get("lat"));
            double lng= Double.parseDouble(googlePlace.get("lng"));

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placename+" : "+vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
            googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }
}
