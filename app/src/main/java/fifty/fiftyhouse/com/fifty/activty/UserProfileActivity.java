package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gun0912.tedpermission.util.ObjectUtils;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.fragment.UserProfileFragment;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class UserProfileActivity extends AppCompatActivity {

    View ui_UserProfile_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    ImageView iv_UserProfile_Alert, iv_UserProfile_BottomBar_Like, iv_UserProfile_BottomBar_Friend;
    TextView tv_UserProfile_BottomBar_Friend;
    ConstraintLayout v_UserProfile_BottomBar_Like, v_UserProfile_BottomBar_Chat, v_UserProfile_BottomBar_Friend;

    Context mContext;
    Activity mActivity;
    FragmentManager mFragmentMgr;

    UserProfileFragment mUserProfileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        mActivity = this;
        mContext = getApplicationContext();

        DialogFunc.getInstance().ShowToast(mContext, TKManager.getInstance().TargetUserData.GetUserIndex(), false);

        mFragmentMgr = getSupportFragmentManager();
        // mFragmentMgr.beginTransaction().addToBackStack(null);

        mUserProfileFragment = new UserProfileFragment();
        mUserProfileFragment.setMyProfileView(false);

        CommonFunc.getInstance().mCurActivity = this;
        FirebaseManager.getInstance().RegistVisitUser(TKManager.getInstance().TargetUserData.GetUserIndex());

        DialogFunc.getInstance().DismissLoadingPage();

        mFragmentMgr.beginTransaction().replace(R.id.fl_UserProfile_FrameLayout, mUserProfileFragment, "UserProfileInfoFragment").commit();

        ui_UserProfile_TopBar = findViewById(R.id.ui_UserProfile_TopBar);
        tv_TopBar_Title = ui_UserProfile_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_UserProfile_TopBar.findViewById(R.id.iv_TopBar_Back);
        iv_UserProfile_Alert = findViewById(R.id.iv_UserProfile_Alert);
        iv_UserProfile_BottomBar_Like = findViewById(R.id.iv_UserProfile_BottomBar_Like);
        iv_UserProfile_BottomBar_Friend = findViewById(R.id.iv_UserProfile_BottomBar_Friend);
        v_UserProfile_BottomBar_Like = findViewById(R.id.v_UserProfile_BottomBar_Like);
        v_UserProfile_BottomBar_Chat = findViewById(R.id.v_UserProfile_BottomBar_Chat);
        v_UserProfile_BottomBar_Friend = findViewById(R.id.v_UserProfile_BottomBar_Friend);
        tv_UserProfile_BottomBar_Friend = findViewById(R.id.tv_UserProfile_BottomBar_Friend);

        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });
        tv_TopBar_Title.setText(TKManager.getInstance().TargetUserData.GetUserNickName());


        RefreshFriendIcon();
        RefreshLikeIcon();


        iv_UserProfile_Alert.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
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

                DialogFunc.getInstance().ShowMenuListPopup(UserProfileActivity.this, menuList, menuListenerList, null);
            }
        });

        v_UserProfile_BottomBar_Like.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (v.getId() == R.id.v_UserProfile_BottomBar_Like) {

                    DialogFunc.getInstance().ShowLoadingPage(UserProfileActivity.this);

                    if(TKManager.getInstance().TargetUserData.GetUserLikeList(TKManager.getInstance().MyData.GetUserIndex()) != null)
                    {
                        CommonFunc.getInstance().DrawImageByGlide(mContext, iv_UserProfile_BottomBar_Like, R.drawable.ic_like_empty, false);

                        TKManager.getInstance().TargetUserData.AddUserTotalLike(-1);
                        TKManager.getInstance().TargetUserData.AddUserTodayLike(-1);
                        TKManager.getInstance().TargetUserData.DelUserLikeList(TKManager.getInstance().MyData.GetUserIndex());

                        FirebaseManager.getInstance().RemoveLikeUser( TKManager.getInstance().TargetUserData.GetUserIndex());
                        DialogFunc.getInstance().ShowToast(mContext, CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_USER_UNLIKE), true);
                    }
                    else
                    {
                        CommonFunc.getInstance().DrawImageByGlide(mContext, iv_UserProfile_BottomBar_Like, R.drawable.ic_like, false);

                        TKManager.getInstance().TargetUserData.AddUserTotalLike(1);
                        TKManager.getInstance().TargetUserData.AddUserTodayLike(1);

                        TKManager.getInstance().TargetUserData.SetUserLikeList(TKManager.getInstance().MyData.GetUserIndex(), CommonFunc.getInstance().GetCurrentDate());
                        DialogFunc.getInstance().ShowToast(mContext, TKManager.getInstance().TargetUserData.GetUserNickName()  + CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_USER_LIKE), true);
                        FirebaseManager.getInstance().RegistLikeUser( TKManager.getInstance().TargetUserData.GetUserIndex());
                    }

                    DialogFunc.getInstance().DismissLoadingPage();

                    mUserProfileFragment.RefreshCountText();

                }
            }
        });


        v_UserProfile_BottomBar_Friend.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (v.getId() == R.id.v_UserProfile_BottomBar_Friend) {

                    if( TKManager.getInstance().MyData.GetUserFriendList(TKManager.getInstance().TargetUserData.GetUserIndex()) == null)
                    {
                        DialogFunc.MsgPopupListener listener = new DialogFunc.MsgPopupListener()
                        {

                            @Override
                            public void Listener()
                            {
                                FirebaseManager.CheckFirebaseComplete firebaseListener = new FirebaseManager.CheckFirebaseComplete() {
                                    @Override
                                    public void CompleteListener() {
                                        DialogFunc.getInstance().DismissLoadingPage();
                                        DialogFunc.getInstance().ShowToast(mContext, TKManager.getInstance().TargetUserData.GetUserNickName()  + CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_ASK_FRIND_REGIST), true);
                                    }

                                    @Override
                                    public void CompleteListener_Yes() {
                                    }

                                    @Override
                                    public void CompleteListener_No() {
                                    }
                                };

                                DialogFunc.getInstance().ShowLoadingPage(UserProfileActivity.this);
                                FirebaseManager.getInstance().RegistFriendInUserData(TKManager.getInstance().TargetUserData.GetUserIndex(), firebaseListener);
                                TKManager.getInstance().MyData.SetUserFriend(TKManager.getInstance().TargetUserData.GetUserIndex(), TKManager.getInstance().TargetUserData.GetUserIndex());
                                RefreshFriendIcon();
                            }
                        };
                        DialogFunc.getInstance().ShowMsgPopup(UserProfileActivity.this, listener, null, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_ASK_FRIND_ADD), CommonFunc.getInstance().getStr(getResources(), R.string.MSG_OK), CommonFunc.getInstance().getStr(getResources(), R.string.MSG_CANCEL));
                    }
                    else
                    {
                        DialogFunc.MsgPopupListener listener = new DialogFunc.MsgPopupListener()
                        {
                            @Override
                            public void Listener()
                            {
                                FirebaseManager.CheckFirebaseComplete firebaseListener = new FirebaseManager.CheckFirebaseComplete() {
                                    @Override
                                    public void CompleteListener() {
                                        DialogFunc.getInstance().DismissLoadingPage();
                                        DialogFunc.getInstance().ShowToast(mContext, TKManager.getInstance().TargetUserData.GetUserNickName() + CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_ASK_FRIND_REMOVE_SUCCESS), true);
                                    }

                                    @Override
                                    public void CompleteListener_Yes() {
                                    }

                                    @Override
                                    public void CompleteListener_No() {
                                    }
                                };
                                DialogFunc.getInstance().ShowLoadingPage(UserProfileActivity.this);
                                FirebaseManager.getInstance().RemoveFriendUser(TKManager.getInstance().TargetUserData.GetUserIndex(), firebaseListener);
                                TKManager.getInstance().MyData.DelUserFriendList(TKManager.getInstance().TargetUserData.GetUserIndex());
                                RefreshFriendIcon();
                            }
                        };
                        DialogFunc.getInstance().ShowMsgPopup(UserProfileActivity.this, listener, null, CommonFunc.getInstance().getStr(getResources(), R.string.MSG_ASK_FRIND_REMOVE), CommonFunc.getInstance().getStr(getResources(), R.string.MSG_OK), CommonFunc.getInstance().getStr(getResources(), R.string.MSG_CANCEL));
                    }

                }
            }

        });

        v_UserProfile_BottomBar_Chat.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (v.getId() == R.id.v_UserProfile_BottomBar_Chat) {

                    final String userIndex = TKManager.getInstance().MyData.GetUserIndex();
                    final String targetIndex =  TKManager.getInstance().TargetUserData.GetUserIndex();
                    final String ChatRoomIndex = userIndex + "_" +targetIndex;
                    final String AnotherChatRoomIndex = targetIndex+ "_" + userIndex ;

                    DialogFunc.getInstance().ShowLoadingPage(UserProfileActivity.this);

                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {

                              FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                                @Override
                                public void CompleteListener() {

                                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                                        @Override
                                        public void CompleteListener() {
                                            DialogFunc.getInstance().DismissLoadingPage();
                                            Intent intent = new Intent(mContext, ChatBodyActivity.class);
                                            intent.putExtra("RoomIndex",ChatRoomIndex);
                                            if(!ObjectUtils.isEmpty(TKManager.getInstance().MyData.GetUserChatDataList(ChatRoomIndex)))
                                            {
                                                intent.putExtra("RoomType",TKManager.getInstance().MyData.GetUserChatDataList(ChatRoomIndex).GetRoomType().name());
                                            }
                                            else if(!ObjectUtils.isEmpty(TKManager.getInstance().MyData.GetUserBookMarkChatDataList(ChatRoomIndex)))
                                            {
                                                intent.putExtra("RoomType",TKManager.getInstance().MyData.GetUserBookMarkChatDataList(ChatRoomIndex).GetRoomType().name());
                                            }
                                            else
                                            {
                                                intent.putExtra("RoomType",CommonData.CHAT_ROOM_TYPE.DEFAULT);
                                            }
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void CompleteListener_Yes() {
                                        }

                                        @Override
                                        public void CompleteListener_No() {
                                        }
                                    };

                                    if(TKManager.getInstance().UserData_Simple.get(targetIndex) != null)
                                    {
                                        DialogFunc.getInstance().DismissLoadingPage();
                                        Intent intent = new Intent(mContext, ChatBodyActivity.class);
                                        intent.putExtra("RoomIndex",ChatRoomIndex);
                                        if(!ObjectUtils.isEmpty(TKManager.getInstance().MyData.GetUserChatDataList(ChatRoomIndex)))
                                        {
                                            intent.putExtra("RoomType",TKManager.getInstance().MyData.GetUserChatDataList(ChatRoomIndex).GetRoomType().name());
                                        }
                                        else if(!ObjectUtils.isEmpty(TKManager.getInstance().MyData.GetUserBookMarkChatDataList(ChatRoomIndex)))
                                        {
                                            intent.putExtra("RoomType",TKManager.getInstance().MyData.GetUserBookMarkChatDataList(ChatRoomIndex).GetRoomType().name());
                                        }
                                        else
                                        {
                                            intent.putExtra("RoomType","DEFAULT");
                                        }
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        FirebaseManager.getInstance().SetFireBaseLoadingCount(2);
                                        FirebaseManager.getInstance().GetUserData_Simple(targetIndex, TKManager.getInstance().UserData_Simple, listener);
                                    }

                                }

                                @Override
                                public void CompleteListener_Yes() {
                                }

                                @Override
                                public void CompleteListener_No() {
                                }
                            };

                            FirebaseManager.getInstance().GetUserChatData(ChatRoomIndex, TKManager.getInstance().MyData, listener);
                        }

                        @Override
                        public void CompleteListener_Yes() {
                            FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                                @Override
                                public void CompleteListener() {

                                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                                        @Override
                                        public void CompleteListener() {
                                            DialogFunc.getInstance().DismissLoadingPage();
                                            Intent intent = new Intent(mContext, ChatBodyActivity.class);
                                            intent.putExtra("RoomIndex",AnotherChatRoomIndex);
                                            if(!ObjectUtils.isEmpty(TKManager.getInstance().MyData.GetUserChatDataList(ChatRoomIndex)))
                                            {
                                                intent.putExtra("RoomType",TKManager.getInstance().MyData.GetUserChatDataList(ChatRoomIndex).GetRoomType().name());
                                            }
                                            else if(!ObjectUtils.isEmpty(TKManager.getInstance().MyData.GetUserBookMarkChatDataList(ChatRoomIndex)))
                                            {
                                                intent.putExtra("RoomType",TKManager.getInstance().MyData.GetUserBookMarkChatDataList(ChatRoomIndex).GetRoomType().name());
                                            }
                                            else
                                            {
                                                intent.putExtra("RoomType","DEFAULT");
                                            }
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void CompleteListener_Yes() {
                                        }

                                        @Override
                                        public void CompleteListener_No() {
                                        }
                                    };

                                    if(TKManager.getInstance().UserData_Simple.get(targetIndex) != null)
                                    {
                                        DialogFunc.getInstance().DismissLoadingPage();
                                        Intent intent = new Intent(mContext, ChatBodyActivity.class);
                                        intent.putExtra("RoomIndex",AnotherChatRoomIndex);

                                        if(!ObjectUtils.isEmpty(TKManager.getInstance().MyData.GetUserChatDataList(ChatRoomIndex)))
                                        {
                                            intent.putExtra("RoomType",TKManager.getInstance().MyData.GetUserChatDataList(ChatRoomIndex).GetRoomType().name());
                                        }
                                        else if(!ObjectUtils.isEmpty(TKManager.getInstance().MyData.GetUserBookMarkChatDataList(ChatRoomIndex)))
                                        {
                                            intent.putExtra("RoomType",TKManager.getInstance().MyData.GetUserBookMarkChatDataList(ChatRoomIndex).GetRoomType().name());
                                        }
                                        else
                                        {
                                            intent.putExtra("RoomType","DEFAULT");
                                        }
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        FirebaseManager.getInstance().SetFireBaseLoadingCount(2);
                                        FirebaseManager.getInstance().GetUserData_Simple(targetIndex, TKManager.getInstance().UserData_Simple, listener);
                                    }

                                }

                                @Override
                                public void CompleteListener_Yes() {
                                }

                                @Override
                                public void CompleteListener_No() {
                                }
                            };

                            FirebaseManager.getInstance().GetUserChatData(ChatRoomIndex, TKManager.getInstance().MyData, listener);
                        }

                        @Override
                        public void CompleteListener_No() {
                            TKManager.getInstance().MyData.ClearUserChatData();
                            DialogFunc.getInstance().DismissLoadingPage();

                            ChatData tempChatData = new ChatData();
                            tempChatData.SetRoomIndex(ChatRoomIndex);

                            tempChatData.SetFromIndex(userIndex);
                            tempChatData.SetFromNickName(TKManager.getInstance().MyData.GetUserNickName());
                            tempChatData.SetFromThumbNail(TKManager.getInstance().MyData.GetUserImgThumb());

                            tempChatData.SetToIndex(targetIndex);
                            tempChatData.SetToNickName(TKManager.getInstance().UserData_Simple.get(targetIndex).GetUserNickName());
                            tempChatData.SetToThumbNail(TKManager.getInstance().UserData_Simple.get(targetIndex).GetUserImgThumb());

                            tempChatData.SetMsgIndex(0);
                            tempChatData.SetMsgReadCheck(false);
                            tempChatData.SetMsgDate(Long.parseLong(CommonFunc.getInstance().GetCurrentTime()));
                            tempChatData.SetMsgType(CommonData.MSGType.MSG);
                            tempChatData.SetMsgSender(userIndex);
                            tempChatData.SetMsg(TKManager.getInstance().MyData.GetUserNickName() + "님과 " + TKManager.getInstance().UserData_Simple.get(targetIndex).GetUserNickName() + "님의 채팅방입니다");
                            tempChatData.SetRoomType(CommonData.CHAT_ROOM_TYPE.DEFAULT);
                            tempChatData.SetRoomName(TKManager.getInstance().UserData_Simple.get(targetIndex).GetUserNickName());
                            tempChatData.SetRoomThumb(TKManager.getInstance().UserData_Simple.get(targetIndex).GetUserImgThumb());

                            FirebaseManager.getInstance().RegistChatList(TKManager.getInstance().TargetUserData.GetUserIndex(), tempChatData);
                            FirebaseManager.getInstance().RegistChatData(TKManager.getInstance().TargetUserData.GetUserIndex(), tempChatData);

                            Intent intent = new Intent(mContext, ChatBodyActivity.class);
                            intent.putExtra("RoomIndex",tempChatData.GetRoomIndex());
                            intent.putExtra("RoomType", tempChatData.GetRoomType().name());
                            startActivity(intent);

                        }
                    };

                    FirebaseManager.getInstance().ExistChatRoom(targetIndex, listener);


                }

            }
        });
    }

    private void RefreshLikeIcon()
    {
        if(TKManager.getInstance().TargetUserData.GetUserLikeList(TKManager.getInstance().MyData.GetUserIndex()) != null)
        {
            CommonFunc.getInstance().DrawImageByGlide(mContext, iv_UserProfile_BottomBar_Like, R.drawable.ic_like, false);
        }
        else
        {
            CommonFunc.getInstance().DrawImageByGlide(mContext, iv_UserProfile_BottomBar_Like, R.drawable.ic_like_empty, false);
        }

    }

    private void RefreshFriendIcon()
    {
        if( TKManager.getInstance().MyData.GetUserFriendList(TKManager.getInstance().TargetUserData.GetUserIndex()) == null){
            Glide.with(mContext).load(R.drawable.ic_add_friend)
                    .into(iv_UserProfile_BottomBar_Friend);
            tv_UserProfile_BottomBar_Friend.setText(CommonFunc.getInstance().getStr(getResources(), R.string.FRIEND_ADD));
        }

        else
        {
            Glide.with(mContext).load(R.drawable.ic_remove_friend)
                    .into(iv_UserProfile_BottomBar_Friend);
            tv_UserProfile_BottomBar_Friend.setText(CommonFunc.getInstance().getStr(getResources(), R.string.FRIEND_REMOVE));
        }
    }
}
