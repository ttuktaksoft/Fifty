package fifty.fiftyhouse.com.fifty.activty;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.ChatBodyAdapter;
import fifty.fiftyhouse.com.fifty.adapter.ClubContentAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class ChatBodyActivity extends AppCompatActivity {
    Toolbar v_ChatBody_ToolBar;
    RecyclerView rv_Chat_Body_List;
    Button bt_Chat_Body_Plus, bt_Chat_Body_Send;
    EditText et_Chat_Body_Msg;

    ChatBodyAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_body);

        v_ChatBody_ToolBar = findViewById(R.id.v_ChatBody_ToolBar);
        rv_Chat_Body_List = findViewById(R.id.rv_Chat_Body_List);
        bt_Chat_Body_Plus = findViewById(R.id.bt_Chat_Body_Plus);
        bt_Chat_Body_Send = findViewById(R.id.bt_Chat_Body_Send);
        et_Chat_Body_Msg = findViewById(R.id.et_Chat_Body_Msg);

        v_ChatBody_ToolBar.setNavigationIcon(R.drawable.icon_backarrow);
        v_ChatBody_ToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        v_ChatBody_ToolBar.setTitle("피프티");
        setSupportActionBar(v_ChatBody_ToolBar);

        initRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private void initRecyclerView()
    {
        mAdapter = new ChatBodyAdapter(getApplicationContext());
        mAdapter.setHasStableIds(true);

        rv_Chat_Body_List.setAdapter(mAdapter);
        rv_Chat_Body_List.setLayoutManager(new LinearLayoutManager(this)) ;
        //rv_Chat_Body_List.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rv_Chat_Body_List.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rv_Chat_Body_List, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //startActivity(new Intent(getApplicationContext(), ClubBodyActivity.class));
                //startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
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

        rv_Chat_Body_List.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
