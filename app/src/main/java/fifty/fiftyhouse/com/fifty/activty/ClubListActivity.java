package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.ClubAdapter;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class ClubListActivity extends AppCompatActivity {

    View ui_ClubList_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    TextView tv_ClubList_Empty;
    RecyclerView rv_ClubList;
    ClubAdapter mAdapter;
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
        ClubAdapter.ClubListener listener = new ClubAdapter.ClubListener()
        {
            @Override
            public void Listener(String key)
            {
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
        };
        mAdapter =  new ClubAdapter(mContext, listener);
        RefreshClubList();
        mAdapter.setItemData(mClubList);
        rv_ClubList.setAdapter(mAdapter);
        rv_ClubList.setLayoutManager(new LinearLayoutManager(mContext));
    }

    public void RefreshAdapter()
    {
        ArrayList<String> tempList = new ArrayList<>();
        tempList.addAll(mClubList);

        RefreshClubList();

        if(tempList.equals(mClubList) == false)
        {
            mAdapter.setItemData(mClubList);
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
