package fifty.fiftyhouse.com.fifty.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.R;

public class MainFragment extends Fragment {

    ViewPager vp_UserList;
    TabLayout tl_TopBarTab;
    TextView tv_Main_Curr_Pos;
    Button bt_Main_Sort_Type;
    View v_FragmentView = null;

    public CommonData.MainSortType mSortType = CommonData.MainSortType.ALL;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v_FragmentView = inflater.inflate(R.layout.fragment_main, container, false);
        tl_TopBarTab = v_FragmentView.findViewById(R.id.tl_Main_TopTab);
        vp_UserList = v_FragmentView.findViewById(R.id.vp_Main_UserList);
        tv_Main_Curr_Pos = v_FragmentView.findViewById(R.id.tv_Main_Curr_Pos);
        bt_Main_Sort_Type = v_FragmentView.findViewById(R.id.bt_Main_Sort_Type);
        mSortType = CommonData.MainSortType.ALL;

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
                                break;
                            case 1:
                                mSortType = CommonData.MainSortType.ONLINE;
                                break;
                        }
                        dialog.dismiss();
                    }
                });
                builder.show();
            }
        });

        tl_TopBarTab.addTab(tl_TopBarTab.newTab().setText(getResources().getString(R.string.bt_Main_Distance)));
        tl_TopBarTab.addTab(tl_TopBarTab.newTab().setText(getResources().getString(R.string.bt_Main_New)));
        tl_TopBarTab.addTab(tl_TopBarTab.newTab().setText(getResources().getString(R.string.bt_Main_Today)));
        tl_TopBarTab.addTab(tl_TopBarTab.newTab().setText(getResources().getString(R.string.bt_Main_Friend)));
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

        return v_FragmentView;
    }

    public String GetStr(int id)
    {
        return getResources().getString(id);
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
                    return new MainDistanceFragment();
                case 1:
                    return new MainDistanceFragment();
                case 2:
                    return new MainDistanceFragment();
                case 3:
                    return new MainDistanceFragment();
            }

            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }
}
