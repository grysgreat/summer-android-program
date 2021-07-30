package com.example.work_9;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private SensorManager mSensorManager=null;
    private Sensor mSensor=null;
    private TextView textView1=null;
    private TextView textView2=null;
    private TextView textView3=null;
    private Button button1=null;
    private Button button2=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView1 = (TextView) findViewById(R.id.textView1);
        /*获取系统服务（SENSOR_SERVICE）返回一个SensorManager对象*/
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        /*通过SensorManager获取相应的（接近传感器）Sensor类型对象*/
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        /*注册相应的SensorService*/
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                mSensorManager.registerListener(mSensorEventListener, mSensor
                        , SensorManager.SENSOR_DELAY_NORMAL);
            }
        });

        button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {
                mSensorManager.unregisterListener(mSensorEventListener, mSensor);
            }
        });
    }

    /*声明一个SensorEventListener对象用于侦听Sensor事件，并重载onSensorChanged方法*/
    private final SensorEventListener mSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            Log.e(TAG, "onSensorChanged: -----0------"+event.values[0]);

            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                /*接近传感器检测物体与听筒的距离，单位是厘米。*/
                //这里要注意，正常都是取第一位的值，但我碰到一个取第二位的
                float distance1 = event.values[0];

                textView1.setText("[0]距离："+String.valueOf(distance1) + "cm");

            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
