package jacklin.com.youtubefxc.ui.youtube;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import jacklin.com.youtubefxc.R;
import jacklin.com.youtubefxc.api.SearchResponse;
import jacklin.com.youtubefxc.data.YouTubeVideo;
import jacklin.com.youtubefxc.ui.YouTubeCardPresenter;
import jacklin.com.youtubefxc.viewmodel.YoutubeViewModel;

public class YoutubeFragment extends RowsSupportFragment {

    private YoutubeViewModel mViewModel;
    private List<YouTubeVideo> mVideoList = new ArrayList<>();
    private ArrayObjectAdapter mCardsAdapter;
    private ArrayObjectAdapter mRowsAdapter;

    public static YoutubeFragment newInstance() {
        return new YoutubeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.youtube_fragment, container, false);

        YouTubeCardPresenter mYouTubeCardPresenter = new YouTubeCardPresenter();
        mCardsAdapter = new ArrayObjectAdapter(mYouTubeCardPresenter);

        if(mVideoList!=null) {
            for (YouTubeVideo v : mVideoList) {
                mCardsAdapter.add(v);
            }
        }

        ListRowPresenter listRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL);
//        listRowPresenter.setShadowEnabled(false);
        listRowPresenter.setSelectEffectEnabled(false);
        mRowsAdapter = new ArrayObjectAdapter(listRowPresenter);
        for (int i = 0; i < 2; i++) {
            ListRow row = new ListRow(new HeaderItem("Row " + i), mCardsAdapter);
            mRowsAdapter.add(row);
        }
        setAdapter(mRowsAdapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(YoutubeViewModel.class);

        mViewModel.getVideoList().observe(getActivity(), (videos) ->{
            mVideoList = videos;
            mCardsAdapter.clear();
            for (YouTubeVideo v : mVideoList){
                mCardsAdapter.add(v);
            }
            mCardsAdapter.notifyArrayItemRangeChanged(0, 1);
            Log.d("Fragment ViewModel", "notify");
        });
        mViewModel.searchRx("Surfing");
        mVideoList = mViewModel.getVideoList().getValue();

        setOnItemViewSelectedListener(new YouTubeCardSelectedListener());
    }

    private final class YouTubeCardSelectedListener implements OnItemViewSelectedListener{

        private ImageCardView imgCard = null;

        @Override
        public void onItemSelected(Presenter.ViewHolder viewHolder, Object o,
                                   RowPresenter.ViewHolder viewHolder1, Row row) {
            if(o instanceof YouTubeVideo) {

                if(imgCard != null){
                    imgCard.setInfoAreaBackgroundColor(getResources().getColor(R.color.background));
                    ((TextView) imgCard.findViewById(R.id.title_text))
                            .setTextColor(Color.WHITE);
                }

                imgCard = (ImageCardView) viewHolder.view;
                imgCard.setInfoAreaBackgroundColor(Color.WHITE);
                ((TextView) imgCard.findViewById(R.id.title_text))
                        .setTextColor(getResources().getColor(R.color.background));
            }
        }

    }
}
