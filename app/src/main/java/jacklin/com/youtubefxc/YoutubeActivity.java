package jacklin.com.youtubefxc;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import jacklin.com.youtubefxc.ui.youtube.YoutubeFragment;

public class YoutubeActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, YoutubeFragment.newInstance())
                    .commitNow();
        }
    }
}
