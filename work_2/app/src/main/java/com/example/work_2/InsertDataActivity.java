package com.example.work_2;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InsertDataActivity extends Activity{
    private String name;
    private String phone;
    private  String email ;
    private SQLiteDatabase db;
    private EditText et_name;
    private EditText et_phone;
    private EditText et_email;
    private Button bt2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updata_data);
        bt2=(Button)findViewById(R.id.save) ;
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(v);
                finish();
            }
        });
        init();
    }
    private  void init(){
        db=MainActivity.getDb();
        name="";
        phone="";
        email = "";
        et_name=(EditText)findViewById(R.id.et_name);
        et_phone=(EditText)findViewById(R.id.et_phone);
        et_email = (EditText)findViewById(R.id.et_email);
    }

    public void save (View v){
        name=et_name.getText().toString();
        phone=et_phone.getText().toString();
        email = et_email.getText().toString();
        ContentValues cv=new ContentValues();
        cv.put("name",name);
        cv.put("phone",phone);
        cv.put("email",email);
        db.insert("contacter", "", cv);
        Toast.makeText(InsertDataActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
    }
}
