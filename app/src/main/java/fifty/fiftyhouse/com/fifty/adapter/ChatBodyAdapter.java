package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.R;

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
        return 20;
    }
}

class ChatBodyListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Chat_Body_Profile;
    public ConstraintLayout v_Chat_Body, v_Chat_Body_Type_Msg, v_Chat_Body_Type_Img, v_Chat_Body_Type_Video;
    public ImageView iv_Chat_Body_Msg_BG, iv_Chat_Body_Img, iv_Chat_Body_Video;
    public TextView tv_Chat_Body_NickName, tv_Chat_Body_Msg, tv_Chat_Body_Date, tv_Chat_Body_Check;

    private Context mContext;

    public ChatBodyListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_Chat_Body_Profile = itemView.findViewById(R.id.iv_Chat_Body_Profile);
        v_Chat_Body = itemView.findViewById(R.id.v_Chat_Body);
        v_Chat_Body_Type_Msg = itemView.findViewById(R.id.v_Chat_Body_Type_Msg);
        v_Chat_Body_Type_Img = itemView.findViewById(R.id.v_Chat_Body_Type_Img);
        v_Chat_Body_Type_Video = itemView.findViewById(R.id.v_Chat_Body_Type_Video);
        iv_Chat_Body_Msg_BG = itemView.findViewById(R.id.iv_Chat_Body_Msg_BG);
        iv_Chat_Body_Img = itemView.findViewById(R.id.iv_Chat_Body_Img);
        iv_Chat_Body_Video = itemView.findViewById(R.id.iv_Chat_Body_Video);
        tv_Chat_Body_NickName = itemView.findViewById(R.id.tv_Chat_Body_NickName);
        tv_Chat_Body_Msg = itemView.findViewById(R.id.tv_Chat_Body_Msg);
        tv_Chat_Body_Date = itemView.findViewById(R.id.tv_Chat_Body_Date);
        tv_Chat_Body_Check = itemView.findViewById(R.id.tv_Chat_Body_Check);
    }

    public void setChatData(int pos)
    {
        Boolean mSend = pos % 2 == 1 ? true : false;
        int ChatType = pos % 3;

        ConstraintLayout.LayoutParams lp_Chat_Body_Profile = null;
        ConstraintLayout.LayoutParams lp_Chat_Body_NickName = null;
        ConstraintLayout.LayoutParams lp_Chat_Body_Type_Msg = null;
        ConstraintLayout.LayoutParams lp_Chat_Body_Type_Img = null;
        ConstraintLayout.LayoutParams lp_Chat_Body_Type_Video = null;
        ConstraintLayout.LayoutParams lp_Chat_Body_Date = null;
        ConstraintLayout.LayoutParams lp_Chat_Body_Check = null;

        if(mSend)
        {
            // 내가 메세지를 보냄
            int ProfileSize = CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 40);
            lp_Chat_Body_Profile = new ConstraintLayout.LayoutParams(ProfileSize, ProfileSize);
            lp_Chat_Body_Profile.rightToRight = v_Chat_Body.getId();
            lp_Chat_Body_Profile.topToTop = v_Chat_Body.getId();

            lp_Chat_Body_NickName = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_NickName.rightToRight = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_NickName.topToTop = iv_Chat_Body_Profile.getId();

            lp_Chat_Body_Type_Msg = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Type_Msg.rightToLeft = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Type_Msg.topToBottom = tv_Chat_Body_NickName.getId();

            tv_Chat_Body_Msg.setGravity(Gravity.RIGHT);

            lp_Chat_Body_Type_Img = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Type_Img.rightToLeft = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Type_Img.topToBottom = tv_Chat_Body_NickName.getId();

            lp_Chat_Body_Type_Video = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Type_Video.rightToLeft = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Type_Video.topToBottom = tv_Chat_Body_NickName.getId();

            // 타입별 나누기
            int AttachID = 0;
            if(ChatType == 0)
                AttachID = v_Chat_Body_Type_Msg.getId();
            else if(ChatType == 1)
                AttachID = v_Chat_Body_Type_Img.getId();
            else
                AttachID = v_Chat_Body_Type_Video.getId();

            // 문자
            lp_Chat_Body_Date = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Date.rightToLeft = AttachID;
            lp_Chat_Body_Date.bottomToBottom = AttachID;

            lp_Chat_Body_Check = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Check.rightToLeft = tv_Chat_Body_Date.getId();
            lp_Chat_Body_Check.bottomToBottom = tv_Chat_Body_Date.getId();
        }
        else
        {
            // 상대방이 나에게 메세지를 보냄
            int ProfileSize = CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 40);
            lp_Chat_Body_Profile = new ConstraintLayout.LayoutParams(ProfileSize, ProfileSize);
            lp_Chat_Body_Profile.leftToLeft = v_Chat_Body.getId();
            lp_Chat_Body_Profile.topToTop = v_Chat_Body.getId();

            lp_Chat_Body_NickName = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_NickName.leftToRight = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_NickName.topToTop = iv_Chat_Body_Profile.getId();

            lp_Chat_Body_Type_Msg = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Type_Msg.leftToRight = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Type_Msg.topToBottom = tv_Chat_Body_NickName.getId();

            tv_Chat_Body_Msg.setGravity(Gravity.LEFT);

            lp_Chat_Body_Type_Img = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Type_Img.leftToRight = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Type_Img.topToBottom = tv_Chat_Body_NickName.getId();

            lp_Chat_Body_Type_Video = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Type_Video.leftToRight = iv_Chat_Body_Profile.getId();
            lp_Chat_Body_Type_Video.topToBottom = tv_Chat_Body_NickName.getId();

            // 타입별 나누기
            int AttachID = 0;
            if(ChatType == 0)
                AttachID = v_Chat_Body_Type_Msg.getId();
            else if(ChatType == 1)
                AttachID = v_Chat_Body_Type_Img.getId();
            else
                AttachID = v_Chat_Body_Type_Video.getId();

            // 문자
            lp_Chat_Body_Date = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Date.leftToRight = AttachID;
            lp_Chat_Body_Date.bottomToBottom = AttachID;

            lp_Chat_Body_Check = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp_Chat_Body_Check.leftToRight = tv_Chat_Body_Date.getId();
            lp_Chat_Body_Check.bottomToBottom = tv_Chat_Body_Date.getId();
        }

        iv_Chat_Body_Profile.setLayoutParams(lp_Chat_Body_Profile);
        tv_Chat_Body_NickName.setLayoutParams(lp_Chat_Body_NickName);
        v_Chat_Body_Type_Msg.setLayoutParams(lp_Chat_Body_Type_Msg);
        v_Chat_Body_Type_Img.setLayoutParams(lp_Chat_Body_Type_Img);
        v_Chat_Body_Type_Video.setLayoutParams(lp_Chat_Body_Type_Video);
        tv_Chat_Body_Date.setLayoutParams(lp_Chat_Body_Date);
        tv_Chat_Body_Check.setLayoutParams(lp_Chat_Body_Check);


        v_Chat_Body_Type_Msg.setVisibility(View.GONE);
        v_Chat_Body_Type_Img.setVisibility(View.GONE);
        v_Chat_Body_Type_Video.setVisibility(View.GONE);
        if(ChatType == 0)
            v_Chat_Body_Type_Msg.setVisibility(View.VISIBLE);
        else if(ChatType == 1)
            v_Chat_Body_Type_Img.setVisibility(View.VISIBLE);
        else
            v_Chat_Body_Type_Video.setVisibility(View.VISIBLE);

        if(mSend)
        {
            iv_Chat_Body_Profile.setVisibility(View.GONE);
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
            tv_Chat_Body_NickName.setVisibility(View.VISIBLE);
            tv_Chat_Body_NickName.setPadding(CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 5), 0,0,0);
            v_Chat_Body_Type_Msg.setPadding(CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 5), 0,0,0);
            v_Chat_Body_Type_Img.setPadding(CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 5), 0,0, 0);
            v_Chat_Body_Type_Video.setPadding(CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 5), 0,0, 0);
            tv_Chat_Body_Date.setPadding(CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 5), 0,0, 0);
            tv_Chat_Body_Check.setPadding(CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 5), 0,0, 0);
        }

        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.dummy_4)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(iv_Chat_Body_Profile);
    }
}
