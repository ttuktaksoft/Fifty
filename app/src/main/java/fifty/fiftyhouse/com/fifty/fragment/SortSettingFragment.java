package fifty.fiftyhouse.com.fifty.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.edmodo.rangebar.RangeBar;

import fifty.fiftyhouse.com.fifty.CommonData;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class SortSettingFragment extends Fragment {

    Context mContext;
    View v_FragmentView = null;

    TextView tv_SortSetting_Distance_Count, tv_SortSetting_Age_Count, tv_SortSetting_Gender_All, tv_SortSetting_Gender_Woman, tv_SortSetting_Gender_Man,
            tv_SortSetting_Time_1, tv_SortSetting_Time_2,tv_SortSetting_Time_3;
    SeekBar sb_SortSetting_Distance;
    RangeBar sb_SortSetting_Age;

    public CommonData.SORT_SETTING_GENDER mGender = CommonData.SORT_SETTING_GENDER.ALL;
    public CommonData.SORT_SETTING_ONLINE mOnLineTime = CommonData.SORT_SETTING_ONLINE.ONLINE;

    public SortSettingFragment() {
    }

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
        v_FragmentView = inflater.inflate(R.layout.fragment_sort_setting, container, false);

        tv_SortSetting_Distance_Count = v_FragmentView.findViewById(R.id.tv_SortSetting_Distance_Count);
        tv_SortSetting_Age_Count = v_FragmentView.findViewById(R.id.tv_SortSetting_Age_Count);
        tv_SortSetting_Gender_All = v_FragmentView.findViewById(R.id.tv_SortSetting_Gender_All);
        tv_SortSetting_Gender_Woman = v_FragmentView.findViewById(R.id.tv_SortSetting_Gender_Woman);
        tv_SortSetting_Gender_Man = v_FragmentView.findViewById(R.id.tv_SortSetting_Gender_Man);
        tv_SortSetting_Time_1 = v_FragmentView.findViewById(R.id.tv_SortSetting_Time_1);
        tv_SortSetting_Time_2 = v_FragmentView.findViewById(R.id.tv_SortSetting_Time_2);
        tv_SortSetting_Time_3 = v_FragmentView.findViewById(R.id.tv_SortSetting_Time_3);
        sb_SortSetting_Distance = v_FragmentView.findViewById(R.id.sb_SortSetting_Distance);
        sb_SortSetting_Age = v_FragmentView.findViewById(R.id.sb_SortSetting_Age);

        sb_SortSetting_Distance.setMax(100);
        sb_SortSetting_Distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                if(progress == 100)
                    tv_SortSetting_Distance_Count.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_SORT_TYPE_DISTANCE_ALL));
                else
                    tv_SortSetting_Distance_Count.setText("0~"+progress+"km");
            }
        });

        sb_SortSetting_Age.setTickCount(50);
        sb_SortSetting_Age.setTickHeight(0);
        sb_SortSetting_Age.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onIndexChangeListener(RangeBar rangeBar, int leftThumbIndex, int rightThumbIndex) {

                if(leftThumbIndex == 0 && rightThumbIndex == 49)
                    tv_SortSetting_Age_Count.setText(CommonFunc.getInstance().getStr(mContext.getResources(), R.string.MSG_SORT_TYPE_AGE_ALL));
                else
                    tv_SortSetting_Age_Count.setText((leftThumbIndex + 50) + "~" + (rightThumbIndex + 50));
            }
        });


        tv_SortSetting_Gender_All.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                mGender = CommonData.SORT_SETTING_GENDER.ALL;
                RefreshGender();
            }
        });

        tv_SortSetting_Gender_Woman.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                mGender = CommonData.SORT_SETTING_GENDER.WOMAN;
                RefreshGender();
            }
        });

        tv_SortSetting_Gender_Man.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                mGender = CommonData.SORT_SETTING_GENDER.MAN;
                RefreshGender();
            }
        });

        tv_SortSetting_Time_1.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                mOnLineTime = CommonData.SORT_SETTING_ONLINE.ONLINE;
                RefreshOnlineTime();
            }
        });

        tv_SortSetting_Time_2.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                mOnLineTime = CommonData.SORT_SETTING_ONLINE.SHORT;
                RefreshOnlineTime();
            }
        });

        tv_SortSetting_Time_3.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                mOnLineTime = CommonData.SORT_SETTING_ONLINE.LONG;
                RefreshOnlineTime();
            }
        });

        RefreshGender();
        RefreshOnlineTime();

        return v_FragmentView;
    }

    public void RefreshGender()
    {
        int selectBGColor = ContextCompat.getColor(mContext, R.color.button_enable);
        int selectSrtColor = ContextCompat.getColor(mContext, R.color.button_enable_str);
        int disableBGColor = ContextCompat.getColor(mContext, R.color.white);
        int disableSrtColor = ContextCompat.getColor(mContext, R.color.str_color_1);

        tv_SortSetting_Gender_All.setBackgroundColor(mGender == CommonData.SORT_SETTING_GENDER.ALL ? selectBGColor : disableBGColor);
        tv_SortSetting_Gender_All.setTextColor(mGender == CommonData.SORT_SETTING_GENDER.ALL ? selectSrtColor : disableSrtColor);
        tv_SortSetting_Gender_Woman.setBackgroundColor(mGender == CommonData.SORT_SETTING_GENDER.WOMAN ? selectBGColor : disableBGColor);
        tv_SortSetting_Gender_Woman.setTextColor(mGender == CommonData.SORT_SETTING_GENDER.WOMAN ? selectSrtColor : disableSrtColor);
        tv_SortSetting_Gender_Man.setBackgroundColor(mGender == CommonData.SORT_SETTING_GENDER.MAN ? selectBGColor : disableBGColor);
        tv_SortSetting_Gender_Man.setTextColor(mGender == CommonData.SORT_SETTING_GENDER.MAN ? selectSrtColor : disableSrtColor);
    }

    public void RefreshOnlineTime()
    {
        int selectBGColor = ContextCompat.getColor(mContext, R.color.button_enable);
        int selectSrtColor = ContextCompat.getColor(mContext, R.color.button_enable_str);
        int disableBGColor = ContextCompat.getColor(mContext, R.color.white);
        int disableSrtColor = ContextCompat.getColor(mContext, R.color.str_color_1);

        tv_SortSetting_Time_1.setBackgroundColor(mOnLineTime == CommonData.SORT_SETTING_ONLINE.ONLINE ? selectBGColor : disableBGColor);
        tv_SortSetting_Time_1.setTextColor(mOnLineTime == CommonData.SORT_SETTING_ONLINE.ONLINE ? selectSrtColor : disableSrtColor);
        tv_SortSetting_Time_2.setBackgroundColor(mOnLineTime == CommonData.SORT_SETTING_ONLINE.SHORT ? selectBGColor : disableBGColor);
        tv_SortSetting_Time_2.setTextColor(mOnLineTime == CommonData.SORT_SETTING_ONLINE.SHORT ? selectSrtColor : disableSrtColor);
        tv_SortSetting_Time_3.setBackgroundColor(mOnLineTime == CommonData.SORT_SETTING_ONLINE.LONG ? selectBGColor : disableBGColor);
        tv_SortSetting_Time_3.setTextColor(mOnLineTime == CommonData.SORT_SETTING_ONLINE.LONG ? selectSrtColor : disableSrtColor);
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        // ((MainActivity)context).setOnKeyBackPressedListener((MainActivity.onKeyBackPressedListener) this);
    }

/*    @Override
    public void onBackKey() {
        MainActivity activity = (MainActivity)getActivity();
    //    activity.setOnKeyBackPressedListener(null);
        activity.onBackPressed();
    }*/


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
}