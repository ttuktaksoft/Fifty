package fifty.fiftyhouse.com.fifty.viewPager;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ShopActivity;
import fifty.fiftyhouse.com.fifty.adapter.UserListAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class FriendRequestListViewPager extends Fragment {

    View ui_vip_shop_info;
    TextView tv_VIP_Info_Desc;
    TextView tv_VIP_Info_Shop;

    RecyclerView rv_Friend_Request_UserList;
    TextView tv_Friend_Request_UserList_Empty;
    public View v_FragmentView = null;

    UserListAdapter mAdapter;
    ArrayList<String> mUserList = new ArrayList<>();

    public FriendRequestListViewPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(v_FragmentView == null)
        {
            v_FragmentView = inflater.inflate(R.layout.viewpager_friend_request_list, container, false);
            rv_Friend_Request_UserList = v_FragmentView.findViewById(R.id.rv_Friend_Request_UserList);
            tv_Friend_Request_UserList_Empty = v_FragmentView.findViewById(R.id.tv_Friend_Request_UserList_Empty);
            ui_vip_shop_info = v_FragmentView.findViewById(R.id.ui_vip_shop_info);
            tv_VIP_Info_Desc = v_FragmentView.findViewById(R.id.tv_VIP_Info_Desc);
            tv_VIP_Info_Shop = v_FragmentView.findViewById(R.id.tv_VIP_Info_Shop);

            ui_vip_shop_info.setVisibility(View.GONE);
            ui_vip_shop_info.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                }
            });

            tv_VIP_Info_Shop.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    startActivityForResult(new Intent(getContext(), ShopActivity.class), 1000);
                }
            });
            initRecyclerView();
        }
        else
        {
            mAdapter.notifyDataSetChanged();
        }

        RefreshVIP();

        return v_FragmentView;
    }

    private void initRecyclerView()
    {
        mAdapter = new UserListAdapter(getContext());
        RefreshAdapter();
        mAdapter.setHasStableIds(true);

        rv_Friend_Request_UserList.setAdapter(mAdapter);
        rv_Friend_Request_UserList.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rv_Friend_Request_UserList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_Friend_Request_UserList, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

                String UserIndex = null;
                Set tempKey = TKManager.getInstance().MyData.GetRequestFriendListKeySet();;
                List array = new ArrayList();

                array = new ArrayList(tempKey);
                UserIndex = TKManager.getInstance().UserData_Simple.get(array.get(position).toString()).GetUserIndex();

                DialogFunc.getInstance().ShowLoadingPage(MainActivity.mActivity);

                CommonFunc.getInstance().GetUserDataInFireBase(UserIndex, MainActivity.mActivity, false);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        RefreshAdapter();
        mAdapter.notifyDataSetChanged();

        if(requestCode == 1000)
        {
            RefreshVIP();
        }
    }
    public void RefreshUserList() {
        mUserList.clear();
        mUserList.addAll(TKManager.getInstance().MyData.GetRequestFriendListKeySet());

        if(mUserList.size() == 0)
        {
            tv_Friend_Request_UserList_Empty.setVisibility(View.VISIBLE);
            rv_Friend_Request_UserList.setVisibility(View.GONE);
        }
        else
        {
            tv_Friend_Request_UserList_Empty.setVisibility(View.GONE);
            rv_Friend_Request_UserList.setVisibility(View.VISIBLE);
        }
    }
    public void RefreshAdapter()
    {
        RefreshUserList();
        mAdapter.setItemData(mUserList);
        mAdapter.setItemCount(mUserList.size());
    }

    public void RefreshVIP()
    {
        if(TKManager.getInstance().MyData.GetUserVip() == false)
        {
            ui_vip_shop_info.setVisibility(View.VISIBLE);
            tv_VIP_Info_Desc.setText(CommonFunc.getInstance().getStr(getContext().getResources(), R.string.USER_LIST_VIP_SHOP_DESC_FRIEND_REQUEST));
            rv_Friend_Request_UserList.setVisibility(View.GONE);
            tv_Friend_Request_UserList_Empty.setVisibility(View.GONE);
        }
        else
        {
            ui_vip_shop_info.setVisibility(View.GONE);
            RefreshUserList();
            RefreshAdapter();
            mAdapter.notifyDataSetChanged();
        }
    }
}