package fifty.fiftyhouse.com.fifty.viewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.GlobalApplication;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.activty.SortSettingActivity;
import fifty.fiftyhouse.com.fifty.activty.UserProfileActivity;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapter;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapterOne;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

import fifty.fiftyhouse.com.fifty.R;

public class MainDistanceViewPager extends Fragment {

    FloatingActionButton fa_Friend_UserList_Search;
    TextView tv_Main_Dis_Curr_Pos;
    TextView tv_Main_Dis_UserList_Empty;
    ImageView iv_Main_Dis_Sort_Type;

    RecyclerView rv_Main_Dis_UserList;
    View v_FragmentView = null;

    MainAdapterOne mAdapter;

    boolean mSortEnable = false;
    private String UserIndex;
    public MainDistanceViewPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(v_FragmentView == null)
        {
            v_FragmentView = inflater.inflate(R.layout.viewpager_main_distance, container, false);
            rv_Main_Dis_UserList = v_FragmentView.findViewById(R.id.rv_Main_Dis_UserList);
            tv_Main_Dis_Curr_Pos = v_FragmentView.findViewById(R.id.tv_Main_Dis_Curr_Pos);
            iv_Main_Dis_Sort_Type = v_FragmentView.findViewById(R.id.iv_Main_Dis_Sort_Type);
            tv_Main_Dis_UserList_Empty = v_FragmentView.findViewById(R.id.tv_Main_Dis_UserList_Empty);
            fa_Friend_UserList_Search = v_FragmentView.findViewById(R.id.fa_Friend_UserList_Search);

/*            if(TKManager.getInstance().FilterData.GetDistance() == 100)
                tv_Main_Dis_Sort_Type.setText("내 근처 : 전체");
            else
                tv_Main_Dis_Sort_Type.setText("내 근처 : "+ TKManager.getInstance().FilterData.GetDistance() + "KM");*/
            tv_Main_Dis_Curr_Pos.setText(TKManager.getInstance().MyData.GetUserDist_Area());

            iv_Main_Dis_Sort_Type.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    Intent intent = new Intent(MainActivity.mActivity, SortSettingActivity.class);
                    startActivity(intent);
                    MainActivity.mActivity.finish();
                }
            });

            fa_Friend_UserList_Search.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    DialogFunc.getInstance().ShowUserSearchPopup(getContext(), MainActivity.mActivity);
                }
            });

            initRecyclerView();
        }
        else
        {
            RefreshAdapter();
        }

        return v_FragmentView;
    }

    private void initRecyclerView()
    {
        mAdapter = new MainAdapterOne(getContext());
        RefreshAdapter();
        mAdapter.setHasStableIds(true);
        mAdapter.SetItemCountByType(CommonData.MainViewType.DIST, TKManager.getInstance().View_UserList_Dist.size());
        rv_Main_Dis_UserList.setAdapter(mAdapter);

        rv_Main_Dis_UserList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rv_Main_Dis_UserList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_Main_Dis_UserList, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {
                UserIndex = TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().View_UserList_Dist.get(position)).GetUserIndex();
                CommonFunc.getInstance().GetUserDataInFireBase(UserIndex, MainActivity.mActivity, false);
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));

        rv_Main_Dis_UserList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void RefreshAdapter()
    {
        if(TKManager.getInstance().View_UserList_Dist.size() == 0)
        {
            rv_Main_Dis_UserList.setVisibility(View.GONE);
            tv_Main_Dis_UserList_Empty.setVisibility(View.VISIBLE);
        }
        else
        {
            rv_Main_Dis_UserList.setVisibility(View.VISIBLE);
            tv_Main_Dis_UserList_Empty.setVisibility(View.GONE);
        }

        mAdapter.notifyDataSetChanged();
    }
}
