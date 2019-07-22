package fifty.fiftyhouse.com.fifty.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ClubActivity;
import fifty.fiftyhouse.com.fifty.activty.FavoriteSelectActivity;
import fifty.fiftyhouse.com.fifty.activty.MyProfileEditActivity;
import fifty.fiftyhouse.com.fifty.adapter.ClubAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.viewPager.ChatViewPager;
import fifty.fiftyhouse.com.fifty.viewPager.ClubListViewPager;

import static android.content.Context.INPUT_METHOD_SERVICE;




public class ClubFragment extends Fragment {

    ViewPager vp_ClubList;
    TabLayout tl_Club_TopTab;

    ClubListViewPager mRecommendClubList = null;
    ClubListViewPager mMyClubList = null;

    private Context mContext;
    private View v_FragmentView = null;

    ImageView iv_Club_TopBar_Search, iv_Club_TopBar_User;
    EditText et_Club_TopBar_Search;
    InputMethodManager imm;

    boolean bSearchClub = false;
    int MY_PROFILE_EDIT = 1;

    public static ClubFragment mClubFragment;

    public ClubFragment() {
        // Required empty public constructor
    }

    public static ClubFragment newInstance(String param1, String param2) {
        ClubFragment fragment = new ClubFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(v_FragmentView != null)
        {
            if(mRecommendClubList != null)
                mRecommendClubList.RefreshRecyclerView();
            if(mMyClubList != null)
                mMyClubList.RefreshRecyclerView();

            return v_FragmentView;
        }

        // Inflate the layout for this fragment
        mContext = getContext();
        v_FragmentView = inflater.inflate(R.layout.fragment_club, container, false);
        imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        mClubFragment = this;

        iv_Club_TopBar_Search = v_FragmentView.findViewById(R.id.iv_Club_TopBar_Search);
        iv_Club_TopBar_User = v_FragmentView.findViewById(R.id.iv_Club_TopBar_User);
        et_Club_TopBar_Search = v_FragmentView.findViewById(R.id.et_Club_TopBar_Search);

        vp_ClubList = v_FragmentView.findViewById(R.id.vp_ClubList);
        tl_Club_TopTab = v_FragmentView.findViewById(R.id.tl_Club_TopTab);

        TKManager.getInstance().mUpdateClubFragmentkeybordDownFunc = new TKManager.UpdateUIFunc() {
            @Override
            public void UpdateUI() {
                imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);
            }
        };

        et_Club_TopBar_Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        // 검색 동작
                        imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);
                        SearchClub(et_Club_TopBar_Search.getText().toString());

                        break;
                    default:
                        // 기본 엔터키 동작
                        imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);
                        SearchClub(et_Club_TopBar_Search.getText().toString());
                        return false;
                }
                return true;
            }
        });


        iv_Club_TopBar_Search.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);

                SearchClub(et_Club_TopBar_Search.getText().toString());
            }
        });

        iv_Club_TopBar_Search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);
                    SearchClub(et_Club_TopBar_Search.getText().toString());
                    return true;
                }
                return false;
            }
        });

        Glide.with(mContext).load(TKManager.getInstance().MyData.GetUserImgThumb())
                .centerCrop()
                .circleCrop()
                .into(iv_Club_TopBar_User);

        iv_Club_TopBar_User.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);

                startActivityForResult(new Intent(mContext, MyProfileEditActivity.class), MY_PROFILE_EDIT);
            }
        });

        tl_Club_TopTab.addTab(tl_Club_TopTab.newTab().setText(getResources().getString(R.string.MSG_CLUB_LIST_RECOMMEND)));
        tl_Club_TopTab.addTab(tl_Club_TopTab.newTab().setText(getResources().getString(R.string.MSG_CLUB_LIST_MY)));
        tl_Club_TopTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp_ClubList.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vp_ClubList.setOffscreenPageLimit(3);
        vp_ClubList.setAdapter(new TabPagerAdapter(getFragmentManager(), tl_Club_TopTab.getTabCount()));
        vp_ClubList.setCurrentItem(0);
        vp_ClubList.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl_Club_TopTab));

        /*tv_Club_Create.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);
                TKManager.getInstance().CreateTempClubData.ClubFavorite.clear();
                DialogFunc.getInstance().ShowLoadingPage(mContext);
                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();
                        Intent intent = new Intent(mContext, FavoriteSelectActivity.class);
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

        tv_Club_Recom.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);
            }
        });*/

        TKManager.getInstance().mUpdateClubFragmentFunc = new TKManager.UpdateUIFunc(){
            @Override
            public void UpdateUI() {
                if(mMyClubList != null)
                    mMyClubList.RefreshRecyclerView();
            }
        };

        return v_FragmentView;
    }

    private void SearchClub(String name)
    {
        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
            @Override
            public void CompleteListener() {
                // TODO 검색한걸 어떻게 보여줄까?
                /*RefreshAdapter();
                mAdapter.notifyDataSetChanged();*/
            }

            @Override
            public void CompleteListener_Yes() {

            }

            @Override
            public void CompleteListener_No() {

            }
        };

        if(CommonFunc.getInstance().CheckStringNull(name))
        {
            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.CLUB_SEARCH_EMPTY));
        }
        else {
            bSearchClub = true;
            FirebaseManager.getInstance().SearchClubList(name, listener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_PROFILE_EDIT) {
            Glide.with(mContext).load(TKManager.getInstance().MyData.GetUserImgThumb())
                    .centerCrop()
                    .circleCrop()
                    .into(iv_Club_TopBar_User);
        }
    }


    private class TabPagerAdapter extends FragmentStatePagerAdapter {
        private  int tabCount;
        public TabPagerAdapter(FragmentManager fm, int tabCount) {
            super(fm);
            this.tabCount =tabCount;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    mRecommendClubList = new ClubListViewPager();
                    return mRecommendClubList;
                case 1:
                    mMyClubList = new ClubListViewPager();
                    return mMyClubList;
            }

            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

    public void KeyboardDown()
    {
        imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);
    }

}
