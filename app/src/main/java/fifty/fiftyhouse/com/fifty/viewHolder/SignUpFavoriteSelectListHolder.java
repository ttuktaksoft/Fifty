package fifty.fiftyhouse.com.fifty.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.R;

public class SignUpFavoriteSelectListHolder extends RecyclerView.ViewHolder {

    TextView tv_SignUp_Favorite_Select_Name;

    public SignUpFavoriteSelectListHolder(View itemView) {
        super(itemView);

        tv_SignUp_Favorite_Select_Name = itemView.findViewById(R.id.tv_SignUp_Favorite_Select_Name);
    }

    public void setData(int i)
    {
        if(i == 0)
            tv_SignUp_Favorite_Select_Name.setText("야구");
        else
            tv_SignUp_Favorite_Select_Name.setText("축구");
    }
}
