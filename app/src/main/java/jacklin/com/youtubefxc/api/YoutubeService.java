package jacklin.com.youtubefxc.api;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeService {
    String key = "AIzaSyDXOC5ojIydszcxB2tOuZRX8o6iqemxRSg";

    @GET("search?part=snippet&maxResults=15&key=" + key)
    Call<SearchResponse> searchVideo(@Query("q") String query);
}
