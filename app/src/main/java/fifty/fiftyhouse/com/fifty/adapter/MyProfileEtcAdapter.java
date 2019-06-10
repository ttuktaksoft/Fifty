package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import fifty.fiftyhouse.com.fifty.R;

public class MyProfileEtcAdapter extends RecyclerView.Adapter<MyProfileEtcListHolder> {

    Context mContext;

    public MyProfileEtcAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MyProfileEtcListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_myprofile_etc, parent, false);

        return new MyProfileEtcListHolder(view);
    }


    @Override
    public void onBindViewHolder(MyProfileEtcListHolder holder, final int position) {
        int i = position;
        holder.setData(i);;
    }

    @Override
    public int getItemCount() {
        return 4;
        //return mMyData.arrChatTargetData.size();
    }
}

class MyProfileEtcListHolder extends RecyclerView.ViewHolder {

    ImageView iv_MyProfile_Etc;
    TextView tv_MyProfile_Etc;
    Context mContext;
    public MyProfileEtcListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        iv_MyProfile_Etc = itemView.findViewById(R.id.iv_MyProfile_Etc);
        tv_MyProfile_Etc = itemView.findViewById(R.id.tv_MyProfile_Etc);
    }

    public void setData(int i)
    {
        if(i == 0)
        {
            tv_MyProfile_Etc.setText("이벤트");
            Glide.with(mContext).load(R.drawable.ic_confetti)
                    .centerCrop()
                    .into(iv_MyProfile_Etc);
        }
        else if(i == 1)
        {
            tv_MyProfile_Etc.setText("공지사항");
            Glide.with(mContext).load(R.drawable.ic_loudspeaker)
                    .centerCrop()
                    .into(iv_MyProfile_Etc);
        }
        else if(i == 2)
        {
            tv_MyProfile_Etc.setText("FAQ");
            Glide.with(mContext).load(R.drawable.ic_faq)
                    .centerCrop()
                    .into(iv_MyProfile_Etc);
        }
        else
        {
            tv_MyProfile_Etc.setText("설정");
            Glide.with(mContext).load(R.drawable.ic_settings)
                    .centerCrop()
                    .into(iv_MyProfile_Etc);
        }
    }
}
