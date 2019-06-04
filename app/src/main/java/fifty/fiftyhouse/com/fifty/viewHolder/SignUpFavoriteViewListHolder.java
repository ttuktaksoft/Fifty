package fifty.fiftyhouse.com.fifty.viewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.R;

public class SignUpFavoriteViewListHolder extends RecyclerView.ViewHolder {

    TextView tv_SignUp_Favorite_View_Name;
    public SignUpFavoriteViewListHolder(View itemView) {
        super(itemView);
        tv_SignUp_Favorite_View_Name = itemView.findViewById(R.id.tv_SignUp_Favorite_View_Name);
    }

    public void setData(int i)
    {

    }
}