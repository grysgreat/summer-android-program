package com.example.work_2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedList;



public class MainActivity extends AppCompatActivity {
    private static SQLiteDatabase db;
    private int rawCount;//表的行数
    private ListView lv;
    private Cursor cursor;
    private ListAdapter adapter;
    private LinkedList<com.example.work_2.ContacterUser> list;
    private Button bt1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        bt1=(Button)findViewById(R.id.button1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertNewContacter(v);
            }
        });
    }

    protected void init() {
        com.example.work_2.DBOpenHelper oh = new com.example.work_2.DBOpenHelper(MainActivity.this, "contacter.db", null, 1);
        db = oh.getWritableDatabase();
        lv=(ListView)findViewById(R.id.lv);
        list=new LinkedList<com.example.work_2.ContacterUser>();
        cursor = db.query("contacter", new String[]{"name", "phone", "_id","email"}, null, null, null, null, "name");
        while(cursor.moveToNext()) {
            com.example.work_2.ContacterUser contacter=new com.example.work_2.ContacterUser(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3)
            );
            list.add(contacter);
        }
        disContacter();
    }

    public void disContacter() {
        cursor.moveToFirst();
        rawCount=cursor.getCount();

        adapter=new BaseAdapter() {
            @Override
            public int getCount() {
                return list.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final com.example.work_2.ContacterUser contacter=list.get(position);
                LinearLayout layout=new LinearLayout(MainActivity.this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                TextView tv= new TextView(MainActivity.this);
                Button bt=new Button(MainActivity.this);
                tv.setText(contacter.toString());
                tv.setTextSize(18);
                tv.setLongClickable(true);
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contacter.getPhone()));
                        startActivity(intent);
                    }
                });
                tv.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Intent intent = new Intent(MainActivity.this, com.example.work_2.UpdataDataActivity.class);
                        intent.putExtra("name", contacter.getName());
                        intent.putExtra("phone", contacter.getPhone());
                        intent.putExtra("_id", contacter.getId());
                        intent.putExtra("email",contacter.getEmail());
                        startActivity(intent);
                        return false;
                    }
                });
                bt.setText("删除");
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.delete("contacter", "_id=?",
                                new String[]{contacter.getId()});
                        init();
                    }
                });
                layout.addView(tv);
                layout.addView(bt);
                return layout;
            }
        };
        lv.setAdapter(adapter);
    }

    public void insertNewContacter(View v){
        Intent intent=new Intent(MainActivity.this, com.example.work_2.InsertDataActivity.class);
        startActivity(intent);
        disContacter();
    }

    public static SQLiteDatabase getDb() {
        return db;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    @Override
    protected void onRestart() {
        init();
        super.onRestart();
    }
}