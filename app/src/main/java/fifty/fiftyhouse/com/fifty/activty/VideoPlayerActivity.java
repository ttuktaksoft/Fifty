package fifty.fiftyhouse.com.fifty.activty;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.huxq17.download.DownloadConfig;
import com.huxq17.download.DownloadDetailsInfo;
import com.huxq17.download.Pump;
import com.huxq17.download.message.DownloadListener;

import java.util.List;

import fifty.fiftyhouse.com.fifty.R;
import fifty.fiftyhouse.com.fifty.util.OnSingleClickListener;

public class VideoPlayerActivity extends AppCompatActivity {

    View ui_VideoPlayer_TopBar;
    TextView tv_TopBar_Title;
    ImageView iv_TopBar_Back, iv_VideoPlayer_Down;

    PlayerView ex_VideoPlayer;

    Context mContext;
    String mUrl;

    private SimpleExoPlayer player;

    private Boolean playWhenReady = true;
    private int currentWindow = 0;
    private Long playbackPosition = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        mContext = getApplicationContext();

        Intent intent = getIntent(); //getIntent()로 받을준비
        mUrl = intent.getStringExtra("url");

        ui_VideoPlayer_TopBar = findViewById(R.id.ui_VideoPlayer_TopBar);
        tv_TopBar_Title = ui_VideoPlayer_TopBar.findViewById(R.id.tv_TopBar_Title);
        iv_TopBar_Back = ui_VideoPlayer_TopBar.findViewById(R.id.iv_TopBar_Back);

        ex_VideoPlayer = findViewById(R.id.ex_VideoPlayer);
        iv_VideoPlayer_Down = findViewById(R.id.iv_VideoPlayer_Down);

        tv_TopBar_Title.setText("");

        iv_TopBar_Back.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                finish();
            }
        });

        iv_VideoPlayer_Down.setOnClickListener(new OnSingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                player.stop(true);

                String sample = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

                DownloadConfig.newBuilder(getApplicationContext())
                        .build();



                Pump.newRequest(sample)
                        .listener(new DownloadListener(sample) {

                            @Override
                            public void onProgress(int progress) {
                                //progressDialog.setProgress(progress);
                                //tv_TopBar_Title.setText("" + progress);
                            }

                            @Override
                            public void onSuccess() {
/*                                progressDialog.dismiss();
                                String apkPath = getDownloadInfo().getFilePath();
                                APK.with(MainActivity.this)
                                        .from(apkPath)
//                                        .forceInstall();
                                        .install();
                                Toast.makeText(MainActivity.this, "Download Finished", Toast.LENGTH_SHORT).show();*/
                            }

                            @Override
                            public void onFailed() {
 /*                               progressDialog.dismiss();
                                Toast.makeText(MainActivity.this, "Download failed", Toast.LENGTH_SHORT).show();*/
                            }
                        })
                        //Optionally,Set whether to repeatedly download the downloaded file,default false.
                        .forceReDownload(true)
                        //Optionally,Set how many threads are used when downloading,default 3.
                        .threadNum(3)
                        .setRetry(3, 200)
                        .tag("video")
                        .submit();

                DownloadListener downloadObserver = new DownloadListener() {
                    @Override
                    public void onProgress(int progress) {

                        List<DownloadDetailsInfo> list = Pump.getDownloadListByTag("video");

                      //  tv_TopBar_Title.setText(list.get(0).getContentLength() + "/" + list.get(0).getCompletedSize());

                    }

                    @Override
                    public void onFailed() {
                        super.onFailed();

                    }
                };

                downloadObserver.enable();



                //finish();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void initializePlayer() {
        if (player == null) {

            /*DefaultRenderersFactory renderersFactory = new DefaultRenderersFactory(this.getApplicationContext());
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            DefaultLoadControl loadControl = new DefaultLoadControl();

            player = ExoPlayerFactory.newSimpleInstance(
                    this.getApplicationContext(),
                    renderersFactory,
                    trackSelector,
                    loadControl);*/

            player = ExoPlayerFactory.newSimpleInstance(this.getApplicationContext());

            //플레이어 연결
            ex_VideoPlayer.setPlayer(player);

            //컨트롤러 없애기
            //exoPlayerView.setUseController(false);

            //사이즈 조절
            //exoPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_ZOOM); // or RESIZE_MODE_FILL

            //음량조절
            //player.setVolume(0);

            //프레임 포지션 설정
            //player.seekTo(currentWindow, playbackPosition);

        }

        String sample = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";

        MediaSource mediaSource = buildMediaSource(Uri.parse(sample));

        //prepare
        player.prepare(mediaSource, true, false);

        //start,stop
        player.setPlayWhenReady(playWhenReady);
    }

    private MediaSource buildMediaSource(Uri uri) {

        String userAgent = Util.getUserAgent(this, "blackJin");

        if (uri.getLastPathSegment().contains("mp3") || uri.getLastPathSegment().contains("mp4")) {

            return new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(userAgent))
                    .createMediaSource(uri);

        }else {

            return new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(this, userAgent))
                    .createMediaSource(uri);
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();

            ex_VideoPlayer.setPlayer(null);
            player.release();
            player = null;

        }
    }
}
