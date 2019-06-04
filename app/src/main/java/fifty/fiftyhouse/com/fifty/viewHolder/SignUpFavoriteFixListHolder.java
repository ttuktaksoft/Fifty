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
        if(i == 0)
            tv_SignUp_Favorite_Fix_Name.setText("야구");
        else if(i == 1)
            tv_SignUp_Favorite_Fix_Name.setText("야구농구배구축구야구요가헬스");
        else if(i == 2)
            tv_SignUp_Favorite_Fix_Name.setText("배구");
        else
            tv_SignUp_Favorite_Fix_Name.setText("축구");
    }
}
