package com.example.modulemain.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.modulecommon.common.ARouteContants;
import com.example.modulecommon.widget.dialog.DialogUIUtils;
import com.example.modulecommon.widget.dialog.adapter.TieAdapter;
import com.example.modulecommon.widget.dialog.bean.BuildBean;
import com.example.modulecommon.widget.dialog.bean.PopuBean;
import com.example.modulecommon.widget.dialog.bean.TieBean;
import com.example.modulecommon.widget.dialog.listener.DialogUIDateTimeSaveListener;
import com.example.modulecommon.widget.dialog.listener.DialogUIItemListener;
import com.example.modulecommon.widget.dialog.listener.DialogUIListener;
import com.example.modulecommon.widget.dialog.listener.TdataListener;
import com.example.modulecommon.widget.dialog.widget.DateSelectorWheelView;
import com.example.modulemain.R;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouteContants.ModuleMain.DIALOG_ACTIVITY)
public class DialogActivity extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;
    Context mContext;
    LinearLayout llMain;
    Button btnPopu, btnCustomAlert, btnCustomBbottomAlert, btnSystemAlert, btnLoading,
            btnMdLoading, btnMdAlert, btnTieAlert, btnBottomSheetCancel, btnCenterSheet,
            btnAlertInput, btnAlertMultichoose, btnAlertSinglechoose, btnMdBottomVertical,
            btnMdBottomHorizontal, btnToastTop, btnToastCenter, btnToast, btnSelectYmd,
            btnSelectYmdhm,btnSelectYmdhms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        mActivity = this;
        mContext = getApplication();

        initView();
        initListener();

        DialogUIUtils.init(mContext);
    }

    private void initView() {
        llMain = findViewById(R.id.ll_main);
        btnPopu = findViewById(R.id.btn_popu);
        btnCustomAlert = findViewById(R.id.btn_custom_alert);
        btnCustomBbottomAlert = findViewById(R.id.btn_custom_bottom_alert);
        btnSystemAlert = findViewById(R.id.btn_system_alert);
        btnLoading = findViewById(R.id.btn_loading);
        btnMdLoading = findViewById(R.id.btn_md_loading);
        btnMdAlert = findViewById(R.id.btn_md_alert);
        btnTieAlert = findViewById(R.id.btn_tie_alert);
        btnBottomSheetCancel = findViewById(R.id.btn_bottom_sheet_cancel);
        btnCenterSheet = findViewById(R.id.btn_center_sheet);
        btnAlertInput = findViewById(R.id.btn_alert_input);
        btnAlertMultichoose = findViewById(R.id.btn_alert_multichoose);
        btnAlertSinglechoose = findViewById(R.id.btn_alert_singlechoose);
        btnMdBottomVertical = findViewById(R.id.btn_md_bottom_vertical);
        btnMdBottomHorizontal = findViewById(R.id.btn_md_bottom_horizontal);
        btnToastTop = findViewById(R.id.btn_toast_top);
        btnToastCenter = findViewById(R.id.btn_toast_center);
        btnToast = findViewById(R.id.btn_toast);
        btnSelectYmd = findViewById(R.id.btn_select_ymd);
        btnSelectYmdhm = findViewById(R.id.btn_select_ymdhm);
        btnSelectYmdhms = findViewById(R.id.btn_select_ymdhms);
    }

    private void initListener() {
        btnPopu.setOnClickListener(this);
        btnCustomAlert.setOnClickListener(this);
        btnCustomBbottomAlert.setOnClickListener(this);
        btnSystemAlert.setOnClickListener(this);
        btnLoading.setOnClickListener(this);
        btnMdLoading.setOnClickListener(this);
        btnMdAlert.setOnClickListener(this);
        btnTieAlert.setOnClickListener(this);
        btnBottomSheetCancel.setOnClickListener(this);
        btnCenterSheet.setOnClickListener(this);
        btnAlertInput.setOnClickListener(this);
        btnAlertMultichoose.setOnClickListener(this);
        btnAlertSinglechoose.setOnClickListener(this);
        btnMdBottomVertical.setOnClickListener(this);
        btnMdBottomHorizontal.setOnClickListener(this);
        btnToastTop.setOnClickListener(this);
        btnToastCenter.setOnClickListener(this);
        btnToast.setOnClickListener(this);
        btnSelectYmd.setOnClickListener(this);
        btnSelectYmdhm.setOnClickListener(this);
        btnSelectYmdhms.setOnClickListener(this);
    }

    String msg = "别总是来日方长，这世上挥手之间的都是人走茶凉。";

    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_popu) {
            DialogUIUtils.showPopuWindow(mContext, LinearLayout.LayoutParams.MATCH_PARENT, 4, btnPopu, new TdataListener() {
                @Override
                public void initPupoData(List<PopuBean> lists) {
                    for (int i = 0; i < 5; i++) {
                        PopuBean popu = new PopuBean();
                        popu.setTitle("item" + i);
                        popu.setId(i);
                        lists.add(popu);
                    }
                }

                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position) {
                }
            });
        } else if (i == R.id.btn_custom_alert) {
            View rootView = View.inflate(mActivity, R.layout.custom_dialog_layout, null);
            final Dialog dialog = DialogUIUtils.showCustomAlert(this, rootView, Gravity.CENTER, true, false).show();
            rootView.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUIUtils.dismiss(dialog);
                }
            });
        } else if (i == R.id.btn_custom_bottom_alert) {
            View rootViewB = View.inflate(mActivity, R.layout.custom_dialog_bottom_layout, null);
            DialogUIUtils.showCustomBottomAlert(this, rootViewB).show();
        } else if (i == R.id.btn_system_alert) {
            new AlertDialog
                    .Builder(mActivity)
                    .setTitle("标题")
                    .setMessage("这是内容")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create()
                    .show();
        } else if (i == R.id.btn_loading) {
            DialogUIUtils.showLoading(this, "加载中...", false, true, true, true).show();
        } else if (i == R.id.btn_md_loading) {
            DialogUIUtils.showMdLoading(this, "加载中...", true, true, true, true).show();
        } else if (i == R.id.btn_alert_multichoose) {
            String[] words = new String[]{"1", "2", "3"};
            boolean[] choseDefault = new boolean[]{false, false, false};
            DialogUIUtils.showMdMultiChoose(mActivity, "标题", words, choseDefault, new DialogUIListener() {
                @Override
                public void onPositive() {

                }

                @Override
                public void onNegative() {

                }
            }).show();
        } else if (i == R.id.btn_alert_singlechoose) {
            String[] words2 = new String[]{"1", "2", "3"};
            DialogUIUtils.showSingleChoose(mActivity, "单选", 0, words2, new DialogUIItemListener() {
                @Override
                public void onItemClick(CharSequence text, int position) {
                    showToast(text + "--" + position);
                }
            }).show();
        } else if (i == R.id.btn_md_alert) {
            DialogUIUtils.showMdAlert(mActivity, "标题", msg, new DialogUIListener() {
                @Override
                public void onPositive() {
                    showToast("onPositive");
                }

                @Override
                public void onNegative() {
                    showToast("onNegative");
                }

            }).show();
        } else if (i == R.id.btn_tie_alert) {
            DialogUIUtils.showAlert(mActivity, "标题", msg, "", "", "确定", "", true, true, true, new DialogUIListener() {
                @Override
                public void onPositive() {
                    showToast("onPositive");
                }

                @Override
                public void onNegative() {
                    showToast("onNegative");
                }

            }).show();
        } else if (i == R.id.btn_alert_input) {
            DialogUIUtils.showAlert(mActivity, "登录", "", "请输入用户名", "请输入密码", "登录", "取消", false, true, true, new DialogUIListener() {
                @Override
                public void onPositive() {

                }

                @Override
                public void onNegative() {

                }

                @Override
                public void onGetInput(CharSequence input1, CharSequence input2) {
                    super.onGetInput(input1, input2);
                    showToast("input1:" + input1 + "--input2:" + input2);
                }
            }).show();
        } else if (i == R.id.btn_center_sheet) {
            List<TieBean> strings = new ArrayList<TieBean>();
            strings.add(new TieBean("1"));
            strings.add(new TieBean("2"));
            strings.add(new TieBean("3"));
            DialogUIUtils.showSheet(mActivity, strings, "", Gravity.CENTER, true, true, new DialogUIItemListener() {
                @Override
                public void onItemClick(CharSequence text, int position) {
                    showToast(text);
                }
            }).show();
        } else if (i == R.id.btn_bottom_sheet_cancel) {
            List<TieBean> strings = new ArrayList<TieBean>();
            strings.add(new TieBean("1"));
            strings.add(new TieBean("2"));
            strings.add(new TieBean("3"));
            DialogUIUtils.showSheet(mActivity, strings, "取消", Gravity.BOTTOM, true, true, new DialogUIItemListener() {
                @Override
                public void onItemClick(CharSequence text, int position) {
                    showToast(text + "---" + position);
                }

                @Override
                public void onBottomBtnClick() {
                    showToast("取消");
                }
            }).show();
        } else if (i == R.id.btn_md_bottom_vertical) {
            List<TieBean> datas2 = new ArrayList<TieBean>();
            datas2.add(new TieBean("1"));
            datas2.add(new TieBean("2"));
            datas2.add(new TieBean("3"));
            datas2.add(new TieBean("4"));
            datas2.add(new TieBean("5"));
            datas2.add(new TieBean("6"));
            TieAdapter adapter = new TieAdapter(mContext, datas2, true);
            BuildBean buildBean = DialogUIUtils.showMdBottomSheet(mActivity, true, "", datas2, 0, new DialogUIItemListener() {
                @Override
                public void onItemClick(CharSequence text, int position) {
                    showToast(text + "---" + position);
                }
            });
            buildBean.mAdapter = adapter;
            buildBean.show();
        } else if (i == R.id.btn_md_bottom_horizontal) {
            List<TieBean> datas3 = new ArrayList<TieBean>();
            datas3.add(new TieBean("1"));
            datas3.add(new TieBean("2"));
            datas3.add(new TieBean("3"));
            datas3.add(new TieBean("4"));
            datas3.add(new TieBean("5"));
            datas3.add(new TieBean("6"));
            DialogUIUtils.showMdBottomSheet(mActivity, false, "标题", datas3, 4, new DialogUIItemListener() {
                @Override
                public void onItemClick(CharSequence text, int position) {
                    showToast(text + "---" + position);
                }
            }).show();
        } else if (i == R.id.btn_toast_top) {
            DialogUIUtils.showToastTop("上部的Toast弹出方式");
        } else if (i == R.id.btn_toast_center) {
            DialogUIUtils.showToastCenter("中部的Toast弹出方式");
        } else if (i == R.id.btn_toast) {
            DialogUIUtils.showToast("默认的Toast弹出方式");
        } else if (i == R.id.btn_select_ymd) {
            DialogUIUtils.showDatePick(mActivity, Gravity.CENTER, "选择日期", System.currentTimeMillis() + 60000, DateSelectorWheelView.TYPE_YYYYMMDD, 0, new DialogUIDateTimeSaveListener() {
                @Override
                public void onSaveSelectedDate(int tag, String selectedDate) {

                }
            }).show();
        } else if (i == R.id.btn_select_ymdhm) {
            DialogUIUtils.showDatePick(mActivity, Gravity.CENTER, "选择日期", System.currentTimeMillis() + 60000, DateSelectorWheelView.TYPE_YYYYMMDDHHMM, 0, new DialogUIDateTimeSaveListener() {
                @Override
                public void onSaveSelectedDate(int tag, String selectedDate) {

                }
            }).show();
        } else if (i == R.id.btn_select_ymdhms) {
            DialogUIUtils.showDatePick(mActivity, Gravity.BOTTOM, "选择日期", System.currentTimeMillis() + 60000, DateSelectorWheelView.TYPE_YYYYMMDDHHMMSS, 0, new DialogUIDateTimeSaveListener() {
                @Override
                public void onSaveSelectedDate(int tag, String selectedDate) {

                }
            }).show();
        }
    }

    public void showToast(CharSequence msg) {
        DialogUIUtils.showToastLong(msg.toString());
    }
}
