package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.UserNoticeAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class UserNoticeActivity extends AppCompatActivity {

    View ui_User_Notice_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    RecyclerView rv_User_Notice_List;

    View ui_vip_shop_info;
    TextView tv_VIP_Info_Desc;
    TextView tv_VIP_Info_Shop;

    TextView tv_User_Notice_List_Empty;

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
        ui_vip_shop_info = findViewById(R.id.ui_vip_shop_info);
        tv_VIP_Info_Desc = findViewById(R.id.tv_VIP_Info_Desc);
        tv_VIP_Info_Shop = findViewById(R.id.tv_VIP_Info_Shop);
        rv_User_Notice_List = findViewById(R.id.rv_User_Notice_List);
        tv_User_Notice_List_Empty = findViewById(R.id.tv_User_Notice_List_Empty);

        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_USER_NOTICE));
        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        ui_vip_shop_info.setVisibility(View.GONE);
        ui_vip_shop_info.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

            }
        });

        tv_VIP_Info_Shop.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), ShopActivity.class), 1000);
            }
        });

        initRecyclerView();

        if(TKManager.getInstance().MyData.GetAlarmListCount() == 0)
            tv_User_Notice_List_Empty.setVisibility(View.VISIBLE);
        else
            tv_User_Notice_List_Empty.setVisibility(View.GONE);

        RefreshVIP();
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

                CommonFunc.getInstance().GetUserDataInFireBase(tempIndex, MainActivity.mActivity, false);
                /*DialogFunc.getInstance().ShowLoadingPage(MainActivity.mActivity);

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

                FirebaseManager.getInstance().GetUserData(tempIndex, TKManager.getInstance().TargetUserData, listener);*/
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void RefreshVIP()
    {
        if(TKManager.getInstance().MyData.GetUserVip().equals("nVip") == true)
        {
            ui_vip_shop_info.setVisibility(View.VISIBLE);
            tv_VIP_Info_Desc.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.USER_LIST_VIP_SHOP_DESC_NOTICE));
            tv_User_Notice_List_Empty.setVisibility(View.GONE);
            rv_User_Notice_List.setVisibility(View.GONE);
        }
        else
        {
            ui_vip_shop_info.setVisibility(View.GONE);
            if(TKManager.getInstance().MyData.GetAlarmListCount() == 0)
            {
                tv_User_Notice_List_Empty.setVisibility(View.VISIBLE);
                rv_User_Notice_List.setVisibility(View.GONE);
            }
            else
            {
                tv_User_Notice_List_Empty.setVisibility(View.GONE);
                rv_User_Notice_List.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000)
        {
            RefreshVIP();
        }

    }
}
