package jacklin.com.youtubefxc.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import jacklin.com.youtubefxc.api.SearchResponse;
import jacklin.com.youtubefxc.data.YouTubeVideo;
import jacklin.com.youtubefxc.network.NetworkDataModel;

public class YoutubeViewModel extends ViewModel {

    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

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
        return videos;}

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
                                    i.getSnippet().getPublishedAt()
                            )
                    );
                }
                videos.setValue(ytv);

            }
        });
    }
}
