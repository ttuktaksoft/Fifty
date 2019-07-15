package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fifty.fiftyhouse.com.fifty.GlobalApplication;
import fifty.fiftyhouse.com.fifty.R;

public class UserProfileMenuAdapter extends RecyclerView.Adapter<UserProfileMenuListHolder> {

    Context mContext;

    public UserProfileMenuAdapter(Context context) {
        mContext = context;
    }

    @Override
    public UserProfileMenuListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_user_profile_menu, parent, false);

        return new UserProfileMenuListHolder(view);
    }


    @Override
    public void onBindViewHolder(UserProfileMenuListHolder holder, final int position) {
        int i = position;
        holder.setData(i);;
    }

    @Override
    public int getItemCount() {
        return 4;
        //return mMyData.arrChatTargetData.size();
    }
}

class UserProfileMenuListHolder extends RecyclerView.ViewHolder {

    ImageView iv_UserProfile_Menu;
    TextView tv_UserProfile_Menu;
    Context mContext;
    public UserProfileMenuListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_UserProfile_Menu = itemView.findViewById(R.id.iv_UserProfile_Menu);
        tv_UserProfile_Menu = itemView.findViewById(R.id.tv_UserProfile_Menu);
        GlobalApplication.getGlobalApplicationContext().SetContentFont(tv_UserProfile_Menu);
    }

    public void setData(int i)
    {
        if(i == 0)
        {
            tv_UserProfile_Menu.setText("이벤트");
            Glide.with(mContext).load(R.drawable.ic_confetti)
                    .centerCrop()
                    .into(iv_UserProfile_Menu);
        }
        else if(i == 1)
        {
            tv_UserProfile_Menu.setText("공지사항");
            Glide.with(mContext).load(R.drawable.ic_loudspeaker)
                    .centerCrop()
                    .into(iv_UserProfile_Menu);
        }
        else if(i == 2)
        {
            tv_UserProfile_Menu.setText("FAQ");
            Glide.with(mContext).load(R.drawable.ic_faq)
                    .centerCrop()
                    .into(iv_UserProfile_Menu);
        }
        else
        {
            tv_UserProfile_Menu.setText("설정");
            Glide.with(mContext).load(R.drawable.ic_settings)
                    .centerCrop()
                    .into(iv_UserProfile_Menu);
        }
    }
}
