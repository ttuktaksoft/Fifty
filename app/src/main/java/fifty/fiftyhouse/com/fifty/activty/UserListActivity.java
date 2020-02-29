package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
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

    View ui_vip_shop_info;
    TextView tv_VIP_Info_Desc;
    TextView tv_VIP_Info_Shop;

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
        ui_vip_shop_info = findViewById(R.id.ui_vip_shop_info);
        tv_VIP_Info_Desc = findViewById(R.id.tv_VIP_Info_Desc);
        tv_VIP_Info_Shop = findViewById(R.id.tv_VIP_Info_Shop);

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

        ui_vip_shop_info.setVisibility(View.GONE);
        ui_vip_shop_info.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

            }
        });

        tv_VIP_Info_Shop.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if(TKManager.getInstance().MyData.GetRemainVipDate() > 0)
                {
                    StringBuilder desc = new StringBuilder();
                    desc.append(CommonFunc.getInstance().getStr(getApplicationContext().getResources(), R.string.MSG_STR_VIP_REMAIN_1));
                    desc.append(" ");
                    desc.append(TKManager.getInstance().MyData.GetRemainVipDate());
                    desc.append(CommonFunc.getInstance().getStr(getApplicationContext().getResources(), R.string.MSG_STR_VIP_REMAIN_2));
                    DialogFunc.getInstance().ShowMsgPopup(getApplicationContext(), desc.toString());
                }
                else
                {
                    startActivityForResult(new Intent(getApplicationContext(), ShopActivity.class), 1000);
                }
            }
        });

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
        else if(mUserListType == CommonData.USER_LIST_CLUB_JOIN_WAIT)
        {
            tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_USER_LIST_CLUB_JOIN_WAIT));
        }
        else if(mUserListType == CommonData.USER_LIST_CLUB_CHAT)
        {
            tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_USER_LIST_CLUB_CHAT));
        }
        else if(mUserListType == CommonData.USER_LIST_BLOCK)
        {
            tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_USER_LIST_BLOCK));
        }
        else if(mUserListType == CommonData.USER_LIST_CLUB_INVITE)
        {
            tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_USER_LIST_CLUB_INVITE));
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
        else if(mUserListType == CommonData.USER_LIST_CLUB_JOIN_WAIT)
        {
            tv_UserList_List_Empty.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_USER_LIST_EMPTY_CLUB_JOIN_WAIT));
        }
        else if(mUserListType == CommonData.USER_LIST_CLUB_CHAT)
        {
            tv_UserList_List_Empty.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_USER_LIST_EMPTY_CLUB_CHAT));
        }
        else if(mUserListType == CommonData.USER_LIST_BLOCK)
        {
            tv_UserList_List_Empty.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_USER_LIST_EMPTY_BLOCK));
        }
        else if(mUserListType == CommonData.USER_LIST_CLUB_INVITE)
        {
            tv_UserList_List_Empty.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_USER_LIST_EMPTY_CLUB_INVITE));
        }

        initRecyclerView();

        RefreshVIP();
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

                final String tempUserIndex;
                Set tempKey;
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
                    case CommonData.USER_LIST_CLUB_JOIN_WAIT:
                        // 가입 대기 목록
                        tempKey = TKManager.getInstance().UserData_RequestJoin.keySet();
                        break;
                    case CommonData.USER_LIST_CLUB_CHAT:
                        tempKey = TKManager.getInstance().MyData.GetChatUserListKeySet();
                        break;
                    case CommonData.USER_LIST_BLOCK:
                        tempKey = TKManager.getInstance().MyData.GetReportUserListKeySet();
                        break;
                    case CommonData.USER_LIST_CLUB_INVITE:
                        tempKey = TKManager.getInstance().UserList_Invite_Club.keySet();
                        break;
                    default:
                        tempKey = null;
                        break;
                }


                array = new ArrayList(tempKey);
                tempUserIndex = TKManager.getInstance().UserData_Simple.get(array.get(position).toString()).GetUserIndex();


                if(mUserListType == CommonData.USER_LIST_CLUB_JOIN_WAIT)
                {
                    ArrayList<String> menuList = new ArrayList<>();
                    menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_VIEW_PROFILE));
                    menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CLUB_JOIN_OK));
                    menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CLUB_JOIN_CENCEL));
                    menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CANCEL));

                    ArrayList<DialogFunc.MsgPopupListener> list = new ArrayList<>();
                    list.add(new DialogFunc.MsgPopupListener()
                    {
                        @Override
                        public void Listener()
                        {
                            ShowUserProfile(tempUserIndex);
                        }
                    });
                    list.add(new DialogFunc.MsgPopupListener()
                    {
                        @Override
                        public void Listener()
                        {
                            // 가입 승인
                            if(TKManager.getInstance().TargetClubData.GetClubMemberCount() + 1 > TKManager.getInstance().TargetClubData.GetClubMaxMember())
                            {
                                DialogFunc.getInstance().ShowMsgPopup(UserListActivity.this, CommonFunc.getInstance().getStr(UserListActivity.this.getResources(), R.string.MSG_CLUB_JOIN_FULL_DESC));
                            }
                            else
                            {
                                FirebaseManager.CheckFirebaseComplete RequestListener = new FirebaseManager.CheckFirebaseComplete() {
                                    @Override
                                    public void CompleteListener() {
                                        TKManager.getInstance().UserData_RequestJoin.remove(tempUserIndex);
                                        DialogFunc.getInstance().ShowToast(UserListActivity.this, "가입승인", true);

                                        RefreshAdapter(CommonData.USER_LIST_CLUB_JOIN_WAIT);
                                        mAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void CompleteListener_Yes() {

                                    }

                                    @Override
                                    public void CompleteListener_No() {

                                    }
                                };

                                FirebaseManager.getInstance().RegistClubMember(TKManager.getInstance().TargetClubData, tempUserIndex, false, RequestListener);
                            }
                        }
                    });
                    list.add(new DialogFunc.MsgPopupListener()
                    {
                        @Override
                        public void Listener()
                        {
                            // 가입 거절
                            FirebaseManager.CheckFirebaseComplete RequestListener = new FirebaseManager.CheckFirebaseComplete() {
                                @Override
                                public void CompleteListener() {
                                    TKManager.getInstance().UserData_RequestJoin.remove(tempUserIndex);
                                    DialogFunc.getInstance().ShowToast(UserListActivity.this, "가입거절", true);

                                    RefreshAdapter(CommonData.USER_LIST_CLUB_JOIN_WAIT);
                                    mAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void CompleteListener_Yes() {

                                }

                                @Override
                                public void CompleteListener_No() {

                                }
                            };

                            FirebaseManager.getInstance().RegistClubMember(TKManager.getInstance().TargetClubData, tempUserIndex, true, RequestListener);

                        }
                    });

                    ArrayList<DialogFunc.MsgPopupListener> menuListenerList = list;

                    DialogFunc.getInstance().ShowMenuListPopup(UserListActivity.this, menuList, menuListenerList, null);
                }
                else if(mUserListType == CommonData.USER_LIST_CLUB)
                {
                    if(TKManager.getInstance().TargetClubData.GetClubMasterIndex().equals(TKManager.getInstance().MyData.GetUserIndex()) &&
                        tempUserIndex.equals(TKManager.getInstance().MyData.GetUserIndex()) == false)
                    {
                        ArrayList<String> menuList = new ArrayList<>();
                        menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_VIEW_PROFILE));
                        menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CLUB_DEPORT));
                        menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CANCEL));

                        ArrayList<DialogFunc.MsgPopupListener> list = new ArrayList<>();
                        list.add(new DialogFunc.MsgPopupListener()
                        {
                            @Override
                            public void Listener()
                            {
                                ShowUserProfile(tempUserIndex);
                            }
                        });
                        list.add(new DialogFunc.MsgPopupListener()
                        {
                            @Override
                            public void Listener()
                            {
                                FirebaseManager.CheckFirebaseComplete removeListener = new FirebaseManager.CheckFirebaseComplete() {
                                    @Override
                                    public void CompleteListener() {
                                        TKManager.getInstance().TargetClubData.DelClubMember(tempUserIndex);
                                        DialogFunc.getInstance().ShowToast(UserListActivity.this, "클럽 추방", true);

                                        RefreshAdapter(CommonData.USER_LIST_CLUB);
                                        mAdapter.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void CompleteListener_Yes() {

                                    }

                                    @Override
                                    public void CompleteListener_No() {

                                    }
                                };

                                FirebaseManager.getInstance().RemoveClubMember(TKManager.getInstance().TargetClubData, tempUserIndex, removeListener);

                            }
                        });

                        ArrayList<DialogFunc.MsgPopupListener> menuListenerList = list;

                        DialogFunc.getInstance().ShowMenuListPopup(UserListActivity.this, menuList, menuListenerList, null);
                    }
                    else
                    {
                        ShowUserProfile(tempUserIndex);
                    }
                }
                else if(mUserListType == CommonData.USER_LIST_BLOCK)
                {
                    ArrayList<String> menuList = new ArrayList<>();
                    menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_BLOCK_CANCEL));
                    menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CANCEL));

                    ArrayList<DialogFunc.MsgPopupListener> list = new ArrayList<>();
                    list.add(new DialogFunc.MsgPopupListener()
                    {
                        @Override
                        public void Listener()
                        {
                            FirebaseManager.CheckFirebaseComplete ReportCancelUserListener = new FirebaseManager.CheckFirebaseComplete() {
                                @Override
                                public void CompleteListener() {
                                    RefreshAdapter(CommonData.USER_LIST_BLOCK);
                                    mAdapter.notifyDataSetChanged();
                                }

                                @Override
                                public void CompleteListener_Yes() {
                                }

                                @Override
                                public void CompleteListener_No() {
                                }
                            };

                            FirebaseManager.getInstance().RemoveReportUser(tempUserIndex, ReportCancelUserListener);

                            // 차단 해제
                        }
                    });

                    ArrayList<DialogFunc.MsgPopupListener> menuListenerList = list;

                    DialogFunc.getInstance().ShowMenuListPopup(UserListActivity.this, menuList, menuListenerList, null);
                }
                else if(mUserListType == CommonData.USER_LIST_CLUB_INVITE)
                {
                    ArrayList<String> menuList = new ArrayList<>();
                    menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CLUB_INVITE));
                    menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CANCEL));

                    ArrayList<DialogFunc.MsgPopupListener> list = new ArrayList<>();
                    list.add(new DialogFunc.MsgPopupListener()
                    {
                        @Override
                        public void Listener()
                        {
                            // TODO 도형
                            // 클럽 초대
                            DialogFunc.getInstance().ShowToast(UserListActivity.this, "초대하였습니다", true);
                        }
                    });

                    ArrayList<DialogFunc.MsgPopupListener> menuListenerList = list;

                    DialogFunc.getInstance().ShowMenuListPopup(UserListActivity.this, menuList, menuListenerList, null);
                }
                else
                {
                    ShowUserProfile(tempUserIndex);
                }
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
        else if (requestCode == CommonData.USER_LIST_CLUB_JOIN_WAIT)
        {
            RefreshAdapter(CommonData.USER_LIST_CLUB_JOIN_WAIT);
            mAdapter.notifyDataSetChanged();
        }
        else if (requestCode == CommonData.USER_LIST_CLUB_CHAT)
        {
            RefreshAdapter(CommonData.USER_LIST_CLUB_CHAT);
            mAdapter.notifyDataSetChanged();
        }
        else if (requestCode == CommonData.USER_LIST_BLOCK)
        {
            RefreshAdapter(CommonData.USER_LIST_BLOCK);
            mAdapter.notifyDataSetChanged();
        }
        else if (requestCode == CommonData.USER_LIST_CLUB_INVITE)
        {
            RefreshAdapter(CommonData.USER_LIST_CLUB_INVITE);
            mAdapter.notifyDataSetChanged();
        }

        if (requestCode == 1000)
        {
            RefreshVIP();
        }
    }

    public void RefreshAdapter(int type)
    {
        RefreshUserList(type);
        if (type == CommonData.USER_LIST_CLUB)
        {
            ArrayList<Pair<String, Integer>> list = new ArrayList<>();

            Iterator iterator = mUserList.iterator();
            while (iterator.hasNext()) {
                String element = (String) iterator.next();

                if(TKManager.getInstance().TargetClubData.GetClubMasterIndex().equals(element))
                    list.add(new Pair<String, Integer>(element, UserListAdapter.CLUB_MATSER_VIEW));
                else
                    list.add(new Pair<String, Integer>(element, 0));
            }

            mAdapter.setItemCount(list.size());
            mAdapter.setSpecialItemData(list);
        }
        else
        {
            mAdapter.setItemCount(mUserList.size());
            mAdapter.setItemData(mUserList);
        }
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
        else if (type == CommonData.USER_LIST_CLUB_JOIN_WAIT)
        {
            mUserList.addAll(TKManager.getInstance().UserData_RequestJoin.keySet());
        }
        else if (type == CommonData.USER_LIST_CLUB_CHAT)
        {
            mUserList.addAll(TKManager.getInstance().MyData.GetChatUserListKeySet());
        }
        else if (type == CommonData.USER_LIST_BLOCK)
        {
            mUserList.addAll(TKManager.getInstance().MyData.GetReportUserListKeySet());
        }
        else if (type == CommonData.USER_LIST_CLUB_INVITE)
        {
            mUserList.addAll(TKManager.getInstance().UserList_Invite_Club.keySet());
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

    private void ShowUserProfile(String id)
    {
        if(id.equals(TKManager.getInstance().MyData.GetUserIndex()) == false) {

            DialogFunc.getInstance().ShowLoadingPage(UserListActivity.this);

            FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                @Override
                public void CompleteListener() {

                    Set KeySet = TKManager.getInstance().TargetUserData.GetUserClubDataKeySet();

                    if(KeySet.size() > 0)
                    {
                        Iterator iterator = KeySet.iterator();

                        FirebaseManager.getInstance().SetFireBaseLoadingCount("유져페이지", TKManager.getInstance().TargetUserData.GetUserClubDataCount());

                        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                startActivityForResult(new Intent(getApplicationContext(), UserProfileActivity.class), mUserListType);
                            }

                            @Override
                            public void CompleteListener_Yes() {
                            }

                            @Override
                            public void CompleteListener_No() {
                            }
                        };

                        while(iterator.hasNext()){
                            String key = (String)iterator.next();
                            if(TKManager.getInstance().ClubData_Simple.get(key) != null)
                            {
                                FirebaseManager.getInstance().Complete("유져페이지", listener);
                            }
                            else
                                FirebaseManager.getInstance().GetClubData_Simple(key, TKManager.getInstance().ClubData_Simple, listener);
                        }
                    }
                    else
                    {
                        startActivityForResult(new Intent(getApplicationContext(), UserProfileActivity.class), mUserListType);
                    }

                }

                @Override
                public void CompleteListener_Yes() {
                }

                @Override
                public void CompleteListener_No() {
                    DialogFunc.getInstance().DismissLoadingPage();
                }
            };

            FirebaseManager.getInstance().GetUserData(id, TKManager.getInstance().TargetUserData, listener);


        }
        else
        {
            DialogFunc.getInstance().ShowToast(getApplicationContext(), CommonFunc.getInstance().getStr(getApplicationContext().getResources(), R.string.MSG_MY_PROFILE_CLICK), true);
        }
    }

    public void RefreshVIP()
    {
        if(TKManager.getInstance().MyData.GetUserVip().equals("nVip") == true)
        {
            if(mUserListType == CommonData.USER_LIST_MY_VISIT)
            {
                ui_vip_shop_info.setVisibility(View.VISIBLE);
                tv_VIP_Info_Desc.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.USER_LIST_VIP_SHOP_DESC_VISIT));
                tv_UserList_List_Empty.setVisibility(View.GONE);
                rv_UserList_List.setVisibility(View.GONE);
            }
            else if(mUserListType == CommonData.USER_LIST_MY_LIKE)
            {
                ui_vip_shop_info.setVisibility(View.VISIBLE);
                tv_VIP_Info_Desc.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.USER_LIST_VIP_SHOP_DESC_LIKE));
                tv_UserList_List_Empty.setVisibility(View.GONE);
                rv_UserList_List.setVisibility(View.GONE);
            }
        }
        else
        {
            ui_vip_shop_info.setVisibility(View.GONE);
            RefreshUserList(mUserListType);
        }
    }
}
