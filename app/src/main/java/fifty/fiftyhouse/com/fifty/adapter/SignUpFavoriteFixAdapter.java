package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.R;

public class SignUpFavoriteFixAdapter extends RecyclerView.Adapter<SignUpFavoriteFixListHolder> {

    Context mContext;
    public SignUpFavoriteFixAdapter(Context context) {
        mContext = context;
    }

    @Override
    public SignUpFavoriteFixListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_sign_up_favorite_fix, parent, false);

        return new SignUpFavoriteFixListHolder(view);
    }



    @Override
    public void onBindViewHolder(SignUpFavoriteFixListHolder holder, final int position) {
        int i = position;

        holder.setData(i);
    }

    @Override
    public int getItemCount() {
        return 1;
        //return mMyData.arrChatTargetData.size();
    }

}

class SignUpFavoriteFixListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_SignUp_Favorite_Fix_Bg;
    public TextView tv_SignUp_Favorite_Fix_Name;

    public SignUpFavoriteFixListHolder(View itemView) {
        super(itemView);

        iv_SignUp_Favorite_Fix_Bg = itemView.findViewById(R.id.iv_SignUp_Favorite_Fix_Bg);
        tv_SignUp_Favorite_Fix_Name = itemView.findViewById(R.id.tv_SignUp_Favorite_Fix_Name);

    }

    public void setData(int i)
    {
        tv_SignUp_Favorite_Fix_Name.setText("선택해주세요");
    }
}
