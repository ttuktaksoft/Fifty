package fifty.fiftyhouse.com.fifty.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ClubBodyActivity;
import fifty.fiftyhouse.com.fifty.activty.CustomPhotoView;
import fifty.fiftyhouse.com.fifty.activty.MyProfileEditActivity;
import fifty.fiftyhouse.com.fifty.activty.Profile.UserProfileActivity;
import fifty.fiftyhouse.com.fifty.activty.StrContentListActivity;
import fifty.fiftyhouse.com.fifty.activty.UserListActivity;
import fifty.fiftyhouse.com.fifty.adapter.MyProfileClubAdapter;
import fifty.fiftyhouse.com.fifty.adapter.MyProfileEtcAdapter;
import fifty.fiftyhouse.com.fifty.adapter.MyProfileFavoriteAdapter;
import fifty.fiftyhouse.com.fifty.adapter.MyProfilePhotoAdapter;
import fifty.fiftyhouse.com.fifty.adapter.SignUpFavoriteSelectAdapter;
import fifty.fiftyhouse.com.fifty.adapter.UserProfilePhotoAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment  implements MainActivity.onKeyBackPressedListener {

    NestedScrollView ns_MyProfile_Scroll;
    ConstraintLayout v_MyProfile_Info_Detail, v_MyProfile_TopBar;
    ImageView iv_MyProfile_Profile, iv_MyProfile_Info_Gender, iv_MyProfile_Alarm, iv_MyProfile_Shop;
    TextView tv_MyProfile_Info_Name, tv_MyProfile_Info_Age, tv_MyProfile_Info_Memo, tv_MyProfile_Info_Count_Visit, tv_MyProfile_Info_Count_Like, tv_MyProfile_Info_Count_Friend, tv_MyProfile_Name, tv_MyProfile_Info_Favorite;
    RecyclerView rv_MyProfile_Info_Photo, rv_MyProfile_Info_Club, rv_MyProfile_Info_Etc;

    Context mContext;
    private View MyProfileFragView;

    MyProfileFavoriteAdapter mFavoriteAdapter;
    MyProfilePhotoAdapter mPhotoAdapter;
    MyProfileClubAdapter mClubAdapter;
    MyProfileEtcAdapter mEtcAdapter;


    public MyProfileFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyProfileFragment newInstance(String param1, String param2) {
        MyProfileFragment fragment = new MyProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();

        MyProfileFragView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        FragmentManager mFragmentMng = getFragmentManager();
        mFragmentMng.beginTransaction().addToBackStack(null);

        MyProfileFragView.setTag("MyProfileFragment");

        ns_MyProfile_Scroll = MyProfileFragView.findViewById(R.id.ns_MyProfile_Scroll);
        v_MyProfile_Info_Detail = MyProfileFragView.findViewById(R.id.v_MyProfile_Info_Detail);
        iv_MyProfile_Profile = MyProfileFragView.findViewById(R.id.iv_MyProfile_Profile);
        iv_MyProfile_Info_Gender = MyProfileFragView.findViewById(R.id.iv_MyProfile_Info_Gender);
        iv_MyProfile_Alarm = MyProfileFragView.findViewById(R.id.iv_MyProfile_Alarm);
        iv_MyProfile_Shop = MyProfileFragView.findViewById(R.id.iv_MyProfile_Shop);
        tv_MyProfile_Info_Name = MyProfileFragView.findViewById(R.id.tv_MyProfile_Info_Name);
        tv_MyProfile_Info_Age = MyProfileFragView.findViewById(R.id.tv_MyProfile_Info_Age);
        tv_MyProfile_Info_Memo = MyProfileFragView.findViewById(R.id.tv_MyProfile_Info_Memo);
        tv_MyProfile_Info_Count_Visit = MyProfileFragView.findViewById(R.id.tv_MyProfile_Info_Count_Visit);
        tv_MyProfile_Info_Count_Like = MyProfileFragView.findViewById(R.id.tv_MyProfile_Info_Count_Like);
        tv_MyProfile_Info_Count_Friend = MyProfileFragView.findViewById(R.id.tv_MyProfile_Info_Count_Friend);
        tv_MyProfile_Name = MyProfileFragView.findViewById(R.id.tv_MyProfile_Name);
        rv_MyProfile_Info_Photo = MyProfileFragView.findViewById(R.id.rv_MyProfile_Info_Photo);
        rv_MyProfile_Info_Club = MyProfileFragView.findViewById(R.id.rv_MyProfile_Info_Club);
        rv_MyProfile_Info_Etc = MyProfileFragView.findViewById(R.id.rv_MyProfile_Info_Etc);
        v_MyProfile_TopBar = MyProfileFragView.findViewById(R.id.v_MyProfile_TopBar);
        tv_MyProfile_Info_Favorite = MyProfileFragView.findViewById(R.id.tv_MyProfile_Info_Favorite);

        v_MyProfile_TopBar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.alpha));
        tv_MyProfile_Name.setTextColor(ContextCompat.getColor(mContext, R.color.alpha));

        ns_MyProfile_Scroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener(){
            @Override
            public void onScrollChange(NestedScrollView var1, int x, int y, int oldx, int oldy)
            {
                Log.d("test", "test : " + CommonFunc.getInstance().convertPXtoDP(getResources(), y));
                if(CommonFunc.getInstance().convertPXtoDP(getResources(), y) < 50)
                {
                    v_MyProfile_TopBar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.alpha));
                    tv_MyProfile_Name.setTextColor(ContextCompat.getColor(mContext, R.color.alpha));
                }
                else
                {
                    v_MyProfile_TopBar.setBackgroundColor(ContextCompat.getColor(mContext, R.color.topbar_bg));
                    tv_MyProfile_Name.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                }
            }
        });

        v_MyProfile_Info_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 정보 수정 엑티비티
                startActivity(new Intent(mContext, MyProfileEditActivity.class));
            }
        });

        tv_MyProfile_Info_Count_Visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 유저 리스트

                Set KeySet = TKManager.getInstance().MyData.GetUserVisitKeySet();
                Iterator iterator = KeySet.iterator();

                FirebaseManager.getInstance().SetFireBaseLoadingCount(TKManager.getInstance().MyData.GetUserVisitListCount());

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        Intent intent = new Intent(mContext, UserListActivity.class);
                        intent.putExtra("Type",0);
                        intent.putExtra("Count",TKManager.getInstance().MyData.GetUserVisitListCount());
                        startActivity(intent);
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
                    FirebaseManager.getInstance().GetUserData_Simple(key, TKManager.getInstance().MyData_Simple, listener);
                }
            }
        });

        tv_MyProfile_Info_Count_Like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 유저 리스트
                startActivity(new Intent(mContext, UserListActivity.class));
            }
        });

        tv_MyProfile_Info_Count_Friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 유저 리스트
                startActivity(new Intent(mContext, UserListActivity.class));
            }
        });


        initUserData();

        //initFavoriteList();
        initPhotoList();
        initClubList();
        initEtcList();


        return MyProfileFragView;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        ((MainActivity)context).setOnKeyBackPressedListener((MainActivity.onKeyBackPressedListener) this);
    }

    @Override
    public void onBackKey() {
        MainActivity activity = (MainActivity)getActivity();
        activity.setOnKeyBackPressedListener(null);
        activity.onBackPressed();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /*public void initFavoriteList()
    {
        mFavoriteAdapter = new MyProfileFavoriteAdapter(mContext);
        mFavoriteAdapter.setHasStableIds(true);

        rv_MyProfile_Info_Favorite.setAdapter(mFavoriteAdapter);
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(mContext)
                .setChildGravity(Gravity.CENTER)
                .setMaxViewsInRow(2)
                .setGravityResolver(new IChildGravityResolver() {
                    @Override
                    public int getItemGravity(int i) {
                        return Gravity.CENTER;
                    }
                })
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_CENTER_DENSE)
                .withLastRow(true)
                .build();
        rv_MyProfile_Info_Favorite.setLayoutManager(chipsLayoutManager);
    }*/

    public void initUserData()
    {
        tv_MyProfile_Name.setText(TKManager.getInstance().MyData.GetUserNickName());
        tv_MyProfile_Info_Name.setText(TKManager.getInstance().MyData.GetUserNickName());

        Glide.with(mContext).load(TKManager.getInstance().MyData.GetUserImgThumb())
                .centerCrop()
                .circleCrop()
                .into(iv_MyProfile_Profile);


        if (TKManager.getInstance().MyData.GetUserGender() == 0) {
            Glide.with(mContext).load(R.drawable.ic_man_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_MyProfile_Info_Gender);
        } else {
            Glide.with(mContext).load(R.drawable.ic_woman_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_MyProfile_Info_Gender);
        }

        tv_MyProfile_Info_Age.setText(TKManager.getInstance().MyData.GetUserAge() + "세");

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

        tv_MyProfile_Info_Favorite.setText(tempFavorite.get(0) + ", " + tempFavorite.get(1));

        tv_MyProfile_Info_Memo.setText(TKManager.getInstance().MyData.GetUserMemo());

        tv_MyProfile_Info_Count_Visit.setText("방문자 " + TKManager.getInstance().MyData.GetUserTodayVisit() + " / " + TKManager.getInstance().MyData.GetUserTotalVisit());
        tv_MyProfile_Info_Count_Like.setText("좋아요 " + TKManager.getInstance().MyData.GetUserTodayLike() + " / " + TKManager.getInstance().MyData.GetUserTotalLike());
        tv_MyProfile_Info_Count_Friend.setText("친구 " + TKManager.getInstance().MyData.GetUserFriendListCount());
    }


    public void initPhotoList()
    {
        mPhotoAdapter = new MyProfilePhotoAdapter(mContext);
        mPhotoAdapter.setHasStableIds(true);

        rv_MyProfile_Info_Photo.setAdapter(mPhotoAdapter);
        rv_MyProfile_Info_Photo.setLayoutManager(new GridLayoutManager(mContext, 4));
        rv_MyProfile_Info_Photo.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_MyProfile_Info_Photo, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //startActivity(new Intent(getApplicationContext(), ClubBodyActivity.class));
                //startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                /*//CommonFunc.getInstance().ShowToast(view.getContext(), position+"번 째 아이템 클릭", true);
                if (mAppStatus.bCheckMultiSend == false) {
                    stTargetData = mMyData.arrUserAll_Hot_Age.get(position);

                    if (mCommon.getClickStatus() == false)
                        mCommon.MoveUserPage(getActivity(), stTargetData);
                }*/
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void initClubList()
    {
        mClubAdapter = new MyProfileClubAdapter(mContext);
        mClubAdapter.setHasStableIds(true);

        rv_MyProfile_Info_Club.setAdapter(mClubAdapter);
        rv_MyProfile_Info_Club.setLayoutManager(new GridLayoutManager(mContext, 1));
        rv_MyProfile_Info_Club.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_MyProfile_Info_Club, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //startActivity(new Intent(getApplicationContext(), ClubBodyActivity.class));
                //startActivity(new Intent(getApplicationContext(), UserProfileActivity.class));
                /*//CommonFunc.getInstance().ShowToast(view.getContext(), position+"번 째 아이템 클릭", true);
                if (mAppStatus.bCheckMultiSend == false) {
                    stTargetData = mMyData.arrUserAll_Hot_Age.get(position);

                    if (mCommon.getClickStatus() == false)
                        mCommon.MoveUserPage(getActivity(), stTargetData);
                }*/
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }

    public void initEtcList()
    {
        mEtcAdapter = new MyProfileEtcAdapter(mContext);
        mEtcAdapter.setHasStableIds(true);

        rv_MyProfile_Info_Etc.setAdapter(mEtcAdapter);
        rv_MyProfile_Info_Etc.setLayoutManager(new GridLayoutManager(mContext, 1));
        rv_MyProfile_Info_Etc.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_MyProfile_Info_Etc, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(mContext, StrContentListActivity.class));
            }

            @Override
            public void onLongItemClick(View view, int position) {
                //  Toast.makeText(getApplicationContext(),position+"번 째 아이템 롱 클릭",Toast.LENGTH_SHORT).show();
            }
        }));
    }


}
