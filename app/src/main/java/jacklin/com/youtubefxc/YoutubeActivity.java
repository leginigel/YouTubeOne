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
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import jacklin.com.youtubefxc.ui.search.SearchFragment;
import jacklin.com.youtubefxc.ui.youtube.YoutubeFragment;

public class YoutubeActivity extends FragmentActivity {
    private static String TAG = YoutubeActivity.class.getSimpleName();
    private ImageView searchIcon, homeIcon, subIcon, folderIcon, settingIcon;
    View playerBox;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);

        SearchFragment searchFragment = SearchFragment.newInstance();
        YoutubeFragment youtubeFragment = YoutubeFragment.newInstance();
        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_home, youtubeFragment)
                    .commitNow();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_youtube_player, youTubePlayerFragment)
                    .commit();
        }

        playerBox = findViewById(R.id.fragment_youtube_player);
        playerBox.setVisibility(View.INVISIBLE);

        searchIcon = findViewById(R.id.search_btn);
        homeIcon = findViewById(R.id.home_btn);
        subIcon = findViewById(R.id.subscribe_btn);
        folderIcon = findViewById(R.id.folder_btn);
        settingIcon = findViewById(R.id.setting_btn);

        setIconFocusListener(searchIcon);
        setIconFocusListener(homeIcon);
        setIconFocusListener(subIcon);
        setIconFocusListener(folderIcon);
        setIconFocusListener(settingIcon);

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

    void setIconFocusListener(ImageView image){
        image.setOnFocusChangeListener((v, hasFocus)->{
            Drawable drawble = image.getDrawable();
            if(hasFocus) {
                Log.d(TAG, "Icon on Focus");
                drawble.setColorFilter(getResources().getColor(R.color.left_nav), PorterDuff.Mode.MULTIPLY);
                image.setImageDrawable(drawble);
            }
            else{
                drawble.setColorFilter(getResources().getColor(R.color.button), PorterDuff.Mode.SRC_IN);
                image.setImageDrawable(drawble);
            }
        });
    }

    public View getPlayerBox() {
        return playerBox;
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
            errorReason.getErrorDialog(this, 1).show();
        } else {
            String errorMessage = errorReason.toString();
            Log.d("checkYouTubeApi", errorMessage);
        }
    }
}
