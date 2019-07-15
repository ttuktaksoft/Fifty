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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.StrContentListAdapter;
import fifty.fiftyhouse.com.fifty.adapter.UserNoticeAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
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
        rv_User_Notice_List.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_User_Notice_List, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

                Set tempKey = TKManager.getInstance().MyData.GetAlarmListKeySet();
                final List array = new ArrayList(tempKey);

                String tempIndex = array.get(position).toString();
                DialogFunc.getInstance().ShowLoadingPage(MainActivity.mActivity);

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();
                        startActivity(new Intent(MainActivity.mActivity, UserProfileActivity.class));
                    }

                    @Override
                    public void CompleteListener_Yes() {
                    }

                    @Override
                    public void CompleteListener_No() {
                        DialogFunc.getInstance().DismissLoadingPage();
                    }
                };

                FirebaseManager.getInstance().GetUserData(tempIndex, TKManager.getInstance().TargetUserData, listener);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
