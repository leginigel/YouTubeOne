package jacklin.com.youtubefxc.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.List;

import io.reactivex.Observable;
import jacklin.com.youtubefxc.api.SearchResponse;
import jacklin.com.youtubefxc.api.YoutubeService;
import jacklin.com.youtubefxc.network.NetworkDataModel;
import retrofit2.Response;

public class YoutubeViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    private final MutableLiveData<List<SearchResponse.Items>> items = new MutableLiveData<>();

    private NetworkDataModel networkDataModel = new NetworkDataModel();

    public void search(String query) {
        networkDataModel.searchVideo(query, new NetworkDataModel.onDataReadyCallback() {
            @Override
            public void onDataReady(List<SearchResponse.Items> data) {
                Log.d("ViewModel", data.get(0).getId().getVideoId());
                Log.d("ViewModel", data.get(0).getSnippet().getTitle());
                Log.d("ViewModel", data.get(0).getSnippet().getPublishedAt());
                Log.d("ViewModel", data.get(0).getSnippet().getDescription());
                items.setValue(data);
            }
        });

    }
}
