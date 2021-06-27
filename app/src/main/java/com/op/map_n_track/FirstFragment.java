package com.op.map_n_track;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.op.map_n_track.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {

    private static final int REQUEST_CODE_AUTOCOMPLETE = 21;
    private static final int PLACE_SELECTION_REQUEST_CODE = 56789;
    private FragmentFirstBinding binding;
    private MapboxMap mapbox;

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

        binding.mapView.onCreate(savedInstanceState);
        binding.mapView.getMapAsync(mapboxMap -> mapboxMap.setStyle(Style.MAPBOX_STREETS, style -> {
            mapbox = mapboxMap;
            LatLng lucknow = new LatLng(26.8467, 80.9462);
            mapbox.animateCamera(CameraUpdateFactory.newLatLngZoom(lucknow, 16), 3000);
            activateUI();
        }));
    }

    private void activateUI() {
        binding.btnSearch.setOnClickListener(view -> {
            PlaceOptions placeOptions = PlaceOptions.builder().backgroundColor(Color.WHITE).build();
            Intent intent = new PlaceAutocomplete.IntentBuilder()
                    .accessToken(getString(R.string.mapbox_access_token))
                    .placeOptions(placeOptions)
                    .build(getActivity());
            startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
        });
        binding.btnSearch.setOnLongClickListener(view -> {
            Intent intent = new PlacePicker.IntentBuilder()
                    .accessToken(Mapbox.getAccessToken())
                    .placeOptions(
                            PlacePickerOptions.builder()
                                    .statingCameraPosition(
                                            new CameraPosition.Builder()
                                                    .target(new LatLng(26.8467, 80.9462))
                                                    .zoom(16)
                                                    .build())
                                    .build())
                    .build(getActivity());
            startActivityForResult(intent, PLACE_SELECTION_REQUEST_CODE);
            return true;
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            CarmenFeature feature = PlaceAutocomplete.getPlace(data);
            Snackbar.make(binding.getRoot(), feature.text(), BaseTransientBottomBar.LENGTH_INDEFINITE).show();

        }
        if (resultCode == Activity.RESULT_OK && requestCode == PLACE_SELECTION_REQUEST_CODE) {
            CarmenFeature carmenFeature = PlacePicker.getPlace(data);
            Snackbar.make(binding.getRoot(), carmenFeature.text(), BaseTransientBottomBar.LENGTH_INDEFINITE).show();
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