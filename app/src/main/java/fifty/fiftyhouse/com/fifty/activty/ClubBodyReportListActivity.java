package fifty.fiftyhouse.com.fifty.activty;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.ClubBodyReportAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class ClubBodyReportListActivity extends AppCompatActivity {

    View ui_ClubBodyReportList_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    RecyclerView rv_ClubBodyReportList_List;
    TextView tv_ClubBodyReportList_Empty;
    ClubBodyReportAdapter mAdapter;

    ClubData mTempData = new ClubData();
    ArrayList<String> mReportContentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_body_report_list);

        ui_ClubBodyReportList_TopBar = findViewById(R.id.ui_ClubBodyReportList_TopBar);
        tv_TopBar_Title = ui_ClubBodyReportList_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_ClubBodyReportList_TopBar.findViewById(R.id.iv_TopBar_Back);
        rv_ClubBodyReportList_List = findViewById(R.id.rv_ClubBodyReportList_List);
        tv_ClubBodyReportList_Empty = findViewById(R.id.tv_ClubBodyReportList_Empty);

        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });
        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(getApplication().getResources(), R.string.TITLE_CLUB_BODY_REPORT));

        initRecyclerView();
    }

    private void initRecyclerView()
    {
        mAdapter = new ClubBodyReportAdapter(getApplicationContext());
        RefreshAdapter();
        mAdapter.setHasStableIds(true);

        rv_ClubBodyReportList_List.setAdapter(mAdapter);
        rv_ClubBodyReportList_List.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv_ClubBodyReportList_List.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_ClubBodyReportList_List, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {
                Intent intent = new Intent(ClubBodyReportListActivity.this, ClubBodyActivity.class);
                intent.putExtra("Type",1);
                intent.putExtra("key", mReportContentList.get(position));
                startActivityForResult(intent, 1000);
            }

        }));
    }

    public void RefreshAdapter()
    {
        RefreshList();
        mAdapter.setItemCount(mReportContentList.size());
        mAdapter.setItemData(mReportContentList);
    }

    public void RefreshList()
    {
        // 신고 당한 게시물
        mReportContentList.clear();
        mReportContentList.addAll(TKManager.getInstance().TargetReportContextData.keySet());

        if(mReportContentList.size() == 0)
        {
            tv_ClubBodyReportList_Empty.setVisibility(View.VISIBLE);
            rv_ClubBodyReportList_List.setVisibility(View.GONE);
        }
        else
        {
            tv_ClubBodyReportList_Empty.setVisibility(View.GONE);
            rv_ClubBodyReportList_List.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000)
        {
            RefreshAdapter();
            mAdapter.notifyDataSetChanged();
        }
    }
}
