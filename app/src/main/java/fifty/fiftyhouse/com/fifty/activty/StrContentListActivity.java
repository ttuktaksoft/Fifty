package fifty.fiftyhouse.com.fifty.activty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.StrContentListAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class StrContentListActivity extends AppCompatActivity {

    ImageView iv_StrContentList_Back;
    TextView tv_StrContentList_Title;
    RecyclerView rv_StrContent_List;
    StrContentListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_str_content_list);

        iv_StrContentList_Back = findViewById(R.id.iv_StrContentList_Back);
        tv_StrContentList_Title = findViewById(R.id.tv_StrContentList_Title);
        rv_StrContent_List = findViewById(R.id.rv_StrContent_List);

        iv_StrContentList_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initRecyclerView();
    }


    private void initRecyclerView()
    {
        mAdapter = new StrContentListAdapter(getApplicationContext());
        mAdapter.setHasStableIds(true);

        rv_StrContent_List.setAdapter(mAdapter);
        rv_StrContent_List.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv_StrContent_List.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_StrContent_List, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getApplicationContext(), StrContentActivity.class));
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
