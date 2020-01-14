package fifty.fiftyhouse.com.fifty.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;
import fifty.fiftyhouse.com.fifty.CommonFunc;
import fifty.fiftyhouse.com.fifty.MainActivity;
import fifty.fiftyhouse.com.fifty.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getData() == null)
            return;

        Resources res = getResources();

        String body= null;
        String title = null;

        String index= null;

        if (remoteMessage.getData().size() > 0) {
            body = remoteMessage.getData().get("body");
            title = remoteMessage.getData().get("title");
        }
        if (remoteMessage.getNotification() != null) {
            body = remoteMessage.getNotification().getBody();
            title = "피프티";

        }

        sendNotification(title, body);
    }

    private void sendNotification(String title, String content) {
        if(title == null)
            title = "FIFTY";

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // 오레오(8.0) 이상일 경우 채널을 반드시 생성해야 한다.
        final String CHANNEL_ID = "FIFTY";
        NotificationManager mManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            final String CHANNEL_NAME = "FIFTYHOUSE";
            final String CHANNEL_DESCRIPTION = "FIFTY";
            final int importance = NotificationManager.IMPORTANCE_HIGH;

            // add in API level 26
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            mChannel.setDescription(CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 100, 200});
            mChannel.setSound(defaultSoundUri, null);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setAutoCancel(true);
        builder.setDefaults(Notification.DEFAULT_ALL);
        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentText(content);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // 아래 설정은 오레오부터 deprecated 되면서 NotificationChannel에서 동일 기능을 하는 메소드를 사용.
            builder.setContentTitle(title);
            builder.setSound(defaultSoundUri);
            builder.setVibrate(new long[]{500, 500});
        }

        mManager.notify(0, builder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        /*
         * 기존의 FirebaseInstanceIdService에서 수행하던 토큰 생성, 갱신 등의 역할은 이제부터
         * FirebaseMessaging에 새롭게 추가된 위 메소드를 사용하면 된다.
         */
    }
}