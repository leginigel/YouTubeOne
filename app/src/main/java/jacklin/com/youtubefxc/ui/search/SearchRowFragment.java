package jacklin.com.youtubefxc.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import jacklin.com.youtubefxc.data.YouTubeVideo;
import jacklin.com.youtubefxc.ui.YouTubeCardPresenter;
import jacklin.com.youtubefxc.ui.youtube.YoutubeRowFragment;
import jacklin.com.youtubefxc.viewmodel.SearchViewModel;


public class SearchRowFragment extends YoutubeRowFragment {

    public static final String TAG = SearchRowFragment.class.getSimpleName();
    private SearchViewModel mViewModel;
    private List<YouTubeVideo> mSearchVideo;
    private SearchCardPresenter mSearchCardPresenter;
    private ListRowPresenter mListRowPresenter;
    private ArrayObjectAdapter mRowsAdapter, mCardsAdapter;
    private ListRow mRow;

    public static SearchRowFragment newInstance() {
        return new SearchRowFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(SearchViewModel.class);
        mViewModel.getVideoList().observe(getActivity(), videos ->{
            Log.d(TAG, "observe");
            mSearchVideo = videos;
            setRow(mSearchVideo);
        });
    }

    @Override
    public void initial() {
        mSearchCardPresenter = new SearchCardPresenter();
        mListRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL);
        mListRowPresenter.setShadowEnabled(true);
        mListRowPresenter.setSelectEffectEnabled(false);
        mRowsAdapter = new ArrayObjectAdapter(mListRowPresenter);
        mSearchVideo = new ArrayList<>();

        mCardsAdapter = new ArrayObjectAdapter(mSearchCardPresenter);
        mRow = new ListRow(mCardsAdapter);
        mRowsAdapter.add(mRow);
        setAdapter(mRowsAdapter);
    }

    private void setRow(List<YouTubeVideo> searchVideos) {
        mRowsAdapter.clear();

        mCardsAdapter = new ArrayObjectAdapter(mSearchCardPresenter);
        mCardsAdapter.addAll(0, searchVideos);
//        mCardsAdapter.notifyArrayItemRangeChanged(0, 1);
        mRow = new ListRow(mCardsAdapter);
        mRowsAdapter.add(mRow);
    }

    public void clear(){
        if(mRowsAdapter != null)
            mRowsAdapter.clear();
    }

    @Override
    public ArrayObjectAdapter getRowsAdapter() {
        return mRowsAdapter;
    }

    @Override
    public YouTubeCardPresenter getCardPresenter() {
        return mSearchCardPresenter;
    }
}
