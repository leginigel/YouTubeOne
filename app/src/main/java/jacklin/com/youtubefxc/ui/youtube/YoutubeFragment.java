package jacklin.com.youtubefxc.ui.youtube;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.BrowseSupportFragment;
import android.support.v17.leanback.app.RowsSupportFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jacklin.com.youtubefxc.R;
import jacklin.com.youtubefxc.api.SearchResponse;
import jacklin.com.youtubefxc.ui.CardPresenter;
import jacklin.com.youtubefxc.viewmodel.YoutubeViewModel;

public class YoutubeFragment extends RowsSupportFragment {

    private YoutubeViewModel mViewModel;
    private ArrayObjectAdapter mRowsAdapter;
    private ArrayObjectAdapter mCardsAdapter;
    private CardPresenter mCardPresenter;

    public static YoutubeFragment newInstance() {
        return new YoutubeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHeadersState(BrowseSupportFragment.HEADERS_ENABLED);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.youtube_fragment, container, false);



        mCardPresenter = new CardPresenter();
        mCardsAdapter = new ArrayObjectAdapter(mCardPresenter);
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        mCardsAdapter.add(new SearchResponse());
        for (int i = 0; i < 4; i++) {
            ListRow row = new ListRow(new HeaderItem("Row " + i), mCardsAdapter);
            mRowsAdapter.add(row);
        }
        setAdapter(mRowsAdapter);
//        return view;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(YoutubeViewModel.class);
//        mViewModel.search("End Game");
        // TODO: Use the ViewModel
    }

}
