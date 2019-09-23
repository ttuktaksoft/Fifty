package fifty.fiftyhouse.com.fifty.viewPager;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.MainUserAdapter;

public class MainFriendViewPager extends Fragment {

    TextView tv_Main_Friend_Count, tv_Main_Friend_UserList_Empty;

    RecyclerView rv_Main_Friend_UserList;
    ArrayList<String[]> mUserList = new ArrayList<>();
    View v_FragmentView = null;
    public MainUserAdapter mAdapter;
    private String UserIndex;
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
        mAdapter =  new MainUserAdapter(getContext(), listener);
        RefreshUserList();
        mAdapter.setItemData(mUserList);
        rv_Main_Friend_UserList.setAdapter(mAdapter);
        rv_Main_Friend_UserList.setLayoutManager(new LinearLayoutManager(getContext()));
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
    }

    public void RefreshUserList()
    {
        mUserList.clear();
        int cutSize = 0;
        String[] keyArr = new String[3];

        Iterator<String> keys = TKManager.getInstance().MyData.GetUserFriendListKeySet().iterator();
        while( keys.hasNext() ){
            String key = TKManager.getInstance().MyData.GetUserFriendList(keys.next());
            if(cutSize == 3)
            {
                mUserList.add(keyArr);
                keyArr = new String[3];
                cutSize = 0;
            }
            keyArr[cutSize] = key;
            cutSize++;
        }
        mUserList.add(keyArr);

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
