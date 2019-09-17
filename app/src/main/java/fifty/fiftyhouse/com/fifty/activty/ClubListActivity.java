package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridViewAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.ClubAdapter;
import fifty.fiftyhouse.com.fifty.adapter.CustomClubAdapter;
import fifty.fiftyhouse.com.fifty.adapter.CustomGridListHolder;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemOneClickListener;

public class ClubListActivity extends AppCompatActivity {

    View ui_ClubList_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    TextView tv_ClubList_Empty;
    AsymmetricGridView rv_ClubList;
    CustomClubAdapter mAdapter;
    ArrayList<String> mClubList = new ArrayList<>();

    Context mContext;
    String SelectFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);

        mContext = getApplicationContext();

        Intent intent = getIntent(); //getIntent()로 받을준비
        SelectFavorite = getIntent().getStringExtra("FAVORITE");

        ui_ClubList_TopBar = findViewById(R.id.ui_ClubList_TopBar);
        tv_TopBar_Title = ui_ClubList_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_ClubList_TopBar.findViewById(R.id.iv_TopBar_Back);
        tv_ClubList_Empty = findViewById(R.id.tv_ClubList_Empty);
        rv_ClubList = findViewById(R.id.rv_ClubList);

        if(SelectFavorite.equals("검색결과"))
            tv_TopBar_Title.setText(SelectFavorite);
        else
            tv_TopBar_Title.setText(SelectFavorite +" " + CommonFunc.getInstance().getStr(mContext.getResources(), R.string.TITLE_CLUB_SELECT_FAVORITE));

        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        /*mAdapter = new ClubAdapter(mContext);
        RefreshAdapter();
        mAdapter.setHasStableIds(true);

        rv_ClubList.setAdapter(mAdapter);
        rv_ClubList.setLayoutManager(new GridLayoutManager(mContext, 2));
        rv_ClubList.offsetLeftAndRight(CommonFunc.getInstance().convertDPtoPX(getResources(),20));
        rv_ClubList.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_ClubList, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, final int position) {

                Map<String, ClubData> tempClubKey = new LinkedHashMap<>();

                mClubList.addAll(TKManager.getInstance().SearchClubList);

                //Set tempKey = tempClubKey.keySet(); //TKManager.getInstance().MyData.GetUserClubDataKeySet();
                final List array = new ArrayList(mClubList);

                DialogFunc.getInstance().ShowLoadingPage(ClubListActivity.this);

                FirebaseManager.CheckFirebaseComplete GetClubDataListener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {

                        FirebaseManager.CheckFirebaseComplete GetClubContextListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                DialogFunc.getInstance().DismissLoadingPage();
                                startActivityForResult(new Intent(mContext, ClubActivity.class), 1000);
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

                FirebaseManager.getInstance().GetClubData(TKManager.getInstance().MyData, TKManager.getInstance().ClubData_Simple.get(array.get(position).toString()).GetClubIndex(),
                        GetClubDataListener);
            }
        }));*/

        RefreshClubList();
        mAdapter = new CustomClubAdapter(mContext, CommonFunc.getInstance().getCustomGridListHolderList(mClubList));

        rv_ClubList.setRequestedColumnCount(3);
        rv_ClubList.setAdapter(new AsymmetricGridViewAdapter(mContext, rv_ClubList, mAdapter));
        rv_ClubList.setOnItemClickListener(
                new RecyclerItemOneClickListener() {
                    @Override
                    public void RecyclerItemOneClick(int position) {
                        Map<String, ClubData> tempClubKey = new LinkedHashMap<>();

                        mClubList.addAll(TKManager.getInstance().SearchClubList);

                        //Set tempKey = tempClubKey.keySet(); //TKManager.getInstance().MyData.GetUserClubDataKeySet();
                        final List array = new ArrayList(mClubList);

                        DialogFunc.getInstance().ShowLoadingPage(ClubListActivity.this);

                        FirebaseManager.CheckFirebaseComplete GetClubDataListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {

                                FirebaseManager.CheckFirebaseComplete GetClubContextListener = new FirebaseManager.CheckFirebaseComplete() {
                                    @Override
                                    public void CompleteListener() {
                                        DialogFunc.getInstance().DismissLoadingPage();
                                        startActivityForResult(new Intent(mContext, ClubActivity.class), 1000);
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

                        FirebaseManager.getInstance().GetClubData(TKManager.getInstance().MyData, TKManager.getInstance().ClubData_Simple.get(array.get(position).toString()).GetClubIndex(),
                                GetClubDataListener);
                    }
                });
    }

    public void RefreshAdapter()
    {
        /*RefreshClubList();
        mAdapter.setItemCount(mClubList.size());
        mAdapter.setItemData(mClubList);*/

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





        mClubList.addAll(TKManager.getInstance().SearchClubList);





        if (mClubList.size() == 0) {
            tv_ClubList_Empty.setVisibility(View.VISIBLE);
            rv_ClubList.setVisibility(View.GONE);
        } else {
            tv_ClubList_Empty.setVisibility(View.GONE);
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
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000)
        {
            RefreshRecyclerView();
        }
    }
}
