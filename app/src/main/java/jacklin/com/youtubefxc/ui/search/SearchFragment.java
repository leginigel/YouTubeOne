package jacklin.com.youtubefxc.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import jacklin.com.youtubefxc.R;
import jacklin.com.youtubefxc.api.SearchResponse;
import jacklin.com.youtubefxc.api.VideoResponse;
import jacklin.com.youtubefxc.viewmodel.SearchViewModel;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;
    private CompositeDisposable mDisposable;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(SearchViewModel.class);

//        mDisposable.add(
//                mViewModel.searchVideo("End Game")
//                        .subscribeOn(Schedulers.io())
//                        .flatMap(new Function<Response<SearchResponse>, ObservableSource<Response<VideoResponse>>>() {
//                            @Override
//                            public ObservableSource<Response<VideoResponse>> apply(Response<SearchResponse> searchResponseResponse) throws Exception {
//                                String videoId = searchResponseResponse.body().getItems().get(0).getId().getVideoId();
//                                return mViewModel.videoDetail(videoId);
//                            }
//                        })
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribeWith(new DisposableObserver<Response<VideoResponse>>() {
//                            @Override
//                            public void onNext(Response<VideoResponse> searchResponseResponse) {
////                                Log.d(SearchFragment.class.getSimpleName(), );
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//
//                            }
//
//                            @Override
//                            public void onComplete() {
//
//                            }
//                        })

//        mDisposable.add(
                mViewModel.searchVideo("End Game")
                        .subscribeOn(Schedulers.io())
                        .map(searchResponseResponse -> searchResponseResponse.body().getItems())
                        .flatMap(s -> Observable.fromIterable(s))
                        .map(item -> item.getId())
                        .flatMap(new Function<SearchResponse.Items.ID, ObservableSource<Response<VideoResponse>>>() {
                            @Override
                            public ObservableSource<Response<VideoResponse>> apply(SearchResponse.Items.ID id) throws Exception {
                                return mViewModel.videoDetail(id.getVideoId());
                            }
                        })
//                        .doOnNext(response -> Log.d("test", "onActivityCreated: : " + response.body().getItems().get(0).getSnippet().getTitle()))
                        .subscribe();
//        );

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposable.clear();
    }
}
