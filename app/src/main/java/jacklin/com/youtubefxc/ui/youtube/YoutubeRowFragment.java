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
import java.util.HashMap;
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
    private Map<String, List<YouTubeVideo>> mChannelList;
    private ArrayObjectAdapter mCardsAdapter;
    private ArrayObjectAdapter mRowsAdapter;
    private ListRowPresenter mListRowPresenter;
    private YouTubeCardPresenter mYouTubeCardPresenter;

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
        mCardsAdapter = new ArrayObjectAdapter(mYouTubeCardPresenter);
        mListRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL);
        mListRowPresenter.setShadowEnabled(true);
        mListRowPresenter.setSelectEffectEnabled(false);
        mRowsAdapter = new ArrayObjectAdapter(mListRowPresenter);

        setRows();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(YoutubeViewModel.class);

        mViewModel.getVideoList().observe(getActivity(), (videos) ->{
//            mVideoList = videos;
//            mCardsAdapter.clear();
//            for (YouTubeVideo v : mVideoList){
//                mCardsAdapter.add(v);
//            }
//            mCardsAdapter.notifyArrayItemRangeChanged(0, 1);
//            Log.d("Fragment ViewModel", "notify");
//            mRowsAdapter.clear();
//            ListRow row = new ListRow(new HeaderItem("Movie"), mCardsAdapter);
//            mRowsAdapter.add(row);
//            setAdapter(mRowsAdapter);
        });

        mViewModel.getChannelList().observe(getActivity(), (channels)->{
            Log.i("Fragment ViewModel", "observe");
            setRows();
        });

        String [] playlist_url = {"PLzjFbaFzsmMS-b4t5Eh3LJcf3HYlmVWYe",
                "PLDcnymzs18LWLKtkNrKYzPpHLbnXRu4nN"};
        mViewModel.playlist(playlist_url);
//        mVideoList = mViewModel.getVideoList().getValue();
        mChannelList = mViewModel.getChannelList().getValue();


        setOnItemViewSelectedListener(new YouTubeCardSelectedListener());
    }

    private void setRows(){
        if(mViewModel != null)
            mChannelList = mViewModel.getChannelList().getValue();

        if(mChannelList == null){
            Log.d("Fragment Set Rows", "Channel NULL");
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
            mChannelList.forEach((channel,videos)->{
                mCardsAdapter = new ArrayObjectAdapter(mYouTubeCardPresenter);
                for (YouTubeVideo video : videos) {
                    mCardsAdapter.add(video);
                }
                Log.d("Fragment Set Rows", "Get Channel : " + channel + videos.get(0).getTitle());
                HeaderItem headerItem = new HeaderItem(channel);

                ListRow row = new ListRow(headerItem, mCardsAdapter);
                mRowsAdapter.add(row);
            });
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
                imgCard = (ImageCardView) cardViewHolder.getImageCardView();
                imgCard.setInfoAreaBackgroundColor(Color.WHITE);
                ((TextView) imgCard.findViewById(R.id.title_text))
                        .setTextColor(getResources().getColor(R.color.background));
            }
        }

    }
}
