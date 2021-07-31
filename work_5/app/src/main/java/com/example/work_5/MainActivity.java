package com.example.work_5;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TimePicker timePicker ;
    DatePicker datePicker ;
    Button bt1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt1 = (Button)findViewById(R.id.select);
        timePicker =(TimePicker)findViewById(R.id.tiem1);
        timePicker.setIs24HourView(true);
        datePicker = (DatePicker)findViewById(R.id.date1);


        bt1.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String out = "Time selected:"+(timePicker.getHour()) + ":" + timePicker.getMinute();
                out += "Data selected"+datePicker.getYear()+"/"+datePicker.getMonth()+"/"+datePicker.getDayOfMonth();

                Toast.makeText(getBaseContext(),out,Toast.LENGTH_SHORT).show();

            }
        });
    }
}