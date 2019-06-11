package fifty.fiftyhouse.com.fifty.activty.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.w3c.dom.Text;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.CustomPhotoView;
import fifty.fiftyhouse.com.fifty.adapter.UserProfilePhotoAdapter;
import fifty.fiftyhouse.com.fifty.fragment.UserProfileFragment;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class UserProfileActivity extends AppCompatActivity {

    ImageView iv_UserProfile_Back, iv_UserProfile_Alert, iv_UserProfile_BottomBar_Like, iv_UserProfile_BottomBar_Friend;
    TextView tv_UserProfile_Name;
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

        mFragmentMgr.beginTransaction().replace(R.id.fl_UserProfile_FrameLayout, mUserProfileFragment, "UserProfileFragment").commit();

        iv_UserProfile_Back = findViewById(R.id.iv_UserProfile_Back);
        iv_UserProfile_Alert = findViewById(R.id.iv_UserProfile_Alert);
        iv_UserProfile_BottomBar_Like = findViewById(R.id.iv_UserProfile_BottomBar_Like);
        iv_UserProfile_BottomBar_Friend = findViewById(R.id.iv_UserProfile_BottomBar_Friend);
        tv_UserProfile_Name = findViewById(R.id.tv_UserProfile_Name);
        v_UserProfile_BottomBar_Like = findViewById(R.id.v_UserProfile_BottomBar_Like);
        v_UserProfile_BottomBar_Chat = findViewById(R.id.v_UserProfile_BottomBar_Chat);
        v_UserProfile_BottomBar_Friend = findViewById(R.id.v_UserProfile_BottomBar_Friend);

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

                    mUserProfileFragment.setCountInfoStr_1("좋아요 " + TKManager.getInstance().TargetUserData.GetUserTodayLike() + " / " + TKManager.getInstance().TargetUserData.GetUserTotalLike());
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
                    int index = 11;
                    TKManager.getInstance().MyData.SetUserIndex(Integer.toString(index));
                    index ++;
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
