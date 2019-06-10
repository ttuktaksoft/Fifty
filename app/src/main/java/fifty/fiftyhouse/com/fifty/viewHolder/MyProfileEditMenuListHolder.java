package fifty.fiftyhouse.com.fifty.viewHolder;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.adapter.MyProfileEditMenuAdapter;

public class MyProfileEditMenuListHolder extends RecyclerView.ViewHolder {

    TextView tv_MyProfile_Edit_Menu_Title;
    TextView tv_MyProfile_Edit_Menu_Desc;

    Context mContext;
    public MyProfileEditMenuListHolder(View itemView) {
        super(itemView);
        mContext = itemView.getContext();

        tv_MyProfile_Edit_Menu_Title = itemView.findViewById(R.id.tv_MyProfile_Edit_Menu_Title);
        tv_MyProfile_Edit_Menu_Desc = itemView.findViewById(R.id.tv_MyProfile_Edit_Menu_Desc);

    }

    public void setData(int i)
    {
        if(i == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_NICKNAME_INDEX)
        {
            // 닉네임
            tv_MyProfile_Edit_Menu_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_NIKCNMAE));
            tv_MyProfile_Edit_Menu_Desc.setText(TKManager.getInstance().MyData.GetUserNickName());
        }
        else if(i == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_STORY_INDEX)
        {
            // 자기소개
            if(true)
            {
                // 있음
                tv_MyProfile_Edit_Menu_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_STORY));
                tv_MyProfile_Edit_Menu_Desc.setTextColor(ContextCompat.getColor(mContext, R.color.black));

                if(TKManager.getInstance().MyData.GetUserMemo().length() >= 20)
                    tv_MyProfile_Edit_Menu_Desc.setText(TKManager.getInstance().MyData.GetUserMemo().substring(0, 20));
                else
                    tv_MyProfile_Edit_Menu_Desc.setText(TKManager.getInstance().MyData.GetUserMemo());
            }
            else
            {
                //없음
                tv_MyProfile_Edit_Menu_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_STORY));
                tv_MyProfile_Edit_Menu_Desc.setTextColor(ContextCompat.getColor(mContext, R.color.gray_light));
                tv_MyProfile_Edit_Menu_Desc.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_STORY_HINT));
            }
        }
        else if(i == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_LOC_INDEX)
        {
            // 지역
            if(true)
            {
                // 있음
                tv_MyProfile_Edit_Menu_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_LOCATION));
                tv_MyProfile_Edit_Menu_Desc.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                tv_MyProfile_Edit_Menu_Desc.setText("지역설정");
            }
            else
            {
                //없음
                tv_MyProfile_Edit_Menu_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_LOCATION));
                tv_MyProfile_Edit_Menu_Desc.setTextColor(ContextCompat.getColor(mContext, R.color.gray_light));
                tv_MyProfile_Edit_Menu_Desc.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_LOCATION_HINT));
            }
        }
        else if(i == MyProfileEditMenuAdapter.MY_PROFILE_EDIT_MENU_FAVORITE_INDEX)
        {
            tv_MyProfile_Edit_Menu_Title.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MY_PROFILE_FAVORITE));

            Map<String, String> tempMapFavorite = TKManager.getInstance().MyData.GetUserFavoriteList();
            Set EntrySet = tempMapFavorite.entrySet();
            Iterator iterator = EntrySet.iterator();

            ArrayList<String> tempFavorite = new ArrayList<>();

            while(iterator.hasNext()){
                Map.Entry entry = (Map.Entry)iterator.next();
                String key = (String)entry.getKey();
                String value = (String)entry.getValue();
                tempFavorite.add(value);
            }
            if(tempFavorite.size() >= 2)
            {
                tv_MyProfile_Edit_Menu_Desc.setText(tempFavorite.get(0) + ", " + tempFavorite.get(1));
            }

        }
    }
}