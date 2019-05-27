package jacklin.com.youtubefxc.ui.search;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jacklin.com.youtubefxc.R;
import jacklin.com.youtubefxc.data.YouTubeVideo;
import jacklin.com.youtubefxc.viewmodel.SearchViewModel;

/**
 * A simple {@link Fragment} subclass.
 * ViewModel {@link SearchViewModel}
 */
public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;
    private List<YouTubeVideo> mVideoList = new ArrayList<>();
    private ArrayObjectAdapter mCardsAdapter;
    private ArrayObjectAdapter mRowsAdapter;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }
    FragmentManager fm;
    TextView searchBar;
    CardView searchIcon, clearIcon, spaceIcon, backspaceIcon, shiftIcon;
    View suggest, view;
    FrameLayout mKeyboard;
    AlphabetKeyborad mAlphabet;
    NumberKeyboard mNumber;
    Keyboard mType;

    public enum Keyboard{
        Alphabet, Number
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.search_fragment, container, false);
        findView();

        mAlphabet = AlphabetKeyborad.newInstance();
        mNumber = NumberKeyboard.newInstance();
        if(savedInstanceState == null) {
            fm = getFragmentManager();
            FragmentTransaction fmts = fm.beginTransaction();
            fmts.replace(R.id.keyboard, mAlphabet).commit();
            mType = Keyboard.Alphabet;
        }

        spaceIcon.getChildAt(0).setOnClickListener(v -> {
            if(searchBar.getText() != "Search")
                mViewModel.setQueryString(" ");
        });
        clearIcon.getChildAt(0).setOnClickListener(v -> clearSearchBar());
        shiftIcon.getChildAt(0).setOnClickListener(v -> switchKeyboard());
        searchIcon.getChildAt(0).setOnClickListener(v -> querySearchResult());
        backspaceIcon.getChildAt(0).setOnClickListener(v -> clearSearchBar());

        setOnFocusListener();

        return view;
    }

    private void findView(){
        mKeyboard = view.findViewById(R.id.keyboard);
        suggest = view.findViewById(R.id.view);
        searchBar = view.findViewById(R.id.search_bar);
        searchIcon = view.findViewById(R.id.cardViewSearch);
        clearIcon = view.findViewById(R.id.cardViewClear);
        spaceIcon = view.findViewById(R.id.cardViewSpace);
        backspaceIcon = view.findViewById(R.id.cardViewBackspace);
        shiftIcon = view.findViewById(R.id.cardViewShift);
    }

    private void setOnFocusListener(){
        ViewGroup viewGroup = (ViewGroup) view;
        for (int i=3;i < viewGroup.getChildCount() - 1;i++){
            CardView cardView = (CardView) viewGroup.getChildAt(i);
            TextView textView = (TextView) cardView.getChildAt(0);
            textView.setOnFocusChangeListener((v, hasFocus)->{
                if(hasFocus){
                    cardView.setCardElevation(20);
                }
                else cardView.setCardElevation(0);
            });
        }
        backspaceIcon.getChildAt(0).setOnFocusChangeListener((v, hasFocus)->{
            if(hasFocus){
                ((CardView) v.getParent()).setCardElevation(20);
            }
            else ((CardView) v.getParent()).setCardElevation(0);
        });
    }

    private void switchKeyboard(){
        if(mType == Keyboard.Alphabet){
            fm.beginTransaction().replace(R.id.keyboard, mNumber).commit();
            mType = Keyboard.Number;
        }
        else{
            fm.beginTransaction().replace(R.id.keyboard, mAlphabet).commit();
            mType = Keyboard.Alphabet;
        }
    }

    private void querySearchResult(){

    }

    private void clearSearchBar(){
        mViewModel.setQueryString("");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(SearchViewModel.class);
        mViewModel.getQueryString().observe(getActivity(), (query)->{
            Log.d("getChar", query);
            if(query.equals("")){
                searchBar.setText("Search");
            }
            else
            searchBar.setText(query);
        });
//        mViewModel.getVideoList().observe(getActivity(), (videos) ->{
//            mVideoList = videos;
//            mCardsAdapter.clear();
//            for (YouTubeVideo v : mVideoList){
//                mCardsAdapter.add(v);
//            }
//            mCardsAdapter.notifyArrayItemRangeChanged(0, 1);
//            Log.d("Fragment ViewModel", "notify");
//        });
//        mViewModel.searchVideo("suzy");
        mVideoList = mViewModel.getVideoList().getValue();
    }

}
