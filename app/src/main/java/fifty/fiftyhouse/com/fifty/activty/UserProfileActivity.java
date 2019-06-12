package fifty.fiftyhouse.com.fifty.activty;

import android.app.Activity;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.fragment.UserProfileFragment;

public class UserProfileActivity extends AppCompatActivity {

    View ui_UserProfile_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    ImageView iv_UserProfile_Alert, iv_UserProfile_BottomBar_Like, iv_UserProfile_BottomBar_Friend;
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
        mFragmentMgr = getSupportFragmentManager();
        // mFragmentMgr.beginTransaction().addToBackStack(null);

        mUserProfileFragment = new UserProfileFragment();
        mUserProfileFragment.setMyProfileView(false);

        CommonFunc.getInstance().mCurActivity = this;
        FirebaseManager.getInstance().RegistVisitUser(TKManager.getInstance().TargetUserData.GetUserIndex());

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

        iv_TopBar_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_TopBar_Title.setText(TKManager.getInstance().TargetUserData.GetUserNickName());

        v_UserProfile_BottomBar_Like.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.v_UserProfile_BottomBar_Like) {

                    DialogFunc.getInstance().SetShowLoadingPageMsg(UserProfileActivity.this);

                    if(TKManager.getInstance().TargetUserData.GetUserLikeList(TKManager.getInstance().MyData.GetUserIndex()) == null){

                        Glide.with(mContext).load(R.drawable.ic_like)
                                .into(iv_UserProfile_BottomBar_Like);
                        RefreshLikeCount(true);
                    } else {
                        Glide.with(mContext).load(R.drawable.ic_like_empty)
                                .into(iv_UserProfile_BottomBar_Like);
                        RefreshLikeCount(false);
                    }

                    mUserProfileFragment.setCountInfoStr_2("좋아요 " + TKManager.getInstance().TargetUserData.GetUserTodayLike() + " / " + TKManager.getInstance().TargetUserData.GetUserTotalLike());
                }
            }
        });



        v_UserProfile_BottomBar_Friend.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.v_UserProfile_BottomBar_Friend) {

                    DialogFunc.getInstance().SetShowLoadingPageMsg(UserProfileActivity.this);

                    if( TKManager.getInstance().MyData.GetUserFriendList(TKManager.getInstance().TargetUserData.GetUserIndex()) == null){
                        FirebaseManager.getInstance().RegistFriendInUserData(TKManager.getInstance().TargetUserData.GetUserIndex());
                        TKManager.getInstance().MyData.SetUserFriend(TKManager.getInstance().TargetUserData.GetUserIndex(), TKManager.getInstance().TargetUserData.GetUserIndex());
                    }

                    else
                    {
                        FirebaseManager.getInstance().RemoveFriendUser(TKManager.getInstance().TargetUserData.GetUserIndex());
                        TKManager.getInstance().MyData.DelUserFriendList(TKManager.getInstance().TargetUserData.GetUserIndex());
                    }
                }
            }
        });

        v_UserProfile_BottomBar_Chat.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.v_UserProfile_BottomBar_Chat) {

                    String userIndex = TKManager.getInstance().MyData.GetUserIndex();
                    String targetIndex =  TKManager.getInstance().TargetUserData.GetUserIndex();
                    String ChatRoomIndex = userIndex + "_" +targetIndex;

                    ChatData tempChatData = new ChatData();
                    tempChatData.SetRoomIndex(ChatRoomIndex);

                    tempChatData.SetFromIndex(userIndex);
                    tempChatData.SetFromNickName(TKManager.getInstance().MyData.GetUserNickName());
                    tempChatData.SetFromThumbNail(TKManager.getInstance().MyData.GetUserImgThumb());

                    tempChatData.SetToIndex(targetIndex);
                    tempChatData.SetToNickName(TKManager.getInstance().UserData_Simple.get(targetIndex).GetUserNickName());
                    tempChatData.SetToThumbNail(TKManager.getInstance().UserData_Simple.get(targetIndex).GetUserImgThumb());

                    tempChatData.SetMsgIndex(0);
                    tempChatData.SetMsgDate(Integer.parseInt(CommonFunc.getInstance().GetCurrentDate()));
                    tempChatData.SetMsgType(CommonData.MSGType.MSG);
                    tempChatData.SetMsgSender(userIndex);
                    tempChatData.SetMsg(TKManager.getInstance().MyData.GetUserNickName() + "님과 " + TKManager.getInstance().UserData_Simple.get(targetIndex).GetUserNickName() + "님의 채팅방입니다");


                    FirebaseManager.getInstance().RegistChatList(TKManager.getInstance().TargetUserData.GetUserIndex(), tempChatData);
                    FirebaseManager.getInstance().RegistChatData(TKManager.getInstance().TargetUserData.GetUserIndex(), tempChatData);
                }

            }
        });
    }

    private void RefreshLikeCount(boolean like)
    {
        if(like)
        {
            TKManager.getInstance().TargetUserData.AddUserTotalLike(1);
            TKManager.getInstance().TargetUserData.AddUserTodayLike(1);
            TKManager.getInstance().TargetUserData.SetUserLikeList(TKManager.getInstance().MyData.GetUserIndex(), CommonFunc.getInstance().GetCurrentDate());

            FirebaseManager.getInstance().RegistLikeUser( TKManager.getInstance().TargetUserData.GetUserIndex());
        }
        else
        {
            TKManager.getInstance().TargetUserData.AddUserTotalLike(-1);
            TKManager.getInstance().TargetUserData.AddUserTodayLike(-1);
            TKManager.getInstance().TargetUserData.DelUserLikeList(TKManager.getInstance().MyData.GetUserIndex());

            FirebaseManager.getInstance().RemoveLikeUser( TKManager.getInstance().TargetUserData.GetUserIndex());
        }
    }
}
