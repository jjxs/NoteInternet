package com.example.administrator.note.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.administrator.note.NoteSaveDao;
import com.example.administrator.note.R;
import com.example.administrator.note.bean.NoteBean;
import com.jaeger.library.StatusBarUtil;
import com.wuhenzhizao.titlebar.utils.AppUtils;
import com.wuhenzhizao.titlebar.utils.KeyboardConflictCompat;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditWenBenActivity extends AppCompatActivity {

    private NoteSaveDao instance;
    private EditText note_content;
    private int noteid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wen_ben);

        initView();

        initData();
    }

    private void initData() {
        noteid = getIntent().getIntExtra("id", -1);
        if (noteid !=-1){
            //展示数据
            showNotedata(noteid);
        }
    }

    private void showNotedata(int id) {
        NoteBean wenben = instance.getWenben(id);
        String content = wenben.getContent();
        note_content.setText(content);
    }

    @Override
    public void onAttachedToWindow() {
        KeyboardConflictCompat.assistWindow(getWindow());
        AppUtils.StatusBarLightMode(getWindow());
        AppUtils.transparencyBar(getWindow());
        super.onAttachedToWindow();
    }

    private void initView() {
        StatusBarUtil.setColor(EditWenBenActivity.this, getResources().getColor(R.color.colorAccent),StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
        com.wuhenzhizao.titlebar.widget.CommonTitleBar titlebar = (CommonTitleBar) findViewById(R.id.titlebar);
        note_content = (EditText) findViewById(R.id.note_content);
        titlebar.setBackgroundColor(getResources().getColor(R.color.title));
        //titlebar.setBackgroundResource(R.drawable.bg);
        instance = NoteSaveDao.getInstance(this);


        titlebar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_BUTTON) {
                    //返回上个页面
                    back();
                }
                if (action== CommonTitleBar.ACTION_RIGHT_BUTTON){
                    //添加数据库,并返回

                    if (noteid != -1){
                        updata();
                    }else {
                        save();
                    }
                    //返回
                    back();
                }
            }
        });
    }

    

    /**
     * 获得当前时间
     */

    private String getTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm E");
        Date date = new Date();
        String time = simpleDateFormat.format(date);
        return time;
    }

    /**
     * 更新数据
     */
    private void updata() {
        String time = getTime();
        String content = note_content.getText().toString();
        instance.update(noteid,content,time);
    }
    /**
     * 保存返回
     */
    private void save() {

        String time = getTime();
        String content = note_content.getText().toString();
        instance.insert(time,content);
    }

    /**
     * 返回
     */
    private void back() {
        Intent i = new Intent(EditWenBenActivity.this,MainActivity.class);
        startActivity(i);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        back();
    }
}
