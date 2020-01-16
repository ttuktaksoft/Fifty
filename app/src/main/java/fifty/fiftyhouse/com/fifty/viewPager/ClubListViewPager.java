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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ClubActivity;
import fifty.fiftyhouse.com.fifty.activty.ClubCreateActivity;
import fifty.fiftyhouse.com.fifty.activty.ClubFavoriteActivity;
import fifty.fiftyhouse.com.fifty.adapter.ClubAdapter;
import fifty.fiftyhouse.com.fifty.fragment.ClubFragment;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class ClubListViewPager extends Fragment {

    RecyclerView rv_ClubList;
    TextView tv_Club_Empty;
    FloatingActionButton fa_Club_Create, fa_Club_Favorite;
    View v_FragmentView = null;

    ClubAdapter mAdapter;
    ArrayList<String> mClubList = new ArrayList<>();

    public static int CLUB_LIST_RECOMMEND = 0;
    public static int CLUB_LIST_MY = 1;

    public int mType = CLUB_LIST_RECOMMEND;
    boolean mClubLoading = false;
    private int mClubViewEndIndex = 0;

    public ClubListViewPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v_FragmentView == null) {
            v_FragmentView = inflater.inflate(R.layout.viewpager_club_list, container, false);
            rv_ClubList = v_FragmentView.findViewById(R.id.rv_ClubList);
            tv_Club_Empty = v_FragmentView.findViewById(R.id.tv_Club_Empty);
            fa_Club_Create = v_FragmentView.findViewById(R.id.fa_Club_Create);
            fa_Club_Favorite = v_FragmentView.findViewById(R.id.fa_Club_Favorite);


            fa_Club_Favorite.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    TKManager.getInstance().mUpdateClubFragmentkeybordDownFunc.UpdateUI();

                    DialogFunc.getInstance().ShowLoadingPage(ClubFragment.mClubFragment.getContext());

                    if(TKManager.getInstance().FavoriteLIst_ClubList.size() == 0)
                    {
                        FirebaseManager.CheckFirebaseComplete FavoriteClubList = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                long seed = System.nanoTime();
                                Collections.shuffle( TKManager.getInstance().FavoriteLIst_ClubList, new Random(seed));

                                Intent intent = new Intent(getContext(), ClubFavoriteActivity.class);
                                startActivityForResult(intent, 1000);
                                DialogFunc.getInstance().DismissLoadingPage();
                            }

                            @Override
                            public void CompleteListener_Yes() {

                            }

                            @Override
                            public void CompleteListener_No() {

                            }
                        };

                        FirebaseManager.getInstance().GetUserFavoriteClubList(FavoriteClubList);
                    }
                    else
                    {
                        Intent intent = new Intent(getContext(), ClubFavoriteActivity.class);
                        startActivity(intent);
                        DialogFunc.getInstance().DismissLoadingPage();
                    }
                }
            });

            fa_Club_Create.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    TKManager.getInstance().mUpdateClubFragmentkeybordDownFunc.UpdateUI();

                    TKManager.getInstance().CreateTempClubData.ClubFavorite.clear();

                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {

                            Intent intent = new Intent(getContext(), ClubCreateActivity.class);
                            intent.putExtra("Type",0);
                            startActivityForResult(intent, 1000);
                        }

                        @Override
                        public void CompleteListener_Yes() {

                        }

                        @Override
                        public void CompleteListener_No() {

                        }
                    };

                    FirebaseManager.getInstance().RegistClubIndex(TKManager.getInstance().CreateTempClubData, listener);


                }
            });

            RefreshPagerTypeUI();
            initRecyclerView();
        } else {
            RefreshRecyclerView();
        }

        return v_FragmentView;
    }

    private void RefreshPagerTypeUI()
    {
        fa_Club_Favorite.hide();
        fa_Club_Create.hide();

        if(mType == CLUB_LIST_RECOMMEND)
        {
            fa_Club_Favorite.show();
            tv_Club_Empty.setText(R.string.MSG_CLUB_RECOM_EMPTY);
        }
        else
        {
            tv_Club_Empty.setText(R.string.MSG_CLUB_EMPTY);
            fa_Club_Create.show();
        }
    }

    public void setViewPagerType(int type)
    {
        mType = type;
    }

    private void initRecyclerView() {

        ClubAdapter.ClubListener listener = new ClubAdapter.ClubListener()
        {
            @Override
            public void Listener(String key)
            {
                DialogFunc.getInstance().ShowLoadingPage(MainActivity.mActivity);

                FirebaseManager.CheckFirebaseComplete GetClubDataListener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {

                        FirebaseManager.CheckFirebaseComplete GetClubContextListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                DialogFunc.getInstance().DismissLoadingPage();
                                startActivityForResult(new Intent(getContext(), ClubActivity.class), 1000);
                            }

                            @Override
                            public void CompleteListener_Yes() {}
                            @Override
                            public void CompleteListener_No() {}
                        };

                        FirebaseManager.getInstance().GetClubContextData(TKManager.getInstance().ClubData_Simple.get(key).GetClubIndex(), GetClubContextListener);
                    }

                    @Override
                    public void CompleteListener_Yes() {}
                    @Override
                    public void CompleteListener_No() {}
                };

                FirebaseManager.getInstance().GetClubData(TKManager.getInstance().MyData, TKManager.getInstance().ClubData_Simple.get(key).GetClubIndex(),
                        GetClubDataListener);
            }
        };
        mClubViewEndIndex += CommonData.ClubList_First_View_Count;
        mAdapter =  new ClubAdapter(getContext(), listener);
        RefreshClubList();
        mAdapter.setItemData(mClubList);
        rv_ClubList.setAdapter(mAdapter);
        rv_ClubList.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_ClubList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if(mType != CLUB_LIST_RECOMMEND)
                    return;

                if(mAdapter.mLoadEnable == false)
                    return;

                if(mClubLoading)
                    return;

                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if(totalItemCount == lastVisibleItem + 1)
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mClubViewEndIndex += CommonData.ClubList_View_Count;
                            RefreshAdapter();
                            mClubLoading = false;
                        }
                    }, 3000);

                    mClubLoading = true;
                }
            }
        });
    }

    public void RefreshAdapter()
    {
        ArrayList<String> tempList = new ArrayList<>();
        tempList.addAll(mClubList);

        RefreshClubList();

        if(tempList.equals(mClubList) == false)
        {
            mAdapter.setItemData(mClubList);

            if(mType == CLUB_LIST_RECOMMEND)
            {
                if(mClubList.size() < mClubViewEndIndex)
                {
                    mClubViewEndIndex = mClubList.size();
                    mAdapter.mLoadEnable = false;
                }
            }

            mAdapter.notifyDataSetChanged();
        }
    }

    public void RefreshClubList()
    {
        mClubList.clear();
        if(mType == CLUB_LIST_RECOMMEND)
        {
            ArrayList<String> mTempUserList = new ArrayList<>();
            mTempUserList.addAll(TKManager.getInstance().MyData.GetUserRecommendClubDataKeySet());
            for(int i = 0 ; i < mClubViewEndIndex; ++i)
            {
                if(mTempUserList.size() <= i)
                    break;

                mClubList.add(mTempUserList.get(i));
            }
        }
        else
        {
            mClubList.addAll(TKManager.getInstance().MyData.GetUserClubDataKeySet());
            mClubList.addAll(TKManager.getInstance().MyData.GetRequestJoinClubListKeySet());
        }

        if (mClubList.size() == 0) {
            tv_Club_Empty.setVisibility(View.VISIBLE);
            rv_ClubList.setVisibility(View.GONE);
        } else {
            tv_Club_Empty.setVisibility(View.GONE);
            rv_ClubList.setVisibility(View.VISIBLE);
        }
    }

    public void RefreshRecyclerView()
    {
        RefreshAdapter();

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1000)
        {
            //TKManager.getInstance().SearchClubList.clear();
            ClubFragment.mClubFragment.RefreshViewPager();
/*            RefreshAdapter();
            mAdapter.notifyDataSetChanged();*/
        }
    }
}
