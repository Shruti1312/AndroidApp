package com.csci571.forecast;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.csci571.forecast.dummy.Summary_weekly;
import com.csci571.forecast.dummy.Weekly;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SummaryFragment extends Fragment implements ItemFragment2.OnListFragmentInteractionListener{
    int position;
    public TextView textView;
    public FloatingActionButton fav_add;
    public FloatingActionButton fav_remove;
    String location;
    View currView;

    public static Fragment getInstance(int position, String location) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        bundle.putString("loc", location);
        SummaryFragment tabFragment = new SummaryFragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
        location = getArguments().getString("loc");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Begin the transaction
        return inflater.inflate(R.layout.summary_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currView = view;
        if(location.equals(Sharable.currLocation))
        {
            JSONObject obj =  Sharable.currJsonObj;
            this.setCardValues(location, obj);
            //final FloatingActionButton fav_minus = (FloatingActionButton) view.findViewById(R.id.fab_minus);
        }else
        {
            JSONObject obj = Sharable.getInstance().mMap.get(location.toLowerCase());
            this.setCardValues(location, obj);
        }
    }

    public void setCardValues(String location, JSONObject obj) {

        try
        {
            TextView locationText = (TextView) currView.findViewById(R.id.location);
            locationText.setText(location);

            JSONObject currently = obj.getJSONObject("currently");
            JSONObject daily = obj.getJSONObject("daily");

            String icon = currently.getString("icon");
            Integer temp = currently.getInt("temperature");
            String summary = currently.getString("summary");
            String temperature = temp.toString() + (char) 0x00B0 + " F";
            Sharable.temperature = temperature;
            setCard1(icon, temperature, summary);


            DecimalFormat df = new DecimalFormat("0.00");
            df.setMaximumFractionDigits(2);

            String humidity = df.format(currently.getDouble("humidity") * 100);
            String windSpeed = df.format(currently.getDouble("windSpeed"));
            String visibility = df.format(currently.getDouble("visibility"));
            String pressure = df.format(currently.getDouble("pressure"));
            setCard2(humidity, windSpeed, visibility, pressure);

            JSONArray jsonArr = (JSONArray)daily.get("data");
            TableLayout table = (TableLayout)currView.findViewById(R.id.week_dtl);
            for(int i = 0; i < jsonArr.length(); i++)
            {
                String icon_name =  jsonArr.getJSONObject(i).getString("icon");
                long time =  jsonArr.getJSONObject(i).getLong("time");
                Integer min_temp =  jsonArr.getJSONObject(i).getInt("temperatureLow");
                Integer max_temp =  jsonArr.getJSONObject(i).getInt("temperatureHigh");
                setCard3(table, time, icon_name, min_temp.toString(), max_temp.toString());
            }

            table.requestLayout();
        }catch(JSONException ex)
        {
            ex.printStackTrace();
        }
    }

    public void setCard1(String icon, String temperature, String summary) {
        TextView summaryText = (TextView) currView.findViewById(R.id.summary);
        TextView tempText = (TextView) currView.findViewById(R.id.temperature);
        ImageView iconImage = (ImageView) currView.findViewById(R.id.icon_img);

        iconImage.setImageResource(Sharable.getIconResourceId(icon));
        summaryText.setText(summary);
        tempText.setText(temperature);
    }

    public void setCard2(String humidity, String windSpeed, String visibility, String pressure) {
        TextView humidityText = (TextView) currView.findViewById(R.id.humidity);
        TextView windSpeedText = (TextView) currView.findViewById(R.id.windSpeed);
        TextView visibilityText = (TextView) currView.findViewById(R.id.visibility);
        TextView pressureText = (TextView) currView.findViewById(R.id.pressure);

        humidityText.setText(humidity + "%");
        windSpeedText.setText(windSpeed + " mph");
        visibilityText.setText(visibility + " km");
        pressureText.setText(pressure + " mb");
    }

    public void setCard3(TableLayout table, long time, String icon_name, String min_temperature, String max_temperature){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date timestamp = new Date(time * 1000);
        String dt = sdf.format(timestamp);

        TableRow row = (TableRow) View.inflate(getActivity(), R.layout.fragment_item2, null);

        ((TextView)row.findViewById(R.id.s_date)).setText(dt);
        ((ImageView)row.findViewById(R.id.s_icon)).setImageResource(Sharable.getIconResourceId(icon_name));
        ((TextView)row.findViewById(R.id.smi_temp)).setText(min_temperature);
        ((TextView)row.findViewById(R.id.smx_temp)).setText(max_temperature);

        table.addView(row);
       // Weekly wk = new Weekly(dt, icon_name, min_temperature, max_temperature);
        //Summary_weekly.addItem(wk);
    }

    @Override
    public void onListFragmentInteraction(Weekly item) {

    }
}