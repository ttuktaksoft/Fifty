package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.R;

public class DialogMenuListAdapter  extends RecyclerView.Adapter<DialogMenuListHolder> {

    Context mContext;
    int mItemCount = 0;
    ArrayList<String> mItemList = new ArrayList<>();

    public DialogMenuListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public DialogMenuListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_dialog_menu_list, parent, false);

        return new DialogMenuListHolder(view);
    }

    @Override
    public void onBindViewHolder(DialogMenuListHolder holder, final int position) {
        int i = position;
        if(mItemList.size() <= position)
            return;

        holder.setData(mItemList.get(i));
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public void setItemCount(int count)
    {
        mItemCount = count;
    }

    public void setItemData(ArrayList<String> list)
    {
        mItemList.clear();
        mItemList.addAll(list);
    }

}

class DialogMenuListHolder extends RecyclerView.ViewHolder {

    TextView tv_Diaolog_Menu;
    Context mContext;

    public DialogMenuListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        tv_Diaolog_Menu = itemView.findViewById(R.id.tv_Diaolog_Menu);
    }

    public void setData(String menu)
    {
        tv_Diaolog_Menu.setText(menu);
    }
}
