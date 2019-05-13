package jacklin.com.youtubefxc.ui.youtube;


import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import jacklin.com.youtubefxc.R;
import jacklin.com.youtubefxc.viewmodel.YoutubeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * ViewModel {@link YoutubeViewModel}
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class YoutubeFragment extends Fragment {

    private final static String TAG = YoutubeFragment.class.getSimpleName();

    public static YoutubeFragment newInstance() {
        return new YoutubeFragment();
    }

    FragmentManager fm;

    RecommendedFragment recommendedFragment;
    MusicFragment musicFragment;
    GamingFragment gamingFragment;
    EntertainFragment entertainFragment;

    private TabCategory mTab = TabCategory.Recommended;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        recommendedFragment = RecommendedFragment.newInstance();
        musicFragment = MusicFragment.newInstance();
        gamingFragment = GamingFragment.newInstance();
        entertainFragment = EntertainFragment.newInstance();

        View view = inflater.inflate(R.layout.youtube_fragment, container, false);
        if(savedInstanceState == null) {
            fm = getFragmentManager();
            FragmentTransaction fmts = fm.beginTransaction();
            fmts.replace(R.id.container_row, recommendedFragment).commit();
        }
        Button recommend, latest, music, entertain, gaming;
        recommend = view.findViewById(R.id.recommend_btn);
        latest = view.findViewById(R.id.latest_btn);
        music = view.findViewById(R.id.music_btn);
        entertain = view.findViewById(R.id.entertainment_btn);
        gaming = view.findViewById(R.id.gaming_btn);
        setButtonFocusListener(recommend, recommendedFragment, TabCategory.Recommended);
        setButtonFocusListener(latest, recommendedFragment, TabCategory.Recommended);
        setButtonFocusListener(music, musicFragment, TabCategory.Music);
        setButtonFocusListener(entertain, entertainFragment, TabCategory.Entertainment);
        setButtonFocusListener(gaming, gamingFragment, TabCategory.Gaming);

        setButtonKeyListener(recommend, TabCategory.Recommended);
        setButtonKeyListener(latest, TabCategory.Recommended);
        setButtonKeyListener(music, TabCategory.Music);
        setButtonKeyListener(entertain, TabCategory.Entertainment);
        setButtonKeyListener(gaming, TabCategory.Gaming);
        return view;
    }

        @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
        YoutubeViewModel mViewModel = ViewModelProviders.of(getActivity()).get(YoutubeViewModel.class);
    }

    void setButtonKeyListener(Button button, TabCategory category){
        button.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN
                && keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                    if(getTabCategory() == category) {
                        button.setSelected(true);
                    }
                }
                return false;
            }
        });
    }

    void setButtonFocusListener(Button button, Fragment fragment, TabCategory category){
        button.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                button.setSelected(false);
                button.setTextColor(Color.BLACK);
                if(getTabCategory() != category)
                    setTabCategory(category);
                fm.beginTransaction()
                        .replace(R.id.container_row, fragment).commit();
            }
            else{
                button.setTextColor(getActivity().getResources().getColor(R.color.btn_text));
            }
        });
    }

    public enum TabCategory {
        Recommended, Latest, Music, Entertainment, Gaming
    }

    public void setTabCategory(TabCategory category){
        this.mTab = category;
        switch (category){
            case Recommended:
                Log.d(TAG, "Fragment setTabCategory Recommended");
//                setRows(mRecommendedChannel);
                break;
            case Latest:
//                setRows(mLatestChannel);
                break;
            case Music:
                Log.d(TAG, "Fragment setTabCategory Music");
//                setRows(mMusicChannel);
                break;
            case Entertainment:break;
            case Gaming:break;
        }
    }

    public TabCategory getTabCategory() {
        return mTab;
    }
}
