package jacklin.com.youtubefxc.network;

import android.util.Log;

import java.util.List;

import jacklin.com.youtubefxc.api.SearchResponse;
import jacklin.com.youtubefxc.api.YoutubeService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkDataModel {
    private YoutubeService youtubeService = RetrofitManager.getInstance().getAPI();

    public void searchVideo(String query, final onDataReadyCallback callback){
        youtubeService.searchVideo(query)
                .enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        callback.onDataReady(response.body().getItems());
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        Log.d("retro res ", "Failure" + t);
                    }
                });
    }

    public interface onDataReadyCallback{
        void onDataReady(List<SearchResponse.Items> data);
    }
}
