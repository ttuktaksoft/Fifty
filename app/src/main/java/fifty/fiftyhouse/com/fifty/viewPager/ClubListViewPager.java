package fifty.fiftyhouse.com.fifty.viewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DataBase.ClubData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ChatBodyActivity;
import fifty.fiftyhouse.com.fifty.activty.ClubActivity;
import fifty.fiftyhouse.com.fifty.activty.FavoriteSelectActivity;
import fifty.fiftyhouse.com.fifty.adapter.ChatAdapter;
import fifty.fiftyhouse.com.fifty.adapter.ClubAdapter;
import fifty.fiftyhouse.com.fifty.fragment.ClubFragment;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

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
                }
            });

            fa_Club_Create.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    TKManager.getInstance().mUpdateClubFragmentkeybordDownFunc.UpdateUI();

                    TKManager.getInstance().CreateTempClubData.ClubFavorite.clear();
                    DialogFunc.getInstance().ShowLoadingPage(ClubFragment.mClubFragment.getContext());
                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {
                            DialogFunc.getInstance().DismissLoadingPage();
                            Intent intent = new Intent(getContext(), FavoriteSelectActivity.class);
                            intent.putExtra("Type",2);
                            startActivity(intent);
                        }

                        @Override
                        public void CompleteListener_Yes() {
                        }

                        @Override
                        public void CompleteListener_No() {
                        }
                    };

                    FirebaseManager.getInstance().GetPopFavoriteData(listener);
                }
            });

            RefreshPagerTypeUI();
            initRecyclerView();
        } else {
            mAdapter.notifyDataSetChanged();
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
            tv_Club_Empty.setText(R.string.MSG_RECOM_CLUB_EMPTY);
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
        mAdapter = new ClubAdapter(getContext());
        RefreshAdapter();
        mAdapter.setHasStableIds(true);

        rv_ClubList.setAdapter(mAdapter);
        rv_ClubList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rv_ClubList.offsetLeftAndRight(CommonFunc.getInstance().convertDPtoPX(getResources(),20));
        rv_ClubList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_ClubList, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, final int position) {

                Map<String, ClubData> tempClubKey = new LinkedHashMap<>();
                tempClubKey.putAll(TKManager.getInstance().MyData.GetUserClubData());
                tempClubKey.putAll(TKManager.getInstance().SearchClubList);

                Set tempKey = tempClubKey.keySet(); //TKManager.getInstance().MyData.GetUserClubDataKeySet();
                final List array = new ArrayList(tempKey);

                DialogFunc.getInstance().ShowLoadingPage(MainActivity.mActivity);

                FirebaseManager.CheckFirebaseComplete GetClubDataListener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {

                        FirebaseManager.CheckFirebaseComplete GetClubContextListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                DialogFunc.getInstance().DismissLoadingPage();
                                startActivity(new Intent(getContext(), ClubActivity.class));
                            }

                            @Override
                            public void CompleteListener_Yes() {}
                            @Override
                            public void CompleteListener_No() {}
                        };

                        FirebaseManager.getInstance().GetClubContextData(TKManager.getInstance().ClubData_Simple.get(array.get(position).toString()).GetClubIndex(), GetClubContextListener);
                    }

                    @Override
                    public void CompleteListener_Yes() {}
                    @Override
                    public void CompleteListener_No() {}
                };

                FirebaseManager.getInstance().GetClubData(TKManager.getInstance().MyData, TKManager.getInstance().ClubData_Simple.get(array.get(position).toString()).GetClubIndex(),
                        GetClubDataListener);
            }
        }));
    }

    public void RefreshAdapter()
    {
        RefreshClubList();
        mAdapter.setItemCount(mClubList.size());
        mAdapter.setItemData(mClubList);
    }

    public void RefreshClubList()
    {
        mClubList.clear();

        if(mType == CLUB_LIST_RECOMMEND)
            mClubList.addAll(TKManager.getInstance().MyData.GetUserClubDataKeySet());
        else
            mClubList.addAll(TKManager.getInstance().MyData.GetUserClubDataKeySet());

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
}
