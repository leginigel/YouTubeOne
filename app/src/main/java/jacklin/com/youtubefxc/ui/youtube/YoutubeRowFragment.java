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
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jacklin.com.youtubefxc.R;
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
    private YoutubeViewModel mViewModel;
    private List<YouTubeVideo> mVideoList = new ArrayList<>();
    private Map<String, List<YouTubeVideo>> mRecommendedChannel;
    private Map<String, List<YouTubeVideo>> mLatestChannel;
    private Map<String, List<YouTubeVideo>> mMusicChannel;

    private ArrayObjectAdapter mCardsAdapter;
    private ArrayObjectAdapter mRowsAdapter;
    private ListRowPresenter mListRowPresenter;
    private YouTubeCardPresenter mYouTubeCardPresenter;

    private TabCategory mTab = TabCategory.Recommended;

    public enum TabCategory {
        Recommended, Latest, Music, Entertainment, Gaming
    }

    public static YoutubeRowFragment newInstance() {
        return new YoutubeRowFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube_row, container, false);

        mYouTubeCardPresenter = new YouTubeCardPresenter();
        mListRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL);
        mListRowPresenter.setShadowEnabled(true);
        mListRowPresenter.setSelectEffectEnabled(false);
        mRowsAdapter = new ArrayObjectAdapter(mListRowPresenter);

        mTab = TabCategory.Recommended;

        setRows(null);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(YoutubeViewModel.class);

        mViewModel.getRecommendedChannelList().observe(getActivity(), (channels)->{
            Log.i(TAG + " ViewModel", "Recommended Observe ");
            if(this.mTab == TabCategory.Recommended) {
                mRecommendedChannel = channels;
                setRows(mRecommendedChannel);
            }
        });
        mViewModel.getMusicChannelList().observe(getActivity(), (channels)->{
            Log.i(TAG + " ViewModel", "Music Observe ");
            if(this.mTab == TabCategory.Music) {
//                setRows(channels);
//                mRecommendedChannel = channels;
//                setRows(mRecommendedChannel);
            }
        });

//        mViewModel.playlist(recommend_playlist_url);
        mRecommendedChannel = mViewModel.getRecommendedChannelList().getValue();

        setOnItemViewSelectedListener(new YouTubeCardSelectedListener());
    }

    private void setRows(Map<String, List<YouTubeVideo>> channelList){

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
            mCardsAdapter = new ArrayObjectAdapter(mYouTubeCardPresenter);
            channelList.forEach((channel,videos)->{
                for (YouTubeVideo video : videos) {
                    mCardsAdapter.add(video);
                }
                Log.d("Fragment Set Rows", "Get Channel : " + channel + videos.get(0).getTitle());
                HeaderItem headerItem = new HeaderItem(channel);

                ListRow row = new ListRow(headerItem, mCardsAdapter);
                mRowsAdapter.add(row);
                mCardsAdapter.clear();
            });
//            mRowsAdapter.notifyArrayItemRangeChanged(0, 10);
//            setAdapter(mRowsAdapter);
        }
    }

    public void setTabCategory(TabCategory category){
        this.mTab = category;
        switch (category){
            case Recommended:
                Log.d(TAG, "Fragment setTabCategory Recommended");
                setRows(mRecommendedChannel);
                break;
            case Latest:
//                setRows(mLatestChannel);
                break;
            case Music:
                Log.d(TAG, "Fragment setTabCategory Music");
                setRows(mMusicChannel);
                break;
            case Entertainment:break;
            case Gaming:break;
        }
    }

    public TabCategory getTabCategory() {
        return mTab;
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

                imgCard.setInfoAreaBackgroundColor(Color.WHITE);
                ((TextView) imgCard.findViewById(R.id.title_text))
                        .setTextColor(getResources().getColor(R.color.background));
            }
        }

    }
}
