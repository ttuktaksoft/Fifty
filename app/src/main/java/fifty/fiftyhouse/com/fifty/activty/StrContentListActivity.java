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

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.StrContentListAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class StrContentListActivity extends AppCompatActivity {

    View ui_StrContentList_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    Context mContext;

    RecyclerView rv_StrContent_List;
    StrContentListAdapter mAdapter;
    CommonData.MyProfileMenuType Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_str_content_list);
        mContext = getApplicationContext();
        ui_StrContentList_TopBar = findViewById(R.id.ui_StrContentList_TopBar);
        tv_TopBar_Title = ui_StrContentList_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_StrContentList_TopBar.findViewById(R.id.iv_TopBar_Back);
        rv_StrContent_List = findViewById(R.id.rv_StrContent_List);

        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent(); //getIntent()로 받을준비
        int ntype = getIntent().getIntExtra("Type", 0);

        switch (ntype)
        {
            case 0:
                tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_EVENT));
                Type = CommonData.MyProfileMenuType.EVENT;
                break;
            case 1:
                tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_NOTICE_LONG));
                Type = CommonData.MyProfileMenuType.NOTICE;
                break;
            case 2:
                tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_FAQ));
                Type = CommonData.MyProfileMenuType.FAQ;
                break;
/*            case 3:
                tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_SETTING));
                Type = CommonData.MyProfileMenuType.SETTING;
                break;*/
        }

        initRecyclerView();
    }


    private void initRecyclerView()
    {
        mAdapter = new StrContentListAdapter(getApplicationContext());
        mAdapter.setHasStableIds(true);

        rv_StrContent_List.setAdapter(mAdapter);
        rv_StrContent_List.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv_StrContent_List.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_StrContent_List, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

                if(TKManager.getInstance().NoticeData.get(position).NoticeType == CommonData.NOTICE_TYPE_STR)
                {
                    Intent intent = new Intent(getApplicationContext(), StrContentActivity.class);
                    intent.putExtra("title", TKManager.getInstance().NoticeData.get(position).GetTitle());
                    intent.putExtra("content", TKManager.getInstance().NoticeData.get(position).GetContent());
                    startActivity(intent);
                }
                else
                {
                    Intent intent = new Intent(getApplicationContext(), ImgContentActivity.class);
                    intent.putExtra("title", TKManager.getInstance().NoticeData.get(position).GetTitle());
                    intent.putExtra("content", TKManager.getInstance().NoticeData.get(position).GetContent());
                    startActivity(intent);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
