package com.example.modulemain.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.example.modulecommon.common.ARouteContants;
import com.example.modulemain.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Route(path = ARouteContants.ModuleMain.JSON_ACTIVITY)
public class JsonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);

        TextView textView = findViewById(R.id.tv_json);

        List<String> urls = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            urls.add("type" + i);
        }
        textView.setText(jsonToString(urls).toString());
    }

    private JSONObject jsonToString(List<String> urls) {
        JSONObject jsonObjectUp = new JSONObject();
        try {
            jsonObjectUp.put("url", urls);
            jsonObjectUp.put("type", "type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObjectUp;
    }
}
