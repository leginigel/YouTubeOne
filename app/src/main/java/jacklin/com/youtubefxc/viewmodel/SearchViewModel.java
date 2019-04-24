package jacklin.com.youtubefxc.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import jacklin.com.youtubefxc.api.SearchResponse;
import jacklin.com.youtubefxc.api.VideoResponse;
import jacklin.com.youtubefxc.data.YouTubeVideo;
import jacklin.com.youtubefxc.network.NetworkDataModel;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {
    private CompositeDisposable disposable;

    private MutableLiveData<List<YouTubeVideo>> videos;

    private NetworkDataModel networkDataModel = new NetworkDataModel();

    public LiveData<List<YouTubeVideo>> getVideoList(){
        if(videos == null){
            videos = new MutableLiveData<>();
            List<YouTubeVideo> list = new ArrayList<>();
            for (int i = 0;i < 3;i++)
                list.add(new YouTubeVideo("id","test", "test", 0, "now"));
            videos.setValue(list);
        }
        return videos;
    }

    public void searchVideo(String query){
        List<SearchResponse.Items> temp = new ArrayList<>();
        List<YouTubeVideo> ytv = new ArrayList<>();
//        disposable.add(
        networkDataModel.searchVideoRx(query)
                .flatMap(new Function<Response<SearchResponse>, ObservableSource<SearchResponse.Items>>() {
                    @Override
                    public ObservableSource<SearchResponse.Items> apply(Response<SearchResponse> searchResponse) throws Exception {
                        List<SearchResponse.Items> items = searchResponse.body().getItems();
                        temp.addAll(items);
                        return Observable.fromIterable(items);
                    }
                })
                .flatMap(new Function<SearchResponse.Items, ObservableSource<Response<VideoResponse>>>() {
                    @Override
                    public ObservableSource<Response<VideoResponse>> apply(SearchResponse.Items items) throws Exception {
                        return networkDataModel.videoDetail(items.getId().getVideoId());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<VideoResponse>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onNext(Response<VideoResponse> videoResponse) {
                        if(videoResponse.code() == 200) {
                            Log.d("YoutubeViewModel", "videoResponse : "
                                    + videoResponse.body().getItems().get(0).getSnippet().getTitle());
                            temp.forEach(i -> {
                                if(i.getId().getVideoId() != null &&
                                        i.getId().getVideoId().equals(videoResponse.body().getItems().get(0).getId())) {
                                    Log.d("test", "onActivityCreated:"
                                            + videoResponse.body().getItems().get(0).getSnippet().getTitle());
                                    ytv.add(
                                            new YouTubeVideo(
                                                    i.getId().getVideoId(),
                                                    i.getSnippet().getTitle(),
                                                    i.getSnippet().getChannelTitle(),
                                                    videoResponse.body().getItems().get(0).getStatistics().getViewCount(),
                                                    videoResponse.body().getItems().get(0).getContentDetails().getDuration()
                                            ));
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        videos.setValue(ytv);
                    }
                })
        ;
//    );
    }

    public Observable<Response<SearchResponse>> searchVideo(String query, boolean oldpass){
        return networkDataModel.searchVideoRx(query);
    }

    public Observable<Response<VideoResponse>> videoDetail(String id, boolean oldpass){
        return networkDataModel.videoDetail(id);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
