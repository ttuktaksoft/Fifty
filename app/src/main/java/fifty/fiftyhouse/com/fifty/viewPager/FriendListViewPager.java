package fifty.fiftyhouse.com.fifty.viewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;

import fifty.fiftyhouse.com.fifty.activty.UserListActivity;
import fifty.fiftyhouse.com.fifty.activty.UserProfileActivity;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapter;
import fifty.fiftyhouse.com.fifty.adapter.UserListAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class FriendListViewPager extends Fragment {

    RecyclerView rv_Friend_UserList;
    TextView tv_Friend_UserList_Empty;
    View v_FragmentView = null;

    UserListAdapter mAdapter;
    ArrayList<String> mUserList = new ArrayList<>();

    public FriendListViewPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(v_FragmentView == null)
        {
            v_FragmentView = inflater.inflate(R.layout.viewpager_friend_list, container, false);
            rv_Friend_UserList = v_FragmentView.findViewById(R.id.rv_Friend_UserList);
            tv_Friend_UserList_Empty = v_FragmentView.findViewById(R.id.tv_Friend_UserList_Empty);

            initRecyclerView();
        }
        else
        {
            mAdapter.notifyDataSetChanged();
        }

        return v_FragmentView;
    }

    private void initRecyclerView()
    {
        mAdapter = new UserListAdapter(getContext());
        RefreshAdapter();
        mAdapter.setHasStableIds(true);

        rv_Friend_UserList.setAdapter(mAdapter);
        rv_Friend_UserList.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rv_Friend_UserList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_Friend_UserList, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

                String UserIndex = null;
                Set tempKey = TKManager.getInstance().MyData.GetUserFriendListKeySet();
                List array = new ArrayList();

                array = new ArrayList(tempKey);
                UserIndex = TKManager.getInstance().UserData_Simple.get(array.get(position).toString()).GetUserIndex();

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
    }
    public void RefreshUserList() {
        mUserList.clear();
        mUserList.addAll(TKManager.getInstance().MyData.GetUserFriendListKeySet());

        if(mUserList.size() == 0)
        {
            tv_Friend_UserList_Empty.setVisibility(View.VISIBLE);
            rv_Friend_UserList.setVisibility(View.GONE);
        }
        else
        {
            tv_Friend_UserList_Empty.setVisibility(View.GONE);
            rv_Friend_UserList.setVisibility(View.VISIBLE);
        }
    }
    public void RefreshAdapter()
    {
        RefreshUserList();
        mAdapter.setItemData(mUserList);
        mAdapter.setItemCount(mUserList.size());
    }
}
