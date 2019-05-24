package com.example.modulecommon.widget.dialog.listener;

import com.example.modulecommon.widget.dialog.widget.WheelView;

/**
 * wheel改变监听事件
 */
public interface OnWheelChangedListener {
    /**
     * Callback method to be invoked when current item changed
     *
     * @param wheel    the wheel view whose state has changed
     * @param oldValue the old value of current item
     * @param newValue the new value of current item
     */
    void onChanged(WheelView wheel, int oldValue, int newValue);
}
