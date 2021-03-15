package com.example.dotgeneration;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class DetailPage extends AppCompatActivity {

    BarChart errorRate_chart, time_chart;
    ImageView back_img;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_details_page);
        back_img = findViewById(R.id.activity_record_details_page_time_back_img);
        errorRate_chart = findViewById(R.id.activity_record_details_page_errorRate_chart);
        time_chart = findViewById(R.id.activity_record_details_page_time_chart);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPage.this, HistoryPage.class);
                intent.putExtra("records", getIntent().getStringExtra("records"));
                intent.putExtra("nric", getIntent().getStringExtra("nric"));
                startActivity(intent);
            }
        });
        try {
            JSONObject record = new JSONObject(getIntent().getStringExtra("record"));
            initChart(record);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    void initChart(JSONObject record){
        ArrayList<BarEntry> timeEntries = new ArrayList<>();
        ArrayList<BarEntry> errorRateEntries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>(Arrays.asList("part A", "part B"));
        try {
            timeEntries.add(new BarEntry(Float.parseFloat(record.get("scoreA").toString()), 0));
            timeEntries.add(new BarEntry(Float.parseFloat(record.get("scoreB").toString()), 1));
            errorRateEntries.add(new BarEntry(Float.parseFloat(record.get("errorA").toString()), 0));
            errorRateEntries.add(new BarEntry(Float.parseFloat(record.get("errorB").toString()), 1));
            BarDataSet times = new BarDataSet(timeEntries, "Time Taken");
            BarDataSet errors = new BarDataSet(errorRateEntries, "Error Rate");
            BarData timeDataSet = new BarData(labels, times);
            BarData errorRateDataSet = new BarData(labels, errors);
            times.setColors(ColorTemplate.COLORFUL_COLORS);
            time_chart.setData(timeDataSet); errorRate_chart.setData(errorRateDataSet);
            time_chart.setDescription("Time taken (second)"); errorRate_chart.setDescription("Error rate (%)");
            time_chart.animateY(400);errorRate_chart.animateY(400);
            time_chart.setBorderColor(R.color.purple_200); errorRate_chart.setBorderColor(R.color.black);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
