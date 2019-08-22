package fifty.fiftyhouse.com.fifty.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubContextData;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ClubBodyActivity;
import fifty.fiftyhouse.com.fifty.activty.CustomPhotoView;
import fifty.fiftyhouse.com.fifty.adapter.ClubBodyImgAdapter;
import fifty.fiftyhouse.com.fifty.adapter.ClubBodyReplyAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class ClubBodyFragment extends Fragment {

    ImageView iv_Club_Body_Profile;
    TextView tv_Club_Body_Nickname, tv_Club_Body_Date, tv_Club_Body_Desc, tv_Club_Body_Report;
    RecyclerView rv_Club_Body_Img_List, rv_Club_Body_Reply_List;
    ClubBodyImgAdapter mImgAdapter;
    ClubBodyReplyAdapter mReplyAdapter;

    Context mContext;
    FragmentManager mFragmentMgr;
    View v_FragmentView = null;

    ArrayList<String> mReplyList = new ArrayList<>();

    public ClubContextData tempData = null;

    public ClubBodyFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static ClubWriteFragment newInstance(String param1, String param2) {
        ClubWriteFragment fragment = new ClubWriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(v_FragmentView != null)
        {
            mImgAdapter.notifyDataSetChanged();
            mReplyAdapter.notifyDataSetChanged();
            return v_FragmentView;
        }


        // Inflate the layout for this fragment
        mContext = getActivity();
        mFragmentMgr = ((FragmentActivity) mContext).getSupportFragmentManager();
        v_FragmentView = inflater.inflate(R.layout.fragment_club_body, container, false);

        iv_Club_Body_Profile = v_FragmentView.findViewById(R.id.iv_Club_Body_Profile);
        tv_Club_Body_Nickname = v_FragmentView.findViewById(R.id.tv_Club_Body_Nickname);
        tv_Club_Body_Date = v_FragmentView.findViewById(R.id.tv_Club_Body_Date);
        tv_Club_Body_Desc = v_FragmentView.findViewById(R.id.tv_Club_Body_Desc);
        rv_Club_Body_Img_List = v_FragmentView.findViewById(R.id.rv_Club_Body_Img_List);
        rv_Club_Body_Reply_List = v_FragmentView.findViewById(R.id.rv_Club_Body_Reply_List);
        tv_Club_Body_Report = v_FragmentView.findViewById(R.id.tv_Club_Body_Report);

        CommonFunc.getInstance().DrawImageByGlide(mContext, iv_Club_Body_Profile, TKManager.getInstance().UserData_Simple.get(tempData.GetWriterIndex()).GetUserImgThumb(), true);
        tv_Club_Body_Nickname.setText(TKManager.getInstance().UserData_Simple.get(tempData.GetWriterIndex()).GetUserNickName());
        tv_Club_Body_Date.setText(CommonFunc.getInstance().ConvertTimeSrt(tempData.GetDate(), "MM.dd HH:mm"));
        tv_Club_Body_Desc.setText(tempData.GetContext());

        if(CommonFunc.getInstance().CheckStringNull(tempData.GetReportList(TKManager.getInstance().MyData.GetUserIndex())) == false)
            tv_Club_Body_Report.setVisibility(View.VISIBLE);
        else
            tv_Club_Body_Report.setVisibility(View.GONE);

        iv_Club_Body_Profile.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                String UserIndex = tempData.GetWriterIndex();

                if(UserIndex.equals(TKManager.getInstance().MyData.GetUserIndex()) == false)
                {
                    CommonFunc.getInstance().GetUserDataInFireBase(UserIndex, ClubBodyActivity.mClubBodyActivity, true);
                    /*
                    DialogFunc.getInstance().ShowLoadingPage(ClubBodyActivity.mClubBodyActivity);

                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {


                            Set KeySet = TKManager.getInstance().TargetUserData.GetUserClubDataKeySet();

                            if(KeySet.size() > 0)
                            {
                                Iterator iterator = KeySet.iterator();

                                FirebaseManager.getInstance().SetFireBaseLoadingCount(TKManager.getInstance().TargetUserData.GetUserClubDataCount());

                                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                                    @Override
                                    public void CompleteListener() {
                                        DialogFunc.getInstance().DismissLoadingPage();
                                        Intent intent = new Intent(mContext, UserProfileActivity.class);
                                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                        mContext.startActivity(intent);
                                    }

                                    @Override
                                    public void CompleteListener_Yes() {
                                    }

                                    @Override
                                    public void CompleteListener_No() {
                                    }
                                };

                                while(iterator.hasNext()){
                                    String key = (String)iterator.next();
                                    if(TKManager.getInstance().ClubData_Simple.get(key) != null)
                                    {
                                        FirebaseManager.getInstance().Complete(listener);
                                    }
                                    else
                                        FirebaseManager.getInstance().GetClubData_Simple(key, TKManager.getInstance().ClubData_Simple, listener);
                                }
                            }
                            else
                            {
                                DialogFunc.getInstance().DismissLoadingPage();

                                Intent intent = new Intent(mContext, UserProfileActivity.class);
                                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                mContext.startActivity(intent);
                            }
                        }

                        @Override
                        public void CompleteListener_Yes() {
                        }

                        @Override
                        public void CompleteListener_No() {
                            DialogFunc.getInstance().DismissLoadingPage();
                        }
                    };

                    FirebaseManager.getInstance().GetUserData(UserIndex, TKManager.getInstance().TargetUserData, listener);*/

                }

            }
        });


        initRecyclerImgView();
        initRecyclerReplyView();

        return v_FragmentView;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        // ((MainActivity)context).setOnKeyBackPressedListener((MainActivity.onKeyBackPressedListener) this);
    }

    private void initRecyclerImgView()
    {
        mImgAdapter = new ClubBodyImgAdapter(mContext, tempData);
        mImgAdapter.setImgCount(tempData.GetImgCount());
        mImgAdapter.setHasStableIds(true);

        rv_Club_Body_Img_List.setAdapter(mImgAdapter);
        rv_Club_Body_Img_List.setLayoutManager(new GridLayoutManager(mContext, 1));
        rv_Club_Body_Img_List.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_Club_Body_Img_List, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

                ArrayList<String> list = new ArrayList<>();
                Iterator<String> iterator = tempData.GetImg().keySet().iterator();
                while(iterator.hasNext()){
                    String key = iterator.next();
                    list.add(tempData.GetImg(key));
                }
                Intent intent = new Intent(mContext, CustomPhotoView.class);
                intent.putExtra("Type", CustomPhotoView.PHOTO_VIEW_TYPE_DATAS);
                intent.putExtra("datas", list);
                startActivity(intent);

            }
        }));
    }

    private void initRecyclerReplyView()
    {
        mReplyAdapter = new ClubBodyReplyAdapter(mContext);
        mReplyList.clear();
        mReplyList.addAll(tempData.GetReplyDataKeySet());
        mReplyAdapter.setItemCount(mReplyList.size());
        mReplyAdapter.setItemData(mReplyList);
        mReplyAdapter.setHasStableIds(true);

        rv_Club_Body_Reply_List.setAdapter(mReplyAdapter);
        rv_Club_Body_Reply_List.setLayoutManager(new GridLayoutManager(mContext, 1));
    }

    public void RefreshReply()
    {
        mReplyList.clear();
        mReplyList.addAll(tempData.GetReplyDataKeySet());
        mReplyAdapter.setItemCount(mReplyList.size());
        mReplyAdapter.setItemData(mReplyList);
        mReplyAdapter.notifyDataSetChanged();
    }
}