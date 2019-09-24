package fifty.fiftyhouse.com.fifty.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubContextData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ClubActivity;
import fifty.fiftyhouse.com.fifty.activty.ClubBodyActivity;
import fifty.fiftyhouse.com.fifty.activty.ClubWriteActivity;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

import static fifty.fiftyhouse.com.fifty.adapter.ClubContentListHolder.CLUB_CONTENT_TYPE.BIG_IMG;
import static fifty.fiftyhouse.com.fifty.adapter.ClubContentListHolder.CLUB_CONTENT_TYPE.DESC;
import static fifty.fiftyhouse.com.fifty.adapter.ClubContentListHolder.CLUB_CONTENT_TYPE.IMG;

public class ClubContentAdapter extends RecyclerView.Adapter<ClubContentListHolder> {

    Context mContext;
    int mItemCount = 0;
    ArrayList<String> mItemData = new ArrayList<>();
    boolean mClubBody;
    public ClubContentAdapter(Context context, boolean body) {
        mContext = context;
        mClubBody = body;
    }


    @Override
    public ClubContentListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_club_content, parent, false);

        return new ClubContentListHolder(view, mClubBody);
    }

    @Override
    public void onBindViewHolder(ClubContentListHolder holder, final int position) {
        int i = position;
        holder.setClubContent(mItemData.get(i));
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public void setItemData(ArrayList<String> list) {
        mItemData.clear();
        mItemData.addAll(list);

        mItemCount = mItemData.size();
    }
}

class ClubContentListHolder extends RecyclerView.ViewHolder {

    ClubContextData tempData = new ClubContextData();

    public enum CLUB_CONTENT_TYPE {
        DESC,
        BIG_IMG,
        IMG,
    }
    ConstraintLayout v_Club_Con_View, v_Club_Reply_Count;
    public ImageView iv_Club_Con_Profile, iv_Club_Con_Menu;
    public TextView tv_Club_Con_Nickname, tv_Club_Con_Date, tv_Club_Con_Desc, tv_Club_Reply_Count, tv_Club_Con_Report;
    public ImageView tv_Club_Con_BigImg, tv_Club_Con_Img_1, tv_Club_Con_Img_2, tv_Club_Con_Img_3;
    public RecyclerView rv_Club_Reply_List;
    CLUB_CONTENT_TYPE mContentType;
    ClubContentReplyAdapter mAdapter;
    Context mContext;

    String mClubKey;
    boolean mClubBody;

    public ClubContentListHolder(View itemView, boolean body) {
        super(itemView);
        mContext = itemView.getContext();
        mClubBody = body;

        v_Club_Con_View = itemView.findViewById(R.id.v_Club_Con_View);
        v_Club_Reply_Count = itemView.findViewById(R.id.v_Club_Reply_Count);
        iv_Club_Con_Profile = itemView.findViewById(R.id.iv_Club_Con_Profile);
        tv_Club_Con_Nickname = itemView.findViewById(R.id.tv_Club_Con_Nickname);
        tv_Club_Con_Date = itemView.findViewById(R.id.tv_Club_Con_Date);
        tv_Club_Con_Desc = itemView.findViewById(R.id.tv_Club_Con_Desc);
        tv_Club_Con_Report = itemView.findViewById(R.id.tv_Club_Con_Report);
        tv_Club_Con_BigImg = itemView.findViewById(R.id.tv_Club_Con_BigImg);
        tv_Club_Con_Img_1 = itemView.findViewById(R.id.tv_Club_Con_Img_1);
        tv_Club_Con_Img_2 = itemView.findViewById(R.id.tv_Club_Con_Img_2);
        tv_Club_Con_Img_3 = itemView.findViewById(R.id.tv_Club_Con_Img_3);
        tv_Club_Reply_Count = itemView.findViewById(R.id.tv_Club_Reply_Count);
        rv_Club_Reply_List = itemView.findViewById(R.id.rv_Club_Reply_List);
        iv_Club_Con_Menu = itemView.findViewById(R.id.iv_Club_Con_Menu);


        if(TKManager.getInstance().TargetClubData.ExistClubMember(TKManager.getInstance().MyData.GetUserIndex()))
        {
            iv_Club_Con_Menu.setVisibility(View.VISIBLE);
        }
        else
            iv_Club_Con_Menu.setVisibility(View.GONE);

        int viewCount = 3;
        int thumbnailMargin = CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 2);
        int thumbnailMargin_2 = CommonFunc.getInstance().convertDPtoPX(mContext.getResources(), 5);
        int thumbnailSize = CommonFunc.getInstance().getWidthByDevice() / viewCount;

        ConstraintLayout.LayoutParams lp_Club_Con_BigImg = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, thumbnailSize);
        lp_Club_Con_BigImg.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Club_Con_BigImg.topToBottom = tv_Club_Con_Report.getId();
        //lp_Club_Con_BigImg.setMargins(thumbnailMargin_2,thumbnailMargin,thumbnailMargin_2,thumbnailMargin);
        tv_Club_Con_BigImg.setPadding(thumbnailMargin_2,thumbnailMargin_2,thumbnailMargin_2,thumbnailMargin_2);
        tv_Club_Con_BigImg.setLayoutParams(lp_Club_Con_BigImg);

        ConstraintLayout.LayoutParams lp_Club_Con_Img_1 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Club_Con_Img_1.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
        lp_Club_Con_Img_1.topToBottom = tv_Club_Con_Report.getId();
        //lp_Club_Con_Img_1.setMargins(thumbnailMargin_2,thumbnailMargin,thumbnailMargin,thumbnailMargin_2);
        tv_Club_Con_Img_1.setLayoutParams(lp_Club_Con_Img_1);
        tv_Club_Con_Img_1.setPadding(thumbnailMargin,thumbnailMargin,thumbnailMargin,thumbnailMargin);

        ConstraintLayout.LayoutParams lp_Club_Con_Img_2 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Club_Con_Img_2.leftToRight = tv_Club_Con_Img_1.getId();
        lp_Club_Con_Img_2.topToTop = tv_Club_Con_Img_1.getId();
        //lp_Club_Con_Img_2.setMargins(thumbnailMargin,0,thumbnailMargin,0);
        tv_Club_Con_Img_2.setLayoutParams(lp_Club_Con_Img_2);
        tv_Club_Con_Img_2.setPadding(thumbnailMargin,thumbnailMargin,thumbnailMargin,thumbnailMargin);

        ConstraintLayout.LayoutParams lp_Club_Con_Img_3 = new ConstraintLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp_Club_Con_Img_3.leftToRight = tv_Club_Con_Img_2.getId();
        lp_Club_Con_Img_3.topToTop = tv_Club_Con_Img_2.getId();
        //lp_Club_Con_Img_3.setMargins(thumbnailMargin,0,thumbnailMargin,0);
        tv_Club_Con_Img_3.setLayoutParams(lp_Club_Con_Img_3);
        tv_Club_Con_Img_3.setPadding(thumbnailMargin,thumbnailMargin,thumbnailMargin,thumbnailMargin);

        if(mClubBody)
        {
            v_Club_Reply_Count.setVisibility(View.GONE);
            rv_Club_Reply_List.setVisibility(View.VISIBLE);
        }
        else
        {
            v_Club_Reply_Count.setVisibility(View.VISIBLE);
            rv_Club_Reply_List.setVisibility(View.GONE);
        }
        initReplyList();
    }

    public void setClubContent(final String key)
    {
        tempData = TKManager.getInstance().TargetClubData.GetClubContext(key);
        TKManager.getInstance().TargetContextData = tempData;
        mClubKey = key;

        tv_Club_Con_BigImg.setVisibility(View.GONE);
        tv_Club_Con_Img_1.setVisibility(View.GONE);
        tv_Club_Con_Img_2.setVisibility(View.GONE);
        tv_Club_Con_Img_3.setVisibility(View.GONE);

       switch (tempData.ContextType)
       {
           case 0:
               setClubContentType(DESC);
               break;
           case 1:
               setClubContentType(BIG_IMG);
               if(!CommonFunc.getInstance().CheckStringNull(tempData.GetImg("0")))
               {
                   tv_Club_Con_BigImg.setVisibility(View.VISIBLE);
                   CommonFunc.getInstance().DrawImageByGlide(mContext, tv_Club_Con_BigImg, tempData.GetImg("0"), false);
               }
               break;
           case 2:
               setClubContentType(IMG);

               if(!CommonFunc.getInstance().CheckStringNull(tempData.GetImg("0")))
               {
                   tv_Club_Con_Img_1.setVisibility(View.VISIBLE);
                   CommonFunc.getInstance().DrawImageByGlide(mContext, tv_Club_Con_Img_1, tempData.GetImg("0"), false);
               }
               if(!CommonFunc.getInstance().CheckStringNull(tempData.GetImg("1")))
               {
                   tv_Club_Con_Img_2.setVisibility(View.VISIBLE);
                   CommonFunc.getInstance().DrawImageByGlide(mContext, tv_Club_Con_Img_2, tempData.GetImg("1"), false);
               }
               if(!CommonFunc.getInstance().CheckStringNull(tempData.GetImg("2")))
               {
                   tv_Club_Con_Img_3.setVisibility(View.VISIBLE);
                   CommonFunc.getInstance().DrawImageByGlide(mContext, tv_Club_Con_Img_3, tempData.GetImg("2"), false);
               }


               break;
       }

        iv_Club_Con_Profile.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                String UserIndex = tempData.GetWriterIndex();

                if(UserIndex.equals(TKManager.getInstance().MyData.GetUserIndex()) == false)
                {
                    CommonFunc.getInstance().GetUserDataInFireBase(UserIndex, ClubActivity.mClubActivity, true);
                }

            }
        });

        iv_Club_Con_Menu.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                // 내가 쓴거
                if(tempData.GetWriterIndex().equals(TKManager.getInstance().MyData.GetUserIndex()))
                {
                    ArrayList<String> menuList = new ArrayList<>();
                    menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TRY_DEL));
                    menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TRY_EDIT));
                    menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CANCEL));

                    ArrayList<DialogFunc.MsgPopupListener> menuListenerList = new ArrayList<>();
                    menuListenerList.add(new DialogFunc.MsgPopupListener()
                    {
                        @Override
                        public void Listener()
                        {
                            DialogFunc.getInstance().ShowLoadingPage(ClubActivity.mClubActivity);
                            // 삭제
                            FirebaseManager.CheckFirebaseComplete removeListener = new FirebaseManager.CheckFirebaseComplete() {
                                @Override
                                public void CompleteListener() {
                                    TKManager.getInstance().TargetClubData.DelClubContext(key);
                                    TKManager.getInstance().mUpdateClubActivityFunc.UpdateUI();
                                    DialogFunc.getInstance().DismissLoadingPage();
                                }

                                @Override
                                public void CompleteListener_Yes() {

                                }

                                @Override
                                public void CompleteListener_No() {
                                    DialogFunc.getInstance().DismissLoadingPage();
                                    TKManager.getInstance().mUpdateClubActivityFunc.UpdateUI();
                                }
                            };
                            FirebaseManager.getInstance().RemoveClubContext(TKManager.getInstance().TargetClubData.GetClubIndex(), tempData.GetContextIndex(), removeListener);
                        }
                    });
                    menuListenerList.add(new DialogFunc.MsgPopupListener()
                    {
                        @Override
                        public void Listener()
                        {
                            Intent intent = new Intent(mContext, ClubWriteActivity.class);
                            intent.putExtra("Type", 1);
                            intent.putExtra("key", key);
                            ClubActivity.mClubActivity.startActivityForResult(intent, 1000);
                        }
                    });

                    DialogFunc.getInstance().ShowMenuListPopup(ClubActivity.mClubActivity, menuList, menuListenerList, null);
                }
                else
                {
                    ArrayList<String> menuList = new ArrayList<>();
                    if(TKManager.getInstance().TargetClubData.GetClubMasterIndex().equals(TKManager.getInstance().MyData.GetUserIndex())) {
                        menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TRY_DEL));
                    }
                    menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_TRY_REPORT));
                    menuList.add(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_CANCEL));


                    ArrayList<DialogFunc.MsgPopupListener> menuListenerList = new ArrayList<>();

                    if(TKManager.getInstance().TargetClubData.GetClubMasterIndex().equals(TKManager.getInstance().MyData.GetUserIndex()))
                    {
                        menuListenerList.add(new DialogFunc.MsgPopupListener()
                        {
                            @Override
                            public void Listener()
                            {
                                DialogFunc.getInstance().ShowLoadingPage(ClubActivity.mClubActivity);
                                // 삭제
                                FirebaseManager.CheckFirebaseComplete removeListener = new FirebaseManager.CheckFirebaseComplete() {
                                    @Override
                                    public void CompleteListener() {
                                        TKManager.getInstance().TargetClubData.DelClubContext(key);
                                        TKManager.getInstance().mUpdateClubActivityFunc.UpdateUI();
                                        DialogFunc.getInstance().DismissLoadingPage();
                                    }

                                    @Override
                                    public void CompleteListener_Yes() {

                                    }

                                    @Override
                                    public void CompleteListener_No() {
                                        DialogFunc.getInstance().DismissLoadingPage();
                                        TKManager.getInstance().mUpdateClubActivityFunc.UpdateUI();
                                    }
                                };
                                FirebaseManager.getInstance().RemoveClubContext(TKManager.getInstance().TargetClubData.GetClubIndex(), tempData.GetContextIndex(), removeListener);
                            }
                        });

                    }

                    menuListenerList.add(new DialogFunc.MsgPopupListener()
                    {
                        @Override
                        public void Listener()
                        {
                            // 신고
                            FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                                @Override
                                public void CompleteListener() {
                                    DialogFunc.getInstance().ShowToast(ClubActivity.mClubActivity, "신고되었습니다", false);
                                    TKManager.getInstance().TargetClubData.GetClubContext(key).SetReportList(TKManager.getInstance().MyData.GetUserIndex(), TKManager.getInstance().MyData.GetUserIndex());
                                    TKManager.getInstance().mUpdateClubActivityFunc.UpdateUI();
                                    DialogFunc.getInstance().DismissLoadingPage();
                                }

                                @Override
                                public void CompleteListener_Yes() {

                                }

                                @Override
                                public void CompleteListener_No() {

                                }
                            };

                            FirebaseManager.getInstance().RegistClubReport(TKManager.getInstance().TargetClubData.GetClubIndex(), tempData, listener );

                        }
                    });

                    DialogFunc.getInstance().ShowMenuListPopup(ClubActivity.mClubActivity, menuList, menuListenerList, null);
                }
            }
        });

       v_Club_Con_View.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, ClubBodyActivity.class);
                intent.putExtra("Type",0);
                intent.putExtra("key", key);
                ClubActivity.mClubActivity.startActivityForResult(intent, 1000);
            }
        });

        v_Club_Reply_Count.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                Intent intent = new Intent(mContext, ClubBodyActivity.class);
                intent.putExtra("Type",0);
                intent.putExtra("key", key);
                ClubActivity.mClubActivity.startActivityForResult(intent, 1000);
            }
        });

        tv_Club_Reply_Count.setText(Integer.toString(tempData.GetReplyDataCount()));

        CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Club_Con_Profile, TKManager.getInstance().UserData_Simple.get(tempData.GetWriterIndex()).GetUserImgThumb(), true);

        tv_Club_Con_Nickname.setText(TKManager.getInstance().UserData_Simple.get(tempData.GetWriterIndex()).GetUserNickName());
        tv_Club_Con_Date.setText(CommonFunc.getInstance().ConvertTimeSrt(tempData.Date, "MM.dd HH:mm"));

        if(CommonFunc.getInstance().CheckStringNull(tempData.Context))
        {
            tv_Club_Con_Desc.setVisibility(View.INVISIBLE);
        }
        else
            tv_Club_Con_Desc.setText(tempData.Context);

        if(CommonFunc.getInstance().CheckStringNull(tempData.GetReportList(TKManager.getInstance().MyData.GetUserIndex())) == false)
            tv_Club_Con_Report.setVisibility(View.VISIBLE);
        else
            tv_Club_Con_Report.setVisibility(View.GONE);

        mAdapter.setReplyCount(tempData.GetReplyDataCount());
    }

    private void setClubContentType(CLUB_CONTENT_TYPE type)
    {
        mContentType = type;
        ConstraintLayout.LayoutParams lp_Club_Reply_List = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        switch (type)
        {
            case DESC:
                lp_Club_Reply_List.topToBottom = tv_Club_Con_Report.getId();
                break;
            case BIG_IMG:
                lp_Club_Reply_List.topToBottom = tv_Club_Con_BigImg.getId();
                break;
            case IMG:
                lp_Club_Reply_List.topToBottom = tv_Club_Con_Img_1.getId();
                break;
        }

        if(mClubBody)
            rv_Club_Reply_List.setLayoutParams(lp_Club_Reply_List);
        else
            v_Club_Reply_Count.setLayoutParams(lp_Club_Reply_List);
    }

    private void initReplyList()
    {
        mAdapter = new ClubContentReplyAdapter(mContext);
        mAdapter.setHasStableIds(true);

        rv_Club_Reply_List.setAdapter(mAdapter);
        rv_Club_Reply_List.setLayoutManager(new GridLayoutManager(mContext, 1));
        rv_Club_Reply_List.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_Club_Reply_List, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {
                Intent intent = new Intent(mContext, ClubBodyActivity.class);
                intent.putExtra("Type",0);
                intent.putExtra("key", mClubKey);
                ClubActivity.mClubActivity.startActivityForResult(intent, 1000);
            }
        }));
    }
}
