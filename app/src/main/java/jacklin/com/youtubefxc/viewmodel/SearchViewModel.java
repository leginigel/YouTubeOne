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
import jacklin.com.youtubefxc.api.CompleteSuggestion;
import jacklin.com.youtubefxc.api.GoogleResponse;
import jacklin.com.youtubefxc.api.SearchResponse;
import jacklin.com.youtubefxc.api.VideoResponse;
import jacklin.com.youtubefxc.data.YouTubeVideo;
import jacklin.com.youtubefxc.network.NetworkDataModel;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {
    private static final String TAG = SearchViewModel.class.getSimpleName();
    private CompositeDisposable disposable;

    private MutableLiveData<List<YouTubeVideo>> videos;

    private NetworkDataModel networkDataModel = new NetworkDataModel();

    private MutableLiveData<String> queryString = new MutableLiveData<>();

    public LiveData<String> getQueryString(){
        return queryString;
    }

    public void setQueryString(String query){
        if(query.equals("")){
            queryString.setValue("");
            return;
        }
        String now = queryString.getValue();
        if(now != null){
            queryString.setValue(now + query);

        }
        else
            queryString.setValue(query);
    }

    public LiveData<List<YouTubeVideo>> getVideoList(){
        if(videos == null){
            Log.v(TAG, "getVideoList NULL");
            videos = new MutableLiveData<>();
        }
        return videos;
    }

    public void searchSuggestion(String query){
        networkDataModel.searchSuggestion(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
//                .filter(r-> r.code() == 200)
                .subscribe(new DisposableObserver<Response<GoogleResponse>>() {
                    @Override
                    public void onNext(Response<GoogleResponse> googleResponseResponse) {
                        Log.d("onNext", "" + googleResponseResponse.code());
                        List<CompleteSuggestion> list
                                = googleResponseResponse.body().getCompleteSuggestion();
                        for (CompleteSuggestion i : list){
                            String data = i.getSuggestion().getData();
                            Log.d("data", data);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d("onError", e.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.d("onComplete", "onComplete");
                    }
                });
    }

    public void searchRx(String query){
        List<SearchResponse.Items> temp = new ArrayList<>();
        List<YouTubeVideo> ytv = new ArrayList<>();
//        disposable.add(
        networkDataModel.searchVideoRx(query)
                .flatMap(new Function<Response<SearchResponse>, Observable<String>>() {
                    @Override
                    public Observable<String> apply(Response<SearchResponse> searchResponse) throws Exception {
                        List<SearchResponse.Items> items = searchResponse.body().getItems();
                        temp.addAll(items);
                        String multi_id = "";
                        for (SearchResponse.Items i : items){
                            multi_id = multi_id + i.getId().getVideoId() + ",";
                        }
                        return Observable.just(multi_id);
                    }
                })
                .flatMap(new Function<String, ObservableSource<Response<VideoResponse>>>() {
                    @Override
                    public ObservableSource<Response<VideoResponse>> apply(String s) throws Exception {
                        return networkDataModel.videoDetail(s);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(r-> r.code() == 200)
                .subscribe(new DisposableObserver<Response<VideoResponse>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onNext(Response<VideoResponse> videoResponse) {
                            Log.d("SearchViewModel", "videoResponse : "
                                    + videoResponse.body().getItems().get(0).getSnippet().getTitle());
                            for (int i = 0;i < temp.size();i++){
                                if(temp.get(i).getId().getVideoId() != null &&
                                        temp.get(i).getId().getVideoId().equals(videoResponse.body().getItems().get(i).getId())) {
                                    Log.d("test", "onActivityCreated:"
                                            + videoResponse.body().getItems().get(i).getSnippet().getTitle());
                                    ytv.add(
                                            new YouTubeVideo(
                                                    temp.get(i).getId().getVideoId(),
                                                    temp.get(i).getSnippet().getTitle(),
                                                    temp.get(i).getSnippet().getChannelTitle(),
                                                    videoResponse.body().getItems().get(i).getStatistics().getViewCount(),
                                                    videoResponse.body().getItems().get(i).getSnippet().getPublishedAt(),
                                                    videoResponse.body().getItems().get(i).getContentDetails().getDuration()
                                            ));
                                }
                            }
                            Log.d("SearchViewModel", "onNext :" + ytv.size());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        videos.setValue(ytv);
                    }
                });
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
//        disposable.clear();
    }
}
