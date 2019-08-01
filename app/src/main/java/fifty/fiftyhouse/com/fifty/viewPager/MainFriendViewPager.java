package fifty.fiftyhouse.com.fifty.viewPager;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.UserData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.GlobalApplication;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.UserProfileActivity;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class MainFriendViewPager extends Fragment {

    TextView tv_Main_Friend_Count, tv_Main_Friend_UserList_Empty;

    RecyclerView rv_Main_Friend_UserList;
    View v_FragmentView = null;
    public MainAdapter mAdapter;
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
        mAdapter = new MainAdapter(getContext());
        RefreshUI();
        mAdapter.setHasStableIds(true);

        rv_Main_Friend_UserList.setAdapter(mAdapter);
        rv_Main_Friend_UserList.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rv_Main_Friend_UserList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_Main_Friend_UserList, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

                Set tempKey = TKManager.getInstance().MyData.GetUserFriendListKeySet();
                List array = new ArrayList(tempKey);

                UserIndex = TKManager.getInstance().UserData_Simple.get(array.get(position).toString()).GetUserIndex();

                CommonFunc.getInstance().GetUserDataInFireBase(UserIndex, MainActivity.mActivity, false);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));

        rv_Main_Friend_UserList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                /*int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int nSize = 0;
                nSize = recyclerView.getAdapter().getItemCount() - 1;

                if (lastVisibleItemPosition == nSize) {
                    // Toast.makeText(getContext(), "Last Position", Toast.LENGTH_SHORT).show();
                    //    CommonFunc.getInstance().ShowLoadingPage(getContext(), "로딩중");
                    //  FirebaseData.getInstance().GetHotData(RecvAdapter, false);
                }*/
            }
        });
    }

    public void RefreshUI()
    {
        initSubInfo();
        mAdapter.SetItemCountByType(CommonData.MainViewType.FRIEND, TKManager.getInstance().MyData.GetUserFriendListCount());
        mAdapter.notifyDataSetChanged();

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
