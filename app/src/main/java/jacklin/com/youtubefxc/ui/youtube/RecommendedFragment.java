package jacklin.com.youtubefxc.ui.youtube;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

import jacklin.com.youtubefxc.data.YouTubeVideo;
import jacklin.com.youtubefxc.viewmodel.YoutubeViewModel;

/**
 * {@link YoutubeRowFragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class RecommendedFragment extends YoutubeRowFragment {

    public static RecommendedFragment newInstance() {
        return new RecommendedFragment();
    }

    private static final String TAG = RecommendedFragment.class.getSimpleName();
    private YoutubeViewModel mViewModel;
    private Map<String, List<YouTubeVideo>> mRecommendedChannel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG + " ViewModel", "onActivityCreated");
        mViewModel = ViewModelProviders.of(getActivity()).get(YoutubeViewModel.class);
        mViewModel.getRecommendedChannelList().observe(getActivity(), (channels)->{
            Log.i(TAG + " ViewModel", "Recommended Observe ");
            mRecommendedChannel = channels;
            setRows(mRecommendedChannel);
        });

//        mViewModel.playlist(recommend_playlist_url);
//        mRecommendedChannel = mViewModel.getRecommendedChannelList().getValue();
    }
}
