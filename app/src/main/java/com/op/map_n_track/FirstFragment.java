package com.op.map_n_track;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.maps.UiSettings;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.op.map_n_track.databinding.FragmentFirstBinding;

import static android.app.Activity.RESULT_OK;

public class FirstFragment extends Fragment {

    private static final int REQUEST_CODE_START = 5678;
    private static final int REQUEST_CODE_DEST = 5679;
    private FragmentFirstBinding binding;
    private MapboxMap mapbox;
    private LatLng lucknow;
    private Point startCoord;
    private Point destCoord;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        Mapbox.getInstance(getActivity(), getString(R.string.mapbox_access_token));
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lucknow = new LatLng(26.8467, 80.9462);
        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.getMapAsync(mapboxMap -> mapboxMap.setStyle(Style.SATELLITE_STREETS, style -> {
            mapbox = mapboxMap;
            UiSettings settings = mapbox.getUiSettings();
            settings.setCompassEnabled(true);
            enableLocationComponent(style);
            mapbox.animateCamera(CameraUpdateFactory.newLatLngZoom(lucknow, 16), 3000);
            activateUI();
        }));
    }

    @SuppressLint("MissingPermission")
    private void enableLocationComponent(Style b) {
        LocationComponent component = mapbox.getLocationComponent();
        LocationComponentActivationOptions activationOptions = LocationComponentActivationOptions.builder(getActivity(), b).build();
        component.activateLocationComponent(activationOptions);
        component.setLocationComponentEnabled(true);
        component.setCameraMode(CameraMode.TRACKING_GPS_NORTH);
        component.setRenderMode(RenderMode.COMPASS);

    }

    private void activateUI() {
        CameraPosition camera = new CameraPosition.Builder().target(lucknow).zoom(16).build();
        PlacePickerOptions options = PlacePickerOptions.builder()
                .statingCameraPosition(camera)
                .includeDeviceLocationButton(true)
                .includeReverseGeocode(true).build();
        Intent intent = new PlacePicker.IntentBuilder()
                .accessToken(getString(R.string.mapbox_access_token))
                .placeOptions(options)
                .build(getActivity());
        binding.textStart.setOnClickListener(view -> {
            startActivityForResult(intent, REQUEST_CODE_START);
        });
        binding.textDest.setOnClickListener(view -> {
            startActivityForResult(intent, REQUEST_CODE_DEST);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_START:
                    CarmenFeature place = PlacePicker.getPlace(data);
                    if (place != null) {
                        binding.textStart.setText(place.placeName());
                        startCoord = place.center();
                        mapbox.addMarker(new MarkerOptions().position(new LatLng(startCoord.latitude(), startCoord.longitude())));
                    } else {
                        binding.textStart.setText("No address found");
                    }
                    break;
                case REQUEST_CODE_DEST:
                    CarmenFeature place2 = PlacePicker.getPlace(data);
                    if (place2 != null) {
                        binding.textDest.setText(place2.placeName());
                        destCoord = place2.center();
                        mapbox.addMarker(new MarkerOptions().position(new LatLng(destCoord.latitude(), destCoord.longitude())));
                    } else {
                        binding.textDest.setText("No address found");
                    }

            }
        } else {
            Toast.makeText(getActivity(), "you cancelled the action", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding.mapView.onDestroy();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        binding.mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        binding.mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        binding.mapView.onLowMemory();
    }


}