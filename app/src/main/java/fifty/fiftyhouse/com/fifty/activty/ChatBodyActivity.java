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

import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.SignUp.SignUpActivity;
import fifty.fiftyhouse.com.fifty.adapter.ChatBodyAdapter;
import fifty.fiftyhouse.com.fifty.adapter.ClubContentAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class ChatBodyActivity extends AppCompatActivity {

    View ui_ChatBody_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    RecyclerView rv_Chat_Body_List;
    ImageView iv_ChatBody_Alert, iv_Chat_Body_Plus, iv_Chat_Body_Send;
    EditText et_Chat_Body_Msg;

    ChatBodyAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_body);

        ui_ChatBody_TopBar = findViewById(R.id.ui_ChatBody_TopBar);
        tv_TopBar_Title = ui_ChatBody_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_ChatBody_TopBar.findViewById(R.id.iv_TopBar_Back);
        rv_Chat_Body_List = findViewById(R.id.rv_Chat_Body_List);
        iv_ChatBody_Alert = findViewById(R.id.iv_ChatBody_Alert);
        iv_Chat_Body_Plus = findViewById(R.id.iv_Chat_Body_Plus);
        iv_Chat_Body_Send = findViewById(R.id.iv_Chat_Body_Send);
        et_Chat_Body_Msg = findViewById(R.id.et_Chat_Body_Msg);

        iv_TopBar_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_TopBar_Title.setText("닉네임추가");

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
