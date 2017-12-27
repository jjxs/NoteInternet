package com.example.administrator.note.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.example.administrator.note.Adapter.ItemDragAdapter;
import com.example.administrator.note.NoteSaveDao;
import com.example.administrator.note.R;
import com.example.administrator.note.bean.NoteBean;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.jaeger.library.StatusBarUtil;
import com.ljs.lovelytoast.LovelyToast;
import com.melnykov.fab.ScrollDirectionListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private NoteSaveDao minstance;
    private List<NoteBean> noteDataList = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private ItemDragAdapter mAdapter;
    private ItemDragAndSwipeCallback itemDragAndSwipeCallback;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    /**
     * 加载数据
     */
    private void initData() {
        mContext = this;
        minstance = NoteSaveDao.getInstance(mContext);
        minstance.findAll(noteDataList);
    }

    /**
     * 加载页面
     */
    private void initView() {
        StatusBarUtil.setColor(MainActivity.this, getResources().getColor(R.color.ztlan), StatusBarUtil.DEFAULT_STATUS_BAR_ALPHA);
        setReView();
        FloatingActionButtonMethod();
    }

    /**
     * 设置recyclerView
     */
    private void setReView() {
        View errayView;
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_list);
        //优化:setHasFixedSize 的作用就是确保尺寸是通过用户输入从而确保RecyclerView的尺寸是一个常数。RecyclerView 的Item宽或者高不会变。每一个Item添加或者删除都不会变。
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        errayView = getLayoutInflater().inflate(R.layout.empty_view, (ViewGroup) mRecyclerView.getParent(), false);

        //滑动删除
        OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
            @Override
            public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
            }

            @Override
            public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
                int id = noteDataList.get(pos).getId();
                NoteSaveDao.getInstance(MainActivity.this).delete(id);
            }

            @Override
            public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
                canvas.drawColor(ContextCompat.getColor(MainActivity.this, R.color.color_light_blue));
            }
        };

        mAdapter = new ItemDragAdapter(noteDataList);
        itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        itemDragAndSwipeCallback.setSwipeMoveFlags(ItemTouchHelper.START | ItemTouchHelper.END);

        mAdapter.enableSwipeItem();
        mAdapter.setOnItemSwipeListener(onItemSwipeListener);
        //mAdapter.enableDragItem(mItemTouchHelper);
        mRecyclerView.setAdapter(mAdapter);

        // 设置空列表
        mAdapter.setEmptyView(errayView);

        //设置不显示动画数量
        mAdapter.setNotDoAnimationCount(8);
        //设置动画
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);

        //设置点击事件
        ItemOnClick();
    }

    /**
     * 单击事件,长按事件
     */
    public void ItemOnClick() {
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                TextView tv = (TextView) adapter.getViewByPosition(mRecyclerView, position, R.id.note_id);
                String noteId = tv.getText().toString().trim();
                Intent i = new Intent(MainActivity.this, EditWenBenActivity.class);
                i.putExtra("id", Integer.parseInt(noteId));
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final BaseQuickAdapter adapter, View view, final int position) {
                final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(MainActivity.this);
                dialogBuilder
                        .withTitle("爱的抉择")
                        .withTitleColor("#FFFFFF")
                        .withDividerColor("#11000000")
                        .withMessage("爱妃当真要删除?")
                        .withMessageColor("#FFFFFFFF")
                        .withDialogColor("#FFE74C3C")
                        .withIcon(getResources().getDrawable(R.mipmap.preview))
                        .withDuration(700)
                        .withEffect(Effectstype.Fall)
                        .withButton1Text("残忍删除")
                        .withButton2Text("手残")
                        .isCancelableOnTouchOutside(true)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int id = noteDataList.get(position).getId();
                                NoteSaveDao.getInstance(MainActivity.this).delete(id);
                                noteDataList.remove(position);
                                mAdapter.notifyItemRemoved(position);
                                dialogBuilder.cancel();
                                dialogBuilder.dismiss();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialogBuilder.cancel();
                                dialogBuilder.dismiss();
                            }
                        })
                        .show();
                return false;
            }
        });
    }

    /**
     * 设置FloatingActionButton
     */
    public void FloatingActionButtonMethod() {

        FloatingActionButton action_add_text = (FloatingActionButton) findViewById(R.id.action_add_text);
        action_add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditWenBenActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FloatingActionButton action_setting = (FloatingActionButton) findViewById(R.id.action_setting);

        action_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, SettingsActivity.class);
//                startActivity(intent);
//                finish();
            }
        });


    }
}
