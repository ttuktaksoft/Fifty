package fifty.fiftyhouse.com.fifty.viewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.activty.SortSettingActivity;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapterOneListHolder;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapterOne;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapterOneList;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemOneClickListener;

public class MainDistanceViewPager extends Fragment {

    FloatingActionButton fa_Main_Dis_Search;
    TextView tv_Main_Dis_Curr_Pos;
    TextView tv_Main_Dis_UserList_Empty;
    ImageView iv_Main_Dis_Sort_Type;

    AsymmetricGridView rv_Main_Dis_UserList;
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
            fa_Main_Dis_Search = v_FragmentView.findViewById(R.id.fa_Main_Dis_Search);

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

            fa_Main_Dis_Search.setOnClickListener(new OnSingleClickListener() {
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

        List<MainAdapterOneListHolder> items = new ArrayList<>();
        int currentOffset = 0;
        for (int i = 0; i < TKManager.getInstance().UserData_Simple.size() - 1; i++) {
            int colSpan = Math.random() < 0.2f ? 2 : 1;
            int rowSpan = colSpan;
            MainAdapterOneListHolder item = new MainAdapterOneListHolder(colSpan, rowSpan, currentOffset + i);
            items.add(item);
        }

        currentOffset += TKManager.getInstance().UserData_Simple.size() - 1;
        rv_Main_Dis_UserList.setRequestedColumnCount(3);
        rv_Main_Dis_UserList.setAdapter(new AsymmetricGridViewAdapter(getContext(), rv_Main_Dis_UserList, new MainAdapterOneList(getContext(), items)));
        //rv_Main_Dis_UserList.setDebugging(true);
        rv_Main_Dis_UserList.setOnItemClickListener(
                new RecyclerItemOneClickListener() {
                    @Override
                    public void RecyclerItemOneClick(int position) {
                        UserIndex = TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().View_UserList_Dist.get(position)).GetUserIndex();
                        CommonFunc.getInstance().GetUserDataInFireBase(UserIndex, MainActivity.mActivity, false);
                    }
                });



        /*mAdapter = new MainAdapterOne(getContext());
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
        }));*/
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
