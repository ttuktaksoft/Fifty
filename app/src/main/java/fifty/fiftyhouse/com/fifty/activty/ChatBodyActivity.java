package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.ChatBodyAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

import static fifty.fiftyhouse.com.fifty.activty.SignUpActivity.GET_FROM_GALLERY;

public class ChatBodyActivity extends AppCompatActivity {

    View ui_ChatBody_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;
    RecyclerView rv_Chat_Body_List;
    ImageView iv_ChatBody_Alert, iv_Chat_Body_Plus, iv_Chat_Body_Send;
    EditText et_Chat_Body_Msg;

    Context mContext;
    Activity mActivity;
    String strRoomIndex;
    String strTargetIndex;

    ChatBodyAdapter mAdapter;

    InputMethodManager imm;
    File tempFile;
    boolean isProfileUpload = false;
    boolean isCamera = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_body);
        mContext = getApplicationContext();
        mActivity = this;

        Intent intent = getIntent(); //getIntent()로 받을준비
        strRoomIndex = getIntent().getStringExtra("RoomIndex");
        int idx = strRoomIndex.indexOf("_");
        String tempStr = strRoomIndex.substring(0, idx);
        String tempStrBack = strRoomIndex.substring(idx+1);
        if(tempStr.equals(TKManager.getInstance().MyData.GetUserIndex()))
        {
            strTargetIndex = tempStrBack;
        }
        else
        {
            strTargetIndex = tempStr;
        }

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);



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

        iv_Chat_Body_Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFunc.getInstance().GetPermissionForGallery(ChatBodyActivity.this, GET_FROM_GALLERY);
            }
        });

        iv_Chat_Body_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendChatData(CommonData.MSGType.MSG);
            }
        });
/*
        et_Chat_Body_Msg.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    SendChatData(CommonData.MSGType.MSG);
                    return true;
                }
                return false;
            }
        });*/

        iv_ChatBody_Alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> menuList = new ArrayList<>();
                menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_REPORT_MENU_REPORT));
                menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_REPORT_MENU_BLOCK));
                menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CANCEL));

                ArrayList<DialogFunc.MsgPopupListener> menuListenerList = new ArrayList<>();
                menuListenerList.add(new DialogFunc.MsgPopupListener()
                {
                    @Override
                    public void Listener()
                    {
                        startActivity(new Intent(mContext, UserReportActivity.class));
                    }
                });
                menuListenerList.add(new DialogFunc.MsgPopupListener()
                {
                    @Override
                    public void Listener()
                    {
                        DialogFunc.getInstance().ShowToast(mContext, CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_BLOCK_COMPLETE), true);
                        finish();
                    }
                });

                DialogFunc.getInstance().ShowMenuListPopup(ChatBodyActivity.this, menuList, menuListenerList);
            }
        });

        tv_TopBar_Title.setText(TKManager.getInstance().UserData_Simple.get(strTargetIndex).GetUserNickName());

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


        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
            @Override
            public void CompleteListener() {
                mAdapter.notifyDataSetChanged();
                rv_Chat_Body_List.scrollToPosition(mAdapter.getItemCount() - 1);
            }

            @Override
            public void CompleteListener_Yes() {
            }

            @Override
            public void CompleteListener_No() {
            }
        };

        FirebaseManager.getInstance().MonitorChatData(strRoomIndex, TKManager.getInstance().MyData, listener);

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == GET_FROM_GALLERY) {
            if(data != null && data.getData() != null)
            {
                Uri photoUri = data.getData();
                Cursor cursor = null;

                try {
                    String[] proj = { MediaStore.Images.Media.DATA };
                    assert photoUri != null;
                    cursor = getContentResolver().query(photoUri, proj, null, null, null);

                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    cursor.moveToFirst();
                    tempFile = new File(cursor.getString(column_index));

                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }

                DialogFunc.getInstance().ShowLoadingPage(ChatBodyActivity.this);

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        isProfileUpload = true;
                        DialogFunc.getInstance().DismissLoadingPage();

                        SendChatData(CommonData.MSGType.IMG);
                    }

                    @Override
                    public void CompleteListener_Yes() {
                    }

                    @Override
                    public void CompleteListener_No() {
                    }
                };

                CommonFunc.getInstance().SetImageInChatRoom(ChatBodyActivity.this, tempFile, isCamera, strRoomIndex,  listener);

            }

        }
    }


    private void SendChatData(CommonData.MSGType type)
    {
        ChatData tempData = new ChatData();

        tempData.SetRoomIndex(strRoomIndex);

        tempData.SetFromIndex(TKManager.getInstance().MyData.GetUserIndex());
        tempData.SetFromNickName(TKManager.getInstance().MyData.GetUserNickName());
        tempData.SetFromThumbNail(TKManager.getInstance().MyData.GetUserImgThumb());

        if( type == CommonData.MSGType.MSG)
            tempData.SetMsg(et_Chat_Body_Msg.getText().toString());
        else
            tempData.SetMsg(TKManager.getInstance().MyData.GetUserImgChat());

        tempData.SetMsgSender(TKManager.getInstance().MyData.GetUserIndex());
        tempData.SetMsgType(type);
        tempData.SetMsgReadCheck(false);
        tempData.SetMsgDate(Long.parseLong(CommonFunc.getInstance().GetCurrentTime()));

        tempData.SetToIndex(strTargetIndex);
        tempData.SetToNickName(TKManager.getInstance().UserData_Simple.get(strTargetIndex).GetUserNickName());
        tempData.SetToThumbNail(TKManager.getInstance().UserData_Simple.get(strTargetIndex).GetUserImgThumb());

        FirebaseManager.getInstance().AddChatData(strRoomIndex,tempData);

        imm.hideSoftInputFromWindow(et_Chat_Body_Msg.getWindowToken(), 0);
        et_Chat_Body_Msg.setText(null);
    }
}
