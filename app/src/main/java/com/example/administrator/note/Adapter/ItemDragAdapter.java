package com.example.administrator.note.Adapter;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.note.R;
import com.example.administrator.note.bean.NoteBean;
import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */

public class ItemDragAdapter extends BaseItemDraggableAdapter<NoteBean,BaseViewHolder>{

    public ItemDragAdapter(List<NoteBean> data) {
        super(R.layout.lv_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NoteBean item) {
        if (item == null) {
            return;
        }
        helper.setText(R.id.text_top,item.getContent())
        .setText(R.id.text_bottom,item.getTime())
        .setText(R.id.note_id,item.getId()+"");
    }

}
