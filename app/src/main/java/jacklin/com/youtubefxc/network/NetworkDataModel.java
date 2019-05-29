package jacklin.com.youtubefxc.network;

import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import jacklin.com.youtubefxc.api.GoogleResponse;
import jacklin.com.youtubefxc.api.GoogleService;
import jacklin.com.youtubefxc.api.PlaylistItemsResponse;
import jacklin.com.youtubefxc.api.SearchResponse;
import jacklin.com.youtubefxc.api.VideoResponse;
import jacklin.com.youtubefxc.api.YoutubeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkDataModel {
    private YoutubeService youtubeService = RetrofitManager.getInstance().getAPI();
    private GoogleService googleService = RetrofitManager.getInstance().getGoogleAPI();

    public void searchVideo(String query, final onSearchDataReadyCallback callback){
        youtubeService.searchVideo(query)
                .enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        Log.d("retrofit response ", String.valueOf(response.body()));
                        callback.onSearchDataReady(response.body().getItems());
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        Log.d("retrofit response ", "Failure" + t);
                    }
                });
    }

    public interface onSearchDataReadyCallback {
        void onSearchDataReady(List<SearchResponse.Items> data);
    }

    //RxJava2
    public Observable<Response<SearchResponse>> searchVideoRx(String query){
        return youtubeService.searchVideoRx(query);
    }

    public Observable<Response<VideoResponse>> videoDetail(String id){
        return youtubeService.videoDetail(id);
    }

    public Observable<Response<PlaylistItemsResponse>> playlistItems(String playlistId){
        return youtubeService.playlistItems(playlistId);
    }

    public Observable<Response<SearchResponse>> searchChannelPlaylist(String channelId){
        return youtubeService.searchChannelPlaylist(channelId);
    }

    public Observable<Response<VideoResponse>> videoPopular(){
        return youtubeService.videoPopular();
    }

    public Observable<Response<SearchResponse>> searchLatestWeek(String publishAfter, String publishBefore){
        return youtubeService.searchLatestWeek(publishAfter, publishBefore);
    }

    public Observable<Response<GoogleResponse>> searchSuggestion(String query){
        return googleService.searchGoogle(query);
    }
}
