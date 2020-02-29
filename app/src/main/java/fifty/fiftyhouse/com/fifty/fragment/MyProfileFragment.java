package fifty.fiftyhouse.com.fifty.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.Set;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.ShopActivity;
import fifty.fiftyhouse.com.fifty.activty.UserNoticeActivity;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment{

    ImageView iv_MyProfile_Alarm, iv_MyProfile_Shop;
    TextView tv_MyProfile_Name;
    Context mContext;
    FragmentManager mFragmentMgr;
    View v_FragmentView = null;

    UserProfileFragment mUserProfileFragment;

    public MyProfileFragment() {
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

        if(v_FragmentView != null)
        {
            mUserProfileFragment.RefreshUI();
            return v_FragmentView;
        }


        // Inflate the layout for this fragment
        mContext = getActivity();
        mFragmentMgr = ((FragmentActivity) mContext).getSupportFragmentManager();
        mUserProfileFragment = new UserProfileFragment();
        mUserProfileFragment.setMyProfileView(true);
        v_FragmentView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        mFragmentMgr.beginTransaction().replace(R.id.fl_MyProfile_FrameLayout, mUserProfileFragment, "MyProfileInfoFragment").commit();

        v_FragmentView.setTag("MyProfileFragment");

        iv_MyProfile_Alarm = v_FragmentView.findViewById(R.id.iv_MyProfile_Alarm);
        iv_MyProfile_Shop = v_FragmentView.findViewById(R.id.iv_MyProfile_Shop);
        tv_MyProfile_Name = v_FragmentView.findViewById(R.id.tv_MyProfile_Name);

        tv_MyProfile_Name.setText(TKManager.getInstance().MyData.GetUserNickName());

        iv_MyProfile_Alarm.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                DialogFunc.getInstance().ShowLoadingPage(getContext());
                Set KeySet = TKManager.getInstance().MyData.GetAlarmListKeySet();

                if(KeySet.size() > 0)
                {
                    Iterator iterator = KeySet.iterator();

                    FirebaseManager.getInstance().SetFireBaseLoadingCount("알람설정", TKManager.getInstance().MyData.GetAlarmListCount());

                    FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                        @Override
                        public void CompleteListener() {
                            DialogFunc.getInstance().DismissLoadingPage();

                            Intent intent = new Intent(getContext(), UserNoticeActivity.class);
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
                        if(TKManager.getInstance().UserData_Simple.get(key) != null)
                        {
                            FirebaseManager.getInstance().Complete("알람설정", listener);
                        }
                        else
                            FirebaseManager.getInstance().GetUserData_Simple(key, TKManager.getInstance().UserData_Simple, listener);
                    }
                }
                else
                {
                    DialogFunc.getInstance().DismissLoadingPage();

                    Intent intent = new Intent(getContext(), UserNoticeActivity.class);
                    startActivity(intent);
                }
            }
        });

        iv_MyProfile_Shop.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if(TKManager.getInstance().MyData.GetRemainVipDate() > 0)
                {
                    StringBuilder desc = new StringBuilder();
                    desc.append(CommonFunc.getInstance().getStr(getContext().getResources(), R.string.MSG_STR_VIP_REMAIN_1));
                    desc.append(" ");
                    desc.append(TKManager.getInstance().MyData.GetRemainVipDate());
                    desc.append(CommonFunc.getInstance().getStr(getContext().getResources(), R.string.MSG_STR_VIP_REMAIN_2));
                    DialogFunc.getInstance().ShowMsgPopup(getContext(), desc.toString());
                }
                else
                {
                    startActivity(new Intent(getContext(), ShopActivity.class));
                }
            }
        });

        return v_FragmentView;
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
