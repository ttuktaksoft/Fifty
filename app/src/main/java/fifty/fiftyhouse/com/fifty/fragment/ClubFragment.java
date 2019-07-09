package fifty.fiftyhouse.com.fifty.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ClubActivity;
import fifty.fiftyhouse.com.fifty.adapter.ClubAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class ClubFragment extends Fragment {

    private Context mContext;
    private View v_FragmentView;

    private Button  bt_Club_TopBar_User;


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
        bt_Club_TopBar_User = v_FragmentView.findViewById(R.id.bt_Club_TopBar_User);

        initRecyclerView();

        return v_FragmentView;
    }

    private void initRecyclerView() {
        mAdapter = new ClubAdapter(getContext());
        mAdapter.setHasStableIds(true);

        rv_Club_List.setAdapter(mAdapter);
        rv_Club_List.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rv_Club_List.offsetLeftAndRight(CommonFunc.getInstance().convertDPtoPX(getResources(),20));
        rv_Club_List.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_Club_List, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, final int position) {

                Set tempKey = TKManager.getInstance().MyData.GetUserClubDataKeySet();
                final List array = new ArrayList(tempKey);

                //DialogFunc.getInstance().ShowLoadingPage(MainActivity.mActivity);

                FirebaseManager.CheckFirebaseComplete GetClubDataListener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {

                        FirebaseManager.CheckFirebaseComplete GetClubContextListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
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

                FirebaseManager.getInstance().GetClubData(TKManager.getInstance().ClubData_Simple.get(array.get(position).toString()).GetClubIndex(),
                        TKManager.getInstance().TargetClubData, GetClubDataListener);


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

        bt_Club_TopBar_User.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                final ClubData tempClub = new ClubData();
                tempClub.SetClubMasterIndex(TKManager.getInstance().MyData.GetUserIndex());
                tempClub.SetClubName("테스트");
                tempClub.SetClubThumb(TKManager.getInstance().MyData.GetUserImgThumb());
                tempClub.SetClubType(true);

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {

                        FirebaseManager.CheckFirebaseComplete registClubListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {

                                Log.d("%%%%%%", "Club");
                            }

                            @Override
                            public void CompleteListener_Yes() {

                            }

                            @Override
                            public void CompleteListener_No() {

                            }
                        };

                        FirebaseManager.getInstance().RegistClubList(tempClub, registClubListener);

                    }

                    @Override
                    public void CompleteListener_Yes() {

                    }

                    @Override
                    public void CompleteListener_No() {

                    }
                };

                FirebaseManager.getInstance().RegistClubIndex(tempClub, listener);

            }
        });
    }

}
