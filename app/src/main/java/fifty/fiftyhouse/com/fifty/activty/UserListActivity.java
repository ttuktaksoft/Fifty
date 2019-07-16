package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.service.Common;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.GlobalApplication;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.UserListAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class UserListActivity extends AppCompatActivity {

    View ui_UserList_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    RecyclerView rv_UserList_List;
    TextView tv_UserList_List_Empty;
    UserListAdapter mAdapter;

    Context mContext;
    ArrayList<String> mUserList = new ArrayList<>();

    int mUserListType = CommonData.USER_LIST_MY_VISIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        mContext = getApplicationContext();

        ui_UserList_TopBar = findViewById(R.id.ui_UserList_TopBar);
        tv_TopBar_Title = ui_UserList_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_UserList_TopBar.findViewById(R.id.iv_TopBar_Back);
        rv_UserList_List = findViewById(R.id.rv_UserList_List);
        tv_UserList_List_Empty = findViewById(R.id.tv_UserList_List_Empty);

        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent(); //getIntent()로 받을준비
        mUserListType = getIntent().getIntExtra("Type", 0);

        if(mUserListType == CommonData.USER_LIST_MY_VISIT)
        {
            tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_USER_LIST_VISIT));
        }
        else if(mUserListType == CommonData.USER_LIST_MY_LIKE)
        {
            tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_USER_LIST_LIKE));
        }
        else if(mUserListType == CommonData.USER_LIST_MY_FRIEND)
        {
            tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_USER_LIST_FRIEND));
        }
        else if(mUserListType == CommonData.USER_LIST_CLUB)
        {
            tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_USER_LIST_CLUB));
        }

        RefreshUserList(mUserListType);
        if (mUserListType == CommonData.USER_LIST_MY_LIKE)
        {
            tv_UserList_List_Empty.setText(CommonFunc.getInstance().getStr(getResources(), R.string.MSG_USER_LIST_EMPTY_LIKE));
        }
        else if (mUserListType == CommonData.USER_LIST_MY_VISIT)
        {
            tv_UserList_List_Empty.setText(CommonFunc.getInstance().getStr(getResources(), R.string.MSG_USER_LIST_EMPTY_VISIT));
        }
        else if (mUserListType == CommonData.USER_LIST_MY_FRIEND)
        {
            tv_UserList_List_Empty.setText(CommonFunc.getInstance().getStr(getResources(), R.string.MSG_USER_LIST_EMPTY_FRIEND));
        }
        else if(mUserListType == CommonData.USER_LIST_CLUB)
        {
            tv_UserList_List_Empty.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_USER_LIST_EMPTY_CLUB));
        }

        initRecyclerView();



    }

    private void initRecyclerView()
    {
        mAdapter = new UserListAdapter(getApplicationContext());
        RefreshAdapter(mUserListType);
        mAdapter.setHasStableIds(true);

        rv_UserList_List.setAdapter(mAdapter);
        rv_UserList_List.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv_UserList_List.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_UserList_List, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

                String tempUserIndex = null;
                Set tempKey = null;
                List array = new ArrayList();

                switch (mUserListType)
                {
                    case CommonData.USER_LIST_MY_VISIT:
                        tempKey =  TKManager.getInstance().MyData.GetUserVisitKeySet();
                        break;
                    case CommonData.USER_LIST_MY_LIKE:
                        tempKey =  TKManager.getInstance().MyData.GetUserLikeKeySet();
                        break;
                    case CommonData.USER_LIST_MY_FRIEND:
                        tempKey =  TKManager.getInstance().MyData.GetUserFriendListKeySet();
                        break;
                    case CommonData.USER_LIST_CLUB:
                        tempKey =  TKManager.getInstance().TargetClubData.GetClubMemberKeySet();
                        break;
                }

                array = new ArrayList(tempKey);
                tempUserIndex = TKManager.getInstance().UserData_Simple.get(array.get(position).toString()).GetUserIndex();

                DialogFunc.getInstance().ShowLoadingPage(UserListActivity.this);

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();
                        startActivityForResult(new Intent(getApplicationContext(), UserProfileActivity.class), mUserListType);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CommonData.USER_LIST_MY_LIKE)
        {
            RefreshAdapter(CommonData.USER_LIST_MY_LIKE);
            mAdapter.notifyDataSetChanged();
        }
        else if (requestCode == CommonData.USER_LIST_MY_VISIT)
        {
            RefreshAdapter(CommonData.USER_LIST_MY_VISIT);
            mAdapter.notifyDataSetChanged();
        }
        else if (requestCode == CommonData.USER_LIST_MY_FRIEND)
        {
            RefreshAdapter(CommonData.USER_LIST_MY_FRIEND);
            mAdapter.notifyDataSetChanged();
        }
        else if (requestCode == CommonData.USER_LIST_CLUB)
        {
            RefreshAdapter(CommonData.USER_LIST_CLUB);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void RefreshAdapter(int type)
    {
        RefreshUserList(type);
        mAdapter.setItemCount(mUserList.size());
        mAdapter.setItemData(mUserList);
    }

    public void RefreshUserList(int type)
    {
        mUserList.clear();
        if (type == CommonData.USER_LIST_MY_LIKE)
        {
            mUserList.addAll(TKManager.getInstance().MyData.GetUserLikeKeySet());
        }
        else if (type == CommonData.USER_LIST_MY_VISIT)
        {
            mUserList.addAll(TKManager.getInstance().MyData.GetUserVisitKeySet());
        }
        else if (type == CommonData.USER_LIST_MY_FRIEND)
        {
            mUserList.addAll(TKManager.getInstance().MyData.GetUserFriendListKeySet());
        }
        else if (type == CommonData.USER_LIST_CLUB)
        {
            mUserList.addAll(TKManager.getInstance().TargetClubData.GetClubMemberKeySet());
        }

        if(mUserList.size() == 0)
        {
            tv_UserList_List_Empty.setVisibility(View.VISIBLE);
            rv_UserList_List.setVisibility(View.GONE);
        }
        else
        {
            tv_UserList_List_Empty.setVisibility(View.GONE);
            rv_UserList_List.setVisibility(View.VISIBLE);
        }
    }
}
