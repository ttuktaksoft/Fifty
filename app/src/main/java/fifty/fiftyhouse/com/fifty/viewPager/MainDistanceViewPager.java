package fifty.fiftyhouse.com.fifty.viewPager;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.activty.SortSettingActivity;
import fifty.fiftyhouse.com.fifty.adapter.MainUserAdapter;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

import fifty.fiftyhouse.com.fifty.R;

public class MainDistanceViewPager extends Fragment {

    Fragment mFragment;
    FloatingActionButton fa_Main_Dis_Search, fa_Main_Dis_Favorite, fa_Main_Dis_Name;
    TextView tv_Main_Dis_Curr_Pos;
    TextView tv_Main_Dis_UserList_Empty;
    ImageView iv_Main_Dis_Sort_Type;

    RecyclerView rv_Main_Dis_UserList;
    View v_FragmentView = null;

    MainUserAdapter mAdapter;

    ArrayList<String> mUserList = new ArrayList<>();
    boolean mSortEnable = false;
    private String UserIndex;
    private boolean mUserLoading = false;
    private int mUserViewEndIndex = 0;

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
                    DialogFunc.getInstance().ShowFavoriteSearchPopup(getContext(),MainActivity.mActivity);
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

        rv_Main_Dis_UserList.setAdapter(mAdapter);
        rv_Main_Dis_UserList.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_Main_Dis_UserList.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        mTempUserList.addAll(TKManager.getInstance().View_UserList_Dist);
        for(int i = 0 ; i < mUserViewEndIndex; ++i)
        {
            if(mTempUserList.size() <= i)
                break;

            mUserList.add(mTempUserList.get(i));
        }

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
