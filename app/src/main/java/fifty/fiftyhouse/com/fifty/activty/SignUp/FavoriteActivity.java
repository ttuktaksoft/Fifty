package fifty.fiftyhouse.com.fifty.activty.SignUp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.beloo.widget.chipslayoutmanager.layouter.breaker.IRowBreaker;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.Manager.FirebaseManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.activty.Profile.UserProfileActivity;
import fifty.fiftyhouse.com.fifty.adapter.MainAdapter;
import fifty.fiftyhouse.com.fifty.adapter.SignUpFavoriteFixAdapter;
import fifty.fiftyhouse.com.fifty.util.RecyclerItemClickListener;

public class FavoriteActivity extends AppCompatActivity {

    RecyclerView rv_SignUp_Favorite_Fix;
    ImageView iv_SignUp_Favorite_Next;

    SignUpFavoriteFixAdapter mAdapter = null;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        mContext = getApplicationContext();
        CommonFunc.getInstance().mCurActivity = this;

        rv_SignUp_Favorite_Fix = findViewById(R.id.rv_SignUp_Favorite_Fix);
        iv_SignUp_Favorite_Next = findViewById(R.id.iv_SignUp_Favorite_Next);

        iv_SignUp_Favorite_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseManager.CheckFirebaseComplete listener = new FirebaseManager.CheckFirebaseComplete() {
                    @Override
                    public void CompleteListener() {
                        Intent intent = new Intent(getApplicationContext(), FavoriteDetailActivity.class);
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

        RefreshFavoriteList();
    }

    public void RefreshFavoriteList()
    {
        mAdapter = new SignUpFavoriteFixAdapter(mContext);
        mAdapter.setHasStableIds(true);

        rv_SignUp_Favorite_Fix.setAdapter(mAdapter);
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
        rv_SignUp_Favorite_Fix.setLayoutManager(chipsLayoutManager);

        rv_SignUp_Favorite_Fix.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rv_SignUp_Favorite_Fix, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //startActivity(new Intent(getContext(), UserProfileActivity.class));
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
}
