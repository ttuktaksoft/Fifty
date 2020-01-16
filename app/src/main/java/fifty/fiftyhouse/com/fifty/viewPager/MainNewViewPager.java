package fifty.fiftyhouse.com.fifty.viewPager;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.MainUserAdapter;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class MainNewViewPager extends Fragment {

    TextView tv_Main_New_Desc, tv_Main_New_UserList_Empty;
    FloatingActionButton fa_Main_New_Search;
    RecyclerView rv_Main_New_UserList;
    View v_FragmentView = null;
    public MainUserAdapter mAdapter;
    ArrayList<String> mUserList = new ArrayList<>();
    private String UserIndex;

    private boolean mUserLoading = false;
    private int mUserViewEndIndex = 0;

    public MainNewViewPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v_FragmentView == null) {
            // Inflate the layout for this fragment
            v_FragmentView = inflater.inflate(R.layout.viewpager_main_new, container, false);
            tv_Main_New_UserList_Empty = v_FragmentView.findViewById(R.id.tv_Main_New_UserList_Empty);
            rv_Main_New_UserList = v_FragmentView.findViewById(R.id.rv_Main_New_UserList);
            tv_Main_New_Desc = v_FragmentView.findViewById(R.id.tv_Main_New_Desc);
            fa_Main_New_Search = v_FragmentView.findViewById(R.id.fa_Main_New_Search);

            initRecyclerView();


            fa_Main_New_Search.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    DialogFunc.getInstance().ShowUserSearchPopup(getContext(), MainActivity.mActivity);
                }
            });

        } else {
            RefreshAdapter();
        }
        return v_FragmentView;
    }
    boolean isLoading = false;

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
        rv_Main_New_UserList.setAdapter(mAdapter);
        rv_Main_New_UserList.setLayoutManager(new LinearLayoutManager(getContext()));


        rv_Main_New_UserList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    }, 3000);

                    mUserLoading = true;
                }
            }
        });

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
        mTempUserList.addAll(TKManager.getInstance().View_UserList_New);
        for(int i = 0 ; i < mUserViewEndIndex; ++i)
        {
            if(mTempUserList.size() <= i)
                break;

            mUserList.add(mTempUserList.get(i));
        }

        if(TKManager.getInstance().View_UserList_New.size() == 0)
        {
            rv_Main_New_UserList.setVisibility(View.GONE);
            tv_Main_New_UserList_Empty.setVisibility(View.VISIBLE);
        }
        else
        {
            rv_Main_New_UserList.setVisibility(View.VISIBLE);
            tv_Main_New_UserList_Empty.setVisibility(View.GONE);
        }
    }


}
