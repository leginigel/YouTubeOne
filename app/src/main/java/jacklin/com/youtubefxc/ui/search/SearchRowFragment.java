package jacklin.com.youtubefxc.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jacklin.com.youtubefxc.data.YouTubeVideo;
import jacklin.com.youtubefxc.ui.YouTubeCardPresenter;
import jacklin.com.youtubefxc.ui.youtube.YoutubeRowFragment;
import jacklin.com.youtubefxc.viewmodel.SearchViewModel;


public class SearchRowFragment extends YoutubeRowFragment {

    public static final String TAG = SearchRowFragment.class.getSimpleName();
    private SearchViewModel mViewModel;
    private List<YouTubeVideo> mSearchChannel;
    private SearchCardPresenter mSearchCardPresenter;
    private ListRowPresenter mListRowPresenter;
    private ArrayObjectAdapter mRowsAdapter;

    public static SearchRowFragment newInstance() {
        return new SearchRowFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(SearchViewModel.class);
        mViewModel.getVideoList().observe(getActivity(), channel ->{
            Log.d(TAG, "observe");
            mSearchChannel = channel;
            setRows(mSearchChannel);
        });
    }

    @Override
    public void initial() {
        mSearchCardPresenter = new SearchCardPresenter();
        mListRowPresenter = new ListRowPresenter(FocusHighlight.ZOOM_FACTOR_XSMALL);
        mListRowPresenter.setShadowEnabled(true);
        mListRowPresenter.setSelectEffectEnabled(false);
        mRowsAdapter = new ArrayObjectAdapter(mListRowPresenter);
        mSearchChannel = new ArrayList<>();
    }

    private void setRows(List<YouTubeVideo> searchChannel) {
        mRowsAdapter.clear();

        ArrayObjectAdapter mCardsAdapter = new ArrayObjectAdapter(mSearchCardPresenter);
        mCardsAdapter.addAll(0, searchChannel);
//        HeaderItem headerItem = new HeaderItem("FWord");

        ListRow row = new ListRow(mCardsAdapter);
        mRowsAdapter.add(row);

        setAdapter(mRowsAdapter);
    }
}
