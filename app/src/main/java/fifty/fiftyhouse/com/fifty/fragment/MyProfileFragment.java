package fifty.fiftyhouse.com.fifty.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import fifty.fiftyhouse.com.fifty.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyProfileFragment extends Fragment {

    private Context mContext;
    private View MyProfileFragView;
    private ImageView iv_ThumbNail;

    private ImageView[] iv_ProfileImage = new ImageView[8];

    private ImageView[] iv_ClubImage = new ImageView[3];

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
        MyProfileFragView.setTag("MyProfileFragment");
        iv_ThumbNail = MyProfileFragView.findViewById(R.id.iv_MyProfile_ThumbNail);

        iv_ProfileImage[0] = MyProfileFragView.findViewById(R.id.bt_MyProfile_Img_0);
        iv_ProfileImage[1] = MyProfileFragView.findViewById(R.id.bt_MyProfile_Img_1);
        iv_ProfileImage[2] = MyProfileFragView.findViewById(R.id.bt_MyProfile_Img_2);
        iv_ProfileImage[3] = MyProfileFragView.findViewById(R.id.bt_MyProfile_Img_3);
        iv_ProfileImage[4] = MyProfileFragView.findViewById(R.id.bt_MyProfile_Img_4);
        iv_ProfileImage[5] = MyProfileFragView.findViewById(R.id.bt_MyProfile_Img_5);
        iv_ProfileImage[6] = MyProfileFragView.findViewById(R.id.bt_MyProfile_Img_6);
        iv_ProfileImage[7] = MyProfileFragView.findViewById(R.id.bt_MyProfile_Img_7);

        iv_ClubImage[0] = MyProfileFragView.findViewById(R.id.bt_MyProfile_Club_0_Img);
        iv_ClubImage[1] = MyProfileFragView.findViewById(R.id.bt_MyProfile_Club_1_Img);
        iv_ClubImage[2] = MyProfileFragView.findViewById(R.id.bt_MyProfile_Club_2_Img);

        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.dummy_0)
                .circleCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_ThumbNail);


        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.dummy_0)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_ProfileImage[0]);

        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.dummy_1)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_ProfileImage[1]);

        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.dummy_4)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_ProfileImage[2]);

        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.dummy_10)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_ProfileImage[3]);

        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.dummy_14)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_ProfileImage[4]);
        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.dummy_11)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_ProfileImage[5]);

        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.dummy_6)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_ProfileImage[6]);

        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.dummy_13)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_ProfileImage[7]);


        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.dummy_12)
                .circleCrop()
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_ClubImage[0]);


        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.dummy_16)
                .circleCrop()
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_ClubImage[1]);


        Glide.with(mContext)
                //.load(mMyData.arrSendDataList.get(position).strTargetImg)
                .load(R.drawable.dummy_13)
                .circleCrop()
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(iv_ClubImage[2]);


        return MyProfileFragView;
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
}
