package fifty.fiftyhouse.com.fifty.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.UserNoticeActivity;
import fifty.fiftyhouse.com.fifty.viewPager.MainDistanceViewPager;
import fifty.fiftyhouse.com.fifty.viewPager.MainFriendViewPager;
import fifty.fiftyhouse.com.fifty.viewPager.MainNewViewPager;
import fifty.fiftyhouse.com.fifty.viewPager.MainTodayViewPager;

public class MainFragment extends Fragment {

    ViewPager vp_UserList;
    TabLayout tl_TopBarTab;
    View v_FragmentView = null;

    MainDistanceViewPager mMainDistanceViewPager = null;
    MainNewViewPager mMainNewViewPager = null;
    MainTodayViewPager mMainTodayViewPager = null;
    MainFriendViewPager mMainFriendViewPager = null;

    ImageView iv_Main_Alarm, iv_Main_Shop;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(v_FragmentView == null)
        {
            v_FragmentView = inflater.inflate(R.layout.fragment_main, container, false);
            tl_TopBarTab = v_FragmentView.findViewById(R.id.tl_Main_TopTab);
            vp_UserList = v_FragmentView.findViewById(R.id.vp_Main_UserList);
            iv_Main_Alarm = v_FragmentView.findViewById(R.id.iv_Main_Alarm);
            iv_Main_Shop = v_FragmentView.findViewById(R.id.iv_Main_Shop);

            iv_Main_Alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), UserNoticeActivity.class);
                    startActivity(intent);
                }
            });

            iv_Main_Shop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFunc.getInstance().ShowToast(getContext(), "준비중 입니다", true);
                }
            });

/*
            bt_Main_Sort_Type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CharSequence info[] = new CharSequence[] {GetStr(CommonFunc.getInstance().GetMainSortTypeStrID(CommonData.MainSortType.ALL)), GetStr(CommonFunc.getInstance().GetMainSortTypeStrID(CommonData.MainSortType.ONLINE)) };
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setItems(info, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch(which)
                            {
                                case 0:
                                    mSortType = CommonData.MainSortType.ALL;
                                    bt_Main_Sort_Type.setText(GetStr(CommonFunc.getInstance().GetMainSortTypeStrID(CommonData.MainSortType.ALL)));
                                    break;
                                case 1:
                                    mSortType = CommonData.MainSortType.ONLINE;
                                    bt_Main_Sort_Type.setText(GetStr(CommonFunc.getInstance().GetMainSortTypeStrID(CommonData.MainSortType.ONLINE)));
                                    break;
                            }
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });*/

            tl_TopBarTab.addTab(tl_TopBarTab.newTab().setText(getResources().getString(R.string.MSG_SORT_DISTANCE)));
            tl_TopBarTab.addTab(tl_TopBarTab.newTab().setText(getResources().getString(R.string.MSG_SORT_NEW_MEMBER)));
            tl_TopBarTab.addTab(tl_TopBarTab.newTab().setText(getResources().getString(R.string.MSG_SORT_TODAY_MEMBER)));
            tl_TopBarTab.addTab(tl_TopBarTab.newTab().setText(getResources().getString(R.string.MSG_SORT_MY_FRIEND)));
            tl_TopBarTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    vp_UserList.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            vp_UserList.setOffscreenPageLimit(3);
            vp_UserList.setAdapter(new TabPagerAdapter(getFragmentManager(),tl_TopBarTab.getTabCount()));
            vp_UserList.setCurrentItem(0);
            vp_UserList.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl_TopBarTab));
        }

        return v_FragmentView;
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
                    mMainDistanceViewPager = new MainDistanceViewPager();
                    return mMainDistanceViewPager;
                case 1:
                    mMainNewViewPager = new MainNewViewPager();
                    return mMainNewViewPager;
                case 2:
                    mMainTodayViewPager = new MainTodayViewPager();
                    return mMainTodayViewPager;
                case 3:
                    mMainFriendViewPager = new MainFriendViewPager();
                    return mMainFriendViewPager;
            }

            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
