package fifty.fiftyhouse.com.fifty.viewPager;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import fifty.fiftyhouse.com.fifty.adapter.CustomGridListHolder;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapterOne;
import fifty.fiftyhouse.com.fifty.adapter.CustomMainAdapterOne;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemOneClickListener;

public class MainDistanceViewPager extends Fragment {

    FloatingActionButton fa_Main_Dis_Search, fa_Main_Dis_Favorite, fa_Main_Dis_Name;
    TextView tv_Main_Dis_Curr_Pos;
    TextView tv_Main_Dis_UserList_Empty;
    ImageView iv_Main_Dis_Sort_Type;

    AsymmetricGridView rv_Main_Dis_UserList;
    View v_FragmentView = null;

    CustomMainAdapterOne mAdapter;

    ArrayList<String> mUserList = new ArrayList<>();
    boolean mSortEnable = false;
    private String UserIndex;
    public MainDistanceViewPager() {
        super();
    }


    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;
    private void toggleFab() {
        if (isFabOpen) {
            fa_Main_Dis_Favorite.startAnimation(fab_close);
            fa_Main_Dis_Name.startAnimation(fab_close);
            fa_Main_Dis_Favorite.setClickable(false);
            fa_Main_Dis_Name.setClickable(false);
            isFabOpen = false;
        } else {
            fa_Main_Dis_Favorite.startAnimation(fab_open);
            fa_Main_Dis_Name.startAnimation(fab_open);
            fa_Main_Dis_Favorite.setClickable(true);
            fa_Main_Dis_Name.setClickable(true);
            isFabOpen = true;
        }
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
            fa_Main_Dis_Favorite = v_FragmentView.findViewById(R.id.fa_Main_Dis_Favorite);
            fa_Main_Dis_Name = v_FragmentView.findViewById(R.id.fa_Main_Dis_Name);

            fab_open = AnimationUtils.loadAnimation(MainActivity.mActivity, R.anim.fab_open);

            fab_close = AnimationUtils.loadAnimation(MainActivity.mActivity, R.anim.fab_close);

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
                  //  DialogFunc.getInstance().ShowUserSearchPopup(getContext(), MainActivity.mActivity);
                    toggleFab();
                }
            });

            fa_Main_Dis_Favorite.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    DialogFunc.getInstance().ShowUserSearchPopup(getContext(), MainActivity.mActivity);
                }
            });

            fa_Main_Dis_Name.setOnClickListener(new OnSingleClickListener() {
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
/*        mAdapter = new MainAdapterOne(getContext());
        RefreshAdapter();
        mAdapter.setHasStableIds(true);
        mAdapter.SetItemCountByType(CommonData.MainViewType.DIST, TKManager.getInstance().View_UserList_Dist.size());*/

        RefreshUserList();
        mAdapter =  new CustomMainAdapterOne(getContext(), CommonFunc.getInstance().getCustomGridListHolderList(mUserList));
        mAdapter.SetItemCountByType(CommonData.MainViewType.DIST);
        rv_Main_Dis_UserList.setRequestedColumnCount(3);
        rv_Main_Dis_UserList.setAdapter(new AsymmetricGridViewAdapter(getContext(), rv_Main_Dis_UserList,mAdapter));
        rv_Main_Dis_UserList.setOnItemClickListener(
                new RecyclerItemOneClickListener() {
                    @Override
                    public void RecyclerItemOneClick(int position) {
                        UserIndex = TKManager.getInstance().UserData_Simple.get(mUserList.get(position)).GetUserIndex();
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

    public void RefreshAdapter()
    {
        RefreshUserList();

        List<CustomGridListHolder> list = CommonFunc.getInstance().getCustomGridListHolderList(mUserList);
        mAdapter.setItems(list);
    }

    public void RefreshUserList()
    {
        mUserList.clear();
        mUserList.addAll(TKManager.getInstance().View_UserList_Dist);

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
    }
}
