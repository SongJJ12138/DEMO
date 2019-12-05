package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<PhoneDto> phoneDtos;
    private ListView lv_main_list;
    private ListView lv_main_list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check();
    }

    /**
     * 检查权限
     */
    private void check() {
        //判断是否有权限
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CONTACTS},201);
        }else{
            initViews();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==201){
            initViews();
        }else{
            return;
        }
    }

    private void initViews() {
        PhoneUtil phoneUtil = new PhoneUtil(this);
        phoneDtos = phoneUtil.getPhone();
        lv_main_list = (ListView) findViewById(R.id.lv_main_list);
        lv_main_list2 = (ListView) findViewById(R.id.lv_main_list2);
        MyAdapter myAdapter = new MyAdapter(phoneUtil.getPhone());
        MyAdapter myAdapter2 = new MyAdapter(phoneUtil.getContentCallLog());
        lv_main_list2.setAdapter(myAdapter2);
        lv_main_list.setAdapter(myAdapter);
        //给listview增加点击事件
        /*lv_main_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //拨打电话
                Intent intent = new Intent();
                intent.setAction("android.intent.action.CALL");
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("tel:"+phoneDtos.get(position).getTelPhone()));
                startActivity(intent);
            }
        });*/
    }
    //自定义适配器
    private class MyAdapter extends BaseAdapter {
        List<PhoneDto> lists;
        MyAdapter( List<PhoneDto> lists){
            this.lists=lists;
        }
        @Override
        public int getCount() {
            return lists.size();
        }

        @Override
        public Object getItem(int position) {
            return lists.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @SuppressLint("NewApi")
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            PhoneDto phoneDto = lists.get(position);
            LinearLayout linearLayout = new LinearLayout(MainActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            TextView tv_name = new TextView(MainActivity.this);
            tv_name.setId(View.generateViewId());
            tv_name.setLayoutParams(layoutParams);
            tv_name.setText(phoneDto.getName());
            TextView tv_num = new TextView(MainActivity.this);
            tv_num.setId(View.generateViewId());
            tv_num.setLayoutParams(layoutParams);
            tv_num.setText(phoneDto.getTelPhone());
            linearLayout.addView(tv_name);
            linearLayout.addView(tv_num);
            return linearLayout;
        }
    }
}
