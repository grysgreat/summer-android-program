package com.example.work_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.support.v7.app.AppCompatActivity;
public class Main_1_2 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1_2);

        Button bt = (Button) findViewById(R.id.bt1);
        //1.匿名内部类

        Intent intent =getIntent();

        int msg=intent.getIntExtra("data",0);

        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent=new Intent(Main_1_2.this,Main_1_1.class);

                intent.putExtra("data", msg);
                startActivity(intent);
            }
        });
    }
}
