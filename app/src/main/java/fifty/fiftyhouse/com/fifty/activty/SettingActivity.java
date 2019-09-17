package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.SettingAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class SettingActivity extends AppCompatActivity {

    View ui_Setting_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    RecyclerView rv_Setting_List;
    Context mContext;

    SettingAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mContext = getApplicationContext();

        SharedPreferences sf = getSharedPreferences("userFile",MODE_PRIVATE);

        TKManager.getInstance().AppSettingData.put("알림", true);
        TKManager.getInstance().AppSettingData.put("소리", true);
        TKManager.getInstance().AppSettingData.put("진동", true);

        Set EntrySet = TKManager.getInstance().AppSettingData.entrySet();
        Iterator iterator = EntrySet.iterator();


        while (iterator.hasNext()) {

            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            Boolean value = sf.getBoolean(key,true);
            TKManager.getInstance().AppSettingData.put(key, value);
        }

        ui_Setting_TopBar = findViewById(R.id.ui_Setting_TopBar);
        tv_TopBar_Title = ui_Setting_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_Setting_TopBar.findViewById(R.id.iv_TopBar_Back);
        rv_Setting_List = findViewById(R.id.rv_Setting_List);

        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_SETTING));
        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView()
    {
        mAdapter = new SettingAdapter(getApplicationContext());
        mAdapter.setHasStableIds(true);

        rv_Setting_List.setAdapter(mAdapter);
        rv_Setting_List.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv_Setting_List.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_Setting_List, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {
                // 현재 계정 밖에 없음
               // startActivity(new Intent(getApplicationContext(), SettingAccountActivity.class));

                Set tempKey = TKManager.getInstance().AppSettingData.keySet();
                final List array = new ArrayList(tempKey);
                boolean tempData = TKManager.getInstance().AppSettingData.get(array.get(position));
                tempData = !tempData;

                SharedPreferences sharedPreferences = getSharedPreferences("userFile",MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(array.get(position).toString(),tempData);
                editor.commit();

                TKManager.getInstance().AppSettingData.put(array.get(position).toString(), tempData);

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
