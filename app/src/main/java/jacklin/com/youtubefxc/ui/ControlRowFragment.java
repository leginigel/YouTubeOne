package jacklin.com.youtubefxc.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowHeaderPresenter;
import android.support.v17.leanback.widget.RowHeaderView;
import android.support.v17.leanback.widget.RowPresenter;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import jacklin.com.youtubefxc.R;
import jacklin.com.youtubefxc.data.YouTubeVideo;
import jacklin.com.youtubefxc.ui.player.ControlCardPresenter;
import jacklin.com.youtubefxc.ui.youtube.YoutubeRowFragment;
import jacklin.com.youtubefxc.viewmodel.YoutubeViewModel;

public class ControlRowFragment extends YoutubeRowFragment {

    public static ControlRowFragment newInstance(String videoId) {
        ControlRowFragment controlRowFragment = new ControlRowFragment();
        Bundle args = new Bundle();
        args.putString("ID", videoId);
        controlRowFragment.setArguments(args);
        return controlRowFragment;
    }

    private YoutubeViewModel mViewModel;
    private ControlCardPresenter mCardPresenter;
    private ListRowPresenter mListRowPresenter;
    private ArrayObjectAdapter mRowsAdapter, mCardsAdapter;
    private ListRow mRow;

    private void setRows(List<YouTubeVideo> videos) {
        mCardsAdapter = new ArrayObjectAdapter(mCardPresenter);
        mCardsAdapter.addAll(0, videos);
        mRow = new ListRow(new HeaderItem("Suggestion "), mCardsAdapter);
        mRowsAdapter.add(mRow);

    }

    @Override
    public void initial() {
        setExpand(false);
        mCardPresenter = new ControlCardPresenter();
        mListRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL);
        mListRowPresenter.setShadowEnabled(true);
//        mListRowPresenter.setSelectEffectEnabled(true);
        mRowsAdapter = new ArrayObjectAdapter(mListRowPresenter);
        setAdapter(mRowsAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnItemViewSelectedListener(new ControlRowCardSelectedListener());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(YoutubeViewModel.class);
        setRows(mViewModel.getRelatedVideos());
    }

    public final class ControlRowCardSelectedListener implements OnItemViewSelectedListener {

        private ImageCardView imgCard = null;
//        private CustomCardView imgCard = null;

        @Override
        public void onItemSelected(Presenter.ViewHolder viewHolder, Object o,
                                   RowPresenter.ViewHolder viewHolder1, Row row) {
            YouTubeCardPresenter.CardViewHolder cardViewHolder = (YouTubeCardPresenter.CardViewHolder) viewHolder;
            if(o instanceof YouTubeVideo) {
                // Reset the ImageCardView Info Color
                if(imgCard != null){
                    imgCard.setInfoAreaBackgroundColor(Color.TRANSPARENT);
                    ((TextView) imgCard.findViewById(R.id.title_text)).setTextColor(Color.WHITE);
                }

                // Set the Selected Color
                imgCard = cardViewHolder.getImageCardView();
                imgCard.setInfoAreaBackgroundColor(Color.WHITE);
                ((TextView) imgCard.findViewById(R.id.title_text))
                        .setTextColor(getResources().getColor(R.color.background));
            }
        }

    }
}
