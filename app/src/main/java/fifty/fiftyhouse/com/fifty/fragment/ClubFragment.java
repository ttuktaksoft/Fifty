package fifty.fiftyhouse.com.fifty.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
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

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ClubCreateActivity;
import fifty.fiftyhouse.com.fifty.activty.ClubListActivity;
import fifty.fiftyhouse.com.fifty.activty.MyProfileEditActivity;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
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

        tl_Club_TopTab.addTab(tl_Club_TopTab.newTab().setText(getResources().getString(R.string.MSG_MAIN_TAB_CLUB_RECOMMEND)));
        tl_Club_TopTab.addTab(tl_Club_TopTab.newTab().setText(getResources().getString(R.string.MSG_MAIN_TAB_CLUB_MY)));
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
                //RefreshViewPager();
                //mAdapter.notifyDataSetChanged();

                Log.d("@@$#$@", "2342345234");

                DialogFunc.getInstance().DismissLoadingPage();

                Intent intent = new Intent(mContext, ClubListActivity.class);
                intent.putExtra("FAVORITE","검색결과");
                startActivity(intent);


            }

            @Override
            public void CompleteListener_Yes() {

            }

            @Override
            public void CompleteListener_No() {

                DialogFunc.getInstance().DismissLoadingPage();

                final DialogFunc.MsgPopupListener listenerYes = new DialogFunc.MsgPopupListener() {
                    @Override
                    public void Listener() {
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
                };
                DialogFunc.getInstance().ShowMsgPopup(mContext, listenerYes, null, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_CLUB_SEARCH_RESULT_EMPTY),
                        CommonFunc.getInstance().getStr(getResources(), R.string.TITLE_CREAT_CLUB), CommonFunc.getInstance().getStr(getResources(), R.string.MSG_CANCEL));
            }
        };

        if(CommonFunc.getInstance().CheckStringNull(name))
        {
            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_CLUB_SEARCH_EMPTY));
        }
        else {
            DialogFunc.getInstance().ShowLoadingPage(mContext);
            bSearchClub = true;
            et_Club_TopBar_Search.setText(null);
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
        else if(requestCode == 1000)
        {
            RefreshViewPager();
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
                    mRecommendClubList.setViewPagerType(ClubListViewPager.CLUB_LIST_RECOMMEND);
                    return mRecommendClubList;
                case 1:
                    mMyClubList = new ClubListViewPager();
                    mMyClubList.setViewPagerType(ClubListViewPager.CLUB_LIST_MY);
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

    public void RefreshViewPager()
    {
        if(mRecommendClubList != null)
            mRecommendClubList.RefreshRecyclerView();
        if(mMyClubList != null)
            mMyClubList.RefreshRecyclerView();
    }

}
