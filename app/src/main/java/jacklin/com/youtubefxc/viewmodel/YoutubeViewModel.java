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
    public static final String [] recommend_playlistId_url = {
            "PLzjFbaFzsmMS-b4t5Eh3LJcf3HYlmVWYe",
            "PLDcnymzs18LWLKtkNrKYzPpHLbnXRu4nN",
//                "PLS3UB7jaIERzy5Ua5i0nlEYY6_xh2PhUf",
//                "PL8fVUTBmJhHJmpP7sLb9JfLtdwCmYX9xC",
//                "PLAdMV6KkPvD4igiNzQm5Zk3f3vcqN8xFW",
//                "PLiBi9LVIrC-eGpUAUkxkjpw4QIayQJMpD",
//                "PLCmd_pMCXoQLqg3gKa0_c0URlbIEetgXM",
//                "PLkLNgKNlFzZ35-B8UdoLWnMCAf3GsPbr9",
//                "PL57quI9usf_vDPXuhqIjyrPIjkw3C1oPe",
    };

    private String music_url = "UC-9-kyTW8ZkZNDHQJ6FgpwQ";

    private final static String TAG = YoutubeViewModel.class.getSimpleName();

    private CompositeDisposable disposable;

    private NetworkDataModel networkDataModel = new NetworkDataModel();

    private MutableLiveData<Map<String, List<YouTubeVideo>>> recommendedChannelList;

    private MutableLiveData<Map<String, List<YouTubeVideo>>> musicChannelList;

    public LiveData<Map<String, List<YouTubeVideo>>> getMusicChannelList(){
        if(musicChannelList == null) {
            Log.v(TAG, "getMusicChannelList NULL");
            musicChannelList = new MutableLiveData<>();
            Map<String, List<YouTubeVideo>> channelList = searchChannel(music_url);
            musicChannelList.postValue(channelList);
        }
        return musicChannelList;
    }

    public LiveData<Map<String, List<YouTubeVideo>>> getRecommendedChannelList(){
        if(recommendedChannelList == null) {
            Log.v(TAG, "getRecommendedChannelList NULL");
            recommendedChannelList = new MutableLiveData<>();

            List<YouTubeVideo> list = new ArrayList<>();
            for (int i = 0;i <= 3;i++)
                list.add(new YouTubeVideo(null,
                        null,
                        null, 0, null,null));

            Map<String, List<YouTubeVideo>> map = new HashMap<>();
            for (int i = 0;i <= 3;i++)
                map.put(String.valueOf(i), list);

            playlist(recommend_playlistId_url);
        }
        return recommendedChannelList;
    }

    public Map<String, List<YouTubeVideo>> searchChannel(String channelId){
        List<SearchResponse.Items> temp = new ArrayList<>();
        List<YouTubeVideo> ytv = new ArrayList<>();
        networkDataModel.searchChannelPlaylist(channelId)
                .flatMap(new Function<Response<SearchResponse>, Observable<String>>() {
                    @Override
                    public Observable<String> apply(Response<SearchResponse> searchResponse) throws Exception {
                        List<SearchResponse.Items> items = searchResponse.body().getItems();
                        temp.addAll(items);

                        List<String> playlistId = new ArrayList<>();
                        for (SearchResponse.Items id : items){
                            playlistId.add(id.getId().getPlaylistId());
                        }
                        return Observable.fromIterable(playlistId);
                    }
                })
                .flatMap(new Function<String, ObservableSource<Response<PlaylistItems>>>() {
                    @Override
                    public ObservableSource<Response<PlaylistItems>> apply(String s) throws Exception {
                        return networkDataModel.playlistItems(s);
                    }
                })
                .flatMap(new Function<Response<PlaylistItems>, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(Response<PlaylistItems> playlistItemsResponse) throws Exception {
                        List<PlaylistItems.Items> items = playlistItemsResponse.body().getItems();
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
                .subscribeWith(new DisposableObserver<Response<VideoResponse>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onNext(Response<VideoResponse> videoResponse) {
                        if(videoResponse.code() == 200) {
                            Log.d("YoutubeViewModel", "videoResponse : "
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
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return null;
    }

    /**
     * PlaylistId as channel playlists ID
     * @param playlistId
     *
     * Request with playlistId and Return Video Items
     * And Transform Items to Video Information
     * Subscribe These Information to Class Video
     */
    public void playlist(String[] playlistId){
        List<PlaylistItems.Items> temp = new ArrayList<>();
        final String[] channelTitle = new String[1];
        Map<String, List<YouTubeVideo>> channel =
                getRecommendedChannelList().getValue() == null ? new HashMap<>() : getRecommendedChannelList().getValue();
Observable.just(playlistId);
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
                        Log.d(TAG, "onComplete : Size " + channel.size());

                        recommendedChannelList.setValue(channel);
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
