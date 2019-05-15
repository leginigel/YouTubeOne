package jacklin.com.youtubefxc;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import jacklin.com.youtubefxc.api.YoutubeService;
import jacklin.com.youtubefxc.ui.search.SearchFragment;
import jacklin.com.youtubefxc.ui.youtube.YoutubeFragment;

public class YoutubeActivity extends FragmentActivity implements YouTubePlayer.OnInitializedListener{

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private static String TAG = YoutubeActivity.class.getSimpleName();
    private YoutubeFragment youtubeFragment;
    private ImageView searchIcon, homeIcon, subIcon, folderIcon, settingIcon;
    private View playerBox;
    private YouTubePlayer youTubePlayer;
    private PageCategory mPageCategory;

    public enum PageCategory {
        Search, Home
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);

        SearchFragment searchFragment = SearchFragment.newInstance();
        youtubeFragment = YoutubeFragment.newInstance();
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_home, youtubeFragment)
                    .commitNow();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_youtube_player, youTubePlayerFragment)
                    .commit();
        }

        youTubePlayerFragment.initialize(YoutubeService.key, this);
        playerBox = findViewById(R.id.fragment_youtube_player);
        playerBox.setVisibility(View.INVISIBLE);

        mPageCategory = PageCategory.Home;

        searchIcon = findViewById(R.id.search_btn);
        homeIcon = findViewById(R.id.home_btn);
        subIcon = findViewById(R.id.subscribe_btn);
        folderIcon = findViewById(R.id.folder_btn);
        settingIcon = findViewById(R.id.setting_btn);

        setIconFocusListener(searchIcon, PageCategory.Search);
        setIconFocusListener(homeIcon, PageCategory.Home);
        setIconFocusListener(subIcon, PageCategory.Search);
        setIconFocusListener(folderIcon, PageCategory.Search);
        setIconFocusListener(settingIcon, PageCategory.Search);

//        homeIcon.setOnFocusChangeListener((v, hasFocus) -> {
//            if(hasFocus) {
//                Log.d("Home", "Icon on Focus");
//                getSupportFragmentManager().beginTransaction().show(youtubeFragment).commitNow();
//            }
////            else
////                getSupportFragmentManager().beginTransaction().hide(youtubeRowFragment).commitNow();
//        });
//
//        searchIcon.setOnFocusChangeListener((v, hasFocus) -> {
//            if(hasFocus) {
//                Log.d("Search", "Icon on Focus");
//                getSupportFragmentManager().beginTransaction().show(searchFragment).commitNow();
//            }
////            else
////                getSupportFragmentManager().beginTransaction().hide(searchFragment).commitNow();
//        });
        checkYouTubeApi();
    }

    void setIconFocusListener(ImageView image, PageCategory category){
        image.setOnFocusChangeListener((v, hasFocus)->{
            Drawable drawble = image.getDrawable();
            if(hasFocus) {
                Log.d(TAG, "Icon on Focus");
                drawble.setColorFilter(getResources().getColor(R.color.left_nav), PorterDuff.Mode.MULTIPLY);
                image.setImageDrawable(drawble);
            }
            else{
                if(category == this.mPageCategory)
                    drawble.setColorFilter(getResources().getColor(R.color.button_selecting), PorterDuff.Mode.SRC_IN);
                else
                    drawble.setColorFilter(getResources().getColor(R.color.button), PorterDuff.Mode.SRC_IN);
                image.setImageDrawable(drawble);
            }
        });
    }

    public View getPlayerBox() {
        return playerBox;
    }

    public YouTubePlayer getYouTubePlayer() {
        return youTubePlayer;
    }

    public YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerSupportFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_youtube_player);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
//            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
//            youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
        this.youTubePlayer = youTubePlayer;
//            youTubePlayer.setPlaylistEventListener(playlistEventListener);
//            youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
//            youTubePlayer.setPlaybackEventListener(playbackEventListener);
        if (!wasRestored) {
            Log.d("CheckPoint", "CheckPoint !wasRestored");
//                youTubePlayer.cueVideo(video.getId());
//                youTubePlayer.play();
//            youTubePlayer.loadVideo(video.getId());
        }
        else{
            Log.d("CheckPoint", "CheckPoint Restored");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        Log.d("onInitializationFailure", "Failed to initialize.");
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = errorReason.toString();
            Log.d("onInitializationFailure", errorMessage);
        }

    }

    @Override
    public void onBackPressed() {
        if (playerBox.getVisibility() == View.VISIBLE){
            playerBox.setVisibility(View.INVISIBLE);
        }
        else
        super.onBackPressed();
    }

    private void checkYouTubeApi() {
        YouTubeInitializationResult errorReason =
                YouTubeApiServiceUtil.isYouTubeApiServiceAvailable(this);
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        } else {
            String errorMessage = errorReason.toString();
            Log.d("checkYouTubeApi", errorMessage);
        }
    }
}
