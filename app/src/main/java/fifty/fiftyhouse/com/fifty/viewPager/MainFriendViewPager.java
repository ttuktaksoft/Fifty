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

import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.CustomGridListHolder;
import fifty.fiftyhouse.com.fifty.adapter.CustomMainAdapterOne;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapterOne;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemOneClickListener;

public class MainFriendViewPager extends Fragment {

    TextView tv_Main_Friend_Count, tv_Main_Friend_UserList_Empty;

    AsymmetricGridView rv_Main_Friend_UserList;
    ArrayList<String> mUserList = new ArrayList<>();
    View v_FragmentView = null;
    public CustomMainAdapterOne mAdapter;
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
        /*mAdapter = new MainAdapterOne(getContext());
        RefreshUI();
        mAdapter.setHasStableIds(true);

        rv_Main_Friend_UserList.setAdapter(mAdapter);
        rv_Main_Friend_UserList.setLayoutManager(new GridLayoutManager(getContext(), 3));
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
                *//*int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int nSize = 0;
                nSize = recyclerView.getAdapter().getItemCount() - 1;

                if (lastVisibleItemPosition == nSize) {
                    // Toast.makeText(getContext(), "Last Position", Toast.LENGTH_SHORT).show();
                    //    CommonFunc.getInstance().ShowLoadingPage(getContext(), "로딩중");
                    //  FirebaseData.getInstance().GetHotData(RecvAdapter, false);
                }*//*
            }
        });*/

        RefreshUserList();
        mAdapter =  new CustomMainAdapterOne(getContext(), CommonFunc.getInstance().getCustomGridListHolderList(mUserList));
        mAdapter.SetItemCountByType(CommonData.MainViewType.FRIEND);
        rv_Main_Friend_UserList.setRequestedColumnCount(3);
        rv_Main_Friend_UserList.setAdapter(new AsymmetricGridViewAdapter(getContext(), rv_Main_Friend_UserList,mAdapter));
        rv_Main_Friend_UserList.setOnItemClickListener(
                new RecyclerItemOneClickListener() {
                    @Override
                    public void RecyclerItemOneClick(int position) {
                        UserIndex = TKManager.getInstance().UserData_Simple.get(mUserList.get(position)).GetUserIndex();
                        CommonFunc.getInstance().GetUserDataInFireBase(UserIndex, MainActivity.mActivity, false);
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

        List<CustomGridListHolder> list = CommonFunc.getInstance().getCustomGridListHolderList(mUserList);
        mAdapter.setItems(list);
    }

    public void RefreshUserList()
    {
        mUserList.clear();
        mUserList.addAll(TKManager.getInstance().MyData.GetRequestFriendListKeySet());

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
