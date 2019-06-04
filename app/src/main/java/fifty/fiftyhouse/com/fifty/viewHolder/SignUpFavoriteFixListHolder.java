package fifty.fiftyhouse.com.fifty.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.R;

public class SignUpFavoriteFixListHolder extends RecyclerView.ViewHolder {

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
