package jacklin.com.youtubefxc;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v17.leanback.app.RowsSupportFragment;
import android.support.v17.leanback.widget.ListRowView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.youtube.player.YouTubeApiServiceUtil;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import jacklin.com.youtubefxc.api.YoutubeService;
import jacklin.com.youtubefxc.data.YouTubeVideo;
import jacklin.com.youtubefxc.ui.BlankFragment;
import jacklin.com.youtubefxc.ui.player.PlayerControlsFragment;
import jacklin.com.youtubefxc.ui.search.SearchFragment;
import jacklin.com.youtubefxc.ui.search.SearchRowFragment;
import jacklin.com.youtubefxc.ui.search.SuggestListAdapter;
import jacklin.com.youtubefxc.ui.youtube.YoutubeFragment;
import jacklin.com.youtubefxc.ui.youtube.YoutubeRowFragment;
import jacklin.com.youtubefxc.viewmodel.YoutubeViewModel;

public class YoutubeActivity extends FragmentActivity implements YouTubePlayer.OnInitializedListener{

    private static final int RECOVERY_DIALOG_REQUEST = 1;
    private static String TAG = YoutubeActivity.class.getSimpleName();
    private PageCategory mPageCategory;
    private YoutubeViewModel viewModel;

    private SearchFragment searchFragment;
    private YoutubeFragment youtubeFragment;
    private YouTubePlayerSupportFragment youTubePlayerFragment;
    private PlayerControlsFragment playerControlsFragment;
    private PlayerControlsFragment.MyPlaybackEventListener playbackEventListener;

    private YouTubePlayer youTubePlayer;

    private ImageView searchIcon, homeIcon, subIcon, folderIcon, settingIcon;
    private View playerBox;

    public enum PageCategory {
        Search, Home, Subscription, Library, Account, Setting
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtube_activity);

        // Create fragment
        viewModel = ViewModelProviders.of(this).get(YoutubeViewModel.class);
        searchFragment = SearchFragment.newInstance();
        youtubeFragment = YoutubeFragment.newInstance();
        playerControlsFragment = PlayerControlsFragment.newInstance();
        youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
//                    .addToBackStack(tag)
                    .replace(R.id.container_home, youtubeFragment)
                    .commitNow();
            getSupportFragmentManager().beginTransaction()
//                    .addToBackStack(tag)
                    .replace(R.id.fragment_youtube_player, youTubePlayerFragment)
                    .commit();
        }

        // initial playback
        playbackEventListener = playerControlsFragment.getPlaybackEventListener();
        youTubePlayerFragment.initialize(YoutubeService.key, this);

        playerBox = findViewById(R.id.fragment_youtube_player);
        playerBox.setVisibility(View.INVISIBLE);
        playerBox.setOnKeyListener((v, keyCode, event) -> {
            if(!viewModel.getRelated()) {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN || keyCode == KeyEvent.KEYCODE_DPAD_UP
                            || keyCode == KeyEvent.KEYCODE_ENTER) {
//                    Log.d("onKey", "KEYCODE_DPAD_DOWN");
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
                        if(prev != null) {
                            ft.remove(prev);
                        }
//                getSupportFragmentManager().beginTransaction().addToBackStack(null);
                        playerControlsFragment.show(ft, "dialog");
                        return true;
                    }
                }
            }
            return false;
        });

        // setting Home Page View
        ViewGroup leftNav = findViewById(R.id.left_nav);
        leftNav.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        searchIcon = findViewById(R.id.search_btn);
        homeIcon = findViewById(R.id.home_btn);
        subIcon = findViewById(R.id.subscribe_btn);
        folderIcon = findViewById(R.id.folder_btn);
        settingIcon = findViewById(R.id.setting_btn);

        homeIcon.setSelected(true);
        homeIcon.getDrawable().setColorFilter(getResources().getColor(R.color.button_selecting), PorterDuff.Mode.SRC_IN);
        mPageCategory = PageCategory.Home;

        setIconFocusListener();
        setIconOnKeyListener();

        checkYouTubeApi();
    }

    private void setIconFocus(ImageView image, PageCategory category){
        image.setOnFocusChangeListener((v, hasFocus)->{
            Drawable drawable = ((ImageView) v).getDrawable();
            if(hasFocus) {
                Log.d(TAG, "Icon on Focus " + category);
                v.setSelected(false);
                this.mPageCategory = category;
                drawable.setColorFilter(getResources().getColor(R.color.left_nav), PorterDuff.Mode.MULTIPLY);
                ((ImageView) v).setImageDrawable(drawable);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_home, getPageFragment(category)).commitNow();
            }
            else{
                if(category == this.mPageCategory && v.isSelected())
                    drawable.setColorFilter(getResources().getColor(R.color.button_selecting), PorterDuff.Mode.SRC_IN);
                else
                    drawable.setColorFilter(getResources().getColor(R.color.button), PorterDuff.Mode.SRC_IN);
                ((ImageView) v).setImageDrawable(drawable);
            }
        });
    }

    private void setIconFocusListener(){
        setIconFocus(searchIcon, PageCategory.Search);
        setIconFocus(homeIcon, PageCategory.Home);
        setIconFocus(subIcon, PageCategory.Subscription);
        setIconFocus(folderIcon, PageCategory.Library);
        setIconFocus(settingIcon, PageCategory.Setting);
    }

    private void setIconOnKey(ImageView image){
        image.setOnKeyListener((view, keyCode, event)->{
            if(event.getAction() == KeyEvent.ACTION_DOWN) {
                if(KeyEvent.KEYCODE_DPAD_RIGHT == keyCode) {
                    view.setSelected(true);
                    if(homeIcon.isSelected()){
                        YoutubeRowFragment frag = (YoutubeRowFragment) youtubeFragment.getFragmentManager().findFragmentById(R.id.container_row);
                        YoutubeRowFragment.highlightRowFocus(this, frag);
//                        Log.d("check", ""+selected_row);
//                        View e = listRowView.getGridView().getChildAt(selected_row);
//                        i = ((ViewGroup)v.getChildAt(1)).getChildCount();
//                        RecyclerView.ViewHolder cardViewHolder = listRowView.getGridView().getChildViewHolder(e);
//                        listRowView.getGridView().findViewById()
//                        int check = frag.getSelectedPosition();
//                        ListRow listRow = (ListRow) frag.getAdapter().get(check);
//                        ArrayObjectAdapter adapter = (ArrayObjectAdapter) listRow.getAdapter();
//                        YouTubeVideo ytv = (YouTubeVideo) listRow.getAdapter().get(0);
//                        YouTubeCardPresenter ycp = (YouTubeCardPresenter) adapter.getPresenter(ytv);

//                        RecyclerView.ViewHolder cardViewHolder = verticalGridView.getChildViewHolder(v);
//                        cardViewHolder.getLayoutPosition();
//                        GridLayoutManager gridLayoutManager = (GridLayoutManager) frag.getVerticalGridView().getLayoutManager();
//                        View v = gridLayoutManager.findViewByPosition(frag.getVerticalGridView().getSelectedPosition());
//                        gridLayoutManager.getSelection();
//                        adapter.getPresenter()
                    }
                    if(searchIcon.isSelected()) {
                        RecyclerView suggestions = searchFragment.getView().findViewById(R.id.rv_view);
//                        View searchRow = searchFragment.getView().findViewById(R.id.search_row);
                        SearchRowFragment searchRow = (SearchRowFragment) searchFragment.getFragmentManager().findFragmentById(R.id.search_row);
                        if(searchFragment.getFocus() == SearchFragment.FocusLocation.Suggestion) {
                            suggestions.getChildAt(SuggestListAdapter.OutId).requestFocus();
                            return true;
                        }
                        else if(searchFragment.getFocus() == SearchFragment.FocusLocation.SearchRow){
//                            searchRow.requestFocus();
                            YoutubeRowFragment.highlightRowFocus(this, searchRow);
                            searchIcon.setNextFocusRightId(R.id.search_row);
                        }
                        else if(searchFragment.getFocus() == SearchFragment.FocusLocation.Keyboard) {
                            searchIcon.setNextFocusRightId(SearchFragment.KeyboardFocusId);
                        }
                    }
                }
                if(KeyEvent.KEYCODE_BACK == keyCode){
                    if(!homeIcon.isFocused()){
                        homeIcon.requestFocus();
                        return true;
                    }
                }
            }
            return false;
        });
    }

    private void setIconOnKeyListener(){
        setIconOnKey(searchIcon);
        setIconOnKey(homeIcon);
        setIconOnKey(subIcon);
        setIconOnKey(folderIcon);
        setIconOnKey(settingIcon);
    }

    public Fragment getPageFragment(PageCategory category){
        switch (category){
            case Home:
                return youtubeFragment;
            case Search:
                return searchFragment;
        }
        return new BlankFragment();
    }

    public View getPlayerBox() {
        return playerBox;
    }

    public YouTubePlayer getYouTubePlayer() {
        return youTubePlayer;
    }

    public void playVideo(YouTubeVideo video){
        if (playerBox.getVisibility() != View.VISIBLE) {
            viewModel.setRelated(true);
            viewModel.searchRelatedVideo(video.getId());
            playerControlsFragment.setVideo(video);
            playerBox.setVisibility(View.VISIBLE);
            playerBox.requestFocus();
            youTubePlayer.loadVideo(video.getId());
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
        this.youTubePlayer = youTubePlayer;
//        youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
//        youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_CUSTOM_LAYOUT);
//        youTubePlayer.setPlaylistEventListener(playlistEventListener);
//        youTubePlayer.setPlayerStateChangeListener(playbackEventListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        if (!wasRestored) {
            Log.d("CheckPoint", "CheckPoint !wasRestored");
//            youTubePlayer.cueVideo(video.getId());
//            youTubePlayer.play();
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
//            Log.d(TAG, "onBackPressed");
//            getSupportFragmentManager().beginTransaction().hide(youtubeFragment);
//            youTubePlayer.pause();
            playerBox.setVisibility(View.GONE);
            if(searchIcon.isSelected()) {
                ViewGroup search  = searchFragment.getView().findViewById(R.id.search_row);
                search.requestFocus();
            }
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
