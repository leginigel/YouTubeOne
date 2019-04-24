package jacklin.com.youtubefxc;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import jacklin.com.youtubefxc.ui.search.SearchFragment;
import jacklin.com.youtubefxc.ui.youtube.YoutubeFragment;

public class YoutubeActivity extends FragmentActivity {

    private ImageView searchIcon, homeIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);

        SearchFragment searchFragment = SearchFragment.newInstance();
        YoutubeFragment youtubeFragment = YoutubeFragment.newInstance();
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, youtubeFragment)
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
//                getSupportFragmentManager().beginTransaction().hide(youtubeFragment).commitNow();
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
