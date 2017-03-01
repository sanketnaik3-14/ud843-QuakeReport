package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.List;

import static com.example.android.quakereport.R.id.primary_location;
import static com.example.android.quakereport.R.id.magnitude;

/**
 * Created by sanket on 26/02/17.
 */

public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private static final String LOCATION_SEPERATOR = "of";
    public EarthquakeAdapter(Context context, List<Earthquake> earthquakes)
    {
        super(context,0,earthquakes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if(listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Earthquake current_earthquake = getItem(position);

        TextView magnitudeView = (TextView)listItemView.findViewById(magnitude);
        String formattedMagnitude = formatMagnitude(current_earthquake.getMagnitude());
        magnitudeView.setText(formattedMagnitude);

        GradientDrawable magnitudeCircle = (GradientDrawable)magnitudeView.getBackground();
        int magnitudeColor = getMagnitudeColor(current_earthquake.getMagnitude());
        magnitudeCircle.setColor(magnitudeColor);

        String location = current_earthquake.getLocation();

        String part1 = "";
        String part2 = "";

        if(location.contains(LOCATION_SEPERATOR))
        {
            String[] parts = location.split(LOCATION_SEPERATOR);
            part1 = parts[0] + "of";
            part2 = parts[1];
        }
        else
        {
            part1 = getContext().getString(R.string.near_the);
            part2 =  location;
        }


        TextView loc = (TextView)listItemView.findViewById(R.id.primary_location);
        loc.setText(part2);

        TextView near = (TextView)listItemView.findViewById(R.id.location_offset);
        near.setText(part1);

        Date dateObject = new Date(current_earthquake.getTimeInMilliseconds());

        TextView dateView = (TextView)listItemView.findViewById(R.id.date);

        String formattedDate = formatDate(dateObject);
        dateView.setText(formattedDate);

        TextView timeView = (TextView)listItemView.findViewById(R.id.time);

        String formattedTime = formatTime(dateObject);
        timeView.setText(formattedTime);
        return listItemView;
    }

    public String formatDate(Date dateObject)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,yyyy");
        return dateFormat.format(dateObject);
    }

    public String formatTime(Date dateObject)
    {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    public String formatMagnitude(Double magnitude)
    {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        return decimalFormat.format(magnitude);
    }

    public int getMagnitudeColor(Double magnitude)
    {
        int color;
        int magnitudeFloor = (int)Math.floor(magnitude);

        switch(magnitudeFloor)
        {
            case 0:
            case 1:
                color = R.color.magnitude1;
                break;
            case 2:
                color = R.color.magnitude2;
                break;
            case 3:
                color = R.color.magnitude3;
                break;
            case 4:
                color = R.color.magnitude4;
                break;
            case 5:
                color = R.color.magnitude5;
                break;
            case 6:
                color = R.color.magnitude6;
                break;
            case 7:
                color = R.color.magnitude7;
                break;
            case 8:
                color = R.color.magnitude8;
                break;
            case 9:
                color = R.color.magnitude9;
                break;
            default:
                color = R.color.magnitude10plus;
                break;
        }

        return ContextCompat.getColor(getContext() , color);
    }
}

