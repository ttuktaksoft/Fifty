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
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.UserListActivity;
import fifty.fiftyhouse.com.fifty.activty.UserProfileActivity;
import fifty.fiftyhouse.com.fifty.adapter.UserListAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class FriendRequestListViewPager extends Fragment {

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

        rv_Friend_Request_UserList.setAdapter(mAdapter);
        rv_Friend_Request_UserList.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rv_Friend_Request_UserList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_Friend_Request_UserList, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

                String tempUserIndex = null;
                Set tempKey = TKManager.getInstance().MyData.GetRequestFriendListKeySet();;
                List array = new ArrayList();

                array = new ArrayList(tempKey);
                tempUserIndex = TKManager.getInstance().UserData_Simple.get(array.get(position).toString()).GetUserIndex();

                DialogFunc.getInstance().ShowLoadingPage(getContext());

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();
                        startActivityForResult(new Intent(getContext(), UserProfileActivity.class), 0);
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
        RefreshAdapter();
        mAdapter.notifyDataSetChanged();
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
}