package com.jack.weatherlinerchartview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<Float> YValueMax=null;
    public List<Float> YValueMin=null;
    WeatherLineChart weatherlinerchart=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
    }

    public void initData(){
        YValueMax=new ArrayList<>();
        YValueMin=new ArrayList<>();
        weatherlinerchart=(WeatherLineChart)findViewById(R.id.mylinechart);

        YValueMax.add(14f);
        YValueMax.add(15f);
        YValueMax.add(16f);
        YValueMax.add(17f);
        YValueMax.add(9f);
        YValueMax.add(9f);

        YValueMin.add(7f);
        YValueMin.add(5f);
        YValueMin.add(9f);
        YValueMin.add(10f);
        YValueMin.add(3f);
        YValueMin.add(2f);

        weatherlinerchart.setYValueMax(YValueMax);
        weatherlinerchart.setYValueMin(YValueMin);
        weatherlinerchart.invalidate();
    }

}
