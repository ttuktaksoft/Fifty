package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.SettingAdapter;
import fifty.fiftyhouse.com.fifty.adapter.UserNoticeAdapter;
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
                startActivity(new Intent(getApplicationContext(), SettingAccountActivity.class));
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
