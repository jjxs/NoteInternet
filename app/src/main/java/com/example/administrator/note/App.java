package com.example.administrator.note;

import android.app.Application;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobInstallationManager;
import cn.bmob.v3.InstallationListener;
import cn.bmob.v3.exception.BmobException;

/**
 * Created on 17/8/24 13:18
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //CrashHandler.getInstance().init(this);
        /**
         * 初始化日志工具
         */
        Logger.addLogAdapter(new AndroidLogAdapter());
        //TODO 集成：1.4、初始化数据服务SDK、保存设备信息并启动推送服务
        /**
         * 初始化比目数据SDK
         */
        Bmob.initialize(this, "c0e9ab8b184a51e8664c3bda4abed500");
        /**
         * 保存设备信息，用于推送功能
         */
        BmobInstallationManager.getInstance().initialize(new InstallationListener<BmobInstallation>() {
            @Override
            public void done(BmobInstallation bmobInstallation, BmobException e) {
                if (e == null) {
                    Logger.i(bmobInstallation.getObjectId() + "-" + bmobInstallation.getInstallationId());
                } else {
                    Logger.e(e.getMessage());
                }
            }
        });



        /**
         * 启动推送服务
         */
        BmobPush.startWork(this);
    }
}
