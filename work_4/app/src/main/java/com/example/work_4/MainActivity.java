package com.example.work_4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1=(TextView) findViewById(R.id.ttv1);
        tv2=(TextView) findViewById(R.id.ttv2);
        tv3=(TextView) findViewById(R.id.ttv3);
        tv4=(TextView) findViewById(R.id.ttv4);


    }

    //充气Menu，即把菜单显示出来
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    //处理菜单上的点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //mytextView.setVisibility(View.INVISIBLE)

        switch (item.getItemId()){
            case R.id.favo_p1:
                if(tv1.getVisibility()!=View.VISIBLE){
                    tv1.setVisibility(View.VISIBLE);
                }else {
                    tv1.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.favo_p2:
                if(tv2.getVisibility()!=View.VISIBLE){
                    tv2.setVisibility(View.VISIBLE);
                }else {
                    tv2.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.favo_p3:
                if(tv3.getVisibility()!=View.VISIBLE){
                    tv3.setVisibility(View.VISIBLE);
                }else {
                    tv3.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.favo_p4:
                if(tv4.getVisibility()!=View.VISIBLE){
                    tv4.setVisibility(View.VISIBLE);
                }else {
                    tv4.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.favo_p5:
                tv1.setVisibility(View.INVISIBLE);
                tv2.setVisibility(View.INVISIBLE);
                tv3.setVisibility(View.INVISIBLE);
                tv4.setVisibility(View.INVISIBLE);
        }
        return true;
    }

}