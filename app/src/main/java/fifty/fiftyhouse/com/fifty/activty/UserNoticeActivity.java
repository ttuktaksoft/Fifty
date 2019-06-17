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
import fifty.fiftyhouse.com.fifty.adapter.StrContentListAdapter;
import fifty.fiftyhouse.com.fifty.adapter.UserNoticeAdapter;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class UserNoticeActivity extends AppCompatActivity {

    View ui_User_Notice_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    RecyclerView rv_User_Notice_List;

    Context mContext;
    UserNoticeAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_notice);
        mContext = getApplicationContext();
        ui_User_Notice_TopBar = findViewById(R.id.ui_User_Notice_TopBar);
        tv_TopBar_Title = ui_User_Notice_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_User_Notice_TopBar.findViewById(R.id.iv_TopBar_Back);
        rv_User_Notice_List = findViewById(R.id.rv_User_Notice_List);

        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_USER_NOTICE));
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
        mAdapter = new UserNoticeAdapter(getApplicationContext());
        mAdapter.setHasStableIds(true);

        rv_User_Notice_List.setAdapter(mAdapter);
        rv_User_Notice_List.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv_User_Notice_List.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_User_Notice_List, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
