package jacklin.com.youtubefxc.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import jacklin.com.youtubefxc.R;
import jacklin.com.youtubefxc.viewmodel.SearchViewModel;

/**
 * A simple {@link Fragment} subclass.
 * ViewModel {@link SearchViewModel}
 */
public class AlphabetKeyborad extends Fragment {

    public static AlphabetKeyborad newInstance(){
        return new AlphabetKeyborad();
    }
    private SearchViewModel mViewModel;
    public static int OutId_Down, OutId_Left;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alphabet_keyboard, container, false);
        ViewGroup viewGroup = (ViewGroup) view;
        Fragment fragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.container_home);
        RecyclerView recyclerView = fragment.getView().findViewById(R.id.rv_view);

        for (int i = 0;i < viewGroup.getChildCount();i++) {
            CardView cardView = (CardView) viewGroup.getChildAt(i);
            TextView textView = (TextView) cardView.getChildAt(0);
            textView.setOnFocusChangeListener((v, hasFocus)->{
                if(hasFocus){
                    cardView.setCardElevation(20);
                }
                else{
                    cardView.setCardElevation(0);
                }
            });
            textView.setOnClickListener(v -> mViewModel
                    .setQueryString(((String) ((TextView) v).getText()).toLowerCase(), false));
            if(i == 0) {
                OutId_Left = textView.getId();
            }
            if(i > 20) {
                textView.setNextFocusDownId(R.id.keyboard_text_space);
            }
            if(i == 20 || i == 27)
                textView.setNextFocusRightId(textView.getId());

            int finalI = i;
            textView.setOnKeyListener((v, keyCode, event) -> {
                if(event.getAction() == KeyEvent.ACTION_DOWN) {
                    if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        if(finalI % 7 == 0) {
                            OutId_Left = v.getId();
                        }
//                            recyclerView.requestFocus();
//                            ((SuggestListAdapter) recyclerView.getAdapter()).resize(5);
//                            recyclerView.scrollToPosition(0);
//                            recyclerView.getChildAt(0).requestFocus();

                        TextView t = recyclerView.getLayoutManager().findViewByPosition(0).findViewById(R.id.suggest_text);
                        Log.d("CHECK", t.getText().toString());
                        CardView c = recyclerView.getLayoutManager().findViewByPosition(0).findViewById(R.id.suggest_card);
//                            ((ViewGroup) view).setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
//                            c.requestFocus();
//                            recyclerView.restoreDefaultFocus();
//                            recyclerView.getLayoutManager().findViewByPosition(0).requestFocus();
//                            recyclerView.getAdapter().;
                    }
                    else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        if(finalI > 20) {
                            Log.d("Check", textView.getText().toString());
                            OutId_Down = v.getId();
                        }
                    }
                }
                return false;
            });
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(SearchViewModel.class);
    }
}
