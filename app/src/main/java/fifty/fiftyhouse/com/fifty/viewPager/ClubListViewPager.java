package fifty.fiftyhouse.com.fifty.viewPager;

import android.content.Intent;
import android.os.Bundle;

import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ClubActivity;
import fifty.fiftyhouse.com.fifty.activty.ClubCreateActivity;
import fifty.fiftyhouse.com.fifty.activty.ClubFavoriteActivity;
import fifty.fiftyhouse.com.fifty.adapter.CustomClubAdapter;
import fifty.fiftyhouse.com.fifty.adapter.CustomGridListHolder;
import fifty.fiftyhouse.com.fifty.fragment.ClubFragment;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemOneClickListener;

public class ClubListViewPager extends Fragment {

    AsymmetricGridView rv_ClubList;
    TextView tv_Club_Empty;
    FloatingActionButton fa_Club_Create, fa_Club_Favorite;
    View v_FragmentView = null;

    CustomClubAdapter mAdapter;
    ArrayList<String> mClubList = new ArrayList<>();

    public static int CLUB_LIST_RECOMMEND = 0;
    public static int CLUB_LIST_MY = 1;

    public int mType = CLUB_LIST_RECOMMEND;

    public ClubListViewPager() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v_FragmentView == null) {
            v_FragmentView = inflater.inflate(R.layout.viewpager_club_list, container, false);
            rv_ClubList = v_FragmentView.findViewById(R.id.rv_ClubList);
            tv_Club_Empty = v_FragmentView.findViewById(R.id.tv_Club_Empty);
            fa_Club_Create = v_FragmentView.findViewById(R.id.fa_Club_Create);
            fa_Club_Favorite = v_FragmentView.findViewById(R.id.fa_Club_Favorite);


            fa_Club_Favorite.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    TKManager.getInstance().mUpdateClubFragmentkeybordDownFunc.UpdateUI();

                    DialogFunc.getInstance().ShowLoadingPage(ClubFragment.mClubFragment.getContext());

                    if(TKManager.getInstance().FavoriteLIst_ClubList.size() == 0)
                    {
                        FirebaseManager.CheckFirebaseComplete FavoriteClubList = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                long seed = System.nanoTime();
                                Collections.shuffle( TKManager.getInstance().FavoriteLIst_ClubList, new Random(seed));

                                Intent intent = new Intent(getContext(), ClubFavoriteActivity.class);
                                startActivityForResult(intent, 1000);
                                DialogFunc.getInstance().DismissLoadingPage();
                            }

                            @Override
                            public void CompleteListener_Yes() {

                            }

                            @Override
                            public void CompleteListener_No() {

                            }
                        };

                        FirebaseManager.getInstance().GetUserFavoriteClubList(FavoriteClubList);
                    }
                    else
                    {
                        Intent intent = new Intent(getContext(), ClubFavoriteActivity.class);
                        startActivity(intent);
                        DialogFunc.getInstance().DismissLoadingPage();
                    }
                }
            });

            fa_Club_Create.setOnClickListener(new OnSingleClickListener() {
                @Override
                public void onSingleClick(View view) {
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
            });

            RefreshPagerTypeUI();
            initRecyclerView();
        } else {
            RefreshRecyclerView();
        }

        return v_FragmentView;
    }

    private void RefreshPagerTypeUI()
    {
        fa_Club_Favorite.hide();
        fa_Club_Create.hide();

        if(mType == CLUB_LIST_RECOMMEND)
        {
            fa_Club_Favorite.show();
            tv_Club_Empty.setText(R.string.MSG_RECOM_CLUB_EMPTY);
        }
        else
        {
            tv_Club_Empty.setText(R.string.MSG_CLUB_EMPTY);
            fa_Club_Create.show();
        }
    }

    public void setViewPagerType(int type)
    {
        mType = type;
    }

    private void initRecyclerView() {
        /*mAdapter = new ClubAdapter(getContext());
        RefreshAdapter();
        mAdapter.setHasStableIds(true);

        rv_ClubList.setAdapter(mAdapter);
        rv_ClubList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rv_ClubList.offsetLeftAndRight(CommonFunc.getInstance().convertDPtoPX(getResources(),20));
        rv_ClubList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_ClubList, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, final int position) {

                Map<String, ClubData> tempClubKey = new LinkedHashMap<>();


                if(mType == CLUB_LIST_RECOMMEND)
                {
                    tempClubKey.putAll(TKManager.getInstance().MyData.GetUserRecommendClubData());
                }
                else
                {
                    tempClubKey.putAll(TKManager.getInstance().MyData.GetUserClubData());
                    tempClubKey.putAll(TKManager.getInstance().MyData.GetRequestJoinClubList());
                }



                //tempClubKey.putAll(TKManager.getInstance().SearchClubList);

                Set tempKey = tempClubKey.keySet(); //TKManager.getInstance().MyData.GetUserClubDataKeySet();

                List array = new ArrayList();
                array = new ArrayList(tempKey);
                DialogFunc.getInstance().ShowLoadingPage(MainActivity.mActivity);

                final List finalArray = array;
                FirebaseManager.CheckFirebaseComplete GetClubDataListener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {

                        FirebaseManager.CheckFirebaseComplete GetClubContextListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                DialogFunc.getInstance().DismissLoadingPage();
                                startActivityForResult(new Intent(getContext(), ClubActivity.class), 1000);
                            }

                            @Override
                            public void CompleteListener_Yes() {}
                            @Override
                            public void CompleteListener_No() {}
                        };

                        FirebaseManager.getInstance().GetClubContextData(TKManager.getInstance().ClubData_Simple.get(finalArray.get(position).toString()).GetClubIndex(), GetClubContextListener);
                    }

                    @Override
                    public void CompleteListener_Yes() {}
                    @Override
                    public void CompleteListener_No() {}
                };

                FirebaseManager.getInstance().GetClubData(TKManager.getInstance().MyData, TKManager.getInstance().ClubData_Simple.get(array.get(position).toString()).GetClubIndex(),
                        GetClubDataListener);
            }
        }));*/

        RefreshClubList();
        mAdapter = new CustomClubAdapter(getContext(), CommonFunc.getInstance().getCustomGridListHolderList(mClubList));

        rv_ClubList.setRequestedColumnCount(3);
        rv_ClubList.setAdapter(new AsymmetricGridViewAdapter(getContext(), rv_ClubList, mAdapter));
        rv_ClubList.setOnItemClickListener(
                new RecyclerItemOneClickListener() {
                    @Override
                    public void RecyclerItemOneClick(int position) {
                        /*Map<String, ClubData> tempClubKey = new LinkedHashMap<>();


                        if(mType == CLUB_LIST_RECOMMEND)
                        {
                            tempClubKey.putAll(TKManager.getInstance().MyData.GetUserRecommendClubData());
                        }
                        else
                        {
                            tempClubKey.putAll(TKManager.getInstance().MyData.GetUserClubData());
                            tempClubKey.putAll(TKManager.getInstance().MyData.GetRequestJoinClubList());
                        }



                        //tempClubKey.putAll(TKManager.getInstance().SearchClubList);

                        Set tempKey = tempClubKey.keySet(); //TKManager.getInstance().MyData.GetUserClubDataKeySet();

                        List array = new ArrayList();
                        array = new ArrayList(tempKey);*/
                        String key = mClubList.get(position);
                        DialogFunc.getInstance().ShowLoadingPage(MainActivity.mActivity);


                        FirebaseManager.CheckFirebaseComplete GetClubDataListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {

                                FirebaseManager.CheckFirebaseComplete GetClubContextListener = new FirebaseManager.CheckFirebaseComplete() {
                                    @Override
                                    public void CompleteListener() {
                                        DialogFunc.getInstance().DismissLoadingPage();
                                        startActivityForResult(new Intent(getContext(), ClubActivity.class), 1000);
                                    }

                                    @Override
                                    public void CompleteListener_Yes() {}
                                    @Override
                                    public void CompleteListener_No() {}
                                };

                                FirebaseManager.getInstance().GetClubContextData(TKManager.getInstance().ClubData_Simple.get(key).GetClubIndex(), GetClubContextListener);
                            }

                            @Override
                            public void CompleteListener_Yes() {}
                            @Override
                            public void CompleteListener_No() {}
                        };

                        FirebaseManager.getInstance().GetClubData(TKManager.getInstance().MyData, TKManager.getInstance().ClubData_Simple.get(key).GetClubIndex(),
                                GetClubDataListener);
                    }
                });
    }

    public void RefreshAdapter()
    {
        ArrayList<String> tempList = new ArrayList<>();
        tempList.addAll(mClubList);

        RefreshClubList();

        if(tempList.equals(mClubList) == false)
        {
            List<CustomGridListHolder> list = CommonFunc.getInstance().getCustomGridListHolderList(mClubList);
            mAdapter.setItems(list);
        }
    }

    public void RefreshClubList()
    {
        mClubList.clear();

        if(mType == CLUB_LIST_RECOMMEND)
        {
            mClubList.addAll(TKManager.getInstance().MyData.GetUserRecommendClubDataKeySet());
        }

        else
        {
            mClubList.addAll(TKManager.getInstance().MyData.GetUserClubDataKeySet());
            mClubList.addAll(TKManager.getInstance().MyData.GetRequestJoinClubListKeySet());
        }


        if (mClubList.size() == 0) {
            tv_Club_Empty.setVisibility(View.VISIBLE);
            rv_ClubList.setVisibility(View.GONE);
        } else {
            tv_Club_Empty.setVisibility(View.GONE);
            rv_ClubList.setVisibility(View.VISIBLE);
        }
    }

    public void RefreshRecyclerView()
    {
        RefreshAdapter();

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1000)
        {
            //TKManager.getInstance().SearchClubList.clear();
            ClubFragment.mClubFragment.RefreshViewPager();
/*            RefreshAdapter();
            mAdapter.notifyDataSetChanged();*/
        }
    }
}
