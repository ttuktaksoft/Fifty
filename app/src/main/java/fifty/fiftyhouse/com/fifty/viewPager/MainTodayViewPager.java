package fifty.fiftyhouse.com.fifty.viewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.GlobalApplication;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.UserProfileActivity;
import fifty.fiftyhouse.com.fifty.adapter.FavoriteViewAdapter;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

import static fifty.fiftyhouse.com.fifty.CommonData.DAILY_FAVORITE;
public class MainTodayViewPager extends Fragment {
    TextView tv_Main_Today_UserList_Empty;
    RecyclerView rv_Main_Today_UserList, rv_Main_Today_Favorite;
    View v_FragmentView = null;
    public MainAdapter mAdapter;
    FavoriteViewAdapter mFavoriteViewAdapter;
    private String UserIndex;

    ArrayList<String> mFavoriteViewList = new ArrayList<>();
    String mSelectFavoriteKey = "";

    public MainTodayViewPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(v_FragmentView == null)
        {
            v_FragmentView = inflater.inflate(R.layout.viewpager_main_today, container, false);

            tv_Main_Today_UserList_Empty = v_FragmentView.findViewById(R.id.tv_Main_Today_UserList_Empty);
            rv_Main_Today_UserList = v_FragmentView.findViewById(R.id.rv_Main_Today_UserList);
            rv_Main_Today_Favorite = v_FragmentView.findViewById(R.id.rv_Main_Today_Favorite);

            initRecyclerView();
            initFavoriteRecyclerView();
        }
        else
        {
            RefreshAdapter();
        }

        return v_FragmentView;
    }

    /*private void initSubInfo()
    {
        tv_Main_Today_Desc.setText(CommonFunc.getInstance().getComleteWordByJongsung(DAILY_FAVORITE, "을", "를") + " " + CommonFunc.getInstance().getStr(getResources(), R.string.MSG_MAIN_USER_TODAY_DESC));
    }*/
    private void initRecyclerView()
    {
        mAdapter = new MainAdapter(getContext());
        RefreshAdapter();
        mAdapter.setHasStableIds(true);
        mAdapter.SetItemCountByType(CommonData.MainViewType.HOT, TKManager.getInstance().View_UserList_Hot.size());

        rv_Main_Today_UserList.setAdapter(mAdapter);
        rv_Main_Today_UserList.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rv_Main_Today_UserList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_Main_Today_UserList, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

                UserIndex = TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().View_UserList_Hot.get(position)).GetUserIndex();
                CommonFunc.getInstance().GetUserDataInFireBase(UserIndex, MainActivity.mActivity, false);
            }
        }));
    }

    private void RefreshAdapter()
    {
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

        mAdapter.notifyDataSetChanged();
    }

    private void initFavoriteRecyclerView()
    {
        mFavoriteViewAdapter = new FavoriteViewAdapter(getContext());
        RefreshFavoriteViewListSlot();
        mFavoriteViewAdapter.setHasStableIds(true);

        rv_Main_Today_Favorite.setAdapter(mFavoriteViewAdapter);
        rv_Main_Today_Favorite.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_Main_Today_Favorite.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_Main_Today_Favorite, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

                mSelectFavoriteKey = mFavoriteViewList.get(position);
                RefreshFavoriteViewListSlot();
                mFavoriteViewAdapter.notifyDataSetChanged();
/*                UserIndex = TKManager.getInstance().UserData_Simple.get(TKManager.getInstance().View_UserList_Hot.get(position)).GetUserIndex();
                CommonFunc.getInstance().GetUserDataInFireBase(UserIndex, MainActivity.mActivity, false);*/
            }
        }));

        rv_Main_Today_Favorite.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    default:
                        rv.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
    }

    private void RefreshFavoriteViewListSlot() {
        mFavoriteViewList.clear();
        Iterator<String> it = TKManager.getInstance().MyData.GetUserFavoriteListKeySet().iterator();

        while(it.hasNext())
        {
            String key = it.next();
            mFavoriteViewList.add(key);
        }

        mFavoriteViewAdapter.setItemCount(TKManager.getInstance().MyData.GetUserFavoriteListCount());
        mFavoriteViewAdapter.setItemData(mFavoriteViewList);
        mFavoriteViewAdapter.setSelectView(true);

        if(CommonFunc.getInstance().CheckStringNull(mSelectFavoriteKey) ||
                mFavoriteViewList.contains(mSelectFavoriteKey) == false)
            mSelectFavoriteKey = mFavoriteViewList.get(0);

        ArrayList<String> selectlist = new ArrayList<>();
        selectlist.add(mSelectFavoriteKey);
        mFavoriteViewAdapter.setSelectItemData(selectlist);
    }
}
