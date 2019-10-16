package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class SettingActivity extends AppCompatActivity {

    View ui_Setting_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    TextView tv_Setting_Shop, tv_Setting_Alarm, tv_Setting_Sound, tv_Setting_Vibration, tv_Setting_Block, tv_Setting_Version,
            tv_Setting_Version_View, tv_Setting_Terms_1,
            tv_Setting_Terms_2, tv_Setting_Terms_3, tv_Setting_Account;
    CheckBox cb_Setting_Alarm, cb_Setting_Sound, cb_Setting_Vibration;

    Context mContext;
    private Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mContext = getApplicationContext();
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        ui_Setting_TopBar = findViewById(R.id.ui_Setting_TopBar);
        tv_TopBar_Title = ui_Setting_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_Setting_TopBar.findViewById(R.id.iv_TopBar_Back);
        tv_Setting_Shop = findViewById(R.id.tv_Setting_Shop);
        tv_Setting_Alarm = findViewById(R.id.tv_Setting_Alarm);
        tv_Setting_Sound = findViewById(R.id.tv_Setting_Sound);
        tv_Setting_Vibration = findViewById(R.id.tv_Setting_Vibration);
        tv_Setting_Block = findViewById(R.id.tv_Setting_Block);
        tv_Setting_Version = findViewById(R.id.tv_Setting_Version);
        tv_Setting_Version_View = findViewById(R.id.tv_Setting_Version_View);
        tv_Setting_Terms_1 = findViewById(R.id.tv_Setting_Terms_1);
        tv_Setting_Terms_2 = findViewById(R.id.tv_Setting_Terms_2);
        tv_Setting_Terms_3 = findViewById(R.id.tv_Setting_Terms_3);
        tv_Setting_Account = findViewById(R.id.tv_Setting_Account);
        cb_Setting_Alarm = findViewById(R.id.cb_Setting_Alarm);
        cb_Setting_Sound = findViewById(R.id.cb_Setting_Sound);
        cb_Setting_Vibration = findViewById(R.id.cb_Setting_Vibration);

        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_SETTING));
        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        tv_Setting_Alarm.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {
                setAppSetting(CommonData.SETTING_MSG_ALARM, !TKManager.getInstance().AppSettingData.get(CommonData.SETTING_MSG_ALARM));
                cb_Setting_Alarm.setChecked(TKManager.getInstance().AppSettingData.get(CommonData.SETTING_MSG_ALARM));
            }
        });

        tv_Setting_Sound.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {
                setAppSetting(CommonData.SETTING_MSG_SOUND, !TKManager.getInstance().AppSettingData.get(CommonData.SETTING_MSG_SOUND));
                cb_Setting_Sound.setChecked(TKManager.getInstance().AppSettingData.get(CommonData.SETTING_MSG_SOUND));
            }
        });

        tv_Setting_Vibration.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {
                setAppSetting(CommonData.SETTING_MSG_VIBRATION, !TKManager.getInstance().AppSettingData.get(CommonData.SETTING_MSG_VIBRATION));
                cb_Setting_Vibration.setChecked(TKManager.getInstance().AppSettingData.get(CommonData.SETTING_MSG_VIBRATION));

                if(TKManager.getInstance().AppSettingData.get(CommonData.SETTING_MSG_VIBRATION) == true)
                {
                    vibrator.vibrate(500); // 1초간 진동
                }
            }
        });

        tv_Setting_Shop.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShopActivity.class));
            }
        });

        tv_Setting_Block.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {


                FirebaseManager.getInstance().SetFireBaseLoadingCount("차단", 1);
                FirebaseManager.CheckFirebaseComplete ReportUserListener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        Intent intent = new Intent(mContext, UserListActivity.class);
                        intent.putExtra("Type",CommonData.USER_LIST_BLOCK);
                        startActivity(intent);
                    }

                    @Override
                    public void CompleteListener_Yes() {
                    }

                    @Override
                    public void CompleteListener_No() {
                    }
                };

                FirebaseManager.getInstance().GetReportUser(ReportUserListener);


            }
        });

        tv_Setting_Terms_1.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, WebContentActivity.class);
                intent.putExtra("title", CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TERMS_1));
                intent.putExtra("content", CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TERMS_SERVICE));
                startActivity(intent);
            }
        });

        tv_Setting_Terms_2.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, WebContentActivity.class);
                intent.putExtra("title", CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TERMS_2));
                intent.putExtra("content", CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TERMS_PRIVACY));
                startActivity(intent);
            }
        });

        tv_Setting_Terms_3.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, WebContentActivity.class);
                intent.putExtra("title", CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TERMS_3));
                intent.putExtra("content", CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TERMS_REFUND));
                startActivity(intent);
            }
        });

        tv_Setting_Version.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {
                // 버전 체크?
            }
        });

        try {
            PackageInfo i = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            tv_Setting_Version_View.setText(i.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            tv_Setting_Version_View.setText("0");
        }

        tv_Setting_Account.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingAccountActivity.class));
            }
        });

        initNotice();
    }

    public void initNotice()
    {
        SharedPreferences sf = getSharedPreferences("userFile",MODE_PRIVATE);

        TKManager.getInstance().AppSettingData.put(CommonData.SETTING_MSG_ALARM, true);
        TKManager.getInstance().AppSettingData.put(CommonData.SETTING_MSG_SOUND, true);
        TKManager.getInstance().AppSettingData.put(CommonData.SETTING_MSG_VIBRATION, true);

        Set EntrySet = TKManager.getInstance().AppSettingData.entrySet();
        Iterator iterator = EntrySet.iterator();

        while (iterator.hasNext()) {

            Map.Entry entry = (Map.Entry) iterator.next();
            String key = (String) entry.getKey();
            Boolean value = sf.getBoolean(key,true);
            TKManager.getInstance().AppSettingData.put(key, value);
        }

        cb_Setting_Alarm.setChecked(TKManager.getInstance().AppSettingData.get(CommonData.SETTING_MSG_ALARM));
        cb_Setting_Sound.setChecked(TKManager.getInstance().AppSettingData.get(CommonData.SETTING_MSG_SOUND));
        cb_Setting_Vibration.setChecked(TKManager.getInstance().AppSettingData.get(CommonData.SETTING_MSG_VIBRATION));
    }

    public void setAppSetting(String type, boolean enable)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("userFile",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(type,enable);
        editor.commit();

        TKManager.getInstance().AppSettingData.put(type, enable);
    }
}
