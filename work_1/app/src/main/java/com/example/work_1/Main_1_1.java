package com.example.work_1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;




public class Main_1_1 extends AppCompatActivity {

    TextView txt;
    int count =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1_1);

        txt = (TextView) findViewById(R.id.textinput_counter) ;


        Intent intent =getIntent();
        count = intent.getIntExtra("data",0)+1;

        txt.setText(String.valueOf(count));
        Button bt = (Button) findViewById(R.id.bt1);
        //1.匿名内部类
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                Intent intent=new Intent(Main_1_1.this, com.example.work_1.Main_1_2.class);
                intent.putExtra("data", count);
                startActivity(intent);
            }
        });
    }
}
