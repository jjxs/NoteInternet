package com.example.administrator.note.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import com.example.administrator.note.R;
import com.example.administrator.note.util.Notify;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;
import org.json.JSONTokener;

import cn.bmob.push.PushConstants;

/**
 * Created on 17/8/24 14:20
 */

//TODO 集成：1.3、创建自定义的推送消息接收器，并在清单文件中注册
public class PushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        String message = "";
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            String msg = intent.getStringExtra(
                    PushConstants.EXTRA_PUSH_MESSAGE_STRING);

            try {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.getString("alert");
            }catch (Exception e){
                e.printStackTrace();
            }

            Notify notify = new Notify(context);
            notify.setId(msg.hashCode());
            notify.setTitle("圣旨到,爱妃听令~");
            notify.setText(message);
            notify.setAutoCancel(true);
            notify.setSmallIcon(R.mipmap.imgxiaoxi);
            notify.notification();
            notify.show();
        }

    }
}
