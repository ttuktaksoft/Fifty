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
import fifty.fiftyhouse.com.fifty.activty.Profile.UserProfileActivity;
import fifty.fiftyhouse.com.fifty.adapter.ClubContentAdapter;
import fifty.fiftyhouse.com.fifty.adapter.UserListAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class UserListActivity extends AppCompatActivity {

    ImageView iv_UserList_Back;
    TextView tv_UserList_Title;
    RecyclerView rv_UserList_List;
    UserListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        iv_UserList_Back = findViewById(R.id.iv_UserList_Back);
        tv_UserList_Title = findViewById(R.id.tv_UserList_Title);
        rv_UserList_List = findViewById(R.id.rv_UserList_List);

        iv_UserList_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initRecyclerView();
    }


    private void initRecyclerView()
    {
        mAdapter = new UserListAdapter(getApplicationContext());
        mAdapter.setHasStableIds(true);

        rv_UserList_List.setAdapter(mAdapter);
        rv_UserList_List.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv_UserList_List.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_UserList_List, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }
}
