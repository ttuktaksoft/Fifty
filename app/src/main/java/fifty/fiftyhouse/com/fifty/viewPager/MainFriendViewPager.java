package fifty.fiftyhouse.com.fifty.viewPager;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.MainUserAdapter;

public class MainFriendViewPager extends Fragment {

    TextView tv_Main_Friend_Count, tv_Main_Friend_UserList_Empty;

    RecyclerView rv_Main_Friend_UserList;
    ArrayList<String> mUserList = new ArrayList<>();
    View v_FragmentView = null;
    public MainUserAdapter mAdapter;
    private String UserIndex;
    private boolean mUserLoading = false;
    private int mUserViewEndIndex = 0;

    public MainFriendViewPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(v_FragmentView == null)
        {
            v_FragmentView = inflater.inflate(R.layout.viewpager_main_friend, container, false);

            rv_Main_Friend_UserList = v_FragmentView.findViewById(R.id.rv_Main_Friend_UserList);
            tv_Main_Friend_Count = v_FragmentView.findViewById(R.id.tv_Main_Friend_Count);
            tv_Main_Friend_UserList_Empty = v_FragmentView.findViewById(R.id.tv_Main_Friend_UserList_Empty);

            initSubInfo();
            initRecyclerView();
        }
        else
        {
            RefreshUI();
        }

        // Inflate the layout for this fragment
        return v_FragmentView;
    }

    private void initSubInfo()
    {
        tv_Main_Friend_Count.setText("친구 " + TKManager.getInstance().MyData.GetUserFriendListCount() + "명");
    }

    private void initRecyclerView()
    {
        MainUserAdapter.MainUserListener listener = new MainUserAdapter.MainUserListener()
        {
            @Override
            public void Listener(String key)
            {
                CommonFunc.getInstance().GetUserDataInFireBase(key, MainActivity.mActivity, false);
            }
        };

        mUserViewEndIndex += CommonData.UserList_First_View_Count;

        mAdapter =  new MainUserAdapter(getContext(), listener);
        RefreshUserList();
        mAdapter.setItemData(mUserList);
        rv_Main_Friend_UserList.setAdapter(mAdapter);
        rv_Main_Friend_UserList.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_Main_Friend_UserList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if(mAdapter.mLoadEnable == false)
                    return;

                if(mUserLoading)
                    return;

                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if(totalItemCount == lastVisibleItem + 1)
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mUserViewEndIndex += CommonData.UserList_View_Count;
                            RefreshAdapter();
                            mUserLoading = false;
                        }
                    }, 5000);

                    mUserLoading = true;
                }
            }
        });
    }

    public void RefreshUI()
    {
        initSubInfo();
        RefreshAdapter();
    }

    public void RefreshAdapter()
    {
        RefreshUserList();

        mAdapter.setItemData(mUserList);
        if(mUserList.size() < mUserViewEndIndex)
        {
            mUserViewEndIndex = mUserList.size();
            mAdapter.mLoadEnable = false;
        }
        mAdapter.notifyDataSetChanged();
    }

    public void RefreshUserList()
    {
        mUserList.clear();
        ArrayList<String> mTempUserList = new ArrayList<>();
        mTempUserList.addAll(TKManager.getInstance().MyData.GetUserFriendListKeySet());
        for(int i = 0 ; i < mUserViewEndIndex; ++i)
        {
            if(mTempUserList.size() <= i)
                break;

            mUserList.add(mTempUserList.get(i));
        }

        if(TKManager.getInstance().MyData.GetUserFriendListCount() == 0)
        {
            tv_Main_Friend_UserList_Empty.setVisibility(View.VISIBLE);
            rv_Main_Friend_UserList.setVisibility(View.GONE);
        }
        else
        {
            tv_Main_Friend_UserList_Empty.setVisibility(View.GONE);
            rv_Main_Friend_UserList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000)
        {
            RefreshUI();
        }

    }

}
