package com.example.lucy.first.Helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.lucy.first.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindow implements GoogleMap.InfoWindowAdapter {

    View myView;

    public CustomInfoWindow(Context context) {
        myView = LayoutInflater.from(context)
                .inflate(R.layout.custom_client_info_window,null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        TextView txtPickupTitle =((TextView)myView.findViewById(R.id.txtPickupInfo));
        txtPickupTitle.setText(marker.getTitle());

        TextView txtPickupSnippet =((TextView)myView.findViewById(R.id.txtPickupSnippet));
        txtPickupSnippet.setText(marker.getSnippet());

        return myView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
