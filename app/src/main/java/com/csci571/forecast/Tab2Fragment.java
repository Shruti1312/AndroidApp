package com.csci571.forecast;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tab2Fragment extends Fragment {

    int position;
    private TextView textView;
    LineChart mChart;

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        Tab2Fragment tabFragment = new Tab2Fragment();
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
        return inflater.inflate(R.layout.weekly_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try
        {
            JSONObject daily = Sharable.jsonObj.getJSONObject("daily");

            JSONArray jsonArr = (JSONArray)daily.get("data");
            String icon = daily.getString("icon");
            String summary = daily.getString("summary");

            TextView summaryText = (TextView) view.findViewById(R.id.d_summary);
            summaryText.setText(summary);

            ImageView iconImg = (ImageView) view.findViewById(R.id.d_icon);
            iconImg.setImageResource(Sharable.getIconResourceId(icon));

            setChart(view);
        }catch(JSONException ex)
        {

        }
    }

    public void setChart(View view) {
        mChart = view.findViewById(R.id.chart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);

        List<Entry> valsComp1 = new ArrayList<Entry>();
        List<Entry> valsComp2 = new ArrayList<Entry>();
        valsComp1 = new ArrayList<Entry>();
        valsComp2 = new ArrayList<Entry>();
        try
        {
            JSONObject daily = Sharable.jsonObj.getJSONObject("daily");

            JSONArray jsonArr = (JSONArray)daily.get("data");

            for(int i = 0; i < jsonArr.length(); i++)
            {
                valsComp1.add(new Entry(i, jsonArr.getJSONObject(i).getInt("temperatureLow")));
                valsComp2.add(new Entry(i, jsonArr.getJSONObject(i).getInt("temperatureHigh")));
            }

        }catch(JSONException ex)
        {
            //
        }

        LineDataSet setComp1 = new LineDataSet(valsComp1, "Company 1");
        LineDataSet setComp2 = new LineDataSet(valsComp2, "Company 2");

        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp1.setHighlightEnabled(true);
        setComp1.setLineWidth(1);
        setComp1.setColor(Color.parseColor("#BB86FC"));
        setComp1.setCircleColor(Color.WHITE);
        setComp1.setCircleRadius(3);
        setComp1.setCircleHoleRadius(3);
        //setComp1.setDrawHighlightIndicators(true);
        //setComp1.setHighLightColor(Color.RED);
        setComp1.setLabel("Minimum Temperature");
        setComp2.setLabel("Maximum Temperature");

        Legend legend = mChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(Color.WHITE);
        legend.setTextSize(14f);

        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);
        setComp2.setHighlightEnabled(true);
        setComp2.setLineWidth(1);
        setComp2.setColor(Color.parseColor("#F4A71A"));
        setComp2.setCircleColor(Color.WHITE);
        setComp2.setCircleRadius(3);
        setComp2.setCircleHoleRadius(3);
        //setComp2.setDrawHighlightIndicators(true);
       // setComp2.setHighLightColor(Color.YELLOW);

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(setComp1);
        dataSets.add(setComp2);

        LineData lineData = new LineData(dataSets);

        mChart.getDescription().setTextSize(12);
        mChart.getXAxis().setPosition(XAxis.XAxisPosition.TOP);
        mChart.setGridBackgroundColor(Color.parseColor("#080707"));
        mChart.getAxisRight().setGranularity(5);
        mChart.getAxisLeft().setGranularity(5);

        mChart.getAxisLeft().setTextColor(Color.WHITE); // left y-axis
        mChart.getXAxis().setTextColor(Color.WHITE);
        mChart.getAxisRight().setTextColor(Color.WHITE);

        mChart.setData(lineData);
        mChart.invalidate();


    }
}