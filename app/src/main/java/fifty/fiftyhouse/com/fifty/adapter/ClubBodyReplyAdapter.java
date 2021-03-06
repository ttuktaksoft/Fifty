package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubContextData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ClubBodyActivity;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class ClubBodyReplyAdapter extends RecyclerView.Adapter<ClubBodyReplyListHolder> {

    Context mContext;
    int mItemCount = 0;
    ArrayList<String> mItemList = new ArrayList<>();

    public ClubBodyReplyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ClubBodyReplyListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club_body_reply, parent, false);

        return new ClubBodyReplyListHolder(view);
    }

    @Override
    public void onBindViewHolder(ClubBodyReplyListHolder holder, final int position) {
        int i = position;
        holder.setClubBodyReply(i);
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public void setItemCount(int count)
    {
        mItemCount = count;
    }
    public void setItemData(ArrayList<String> list)
    {
        mItemList.clear();
        mItemList.addAll(list);
    }

}

class ClubBodyReplyListHolder extends RecyclerView.ViewHolder {

    ConstraintLayout v_Club_Reply;
    public ImageView iv_Club_Body_Reply_Profile;
    public TextView tv_Club_Body_Reply_Nickname;
    public TextView tv_Club_Body_Reply_Date;
    public TextView tv_Club_Body_Reply_Desc;
    Context mContext;

    public ClubBodyReplyListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        v_Club_Reply = itemView.findViewById(R.id.v_Club_Reply);
        iv_Club_Body_Reply_Profile = itemView.findViewById(R.id.iv_Club_Body_Reply_Profile);
        tv_Club_Body_Reply_Nickname = itemView.findViewById(R.id.tv_Club_Body_Reply_Nickname);
        tv_Club_Body_Reply_Date = itemView.findViewById(R.id.tv_Club_Body_Reply_Date);
        tv_Club_Body_Reply_Desc = itemView.findViewById(R.id.tv_Club_Body_Reply_Desc);
    }

    public void setClubBodyReply(final int pos)
    {

        ClubContextData tempData = TKManager.getInstance().TargetContextData;

        Set tempKey = tempData.GetReplyDataKeySet();
        final List array = new ArrayList(tempKey);
        final ClubContextData tempReplyData = tempData.GetReplyData(array.get(pos).toString());

        //tempData = TKManager.getInstance().TargetClubData.GetClubContext(Integer.toString(pos));
        tv_Club_Body_Reply_Nickname.setText(TKManager.getInstance().UserData_Simple.get(tempReplyData.GetWriterIndex()).GetUserNickName());
        tv_Club_Body_Reply_Date.setText(CommonFunc.getInstance().ConvertTimeSrt(tempReplyData.GetDate(), "MM.dd HH:mm"));
        tv_Club_Body_Reply_Desc.setText(tempReplyData.GetContext());
        CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Club_Body_Reply_Profile, TKManager.getInstance().UserData_Simple.get(tempReplyData.GetWriterIndex()).GetUserImgThumb(), true);

        v_Club_Reply.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                String UserIndex = tempReplyData.GetWriterIndex();

                if(UserIndex.equals(TKManager.getInstance().MyData.GetUserIndex()))
                {
                    // TODO 댓글 수정

                    ArrayList<String> menuList = new ArrayList<>();
                    menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TRY_DEL));
                    menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CANCEL));

                    ArrayList<DialogFunc.MsgPopupListener> menuListenerList = new ArrayList<>();
                    menuListenerList.add(new DialogFunc.MsgPopupListener()
                    {
                        @Override
                        public void Listener()
                        {
                            // TODO 삭제
                        }
                    });

                    DialogFunc.getInstance().ShowMenuListPopup(mContext, menuList, menuListenerList, null);


                }

            }
        });

        iv_Club_Body_Reply_Profile.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                String UserIndex = tempReplyData.GetWriterIndex();

                if(UserIndex.equals(TKManager.getInstance().MyData.GetUserIndex()) == false)
                {

                    CommonFunc.getInstance().GetUserDataInFireBase(UserIndex, ClubBodyActivity.mClubBodyActivity, true);

                  /*  DialogFunc.getInstance().ShowLoadingPage(ClubBodyActivity.mClubBodyActivity);

                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {


                            Set KeySet = TKManager.getInstance().TargetUserData.GetUserClubDataKeySet();

                            if(KeySet.size() > 0)
                            {
                                Iterator iterator = KeySet.iterator();

                                FirebaseManager.getInstance().SetFireBaseLoadingCount(TKManager.getInstance().TargetUserData.GetUserClubDataCount());

                                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                                    @Override
                                    public void CompleteListener() {
                                        DialogFunc.getInstance().DismissLoadingPage();
                                        Intent intent = new Intent(mContext, UserProfileActivity.class);
                                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                        mContext.startActivity(intent);
                                    }

                                    @Override
                                    public void CompleteListener_Yes() {
                                    }

                                    @Override
                                    public void CompleteListener_No() {
                                    }
                                };

                                while(iterator.hasNext()){
                                    String key = (String)iterator.next();
                                    if(TKManager.getInstance().ClubData_Simple.get(key) != null)
                                    {
                                        FirebaseManager.getInstance().Complete(listener);
                                    }
                                    else
                                        FirebaseManager.getInstance().GetClubData_Simple(key, TKManager.getInstance().ClubData_Simple, listener);
                                }
                            }
                            else
                            {
                                DialogFunc.getInstance().DismissLoadingPage();

                                Intent intent = new Intent(mContext, UserProfileActivity.class);
                                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);
                            }
                        }

                        @Override
                        public void CompleteListener_Yes() {
                        }

                        @Override
                        public void CompleteListener_No() {
                            DialogFunc.getInstance().DismissLoadingPage();
                        }
                    };

                    FirebaseManager.getInstance().GetUserData(UserIndex, TKManager.getInstance().TargetUserData, listener);*/

                }
            }
        });

    }
}
