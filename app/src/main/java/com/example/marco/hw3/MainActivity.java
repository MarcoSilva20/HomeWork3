package com.example.marco.hw3;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    static public SensorManager mSensorManager;
    private ArrayList<String> answers;
    private ImageView ball;
    private TextView label;
    private Sensor sensor;
    public static Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ball = (ImageView) findViewById(R.id.ball1);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        label = (TextView) findViewById(R.id.label);
        answers = loadAnswers();
    }

    private ArrayList<String> loadAnswers() {
        ArrayList<String> anw = new ArrayList<>();
        String [] tab = getResources().getStringArray(R.array.answers);
        for (String a:tab) {
            anw.add(a);
        }
        return anw;
    }

    long timestamp = 0;

    @Override
    public void onSensorChanged(SensorEvent event) {
            int val = (int)event.values[0];
            long h =  (new Date()).getTime() + (event.timestamp - System.nanoTime()) / 1000000L;
            if(val<70) timestamp = h;
            else{
                long dif = (event.timestamp - timestamp) % 20;
                if(event.sensor.getType()==Sensor.TYPE_LIGHT){
                    showAnswer(getAnswer((int)dif),false);
                }
                }
    }



    private void showAnswer(String answer, boolean b) {
        label.setText(answer);
    }

    private String getAnswer(int val) {
        int randomint = random.nextInt(answers.size());
        return answers.get(val);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,sensor,500000);
        showAnswer("",false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this,sensor);
    }
}
