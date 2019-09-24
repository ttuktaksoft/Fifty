package fifty.fiftyhouse.com.fifty.viewPager;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.MainUserAdapter;
import fifty.fiftyhouse.com.fifty.adapter.RealTimeFavoriteAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class MainTodayViewPager extends Fragment {
    TextView tv_Main_Today_UserList_Empty, tv_Main_Today_Desc;

    FloatingActionButton fa_Main_Today_Search, fa_Main_Today_Favorite, fa_Main_Today_Name;

    RecyclerView rv_Main_Today_Favorite;
    RecyclerView rv_RealTime_Favorite;
    TextView tv_RealTime_Favorite_Time;
    RecyclerView rv_Main_Today_UserList;
    View v_FragmentView = null;
    public MainUserAdapter mAdapter;
    ArrayList<String> mUserList = new ArrayList<>();
    RealTimeFavoriteAdapter mRealTimeFavoriteAdapter;
    private String UserIndex;

    ArrayList<String> mRealTimeFavoriteViewList = new ArrayList<>();
    String mSelectRealTimeFavorite = "";

    public MainTodayViewPager() {
        super();
    }

    private Animation fab_open, fab_close;
    private boolean isFabOpen = false;
    private void toggleFab() {
        if (isFabOpen) {
            fa_Main_Today_Favorite.startAnimation(fab_close);
            fa_Main_Today_Name.startAnimation(fab_close);
            fa_Main_Today_Favorite.setClickable(false);
            fa_Main_Today_Name.setClickable(false);
            isFabOpen = false;
        } else {
            fa_Main_Today_Favorite.startAnimation(fab_open);
            fa_Main_Today_Name.startAnimation(fab_open);
            fa_Main_Today_Favorite.setClickable(true);
            fa_Main_Today_Name.setClickable(true);
            isFabOpen = true;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(v_FragmentView == null)
        {
            v_FragmentView = inflater.inflate(R.layout.viewpager_main_today, container, false);

            tv_Main_Today_UserList_Empty = v_FragmentView.findViewById(R.id.tv_Main_Today_UserList_Empty);
            rv_RealTime_Favorite = v_FragmentView.findViewById(R.id.rv_RealTime_Favorite);
            tv_RealTime_Favorite_Time = v_FragmentView.findViewById(R.id.tv_RealTime_Favorite_Time);
            rv_Main_Today_UserList = v_FragmentView.findViewById(R.id.rv_Main_Today_UserList);
            fa_Main_Today_Search = v_FragmentView.findViewById(R.id.fa_Main_Today_Search);
            fa_Main_Today_Favorite = v_FragmentView.findViewById(R.id.fa_Main_Today_Favorite);
            fa_Main_Today_Name = v_FragmentView.findViewById(R.id.fa_Main_Today_Name);

            fab_open = AnimationUtils.loadAnimation(MainActivity.mActivity, R.anim.fab_open);

            fab_close = AnimationUtils.loadAnimation(MainActivity.mActivity, R.anim.fab_close);

            tv_Main_Today_Desc = v_FragmentView.findViewById(R.id.tv_Main_Today_Desc);

            Set EntrySet = TKManager.getInstance().MyData.GetUserFavoriteListKeySet();
            List array = new ArrayList(EntrySet);
            mSelectRealTimeFavorite = array.get(0).toString();
            //TKManager.getInstance().SelectFavorite = mSelectRealTimeFavorite;

            initRecyclerView();
            initSubInfo();
            initRealTimeFavoriteRecyclerView();

            fa_Main_Today_Search.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    //  DialogFunc.getInstance().ShowUserSearchPopup(getContext(), MainActivity.mActivity);
                    toggleFab();
                }
            });

            fa_Main_Today_Favorite.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    DialogFunc.getInstance().ShowFavoriteSearchPopup(getContext(), MainActivity.mActivity);
                }
            });

            fa_Main_Today_Name.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    DialogFunc.getInstance().ShowUserSearchPopup(getContext(), MainActivity.mActivity);
                }
            });


            tv_RealTime_Favorite_Time.setText(CommonFunc.getInstance().ConvertTimeSrt(CommonFunc.getInstance().GetCurrentTime(), "yyyy년 MM월 dd일 HH:00") + "기준");

        }
        else
        {
            RefreshUI();
        }

        return v_FragmentView;
    }

    private void initSubInfo()
    {
        tv_Main_Today_Desc.setText(CommonFunc.getInstance().getComleteWordByJongsung(mSelectRealTimeFavorite, "을", "를") + " " + CommonFunc.getInstance().getStr(getResources(), R.string.MSG_MAIN_USER_TODAY_DESC));
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
        rv_Main_Today_UserList.setAdapter(mAdapter);
        rv_Main_Today_UserList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void RefreshAdapter()
    {
        RefreshUserList();
        mAdapter.setItemData(mUserList);
        mAdapter.notifyDataSetChanged();
    }

    public void RefreshUserList()
    {
        mUserList.clear();
        long seed = System.nanoTime();
        Collections.shuffle(TKManager.getInstance().View_UserList_Hot, new Random(seed));
        mUserList.addAll(TKManager.getInstance().View_UserList_Hot);

        if(TKManager.getInstance().View_UserList_Hot.size() == 0)
        {
            rv_Main_Today_UserList.setVisibility(View.GONE);
            tv_Main_Today_UserList_Empty.setVisibility(View.VISIBLE);
        }
        else
        {
            rv_Main_Today_UserList.setVisibility(View.VISIBLE);
            tv_Main_Today_UserList_Empty.setVisibility(View.GONE);
        }
    }

    private void initRealTimeFavoriteRecyclerView()
    {
        mRealTimeFavoriteAdapter = new RealTimeFavoriteAdapter(getContext());
        RefreshRealTimeFavoriteList();
        mRealTimeFavoriteAdapter.setHasStableIds(true);
        rv_RealTime_Favorite.setAdapter(mRealTimeFavoriteAdapter);
        rv_RealTime_Favorite.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rv_RealTime_Favorite.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_RealTime_Favorite, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, final int position) {

                DialogFunc.getInstance().ShowLoadingPage(MainActivity.mActivity);

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {

                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();
                        mSelectRealTimeFavorite = mRealTimeFavoriteViewList.get(position);
                        initSubInfo();
                        RefreshAdapter();
                    }

                    @Override
                    public void CompleteListener_Yes() {

                    }

                    @Override
                    public void CompleteListener_No() {
                        DialogFunc.getInstance().DismissLoadingPage();
                    }
                };

                FirebaseManager.getInstance().FindFavoriteList(mRealTimeFavoriteViewList.get(position), listener);
            }
        }));

/*        rv_RealTime_Favorite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/
    }

    public void RefreshRealTimeFavoriteList()
    {
        mRealTimeFavoriteViewList.clear();
        mRealTimeFavoriteViewList.addAll(TKManager.getInstance().SearchList_Favorite);
        mRealTimeFavoriteAdapter.setItemData(mRealTimeFavoriteViewList);
        mRealTimeFavoriteAdapter.notifyDataSetChanged();
    }

    public void RefreshUI()
    {
        if(CommonFunc.getInstance().CheckStringNull(TKManager.getInstance().SelectFavorite))
            return;

        if(mSelectRealTimeFavorite.equals(TKManager.getInstance().SelectFavorite) == false)
        {
            DialogFunc.getInstance().ShowLoadingPage(MainActivity.mActivity);

            FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {

                @Override
                public void CompleteListener() {
                    DialogFunc.getInstance().DismissLoadingPage();
                    mSelectRealTimeFavorite = TKManager.getInstance().SelectFavorite;
                    initSubInfo();
                    RefreshAdapter();
                }

                @Override
                public void CompleteListener_Yes() {

                }

                @Override
                public void CompleteListener_No() {
                    DialogFunc.getInstance().DismissLoadingPage();
                    DialogFunc.getInstance().ShowMsgPopup(MainActivity.mActivity, CommonFunc.getInstance().getStr(MainActivity.mActivity.getResources(), R.string.MSG_FAVORITE_FIND_EMPTY));
                    TKManager.getInstance().SelectFavorite = mSelectRealTimeFavorite;
                }
            };

            FirebaseManager.getInstance().FindFavoriteList(TKManager.getInstance().SelectFavorite, listener);
        }
    }
}
