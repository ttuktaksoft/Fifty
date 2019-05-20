package fifty.fiftyhouse.com.fifty.viewHolder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.R;

public class ClubListHolder extends RecyclerView.ViewHolder {

    public ImageView iv_Club_Profile;
    public TextView tv_Club_Name;

    public ClubListHolder(View itemView) {
        super(itemView);
        iv_Club_Profile = itemView.findViewById(R.id.iv_Club_Profile);
        tv_Club_Name = itemView.findViewById(R.id.tv_Club_Name);
    }
}
