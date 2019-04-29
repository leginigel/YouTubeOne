package jacklin.com.youtubefxc.ui.youtube;


import android.graphics.Color;
import android.os.Bundle;
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
public class YoutubeFragment extends Fragment {


    public static YoutubeFragment newInstance() {
        return new YoutubeFragment();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        YoutubeRowFragment youtubeRowFragment = YoutubeRowFragment.newInstance();
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
        setButtonFocusListener(recommend);
        setButtonFocusListener(latest);
        setButtonFocusListener(music);
        setButtonFocusListener(entertain);
        setButtonFocusListener(gaming);

        return view;
    }

    void setButtonFocusListener(Button button){
        button.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                button.setTextColor(Color.BLACK);
            }
            else{
                button.setTextColor(getActivity().getResources().getColor(R.color.btn_text));
            }
        });
    }
}
