package fifty.fiftyhouse.com.fifty.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DataBase.ClubData;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ClubActivity;
import fifty.fiftyhouse.com.fifty.activty.FavoriteSelectActivity;
import fifty.fiftyhouse.com.fifty.activty.MyProfileEditActivity;
import fifty.fiftyhouse.com.fifty.adapter.ClubAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

import static android.content.Context.INPUT_METHOD_SERVICE;




public class ClubFragment extends Fragment {

    private Context mContext;
    private View v_FragmentView;

    TextView tv_Club_Create, tv_Club_Recom;
    ImageView iv_Club_TopBar_Search, iv_Club_TopBar_User;
    EditText et_Club_TopBar_Search;

    RecyclerView rv_Club_List;
    ClubAdapter mAdapter;

    InputMethodManager imm;

    MainActivity.MoveFragmentListener mMoveListener = null;
    ArrayList<String> mClubList = new ArrayList<>();

    boolean bSearchClub = false;
    int MY_PROFILE_EDIT = 1;

    public void SetMoveListener(MainActivity.MoveFragmentListener listener)
    {
        mMoveListener = listener;
    }

    public ClubFragment() {
        // Required empty public constructor
    }

    public static ClubFragment newInstance(String param1, String param2) {
        ClubFragment fragment = new ClubFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getContext();
        v_FragmentView = inflater.inflate(R.layout.fragment_club, container, false);
        imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);

        rv_Club_List = v_FragmentView.findViewById(R.id.rv_Club_List);
        iv_Club_TopBar_Search = v_FragmentView.findViewById(R.id.iv_Club_TopBar_Search);
        iv_Club_TopBar_User = v_FragmentView.findViewById(R.id.iv_Club_TopBar_User);
        et_Club_TopBar_Search = v_FragmentView.findViewById(R.id.et_Club_TopBar_Search);
        tv_Club_Recom = v_FragmentView.findViewById(R.id.tv_Club_Recom);
        tv_Club_Create = v_FragmentView.findViewById(R.id.tv_Club_Create);
        //et_Club_TopBar_Search.setImeOptions(EditorInfo.IME_ACTION_SEARCH);

        et_Club_TopBar_Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        switch (actionId) {
                            case EditorInfo.IME_ACTION_SEARCH:
                                // 검색 동작
                                imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);
                                SearchClub(et_Club_TopBar_Search.getText().toString());

                                break;
                            default:
                                // 기본 엔터키 동작
                                imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);
                                SearchClub(et_Club_TopBar_Search.getText().toString());
                                return false;
                        }
                        return true;
                    }
                });

        /*
                .setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        switch (actionId) {
                            case EditorInfo.IME_ACTION_SEARCH:
                                imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);
                                SearchClub(et_Club_TopBar_Search.getText().toString());
                                break;
                            default:
                                // 기본 엔터키 동작
                                return false;
                        }
                        return true;
                    }
                });*/

        iv_Club_TopBar_Search.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);

                SearchClub(et_Club_TopBar_Search.getText().toString());
            }
        });

        iv_Club_TopBar_Search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);
                    SearchClub(et_Club_TopBar_Search.getText().toString());
                    return true;
                }
                return false;
            }
        });

        Glide.with(mContext).load(TKManager.getInstance().MyData.GetUserImgThumb())
                .centerCrop()
                .circleCrop()
                .into(iv_Club_TopBar_User);

        iv_Club_TopBar_User.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);

                startActivityForResult(new Intent(mContext, MyProfileEditActivity.class), MY_PROFILE_EDIT);
            }
        });

        tv_Club_Create.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);
                TKManager.getInstance().CreateTempClubData.ClubFavorite.clear();
                DialogFunc.getInstance().ShowLoadingPage(mContext);
                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();
                        Intent intent = new Intent(mContext, FavoriteSelectActivity.class);
                        intent.putExtra("Type",2);
                        startActivity(intent);
                    }

                    @Override
                    public void CompleteListener_Yes() {
                    }

                    @Override
                    public void CompleteListener_No() {
                    }
                };

                FirebaseManager.getInstance().GetPopFavoriteData(listener);
            }
        });

        tv_Club_Recom.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                imm.hideSoftInputFromWindow(et_Club_TopBar_Search.getWindowToken(), 0);
            }
        });

        TKManager.getInstance().mUpdateClubFragmentFunc = new TKManager.UpdateUIFunc(){
            @Override
            public void UpdateUI() {
                RefreshAdapter();
                mAdapter.notifyDataSetChanged();
            }
        };

        initRecyclerView();

        return v_FragmentView;
    }

    private void initRecyclerView() {
        mAdapter = new ClubAdapter(getContext());
        RefreshAdapter();
        mAdapter.setHasStableIds(true);

        rv_Club_List.setAdapter(mAdapter);
        rv_Club_List.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rv_Club_List.offsetLeftAndRight(CommonFunc.getInstance().convertDPtoPX(getResources(),20));
        rv_Club_List.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), rv_Club_List, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, final int position) {

                Map<String, ClubData> tempClubKey = new LinkedHashMap<>();
                tempClubKey.putAll(TKManager.getInstance().MyData.GetUserClubData());
                tempClubKey.putAll(TKManager.getInstance().SearchClubList);

                Set tempKey = tempClubKey.keySet(); //TKManager.getInstance().MyData.GetUserClubDataKeySet();
                final List array = new ArrayList(tempKey);

                //DialogFunc.getInstance().ShowLoadingPage(MainActivity.mActivity);

            /*    FirebaseManager.CheckFirebaseComplete GetClubDataListener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {

                        FirebaseManager.CheckFirebaseComplete GetClubContextListener = new FirebaseManager.CheckFirebaseComplete() {
                            @Override
                            public void CompleteListener() {
                                startActivity(new Intent(getContext(), ClubActivity.class));
                            }

                            @Override
                            public void CompleteListener_Yes() {}
                            @Override
                            public void CompleteListener_No() {}
                        };

                        FirebaseManager.getInstance().GetClubContextData(TKManager.getInstance().ClubData_Simple.get(array.get(position).toString()).GetClubIndex(), GetClubContextListener);
                    }

                    @Override
                    public void CompleteListener_Yes() {}
                    @Override
                    public void CompleteListener_No() {}
                };

                FirebaseManager.getInstance().GetClubData(TKManager.getInstance().ClubData_Simple.get(array.get(position).toString()).GetClubIndex(),
                        TKManager.getInstance().TargetClubData, GetClubDataListener);*/
            }
        }));
    }

    public void RefreshAdapter()
    {
        RefreshUserList();
        mAdapter.setItemCount(mClubList.size());
        mAdapter.setItemData(mClubList);
    }

    public void RefreshUserList()
    {
        mClubList.clear();

        if(!bSearchClub)
            mClubList.addAll(TKManager.getInstance().MyData.GetUserClubDataKeySet());
        else
            mClubList.addAll(TKManager.getInstance().SearchClubList.keySet());
    }

    private void SearchClub(String name)
    {
        FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
            @Override
            public void CompleteListener() {
                RefreshAdapter();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void CompleteListener_Yes() {

            }

            @Override
            public void CompleteListener_No() {

            }
        };

        if(CommonFunc.getInstance().CheckStringNull(name))
        {
            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.CLUB_SEARCH_EMPTY));
        }
        else {
            bSearchClub = true;
            FirebaseManager.getInstance().SearchClubList(name, listener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MY_PROFILE_EDIT) {
            Glide.with(mContext).load(TKManager.getInstance().MyData.GetUserImgThumb())
                    .centerCrop()
                    .circleCrop()
                    .into(iv_Club_TopBar_User);
        }
    }

}
