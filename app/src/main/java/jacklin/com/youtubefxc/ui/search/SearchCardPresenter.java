package jacklin.com.youtubefxc.ui.search;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import jacklin.com.youtubefxc.R;
import jacklin.com.youtubefxc.ui.YouTubeCardPresenter;

public class SearchCardPresenter extends YouTubeCardPresenter {

    @Override
    protected void setFocusNavigation(CardViewHolder cardViewHolder) {
        cardViewHolder.view.setOnKeyListener((v, keyCode, event) -> {
            if(event.getAction() == KeyEvent.ACTION_DOWN){
                setDefaultFocus(v, keyCode);
            }
            return false;
        });
    }

    @Override
    public void setDefaultFocus(View v, int keyCode) {
        if(keyCode == KeyEvent.KEYCODE_DPAD_UP){
            if(SuggestListAdapter.UpFromSuggestion)
                v.setNextFocusUpId(R.id.rv_view);
            else
                v.setNextFocusUpId(R.id.keyboard_text_clear);
        }
        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            v.setNextFocusLeftId(R.id.search_btn);
            ((SearchFragment) mFragment).setFocus(SearchFragment.FocusLocation.SearchRow);
            mLeftNav.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        }
    }

    @Override
    public void setPressBack(View v) {

    }
}
