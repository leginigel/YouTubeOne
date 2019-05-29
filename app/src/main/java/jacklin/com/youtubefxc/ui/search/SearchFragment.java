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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    private static final String TAG = SearchFragment.class.getSimpleName();
    private SearchViewModel mViewModel;
//    private List<YouTubeVideo> mVideoList = new ArrayList<>();
    private ArrayObjectAdapter mCardsAdapter;
    private ArrayObjectAdapter mRowsAdapter;
    private RecyclerView recyclerView;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }
    FragmentManager fm;
    TextView searchBar;
    CardView searchIcon, clearIcon, spaceIcon, backspaceIcon, shiftIcon;
    View suggest, view;
    FrameLayout mKeyboard, mRow;
    AlphabetKeyborad mAlphabet;
    NumberKeyboard mNumber;
    Keyboard mType;

    SearchRowFragment rowFragment;

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
        rowFragment = SearchRowFragment.newInstance();
        if(savedInstanceState == null) {
            fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.keyboard, mAlphabet).commit();
            mType = Keyboard.Alphabet;

            fm.beginTransaction().replace(R.id.search_row, rowFragment).commit();
        }

        SuggestListAdapter suggestListAdapter = new SuggestListAdapter();
        recyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(suggestListAdapter);

        spaceIcon.getChildAt(0).setOnClickListener(v -> {
            if(searchBar.getText() != "Search")
                mViewModel.setQueryString(" ");
        });
        clearIcon.getChildAt(0).setOnClickListener(v -> clearSearchBar());
        shiftIcon.getChildAt(0).setOnClickListener(v -> switchKeyboard());
        searchIcon.getChildAt(0).setOnClickListener(v -> querySearchResult(searchBar.getText().toString()));
        backspaceIcon.getChildAt(0).setOnClickListener(v -> searchGoogleSuggestion("aladdin"));

        setOnFocusListener();

        return view;
    }

    private void findView(){
        mKeyboard = view.findViewById(R.id.keyboard);
        mRow = view.findViewById(R.id.search_row);
        recyclerView = view.findViewById(R.id.view);
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
        for (int i = 4;i < viewGroup.getChildCount() - 1;i++){
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

    private void querySearchResult(String query){
        Log.d(TAG,"querySearchResult");
        mViewModel.searchRx("nba");
    }

    private void searchGoogleSuggestion(String query){
        mViewModel.searchSuggestion(query);
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

//        mVideoList = mViewModel.getVideoList().getValue();
    }

}
