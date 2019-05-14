package jacklin.com.youtubefxc.ui.youtube;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v17.leanback.app.RowsSupportFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jacklin.com.youtubefxc.R;
import jacklin.com.youtubefxc.YoutubeActivity;
import jacklin.com.youtubefxc.api.YoutubeService;
import jacklin.com.youtubefxc.data.YouTubeVideo;
import jacklin.com.youtubefxc.ui.YouTubeCardPresenter;
import jacklin.com.youtubefxc.viewmodel.YoutubeViewModel;

/**
 * ViewModel {@link YoutubeViewModel}
 *
 */

@RequiresApi(api = Build.VERSION_CODES.N)
public class YoutubeRowFragment extends RowsSupportFragment {

    private final static String TAG = YoutubeRowFragment.class.getSimpleName();
    private List<YouTubeVideo> mVideoList = new ArrayList<>();
    private Map<String, List<YouTubeVideo>> mRecommendedChannel;
    private Map<String, List<YouTubeVideo>> mLatestChannel;
    private Map<String, List<YouTubeVideo>> mMusicChannel;

    private ArrayObjectAdapter mCardsAdapter;
    private ArrayObjectAdapter mRowsAdapter;
    private ListRowPresenter mListRowPresenter;
    private YouTubeCardPresenter mYouTubeCardPresenter;
    private View videoBox;
    private YouTubePlayerSupportFragment youTubePlayerFragment;

    public static YoutubeRowFragment newInstance() {
        return new YoutubeRowFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mYouTubeCardPresenter = new YouTubeCardPresenter();
        mListRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL);
        mListRowPresenter.setShadowEnabled(true);
        mListRowPresenter.setSelectEffectEnabled(false);
        mRowsAdapter = new ArrayObjectAdapter(mListRowPresenter);

        setRows(null);

        youTubePlayerFragment = (YouTubePlayerSupportFragment) getActivity()
                        .getSupportFragmentManager().findFragmentById(R.id.fragment_youtube_player);
        videoBox = ((YoutubeActivity) getActivity()).getPlayerBox();

        setOnItemViewSelectedListener(new YouTubeCardSelectedListener());
        setOnItemViewClickedListener(new YouTubeCardClickedListener());

        return super.onCreateView(inflater, container, savedInstanceState);
    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Log.i(TAG, "onActivityCreated");
//        mViewModel = ViewModelProviders.of(getActivity()).get(YoutubeViewModel.class);
//    }

    public void setRows(Map<String, List<YouTubeVideo>> channelList){

        if(channelList == null){
            Log.d("Fragment Set Rows", "Channel NULL");
            mCardsAdapter = new ArrayObjectAdapter(mYouTubeCardPresenter);
            mRowsAdapter.clear();
            for (int i = 0; i < 3; i++) {
                mCardsAdapter.add(new YouTubeVideo(null, null,
                        null, 0, null, null));
            }
            for (int i = 0; i < 2; i++) {
                ListRow row = new ListRow(new HeaderItem("Row " + i), mCardsAdapter);
                mRowsAdapter.add(row);
            }
            setAdapter(mRowsAdapter);
        }
        else {
            Log.i("Fragment Set Rows", "Get Channels");
            mRowsAdapter.clear();
            channelList.forEach((channel,videos)->{
                mCardsAdapter = new ArrayObjectAdapter(mYouTubeCardPresenter);

                mCardsAdapter.addAll(0,videos);
                Log.d("Fragment Set Rows", "Get Channel : " + channel + videos.get(0).getTitle());
                HeaderItem headerItem = new HeaderItem(channel);

                ListRow row = new ListRow(headerItem, mCardsAdapter);
                mRowsAdapter.add(row);
            });
//            mRowsAdapter.notifyArrayItemRangeChanged(0, 10);
//            setAdapter(mRowsAdapter);
        }
    }

    private final class YouTubeCardSelectedListener implements OnItemViewSelectedListener{

        private ImageCardView imgCard = null;
//        private CustomCardView imgCard = null;

        @Override
        public void onItemSelected(Presenter.ViewHolder viewHolder, Object o,
                                   RowPresenter.ViewHolder viewHolder1, Row row) {
            YouTubeCardPresenter.CardViewHolder cardViewHolder = (YouTubeCardPresenter.CardViewHolder) viewHolder;
            if(o instanceof YouTubeVideo) {

                if(imgCard != null){
                    imgCard.setInfoAreaBackgroundColor(getResources().getColor(R.color.background));
                    ((TextView) imgCard.findViewById(R.id.title_text))
                            .setTextColor(Color.WHITE);
                }
//                Log.d(TAG, "Selected");
//                imgCard = (ImageCardView) viewHolder.view;
//                ImageView mainImage = cardViewHolder.view.findViewById(R.id.main_image);
//                mainImage.animate().scaleX(1.2f).scaleY(1.2f);
                imgCard = (ImageCardView) cardViewHolder.getImageCardView();

                switch (getTabCategory()){
                    case Recommended:
                        imgCard.setNextFocusUpId(R.id.recommend_btn);
                        break;
                    case Music:
                        imgCard.setNextFocusUpId(R.id.music_btn);
                        break;
                    case Entertainment:
                        imgCard.setNextFocusUpId(R.id.entertainment_btn);
                        break;
                    case Gaming:
                        imgCard.setNextFocusUpId(R.id.gaming_btn);
                        break;
                }

                Log.d("RowFrag", "test"+mRowsAdapter.indexOf(row) + mRowsAdapter.size());
//                mRowsAdapter.indexOf(viewHolder1);
                if(mRowsAdapter.indexOf(row) == mRowsAdapter.size() -1)
                    imgCard.setNextFocusDownId(R.id.img_card_view);
                imgCard.setInfoAreaBackgroundColor(Color.WHITE);
                ((TextView) imgCard.findViewById(R.id.title_text))
                        .setTextColor(getResources().getColor(R.color.background));
            }
        }

    }

    private final class YouTubeCardClickedListener implements OnItemViewClickedListener,
            YouTubePlayer.OnInitializedListener {

        private YouTubeVideo video;

        @Override
        public void onItemClicked(Presenter.ViewHolder viewHolder, Object o,
                                  RowPresenter.ViewHolder viewHolder1, Row row) {
            if(o instanceof YouTubeVideo) {
                if(((YouTubeVideo) o).getId() != null){
                    video = (YouTubeVideo) o;
                    if (videoBox.getVisibility() != View.VISIBLE) {
                        videoBox.setVisibility(View.VISIBLE);
                        youTubePlayerFragment.initialize(YoutubeService.key, this);
                    }
                }
            }
        }

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
            if (!wasRestored) {
                Log.d("CheckPoint", "CheckPoint !wasRestored");
                youTubePlayer.cueVideo(video.getId());
            }
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
            Log.d("onInitializationFailure", "Failed to initialize.");
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(youTubePlayerFragment.getActivity(), 1).show();
            } else {
                String errorMessage = errorReason.toString();
                Log.d("onInitializationFailure", errorMessage);
            }

        }
    }

    public YoutubeFragment.TabCategory getTabCategory() {
        return null;
    }
}
