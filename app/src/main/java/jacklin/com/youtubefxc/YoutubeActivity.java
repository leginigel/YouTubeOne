package jacklin.com.youtubefxc;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.ImageView;

import jacklin.com.youtubefxc.ui.search.SearchFragment;
import jacklin.com.youtubefxc.ui.youtube.YoutubeFragment;
import jacklin.com.youtubefxc.ui.youtube.YoutubeRowFragment;

public class YoutubeActivity extends FragmentActivity {

    private ImageView searchIcon, homeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);

        SearchFragment searchFragment = SearchFragment.newInstance();
        YoutubeFragment youtubeFragment = YoutubeFragment.newInstance();
//        YoutubeRowFragment youtubeRowFragment = YoutubeRowFragment.newInstance();
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_home, youtubeFragment)
                    .commitNow();
        }

        searchIcon = findViewById(R.id.search_btn);
        homeIcon = findViewById(R.id.home_btn);
        homeIcon.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                Log.d("Home", "Icon on Focus");
                getSupportFragmentManager().beginTransaction().show(youtubeFragment).commitNow();
            }
//            else
//                getSupportFragmentManager().beginTransaction().hide(youtubeRowFragment).commitNow();
        });

        searchIcon.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                Log.d("Search", "Icon on Focus");
                getSupportFragmentManager().beginTransaction().show(searchFragment).commitNow();
            }
//            else
//                getSupportFragmentManager().beginTransaction().hide(searchFragment).commitNow();
        });
    }
}
