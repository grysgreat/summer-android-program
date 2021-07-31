package com.example.work_6;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.SimpleAdapter;
import android.widget.ViewSwitcher.ViewFactory;

public class MainActivity extends Activity {

    //定义组件
    private GridView gredView = null;
    private ImageSwitcher imageSwt =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取布局组件
        gredView = (GridView) findViewById(R.id.grid);
        imageSwt = (ImageSwitcher) findViewById(R.id.imageSwitch);
        //图片
        final int images[] = new int[]{
                R.drawable.name01,R.drawable.name02,R.drawable.name03,R.drawable.name04,
                R.drawable.name05,R.drawable.name06,R.drawable.name07,R.drawable.name08,
                R.drawable.name09,R.drawable.name10,R.drawable.name11,R.drawable.name12,
                R.drawable.name13,R.drawable.name14,R.drawable.name15,R.drawable.name16
        };
        //创建list集合，存储图片
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        for(int i=0;i<images.length;i++){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("img", images[i]);
            list.add(map);
        }
        //设置图片更换效果
        //淡入效果
        imageSwt.setInAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
        //淡出效果
        imageSwt.setOutAnimation(AnimationUtils.loadAnimation(this,android.R.anim.fade_out));
        //设置图片转换效果
        imageSwt.setFactory(new ViewFactory() {

            @Override
            public View makeView() {
                ImageView imageView = new ImageView(MainActivity.this);
                imageView.setLayoutParams(new ImageSwitcher.LayoutParams(300, 300));
                imageView.setScaleType(ScaleType.FIT_CENTER);
                return imageView;
            }
        });
        //设置适配器
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.img, new String[]{"img"}, new int[]{R.id.img});
        //适配器
        gredView.setAdapter(simpleAdapter);
        //选中事件监听
        gredView.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.i("onItemSelected", "onItemSelected");
                imageSwt.setImageResource(images[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        //点击事件监听
        gredView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.i("onItemClick", "onItemClick");
                imageSwt.setImageResource(images[position]);

            }
        });
    }


}
