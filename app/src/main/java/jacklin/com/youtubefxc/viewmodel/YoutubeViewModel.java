package jacklin.com.youtubefxc.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import jacklin.com.youtubefxc.api.PlaylistItems;
import jacklin.com.youtubefxc.api.SearchResponse;
import jacklin.com.youtubefxc.api.VideoResponse;
import jacklin.com.youtubefxc.data.YouTubeVideo;
import jacklin.com.youtubefxc.network.NetworkDataModel;
import retrofit2.Response;

/**
 *  ViewModel for {@link jacklin.com.youtubefxc.ui.youtube.YoutubeRowFragment}
 */

public class YoutubeViewModel extends ViewModel {

    private CompositeDisposable disposable;

    private MutableLiveData<List<YouTubeVideo>> videos;

    private NetworkDataModel networkDataModel = new NetworkDataModel();

    private MutableLiveData<Map<String, List<YouTubeVideo>>> channelList;

    public LiveData<Map<String, List<YouTubeVideo>>> getChannelList(){
        if(channelList == null) {
            channelList = new MutableLiveData<>();

            List<YouTubeVideo> list = new ArrayList<>();
            for (int i = 0;i <= 3;i++)
                list.add(new YouTubeVideo(null,
                        null,
                        null, 0, null,null));

            Map<String, List<YouTubeVideo>> map = new HashMap<>();
            for (int i = 0;i <= 3;i++)
                map.put(String.valueOf(i), list);

//            channelList.setValue(map);
        }
        return channelList;
    }

    public LiveData<List<YouTubeVideo>> getVideoList(){
        if(videos == null){
            videos = new MutableLiveData<>();
            List<YouTubeVideo> list = new ArrayList<>();
            for (int i = 0;i <= 3;i++)
                list.add(new YouTubeVideo(null,
                        null,
                        null, 0, null,null));
            videos.setValue(list);
        }
        return videos;
    }

    public void search(String query) {

        networkDataModel.searchVideo(query, new NetworkDataModel.onSearchDataReadyCallback() {
            @Override
            public void onSearchDataReady(List<SearchResponse.Items> data) {
                Log.d("ViewModel", data.get(0).getId().getVideoId());
                Log.d("ViewModel", data.get(0).getSnippet().getTitle());
                Log.d("ViewModel", data.get(0).getSnippet().getPublishedAt());
                Log.d("ViewModel", data.get(0).getSnippet().getDescription());

                Log.d("ViewModel", data.get(0).getSnippet().getChannelTitle());
                List<YouTubeVideo> ytv = new ArrayList<>();
                for (SearchResponse.Items i : data) {
                    ytv.add(
                            new YouTubeVideo(
                                    i.getId().getVideoId(),
                                    i.getSnippet().getTitle(),
                                    i.getSnippet().getChannelTitle(),
                                    10,
                                    i.getSnippet().getPublishedAt(),
                                    "0"
                            )
                    );
                }
                videos.setValue(ytv);

            }
        });
    }

//    public void searchRx(String query){
//        List<SearchResponse.Items> temp = new ArrayList<>();
//        List<YouTubeVideo> ytv = new ArrayList<>();
////        disposable.add(
//                networkDataModel.searchVideoRx(query)
//                .flatMap(new Function<Response<SearchResponse>, Observable<String>>() {
//                    @Override
//                    public Observable<String> apply(Response<SearchResponse> searchResponse) throws Exception {
//                        List<SearchResponse.Items> items = searchResponse.body().getItems();
//                        temp.addAll(items);
//                        String multi_id = "";
//                        for (SearchResponse.Items i : items){
//                            multi_id = multi_id + i.getId().getVideoId() + ",";
//                        }
//                        return Observable.just(multi_id);
//                    }
//                })
//                .flatMap(new Function<String, ObservableSource<Response<VideoResponse>>>() {
//                    @Override
//                    public ObservableSource<Response<VideoResponse>> apply(String s) throws Exception {
//                        return networkDataModel.videoDetail(s);
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableObserver<Response<VideoResponse>>() {
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public void onNext(Response<VideoResponse> videoResponse) {
//                        if(videoResponse.code() == 200) {
//                            Log.d("YoutubeViewModel", "videoResponse : "
//                                    + videoResponse.body().getItems().get(0).getSnippet().getTitle());
//                            for (int i = 0;i < temp.size();i++){
//                                if(temp.get(i).getId().getVideoId() != null &&
//                                        temp.get(i).getId().getVideoId().equals(videoResponse.body().getItems().get(i).getId())) {
//                                    Log.d("test", "onActivityCreated:"
//                                            + videoResponse.body().getItems().get(i).getSnippet().getTitle());
//                                    ytv.add(
//                                    new YouTubeVideo(
//                                            temp.get(i).getId().getVideoId(),
//                                            temp.get(i).getSnippet().getTitle(),
//                                            temp.get(i).getSnippet().getChannelTitle(),
//                                            videoResponse.body().getItems().get(i).getStatistics().getViewCount(),
//                                            videoResponse.body().getItems().get(i).getSnippet().getPublishedAt(),
//                                            videoResponse.body().getItems().get(i).getContentDetails().getDuration()
//                                    ));
//                                }
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        videos.setValue(ytv);
//                    }
//                })
//    ;
////    );
//    }


    public void playlist(String[] playlistId){
        List<PlaylistItems.Items> temp = new ArrayList<>();
        List<YouTubeVideo> ytv = new ArrayList<>();
        final String[] channelTitle = new String[1];
        Map<String, List<YouTubeVideo>> channel =
                getChannelList().getValue() == null ? new HashMap<>() : getChannelList().getValue();

        networkDataModel.playlistItems(playlistId[0])
                .mergeWith(networkDataModel.playlistItems(playlistId[1]))
                .flatMap(new Function<Response<PlaylistItems>, Observable<String>>() {
                    @Override
                    public Observable<String> apply(Response<PlaylistItems> playlistItemsResponse) throws Exception {
                        channelTitle[0] = playlistItemsResponse.body().getItems().get(0).getSnippet().getChannelTitle();

                        List<PlaylistItems.Items> items = playlistItemsResponse.body().getItems();
                        temp.addAll(items);
                        String multi_id = "";
                        for (PlaylistItems.Items i : items){
                            multi_id = multi_id + i.getSnippet().getResourceId().getVideoId() + ",";
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
                .filter(response -> response.code() == 200)
                .subscribe(new DisposableObserver<Response<VideoResponse>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onNext(Response<VideoResponse> videoResponse) {
                        List<YouTubeVideo> yt = new ArrayList<>();
                        for (int i = 0;i < videoResponse.body().getItems().size();i++){
                            yt.add(
                                    new YouTubeVideo(
                                            videoResponse.body().getItems().get(i).getId(),
                                            videoResponse.body().getItems().get(i).getSnippet().getTitle(),
                                            videoResponse.body().getItems().get(i).getSnippet().getChannelTitle(),
                                            videoResponse.body().getItems().get(i).getStatistics().getViewCount(),
                                            videoResponse.body().getItems().get(i).getSnippet().getPublishedAt(),
                                            videoResponse.body().getItems().get(i).getContentDetails().getDuration()
                                    ));
                        }
                        Log.d("YoutubeViewModel", "onNext :" + channelTitle[0] + yt.size());
                        channel.put(channelTitle[0], yt);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
//                        videos.setValue(ytv);

                        Log.d("YoutubeViewModel", "onComplete : Size " + channel.size());
                        Log.d("YoutubeViewModel", "onComplete : " + channel.get("Movies - Topic").get(0).getTitle());
                        Log.d("YoutubeViewModel", "onComplete : " + channel.get("Pop Music - Topic").get(0).getTitle());

                        channelList.setValue(channel);
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
