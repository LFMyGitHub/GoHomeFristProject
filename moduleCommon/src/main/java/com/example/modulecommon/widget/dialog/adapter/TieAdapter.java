package com.example.modulecommon.widget.dialog.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.modulecommon.R;
import com.example.modulecommon.widget.dialog.bean.TieBean;
import com.example.modulecommon.widget.dialog.holder.SuperItemHolder;
import com.example.modulecommon.widget.dialog.holder.TieItemHolder;

import java.util.List;

/**
 * 编码器适配器
 */
public class TieAdapter extends SuperAdapter<TieBean> {

    private boolean isItemType;

    public TieAdapter(Context mContext, List<TieBean> list, boolean isItemType) {
        super(mContext, list);
        this.isItemType = isItemType;
    }

    public TieAdapter(Context mContext, List<TieBean> list) {
        super(mContext, list);
    }

    @Override
    public SuperItemHolder getItemHolder(ViewGroup parent, int viewType) {
        return new TieItemHolder(mContext, mListener, LayoutInflater.from(mContext).
                inflate(R.layout.dialogui_holder_item_tie, parent, false));
    }

    /**
     * 1top 2midle 3bottom 4all
     */
    protected int countPosition(int position) {
        if (isItemType) {
            if (mDatas.size() == 1) {
                return 4;
            }
            if (mDatas.size() > 1) {
                if (position == 0) {
                    return 1;
                } else if (position == mDatas.size() - 1) {
                    return 3;
                }
            }
            return 2;
        } else {
            return super.countPosition(position);
        }
    }
}
