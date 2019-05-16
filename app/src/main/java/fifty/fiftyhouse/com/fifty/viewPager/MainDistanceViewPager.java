package fifty.fiftyhouse.com.fifty.viewPager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.activty.Profile.UserProfileActivity;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

import fifty.fiftyhouse.com.fifty.R;

public class MainDistanceViewPager extends Fragment {

    RecyclerView rv_Main_Dis_UserList;
    View v_FragmentView = null;
    public MainAdapter mAdapter;

    public MainDistanceViewPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(v_FragmentView == null)
        {
            v_FragmentView = inflater.inflate(R.layout.viewpager_main_distance, container, false);
            rv_Main_Dis_UserList = v_FragmentView.findViewById(R.id.rv_Main_Dis_UserList);

            mAdapter = new MainAdapter(getContext());
            mAdapter.setHasStableIds(true);

            rv_Main_Dis_UserList.setAdapter(mAdapter);
            rv_Main_Dis_UserList.setLayoutManager(new GridLayoutManager(getContext(), 1));
            rv_Main_Dis_UserList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_Main_Dis_UserList, new RecyclerItemClickListener.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    startActivity(new Intent(getContext(), UserProfileActivity.class));
                /*//CommonFunc.getInstance().ShowToast(view.getContext(), position+"번 째 아이템 클릭", true);
                if (mAppStatus.bCheckMultiSend == false) {
                    stTargetData = mMyData.arrUserAll_Hot_Age.get(position);

                    if (mCommon.getClickStatus() == false)
                        mCommon.MoveUserPage(getActivity(), stTargetData);
                }*/
                }

                @Override
                public void onLongItemClick(View view, int position) {
                    //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
                }
            }));

            rv_Main_Dis_UserList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                /*int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int nSize = 0;
                nSize = recyclerView.getAdapter().getItemCount() - 1;

                if (lastVisibleItemPosition == nSize) {
                    // Toast.makeText(getContext(), "Last Position", Toast.LENGTH_SHORT).show();
                    //    CommonFunc.getInstance().ShowLoadingPage(getContext(), "로딩중");
                    //  FirebaseData.getInstance().GetHotData(RecvAdapter, false);
                }*/
                }
            });
        }
        else
        {
            mAdapter.notifyDataSetChanged();
        }

        return v_FragmentView;
    }
}
