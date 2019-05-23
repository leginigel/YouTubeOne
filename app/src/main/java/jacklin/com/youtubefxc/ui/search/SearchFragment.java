package jacklin.com.youtubefxc.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import jacklin.com.youtubefxc.R;
import jacklin.com.youtubefxc.api.SearchResponse;
import jacklin.com.youtubefxc.api.VideoResponse;
import jacklin.com.youtubefxc.data.YouTubeVideo;
import jacklin.com.youtubefxc.network.NetworkDataModel;
import jacklin.com.youtubefxc.viewmodel.SearchViewModel;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;
    private List<YouTubeVideo> mVideoList = new ArrayList<>();
    private ArrayObjectAdapter mCardsAdapter;
    private ArrayObjectAdapter mRowsAdapter;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    TextView searchBar;
    CardView searchIcon, clearIcon, spaceIcon;
    View suggest;
    FrameLayout mKeyboard;
    AlphabetKeyborad mAlphabet;
    public enum Keyboard{
        Alphabet, Number
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mAlphabet = AlphabetKeyborad.newInstance();
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        mKeyboard = view.findViewById(R.id.keyboard);
        if(savedInstanceState == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fmts = fm.beginTransaction();
            fmts.replace(R.id.keyboard, mAlphabet).commit();
        }
        suggest = view.findViewById(R.id.view);
        searchBar = view.findViewById(R.id.search_bar);
        searchIcon = view.findViewById(R.id.cardViewSearch);
        clearIcon = view.findViewById(R.id.cardViewClear);
        spaceIcon = view.findViewById(R.id.cardViewSpace);
        return view;
    }

    private void setOnFocusListener(){

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

//        mViewModel.getVideoList().observe(getActivity(), (videos) ->{
//            mVideoList = videos;
//            mCardsAdapter.clear();
//            for (YouTubeVideo v : mVideoList){
//                mCardsAdapter.add(v);
//            }
//            mCardsAdapter.notifyArrayItemRangeChanged(0, 1);
//            Log.d("Fragment ViewModel", "notify");
//        });
//        mViewModel.searchVideo("suzy");
        mVideoList = mViewModel.getVideoList().getValue();
    }

}
