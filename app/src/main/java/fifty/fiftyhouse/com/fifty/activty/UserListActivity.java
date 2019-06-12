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

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.UserListAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class UserListActivity extends AppCompatActivity {

    View ui_UserList_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    RecyclerView rv_UserList_List;
    UserListAdapter mAdapter;

    CommonData.MyProfileViewType Type;
    int Count;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mContext = getApplicationContext();

        ui_UserList_TopBar = findViewById(R.id.ui_UserList_TopBar);
        tv_TopBar_Title = ui_UserList_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_UserList_TopBar.findViewById(R.id.iv_TopBar_Back);
        rv_UserList_List = findViewById(R.id.rv_UserList_List);

        iv_TopBar_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent(); //getIntent()로 받을준비
        int ntype = getIntent().getIntExtra("Type", 0);

        switch (ntype)
        {
            case 0:
                tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_USER_LIST_VISIT));
                Type = CommonData.MyProfileViewType.VISIT;
                break;
            case 1:
                tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_USER_LIST_LIKE));
                Type = CommonData.MyProfileViewType.LIKE;
                break;
            case 2:
                tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_USER_LIST_FRIEND));
                Type = CommonData.MyProfileViewType.FRIEND;
                break;

        }
        Count  = getIntent().getIntExtra("Count", 0);

        initRecyclerView(Type, Count);
    }


    private void initRecyclerView(final CommonData.MyProfileViewType type , int count)
    {
        mAdapter = new UserListAdapter(getApplicationContext());
        mAdapter.setHasStableIds(true);
        mAdapter.SetItemCountByType(type, count);

        rv_UserList_List.setAdapter(mAdapter);
        rv_UserList_List.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv_UserList_List.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_UserList_List, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String tempUserIndex = null;
                Set tempKey = null;
                List array = new ArrayList();

                switch (type)
                {
                    case VISIT:
                        tempKey =  TKManager.getInstance().MyData.GetUserVisitKeySet();
                        break;
                    case LIKE:
                        tempKey =  TKManager.getInstance().MyData.GetUserLikeKeySet();
                        break;
                    case FRIEND:
                        tempKey =  TKManager.getInstance().MyData.GetUserFriendListKeySet();
                        break;

                }

                array = new ArrayList(tempKey);
                tempUserIndex = TKManager.getInstance().UserData_Simple.get(array.get(position).toString()).GetUserIndex();

                DialogFunc.getInstance().ShowLoadingPage(UserListActivity.this);

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();
                        startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                    }

                    @Override
                    public void CompleteListener_Yes() {
                    }

                    @Override
                    public void CompleteListener_No() {
                        DialogFunc.getInstance().DismissLoadingPage();
                    }
                };

                FirebaseManager.getInstance().GetUserData(tempUserIndex, TKManager.getInstance().TargetUserData, listener);


            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
