package jacklin.com.youtubefxc.viewmodel;

import android.arch.lifecycle.ViewModel;

import io.reactivex.Observable;
import jacklin.com.youtubefxc.api.SearchResponse;
import jacklin.com.youtubefxc.api.VideoResponse;
import jacklin.com.youtubefxc.network.NetworkDataModel;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {
    private NetworkDataModel networkDataModel = new NetworkDataModel();

    public Observable<Response<SearchResponse>> searchVideo(String query){
        return networkDataModel.searchVideoRx(query);
    }

    public Observable<Response<VideoResponse>> videoDetail(String id){
        return networkDataModel.videoDetail(id);
    }
}
