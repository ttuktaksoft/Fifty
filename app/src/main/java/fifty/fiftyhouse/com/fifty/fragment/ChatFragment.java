package fifty.fiftyhouse.com.fifty.fragment;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.internal.service.Common;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ChatBodyActivity;
import fifty.fiftyhouse.com.fifty.activty.ClubActivity;
import fifty.fiftyhouse.com.fifty.activty.FriendListActivity;
import fifty.fiftyhouse.com.fifty.activty.UserListActivity;
import fifty.fiftyhouse.com.fifty.adapter.ChatAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.viewPager.ChatViewPager;
import fifty.fiftyhouse.com.fifty.viewPager.FriendListViewPager;
import fifty.fiftyhouse.com.fifty.viewPager.FriendRequestListViewPager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {

    ViewPager vp_ChatList;
    TabLayout tl_ChatList_TopTab;

    ChatViewPager mBookmarkViewPager = null;
    ChatViewPager mDefaultViewPager = null;

    Context mContext;
    private View ChatFragView = null;

    public ChatFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static ChatFragment newInstance(String param1, String param2) {
        ChatFragment fragment = new ChatFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
            //CommonFunc.getInstance().RefreshChatListData(mAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        if(ChatFragView != null)
            return ChatFragView;

        mContext = getContext();
        ChatFragView = inflater.inflate(R.layout.fragment_chat, container, false);
        ChatFragView.setTag("ChatFragment");

        vp_ChatList = ChatFragView.findViewById(R.id.vp_ChatList);
        tl_ChatList_TopTab = ChatFragView.findViewById(R.id.tl_ChatList_TopTab);

        tl_ChatList_TopTab.addTab(tl_ChatList_TopTab.newTab().setText(getResources().getString(R.string.MSG_CHAT_LIST_BOOKMARK)));
        tl_ChatList_TopTab.addTab(tl_ChatList_TopTab.newTab().setText(getResources().getString(R.string.MSG_CHAT_LIST_DEFAULT)));
        tl_ChatList_TopTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch(tab.getPosition()){
                    case 0:
                        mBookmarkViewPager.moveViewPager();
                        mBookmarkViewPager.RefreshAdapter();
                        break;
                    case 1:
                        mDefaultViewPager.moveViewPager();
                        mDefaultViewPager.RefreshAdapter();
                        break;
                }
                vp_ChatList.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vp_ChatList.setOffscreenPageLimit(3);
        vp_ChatList.setAdapter(new TabPagerAdapter(getFragmentManager(), tl_ChatList_TopTab.getTabCount()));
        vp_ChatList.setCurrentItem(0);

        vp_ChatList.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tl_ChatList_TopTab));

        return ChatFragView;
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
                    mBookmarkViewPager = new ChatViewPager();
                    mBookmarkViewPager.mType = ChatViewPager.CHAT_LIST_TYPE_BOOKMARK;
                    mBookmarkViewPager.moveViewPager();
                    return mBookmarkViewPager;
                case 1:
                    mDefaultViewPager = new ChatViewPager();
                    mDefaultViewPager.mType = ChatViewPager.CHAT_LIST_TYPE_DEFAULT;
                    mDefaultViewPager.moveViewPager();
                    return mDefaultViewPager;
            }

            return null;
        }

        @Override
        public int getCount() {
            return tabCount;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
