package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ChatData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ChatBodyActivity;
import fifty.fiftyhouse.com.fifty.activty.CustomPhotoView;
import fifty.fiftyhouse.com.fifty.activty.VideoPlayerActivity;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ChatBodyAdapter extends RecyclerView.Adapter<ChatBodyListHolder> {

    Context mContext;
    public ChatBodyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ChatBodyListHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_chat_body, parent, false);
        return new ChatBodyListHolder(view);
    }



    @Override
    public void onBindViewHolder(ChatBodyListHolder holder, final int position) {
        int i = position;

        holder.setChatData(i);
    }

    @Override
    public int getItemCount() {
        return TKManager.getInstance().MyData.GetUserChatDataCount();
    }

    @Override
    public long getItemId(int position) {
        long rtValue = 0;

        Set tempKey = TKManager.getInstance().MyData.GetUserChatDataKeySet();
        List array = new ArrayList(tempKey);
        ChatData tempChatData = TKManager.getInstance().MyData.GetUserChatData(array.get(position).toString());
        rtValue = tempChatData.GetMsgIndex();
        //rtValue = Long.valueOf(mMyData.arrUserAll_Hot_Age.get(position).Idx);

        return rtValue;
    }

}

class ChatBodyListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Chat_Body_Profile, iv_Chat_Body_Profile_Border;
    public ConstraintLayout v_Chat_Body, v_Chat_Body_Type_Msg, v_Chat_Body_Type_Img, v_Chat_Body_Type_Video, v_Chat_Body_Type_Etc;
    public ImageView iv_Chat_Body_Msg_BG, iv_Chat_Body_Img, iv_Chat_Body_Video;
    public TextView tv_Chat_Body_NickName, tv_Chat_Body_Msg, tv_Chat_Body_Date, tv_Chat_Body_Check;

    private Context mContext;

    public ChatBodyListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Chat_Body_Profile = itemView.findViewById(R.id.iv_Chat_Body_Profile);
        iv_Chat_Body_Profile_Border = itemView.findViewById(R.id.iv_Chat_Body_Profile_Border);
        v_Chat_Body = itemView.findViewById(R.id.v_Chat_Body);
        v_Chat_Body_Type_Msg = itemView.findViewById(R.id.v_Chat_Body_Type_Msg);
        v_Chat_Body_Type_Img = itemView.findViewById(R.id.v_Chat_Body_Type_Img);
        v_Chat_Body_Type_Video = itemView.findViewById(R.id.v_Chat_Body_Type_Video);
        v_Chat_Body_Type_Etc = itemView.findViewById(R.id.v_Chat_Body_Type_Etc);
        iv_Chat_Body_Msg_BG = itemView.findViewById(R.id.iv_Chat_Body_Msg_BG);
        iv_Chat_Body_Img = itemView.findViewById(R.id.iv_Chat_Body_Img);
        iv_Chat_Body_Video = itemView.findViewById(R.id.iv_Chat_Body_Video);
        tv_Chat_Body_NickName = itemView.findViewById(R.id.tv_Chat_Body_NickName);
        tv_Chat_Body_Msg = itemView.findViewById(R.id.tv_Chat_Body_Msg);
        tv_Chat_Body_Date = itemView.findViewById(R.id.tv_Chat_Body_Date);
        tv_Chat_Body_Check = itemView.findViewById(R.id.tv_Chat_Body_Check);

        /*tv_Chat_Body_Msg.setTextSize(TypedValue.COMPLEX_UNIT_DIP, CommonData.CHAT_FONT_SIZE);*/
    }

    public void setChatData(int pos)
    {
        Set tempKey = TKManager.getInstance().MyData.GetUserChatDataKeySet();
        List array = new ArrayList(tempKey);

        final ChatData tempData = TKManager.getInstance().MyData.GetUserChatData(array.get(pos).toString());

        Boolean mSend = tempData.GetMsgSender().equals(TKManager.getInstance().MyData.GetUserIndex());

        /*Boolean mSend = pos % 2 == 1 ? true : false;
        int ChatType = pos % 3;*/

        ConstraintLayout.LayoutParams lp_Chat_Body_Profile = null;
        ConstraintLayout.LayoutParams lp_Chat_Body_Profile_Border = null;
        ConstraintLayout.LayoutParams lp_Chat_Body_NickName = null;
        ConstraintLayout.LayoutParams lp_Chat_Body_Type_Msg = null;
        ConstraintLayout.LayoutParams lp_Chat_Body_Type_Img = null;
        ConstraintLayout.LayoutParams lp_Chat_Body_Type_Video = null;
        ConstraintLayout.LayoutParams lp_Chat_Body_Type_Etc = null;
        ConstraintLayout.LayoutParams lp_Chat_Body_Date = null;
        ConstraintLayout.LayoutParams lp_Chat_Body_Check = null;

        int contentMaxSize = (CommonFunc.getInstance().getWidthByDevice() / 5) * 3;
        if(mSend)
        {
            // 내가 메세지를 보냄
            int ProfileSize = CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 40);
            lp_Chat_Body_Profile = new ConstraintLayout.LayoutParams(ProfileSize, ProfileSize);
            lp_Chat_Body_Profile.rightToRight = v_Chat_Body.getId();
            lp_Chat_Body_Profile.topToTop = v_Chat_Body.getId();

            int ProfileSizeBoder = CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 40);
            lp_Chat_Body_Profile_Border = new ConstraintLayout.LayoutParams(ProfileSizeBoder, ProfileSizeBoder);
            lp_Chat_Body_Profile_Border.rightToRight = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Profile_Border.leftToLeft = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Profile_Border.topToTop = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Profile_Border.bottomToBottom = iv_Chat_Body_Profile.getId();

            lp_Chat_Body_NickName = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_NickName.rightToRight = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_NickName.topToTop = iv_Chat_Body_Profile.getId();

            lp_Chat_Body_Type_Msg = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Type_Msg.rightToLeft = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Type_Msg.topToBottom = tv_Chat_Body_NickName.getId();
            lp_Chat_Body_Type_Msg.setMargins(0, CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 3), 0, 0);

            tv_Chat_Body_Msg.setGravity(Gravity.LEFT);
            int padding = CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 10);
            tv_Chat_Body_Msg.setPadding(padding, padding, CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 20), padding);
            tv_Chat_Body_Msg.setMaxWidth(contentMaxSize);
            tv_Chat_Body_Msg.setTextColor(ContextCompat.getColor(mContext, R.color.chat_my_str));

            lp_Chat_Body_Type_Img = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Type_Img.rightToLeft = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Type_Img.topToBottom = tv_Chat_Body_NickName.getId();
            lp_Chat_Body_Type_Img.setMargins(0, CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 6), 0, 0);

            lp_Chat_Body_Type_Video = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Type_Video.rightToLeft = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Type_Video.topToBottom = tv_Chat_Body_NickName.getId();
            lp_Chat_Body_Type_Video.setMargins(0, CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 6), 0, 0);

            // 타입별 나누기
            int AttachID = 0;
            if(tempData.GetMsgType() == CommonData.MSGType.MSG)
                AttachID = v_Chat_Body_Type_Msg.getId();
            else if(tempData.GetMsgType() == CommonData.MSGType.IMG)
                AttachID = v_Chat_Body_Type_Img.getId();
            else
                AttachID = v_Chat_Body_Type_Video.getId();

            lp_Chat_Body_Type_Etc = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Type_Etc.rightToLeft = AttachID;
            lp_Chat_Body_Type_Etc.bottomToBottom = AttachID;

            // 문자
            lp_Chat_Body_Date = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Date.rightToRight = v_Chat_Body_Type_Etc.getId();
            lp_Chat_Body_Date.bottomToBottom = v_Chat_Body_Type_Etc.getId();

            lp_Chat_Body_Check = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Check.rightToLeft = tv_Chat_Body_Date.getId();
            lp_Chat_Body_Check.bottomToBottom = tv_Chat_Body_Date.getId();

            iv_Chat_Body_Msg_BG.setScaleX(1.0f);
            ImageViewCompat.setImageTintList(iv_Chat_Body_Msg_BG, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.chat_my_bg)));
        }
        else
        {
            // 상대방이 나에게 메세지를 보냄
            int ProfileSize = CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 40);
            lp_Chat_Body_Profile = new ConstraintLayout.LayoutParams(ProfileSize, ProfileSize);
            lp_Chat_Body_Profile.leftToLeft = v_Chat_Body.getId();
            lp_Chat_Body_Profile.topToTop = v_Chat_Body.getId();

            int ProfileSizeBoder = CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 40);
            lp_Chat_Body_Profile_Border = new ConstraintLayout.LayoutParams(ProfileSizeBoder, ProfileSizeBoder);
            lp_Chat_Body_Profile_Border.rightToRight = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Profile_Border.leftToLeft = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Profile_Border.topToTop = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Profile_Border.bottomToBottom = iv_Chat_Body_Profile.getId();

            lp_Chat_Body_NickName = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_NickName.leftToRight = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_NickName.topToTop = iv_Chat_Body_Profile.getId();

            lp_Chat_Body_Type_Msg = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Type_Msg.leftToRight = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Type_Msg.topToBottom = tv_Chat_Body_NickName.getId();
            lp_Chat_Body_Type_Msg.setMargins(0, CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 3), 0, 0);

            tv_Chat_Body_Msg.setGravity(Gravity.LEFT);
            int padding = CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 10);
            tv_Chat_Body_Msg.setPadding(CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 20), padding, padding, padding);
            tv_Chat_Body_Msg.setMaxWidth(contentMaxSize);
            tv_Chat_Body_Msg.setTextColor(ContextCompat.getColor(mContext, R.color.chat_you_str));

            lp_Chat_Body_Type_Img = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Type_Img.leftToRight = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Type_Img.topToBottom = tv_Chat_Body_NickName.getId();
            lp_Chat_Body_Type_Img.setMargins(0, CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 6), 0, 0);

            lp_Chat_Body_Type_Video = new ConstraintLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Type_Video.leftToRight = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Type_Video.topToBottom = tv_Chat_Body_NickName.getId();
            lp_Chat_Body_Type_Video.setMargins(0, CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 6), 0, 0);

            // 타입별 나누기
            int AttachID = 0;
            if(tempData.GetMsgType() == CommonData.MSGType.MSG)
                AttachID = v_Chat_Body_Type_Msg.getId();
            else if(tempData.GetMsgType() == CommonData.MSGType.IMG)
                AttachID = v_Chat_Body_Type_Img.getId();
            else
                AttachID = v_Chat_Body_Type_Video.getId();

            lp_Chat_Body_Type_Etc = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Type_Etc.leftToRight = AttachID;
            lp_Chat_Body_Type_Etc.bottomToBottom = AttachID;

            // 문자
            lp_Chat_Body_Date = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Date.leftToLeft = v_Chat_Body_Type_Etc.getId();
            lp_Chat_Body_Date.bottomToBottom = v_Chat_Body_Type_Etc.getId();

            lp_Chat_Body_Check = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Check.leftToRight = tv_Chat_Body_Date.getId();
            lp_Chat_Body_Check.bottomToBottom = tv_Chat_Body_Date.getId();

            iv_Chat_Body_Msg_BG.setScaleX(-1.0f);
            ImageViewCompat.setImageTintList(iv_Chat_Body_Msg_BG, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.chat_you_bg)));


        }

        iv_Chat_Body_Profile.setLayoutParams(lp_Chat_Body_Profile);
        int padding = CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 1);
        iv_Chat_Body_Profile.setPadding(padding, padding, padding, padding);
        iv_Chat_Body_Profile_Border.setLayoutParams(lp_Chat_Body_Profile_Border);
        tv_Chat_Body_NickName.setLayoutParams(lp_Chat_Body_NickName);
        v_Chat_Body_Type_Msg.setLayoutParams(lp_Chat_Body_Type_Msg);
        v_Chat_Body_Type_Img.setLayoutParams(lp_Chat_Body_Type_Img);
        v_Chat_Body_Type_Video.setLayoutParams(lp_Chat_Body_Type_Video);
        v_Chat_Body_Type_Etc.setLayoutParams(lp_Chat_Body_Type_Etc);
        tv_Chat_Body_Date.setLayoutParams(lp_Chat_Body_Date);
        tv_Chat_Body_Check.setLayoutParams(lp_Chat_Body_Check);

        v_Chat_Body_Type_Msg.setVisibility(View.GONE);
        v_Chat_Body_Type_Img.setVisibility(View.GONE);
        v_Chat_Body_Type_Video.setVisibility(View.GONE);

        if(tempData.GetMsgType() == CommonData.MSGType.MSG)
            v_Chat_Body_Type_Msg.setVisibility(View.VISIBLE);
        else if(tempData.GetMsgType() == CommonData.MSGType.IMG)
            v_Chat_Body_Type_Img.setVisibility(View.VISIBLE);
        else
            v_Chat_Body_Type_Video.setVisibility(View.VISIBLE);

        if(mSend)
        {
            iv_Chat_Body_Profile.setVisibility(View.GONE);
            iv_Chat_Body_Profile_Border.setVisibility(View.GONE);
            tv_Chat_Body_NickName.setVisibility(View.GONE);
            tv_Chat_Body_NickName.setPadding(0,0, CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 5), 0);
            v_Chat_Body_Type_Msg.setPadding(0,0, 0, 0);
            v_Chat_Body_Type_Img.setPadding(0,0, 0, 0);
            v_Chat_Body_Type_Video.setPadding(0,0, 0, 0);
            tv_Chat_Body_Date.setPadding(0,0, CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 5), 0);
            tv_Chat_Body_Check.setPadding(0,0, CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 5), 0);

        }
        else
        {
            iv_Chat_Body_Profile.setVisibility(View.VISIBLE);
            iv_Chat_Body_Profile_Border.setVisibility(View.VISIBLE);
            tv_Chat_Body_NickName.setVisibility(View.VISIBLE);
            tv_Chat_Body_NickName.setPadding(CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 5), 0,0,0);
            v_Chat_Body_Type_Msg.setPadding(CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 5), 0,0,0);
            v_Chat_Body_Type_Img.setPadding(CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 5), 0,0, 0);
            v_Chat_Body_Type_Video.setPadding(CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 5), 0,0, 0);
            tv_Chat_Body_Date.setPadding(CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 5), 0,0, 0);
            tv_Chat_Body_Check.setPadding(CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 5), 0,0, 0);

            CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Chat_Body_Profile, TKManager.getInstance().UserData_Simple.get(tempData.GetMsgSender()).GetUserImgThumb(), true);
            tv_Chat_Body_NickName.setText(TKManager.getInstance().UserData_Simple.get(tempData.GetMsgSender()).GetUserNickName());
        }

        tv_Chat_Body_Msg.setText(tempData.GetMsg());

        iv_Chat_Body_Img.setMaxHeight(contentMaxSize);
        iv_Chat_Body_Img.setMaxWidth(contentMaxSize);
        CommonFunc.getInstance().DrawImageByGlideInChatRoom(mContext, iv_Chat_Body_Img, tempData.GetMsg(), false);

        iv_Chat_Body_Video.setMaxHeight(contentMaxSize);
        iv_Chat_Body_Video.setMaxWidth(contentMaxSize);
        CommonFunc.getInstance().DrawImageByGlideInChatRoom(mContext, iv_Chat_Body_Video, tempData.GetMsg(), false);

        iv_Chat_Body_Profile.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                String UserIndex = tempData.GetMsgSender();

                CommonFunc.getInstance().GetUserDataInFireBase(UserIndex,ChatBodyActivity.mChatBodyActivity, true);
              /*  DialogFunc.getInstance().ShowLoadingPage(ChatBodyActivity.mChatBodyActivity);

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
                        DialogFunc.getInstance().DismissLoadingPage();
                    }
                };

                FirebaseManager.getInstance().GetUserData(UserIndex, TKManager.getInstance().TargetUserData, listener);*/
            }

        });

        iv_Chat_Body_Img.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View v) {
                DialogFunc.getInstance().ShowLoadingPage(ChatBodyActivity.mChatBodyActivity);

                Intent intent = new Intent(mContext, CustomPhotoView.class);
                intent.putExtra("ImgSrc",tempData.GetMsg());
                intent.putExtra("Type", CustomPhotoView.PHOTO_VIEW_TYPE_ONE);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                DialogFunc.getInstance().DismissLoadingPage();
            }
        });

        iv_Chat_Body_Video.setOnClickListener(new OnSingleClickListener(){
            @Override
            public void onSingleClick(View v) {
                Intent intent = new Intent(mContext, VideoPlayerActivity.class);
                intent.putExtra("url", tempData.GetFile());
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                DialogFunc.getInstance().DismissLoadingPage();
            }
        });


        tv_Chat_Body_Date.setText(CommonFunc.getInstance().ConvertTimeSrt(Long.toString(tempData.GetMsgDate()), "a hh:mm"));

        if(tempData.GetMsgReadCheckNumber() - tempData.GetMsgReadCheckUserSize() > 0)
        {
            tv_Chat_Body_Check.setVisibility(View.VISIBLE);
            tv_Chat_Body_Check.setText(Long.toString(tempData.GetMsgReadCheckNumber() - tempData.GetMsgReadCheckUserSize()));
        }
        else
        {
            tv_Chat_Body_Check.setVisibility(View.INVISIBLE);
        }
    }
}
