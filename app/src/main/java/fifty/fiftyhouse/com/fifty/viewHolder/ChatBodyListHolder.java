package fifty.fiftyhouse.com.fifty.viewHolder;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.R;

public class ChatBodyListHolder extends RecyclerView.ViewHolder {

    public  ConstraintLayout test;
    public ChatBodyListHolder(View itemView) {
        super(itemView);

        test = itemView.findViewById(R.id.test);
    }

    public void setChatData()
    {
        test.setVisibility(View.GONE);
    }
}
