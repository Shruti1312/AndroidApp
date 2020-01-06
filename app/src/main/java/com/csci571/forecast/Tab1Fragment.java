package com.csci571.forecast;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class Tab1Fragment extends Fragment {

    int position;
    private TextView textView;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        Tab1Fragment tabFragment = new Tab1Fragment();
        tabFragment.setArguments(bundle);
        return tabFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.today_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try
        {
            JSONObject currently = Sharable.jsonObj.getJSONObject("currently");

            DecimalFormat df = new DecimalFormat("0.00");
            df.setMaximumFractionDigits(2);

            String windSpeed = df.format(currently.getDouble("windSpeed")) + " mph";
            String pressure = df.format(currently.getDouble("pressure")) + " mb";
            String chanceRain = currently.getDouble("precipProbability") + "mmph";
            Integer temp = currently.getInt("temperature");
            String temperature = temp.toString() + (char) 0x00B0 + " F";
            String icon = currently.getString("icon");
            String humidity = df.format(currently.getDouble("humidity") * 100) + " %";
            String visibility = df.format(currently.getDouble("visibility")) + " km";
            String cc = df.format(currently.getDouble("cloudCover") * 100) + " %";
            String ozone = df.format(currently.getDouble("ozone")) + " DU";

            TextView windText = (TextView) view.findViewById(R.id.t_windSpeed);
            TextView pressureText = (TextView) view.findViewById(R.id.t_pressure);
            TextView precipationText = (TextView) view.findViewById(R.id.t_precipitation);
            TextView tempText = (TextView) view.findViewById(R.id.t_temperature);

            TextView HumidityText = (TextView) view.findViewById(R.id.t_humidity);
            TextView visibilityText = (TextView) view.findViewById(R.id.t_visibility);
            TextView ccText = (TextView) view.findViewById(R.id.t_cloudCover);
            TextView ozoneText = (TextView) view.findViewById(R.id.t_ozone);

            windText.setText(windSpeed);
            pressureText.setText(pressure);
            precipationText.setText(chanceRain);
            tempText.setText(temperature);
            HumidityText.setText(humidity);
            visibilityText.setText(visibility);
            ccText.setText(cc);
            ozoneText.setText(ozone);


            TextView iconText = (TextView) view.findViewById(R.id.t_iconText);
            ImageView iconImg = (ImageView) view.findViewById(R.id.t_iconImg);
            iconImg.setImageResource(Sharable.getIconResourceId(icon));
            iconText.setText(getIconName(icon));
        }
        catch(JSONException ex)
        {

        }
    }

    public String getIconName(String icon){
        String icon1 = icon.replace("-", " ");
        String icon2 = icon1.replace("partly", "");
        return icon2.trim();
    }
}