package fifty.fiftyhouse.com.fifty.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.FavoriteSelectActivity;
import fifty.fiftyhouse.com.fifty.activty.SignUpActivity;
import fifty.fiftyhouse.com.fifty.adapter.FavoriteViewAdapter;
import fifty.fiftyhouse.com.fifty.util.OnRecyclerItemClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;
import fifty.fiftyhouse.com.fifty.util.OnSingleTouchListener;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class SignUpFragment extends Fragment {
    ImageView iv_SignUp_Profile, iv_SignUp_Gender;
    EditText et_SignUp_NickName, et_SignUp_PassWord;
    TextView tv_SignUp_NickName_Check, tv_SignUp_NickName_Check_Result, tv_SignUp_Gender, tv_SignUp_Age;
    RecyclerView rv_SignUp_Favorite;

    Context mContext;
    FragmentManager mFragmentMgr;
    View v_FragmentView = null;

    static final int GET_FROM_FAVORITE_SELECT = 2;
    boolean isProfileUpload = false;
    boolean mIsCheckNickName = false;
    InputMethodManager imm;

    FavoriteViewAdapter mFavoriteAdapter;

    SignUpActivity.SignUpCheckListener mCheckListener = null;

    public void SetCheckListener(SignUpActivity.SignUpCheckListener listener)
    {
        mCheckListener = listener;
        mCheckListener.UpdateSignCheck(0);
    }

    public SignUpFragment() {
    }

    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        mFragmentMgr = ((FragmentActivity) mContext).getSupportFragmentManager();
        v_FragmentView = inflater.inflate(R.layout.fragment_sign_up, container, false);

        imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);

        isProfileUpload = false;
        mIsCheckNickName = false;

        iv_SignUp_Profile = v_FragmentView.findViewById(R.id.iv_SignUp_Profile);
        iv_SignUp_Gender = v_FragmentView.findViewById(R.id.iv_SignUp_Gender);
        tv_SignUp_NickName_Check = v_FragmentView.findViewById(R.id.tv_SignUp_NickName_Check);
        et_SignUp_NickName = v_FragmentView.findViewById(R.id.et_SignUp_NickName);
        et_SignUp_PassWord = v_FragmentView.findViewById(R.id.et_SignUp_PassWord);
        tv_SignUp_NickName_Check_Result = v_FragmentView.findViewById(R.id.tv_SignUp_NickName_Check_Result);
        tv_SignUp_Gender = v_FragmentView.findViewById(R.id.tv_SignUp_Gender);
        tv_SignUp_Age = v_FragmentView.findViewById(R.id.tv_SignUp_Age);
        rv_SignUp_Favorite = v_FragmentView.findViewById(R.id.rv_SignUp_Favorite);

        iv_SignUp_Profile.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                CommonFunc.getInstance().GetPermissionForGalleryCamera(mContext, SignUpFragment.this, CommonData.GET_PHOTO_FROM_CROP);
            }
        });

        if (TKManager.getInstance().MyData.GetUserAge() == 0) {
            Glide.with(mContext).load(R.drawable.ic_man_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_SignUp_Gender);
            tv_SignUp_Gender.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_MAN));
            TKManager.getInstance().MyData.SetUserGender(0);
        } else {
            Glide.with(mContext).load(R.drawable.ic_woman_simple)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_SignUp_Gender);
            tv_SignUp_Gender.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_WOMAN));
            TKManager.getInstance().MyData.SetUserGender(1);
        }

        tv_SignUp_Age.setText("입력해주세요");

        CommonFunc.getInstance().setEditTextMaxSize(et_SignUp_NickName, CommonData.NickNameMaxSize);

        tv_SignUp_NickName_Check_Result.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.NICKNAME_CHECK_NO));
        tv_SignUp_NickName_Check_Result.setTextColor(ContextCompat.getColor(mContext, R.color.red));

        et_SignUp_NickName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mIsCheckNickName = false;
                tv_SignUp_NickName_Check_Result.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.NICKNAME_CHECK_NO));
                tv_SignUp_NickName_Check_Result.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                UpdateSignCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        et_SignUp_PassWord.setOnTouchListener(new OnSingleTouchListener() {
            @Override
            public void onSingleTouch(View view) {
                // 입력을 시도 했을경우 중복체크 안함으로 수정
                UpdateSignCheck();
                if(mIsCheckNickName == false)
                {
                    DialogFunc.MsgPopupListener listener = new DialogFunc.MsgPopupListener()
                    {
                        @Override
                        public void Listener()
                        {
                            imm.hideSoftInputFromWindow(et_SignUp_PassWord.getWindowToken(), 0);
                            et_SignUp_NickName.post(new Runnable() {
                                @Override
                                public void run() {
                                    et_SignUp_NickName.setFocusableInTouchMode(true);
                                    et_SignUp_NickName.requestFocus();
                                    imm.showSoftInput(et_SignUp_NickName,0);
                                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                                }
                            });
                        }
                    };

                    DialogFunc.getInstance().ShowMsgPopup(mContext, listener, null, CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_CHECK_ASK), "확인", null);

                }
            }
        });

        et_SignUp_PassWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                UpdateSignCheck();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        et_SignUp_NickName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        // 검색 동작
                        imm.hideSoftInputFromWindow(et_SignUp_NickName.getWindowToken(), 0);
                        NickNameCheck();
                        break;
                    default:
                        // 기본 엔터키 동작
                        imm.hideSoftInputFromWindow(et_SignUp_NickName.getWindowToken(), 0);
                        NickNameCheck();
                        return false;
                }
                return true;
            }
        });

        et_SignUp_PassWord.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_DONE:
                        // 검색 동작
                        imm.hideSoftInputFromWindow(et_SignUp_PassWord.getWindowToken(), 0);
                        if(et_SignUp_PassWord.getText().toString().length() < CommonData.PassWordMinSize)
                        {
                            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.PASSWORD_LEAK));
                        }
                        else if(et_SignUp_PassWord.getText().toString().length() > CommonData.PassWordMaxSize)
                        {
                            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.PASSWORD_LEAK));
                        }
                        break;
                    default:
                        // 기본 엔터키 동작
                        imm.hideSoftInputFromWindow(et_SignUp_PassWord.getWindowToken(), 0);
                        if(et_SignUp_PassWord.getText().toString().length() < CommonData.PassWordMinSize)
                        {
                            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.PASSWORD_LEAK));
                        }
                        else if(et_SignUp_PassWord.getText().toString().length() > CommonData.PassWordMaxSize)
                        {
                            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.PASSWORD_LEAK));
                        }
                        return false;
                }
                return true;
            }
        });


        tv_SignUp_NickName_Check.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                imm.hideSoftInputFromWindow(et_SignUp_NickName.getWindowToken(), 0);
                NickNameCheck();
            }
        });

        initFavoriteList();

        return v_FragmentView;
    }

    public void NickNameCheck()
    {
        final String strNickName = et_SignUp_NickName.getText().toString();

        if(CommonFunc.getInstance().CheckStringNull(strNickName))
        {
            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_EMPTY));
        }
        else if(strNickName.length() < CommonData.NickNameMinSize)
        {
            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_LEAK));
        }
        else
        {
            DialogFunc.getInstance().ShowLoadingPage(mContext);
            FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                @Override
                public void CompleteListener() {

                }

                @Override
                public void CompleteListener_Yes() {
                    DialogFunc.getInstance().DismissLoadingPage();
                    DialogFunc.getInstance().ShowToast(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_CHECK_SUCCESS), true);
                    mIsCheckNickName = true;
                    tv_SignUp_NickName_Check_Result.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.NICKNAME_CHECK_YES));
                    tv_SignUp_NickName_Check_Result.setTextColor(ContextCompat.getColor(mContext, R.color.blue));

                    TKManager.getInstance().MyData.SetUserNickName(strNickName);
                    UpdateSignCheck();
                }

                @Override
                public void CompleteListener_No() {
                    DialogFunc.getInstance().DismissLoadingPage();
                    DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_CHECK_FAIL));
                    mIsCheckNickName = false;
                    tv_SignUp_NickName_Check_Result.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.NICKNAME_CHECK_NO));
                    tv_SignUp_NickName_Check_Result.setTextColor(ContextCompat.getColor(mContext, R.color.red));
                    UpdateSignCheck();
                }
            };

            FirebaseManager.getInstance().CheckNickName(strNickName, listener);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        // ((MainActivity)context).setOnKeyBackPressedListener((MainActivity.onKeyBackPressedListener) this);
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void initFavoriteList()
    {
        mFavoriteAdapter = new FavoriteViewAdapter(mContext);
        RefreshFavoriteViewListSlot();
        mFavoriteAdapter.setHasStableIds(true);

        rv_SignUp_Favorite.setAdapter(mFavoriteAdapter);
        ChipsLayoutManager chipsLayoutManager = ChipsLayoutManager.newBuilder(mContext)
                .setChildGravity(Gravity.CENTER)
                .setMaxViewsInRow(3)
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
        rv_SignUp_Favorite.setLayoutManager(chipsLayoutManager);
        rv_SignUp_Favorite.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_SignUp_Favorite, new OnRecyclerItemClickListener() {
            @Override
            public void onSingleClick(View view, int position) {

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        Intent intent = new Intent(mContext, FavoriteSelectActivity.class);
                        intent.putExtra("Type",0);
                        startActivityForResult(intent, GET_FROM_FAVORITE_SELECT);
                        UpdateSignCheck();
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

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
    }

    private void RefreshFavoriteViewListSlot()
    {
        ArrayList<String> list = new ArrayList<>();

        Map<String, String> tempMapFavorite = TKManager.getInstance().MyData.GetUserFavoriteList();
        Set EntrySet = tempMapFavorite.entrySet();
        Iterator iterator = EntrySet.iterator();

        while(iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            String key = (String)entry.getKey();
            String value = (String)entry.getValue();
            list.add(value);
        }

        mFavoriteAdapter.addSlot(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_FAVORITE_PLUS));
        mFavoriteAdapter.setItemCount(list.size());
        mFavoriteAdapter.setItemData(list);
    }

    public boolean isSignUp()
    {
        imm.hideSoftInputFromWindow(et_SignUp_NickName.getWindowToken(), 0);
        if(mIsCheckNickName == false)
        {
            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_CHECK_ASK));
        }
        else if(CommonFunc.getInstance().CheckStringNull(et_SignUp_NickName.getText().toString()))
        {
            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.NICKNAME_EMPTY));
        }
        else if(CommonFunc.getInstance().CheckStringNull(et_SignUp_PassWord.getText().toString()))
        {
            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.PASSWORD_EMPTY));
        }
        else if(et_SignUp_PassWord.getText().toString().length() < CommonData.PassWordMinSize)
        {
            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.PASSWORD_LEAK));
        }
        else if(et_SignUp_PassWord.getText().toString().length() > CommonData.PassWordMaxSize)
        {
            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.PASSWORD_LEAK));
        }
        else if(isProfileUpload == false)
        {
            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.PROFILE_EMPTY));
        }
        else if(TKManager.getInstance().MyData.GetUserFavoriteListCount() < CommonData.FavoriteSelectMinCount)
        {
            DialogFunc.getInstance().ShowMsgPopup(mContext, CommonFunc.getInstance().getStr(getResources(), R.string.FAVORITE_SELECT_LACK));
        }
        else
            return true;

        return false;
    }

    public String getPassword()
    {
        return et_SignUp_PassWord.getText().toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                isProfileUpload = true;
                DialogFunc.getInstance().ShowLoadingPage(mContext);
                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        DialogFunc.getInstance().DismissLoadingPage();

                        UpdateSignCheck();
                    }

                    @Override
                    public void CompleteListener_Yes() {
                    }

                    @Override
                    public void CompleteListener_No() {
                    }
                };
                Uri resultUri = result.getUri();
                CommonFunc.getInstance().SetCropImage(mContext, resultUri, 0, iv_SignUp_Profile, listener);
            }
        }
        else if(requestCode == GET_FROM_FAVORITE_SELECT)
        {
            RefreshFavoriteViewListSlot();
            mFavoriteAdapter.notifyDataSetChanged();

            UpdateSignCheck();
        }
    }

    private void UpdateSignCheck()
    {
        if(mCheckListener == null)
            return;



        int returnCount = 0;
        Log.d("signup", "count = " + returnCount);
        if(isProfileUpload)
            returnCount++;

        Log.d("signup", "isProfileUpload count = " + returnCount);

        if(TKManager.getInstance().MyData.GetUserFavoriteListCount() >= CommonData.FavoriteSelectMinCount)
            returnCount++;

        Log.d("signup", "GetUserFavoriteListCount count = " + returnCount);

        if(mIsCheckNickName)
            returnCount++;

        Log.d("signup", "mIsCheckNickName count = " + returnCount);

        if(CommonFunc.getInstance().CheckStringNull(et_SignUp_PassWord.getText().toString()) == false &&
                et_SignUp_PassWord.getText().toString().length() >= CommonData.PassWordMinSize &&
                et_SignUp_PassWord.getText().toString().length() <= CommonData.PassWordMaxSize)
            returnCount++;

        Log.d("signup", "et_SignUp_PassWord count = " + returnCount);

        mCheckListener.UpdateSignCheck(returnCount);
    }
}
