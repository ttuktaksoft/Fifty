package fifty.fiftyhouse.com.fifty.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ClubActivity;
import fifty.fiftyhouse.com.fifty.activty.Profile.UserProfileActivity;
import fifty.fiftyhouse.com.fifty.adapter.ChatAdapter;
import fifty.fiftyhouse.com.fifty.adapter.ClubAdapter;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class ClubFragment extends Fragment {

    private Context mContext;
    private View v_FragmentView;

    RecyclerView rv_Club_List;
    ClubAdapter mAdapter;

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
        // Inflate the layout for this fragment
        mContext = getContext();
        v_FragmentView = inflater.inflate(R.layout.fragment_club, container, false);
        rv_Club_List = v_FragmentView.findViewById(R.id.rv_Club_List);

        initRecyclerView();

        return v_FragmentView;
    }

    private void initRecyclerView() {
        mAdapter = new ClubAdapter(getContext());
        mAdapter.setHasStableIds(true);

        rv_Club_List.setAdapter(mAdapter);
        rv_Club_List.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rv_Club_List.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_Club_List, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getContext(), ClubActivity.class));
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

        rv_Club_List.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

}
