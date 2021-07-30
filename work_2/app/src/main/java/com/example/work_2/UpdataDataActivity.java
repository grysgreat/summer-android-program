package com.example.work_2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdataDataActivity extends Activity {
    private String name;
    private String phone;
    private  String email ;
    private String _id;
    private EditText et_name;
    private EditText et_phone;
    private EditText et_email;
    private SQLiteDatabase db;
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
    private void init(){
        db=MainActivity.getDb();
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        et_name=(EditText)findViewById(R.id.et_name);
        et_phone=(EditText)findViewById(R.id.et_phone);
        et_email = (EditText)findViewById(R.id.et_email);
        name= (String)b.get("name");
        phone= (String)b.get("phone");
        email = (String)b.get("email");
        _id=(String)b.get("_id");
        et_name.setText(name);
        et_phone.setText(phone);
        et_email.setText(email);
    }
    public void save(View v){
        ContentValues cv=new ContentValues();
        cv.put("name",et_name.getText().toString());
        cv.put("phone",et_phone.getText().toString());
        cv.put("email",et_email.getText().toString());
        db.update("contacter", cv, "_id=?", new String[]{_id});
        Toast.makeText(UpdataDataActivity.this,"更改成功",Toast.LENGTH_SHORT).show();
    }
}
