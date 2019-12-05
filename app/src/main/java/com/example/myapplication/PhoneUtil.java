package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PhoneUtil {
    private Context context;

    public PhoneUtil(Context context) {
        this.context = context;
    }

    /**
     * 获取所有联系人
     * @return
     */
    public List<PhoneDto> getPhone() {
        List<PhoneDto> phoneDtos = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse("content://com.android.contacts/raw_contacts"), null, null, null, null);
        while (cursor.moveToNext()) {
            PhoneDto phone = new PhoneDto();
            int id = cursor.getInt(cursor.getColumnIndex("_id"));
            String displayName = cursor.getString(cursor.getColumnIndex("display_name"));
            phone.setName(displayName);
            Cursor cursor2 = resolver.query(Uri.parse("content://com.android.contacts/raw_contacts/" + id + "/data"), null, null, null, null);
            while (cursor2.moveToNext()) {
                String type = cursor2.getString(cursor2.getColumnIndex("mimetype"));
                if ("vnd.android.cursor.item/organization".equals(type)) {
                    String data1 = cursor2.getString(cursor2.getColumnIndex("data1"));
                    phone.setTelPhone(data1);
                }
            }
            phoneDtos.add(phone);
        }
        return phoneDtos;
    }

    /**
     * 获取通话记录
     *
     */
    public List<PhoneDto> getContentCallLog() {
        @SuppressLint("MissingPermission")
        Cursor cursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        List<PhoneDto> phoneDtos = new ArrayList<>();
        while (cursor.moveToNext()) {
            PhoneDto phone=new PhoneDto();
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));  //姓名
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));  //号码
            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE)); //获取通话日期
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(dateLong));
            String time = new SimpleDateFormat("HH:mm").format(new Date(dateLong));
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));//获取通话时长，值为多少秒
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE)); //获取通话类型：1.呼入2.呼出3.未接
            String dayCurrent = new SimpleDateFormat("dd").format(new Date());
            String dayRecord = new SimpleDateFormat("dd").format(new Date(dateLong));
            phone.setName(name);
            phone.setTelPhone(number);
            phoneDtos.add(phone);
        }
        return phoneDtos;
    }

}
