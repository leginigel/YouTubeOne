package jacklin.com.youtubefxc.ui.youtube;


import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import jacklin.com.youtubefxc.R;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class YoutubeFragment extends Fragment {


    public static YoutubeFragment newInstance() {
        return new YoutubeFragment();
    }

    YoutubeRowFragment youtubeRowFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        youtubeRowFragment = YoutubeRowFragment.newInstance();
        View view = inflater.inflate(R.layout.youtube_fragment, container, false);
        if(savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fmts = fm.beginTransaction();
            fmts.replace(R.id.container, youtubeRowFragment).commit();
        }
        Button recommend, latest, music, entertain, gaming;
        recommend = view.findViewById(R.id.recommend_btn);
        latest = view.findViewById(R.id.latest_btn);
        music = view.findViewById(R.id.music_btn);
        entertain = view.findViewById(R.id.entertainment_btn);
        gaming = view.findViewById(R.id.gaming_btn);
        setButtonFocusListener(recommend, YoutubeRowFragment.TabCategory.Recommended);
        setButtonFocusListener(latest, YoutubeRowFragment.TabCategory.Recommended);
        setButtonFocusListener(music, YoutubeRowFragment.TabCategory.Music);
        setButtonFocusListener(entertain, YoutubeRowFragment.TabCategory.Recommended);
        setButtonFocusListener(gaming, YoutubeRowFragment.TabCategory.Recommended);

        return view;
    }

    void setButtonFocusListener(Button button, YoutubeRowFragment.TabCategory category){
        button.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                button.setTextColor(Color.BLACK);
                if(youtubeRowFragment.getTabCategory() != category)
                    youtubeRowFragment.setTabCategory(category);
            }
            else{
                button.setTextColor(getActivity().getResources().getColor(R.color.btn_text));
            }
        });
    }
}
