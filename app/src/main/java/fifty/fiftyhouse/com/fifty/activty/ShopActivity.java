package fifty.fiftyhouse.com.fifty.activty;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.DialogFunc;
import fifty.fiftyhouse.com.fifty.Manager.TKManager;
import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class ShopActivity extends AppCompatActivity {

    View ui_Shop_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back;

    TextView tv_Shop_Info_Buy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        ui_Shop_TopBar = findViewById(R.id.ui_Shop_TopBar);
        tv_TopBar_Title = ui_Shop_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_Shop_TopBar.findViewById(R.id.iv_TopBar_Back);
        tv_Shop_Info_Buy = findViewById(R.id.tv_Shop_Info_Buy);

        tv_TopBar_Title.setText(CommonFunc.getInstance().getStr(getApplicationContext().getResources(), R.string.MSG_SHOP));
        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                finish();
            }
        });

        tv_Shop_Info_Buy.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                TKManager.getInstance().MyData.SetUserVip(!TKManager.getInstance().MyData.GetUserVip());
                DialogFunc.getInstance().ShowToast(getApplicationContext(), "변경 되었습니다.", true);
            }
        });
    }
}
