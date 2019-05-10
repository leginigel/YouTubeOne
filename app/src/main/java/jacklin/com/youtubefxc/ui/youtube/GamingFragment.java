package jacklin.com.youtubefxc.ui.youtube;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.List;
import java.util.Map;

import jacklin.com.youtubefxc.data.YouTubeVideo;
import jacklin.com.youtubefxc.viewmodel.YoutubeViewModel;

/**
 * {@link YoutubeRowFragment} subclass.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class GamingFragment extends YoutubeRowFragment {
    public static GamingFragment newInstance() {
        return new GamingFragment();
    }

    private static final String TAG = GamingFragment.class.getSimpleName();
    private YoutubeViewModel mViewModel;
    private Map<String, List<YouTubeVideo>> mGamingChannel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG + " ViewModel", "onActivityCreated");
        mViewModel = ViewModelProviders.of(getActivity()).get(YoutubeViewModel.class);
        mViewModel.getGamingChannelList().observe(getActivity(), (channels)->{
            Log.i(TAG + " ViewModel", "GamingFragment Observe ");
            mGamingChannel = channels;
            setRows(mGamingChannel);
        });

    }
}
