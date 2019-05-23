package jacklin.com.youtubefxc.ui.search;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jacklin.com.youtubefxc.R;

public class AlphabetKeyborad extends Fragment {
    public static AlphabetKeyborad newInstance(){
        return new AlphabetKeyborad();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alphabet_keyboard, container, false);
        return view;
    }
}
