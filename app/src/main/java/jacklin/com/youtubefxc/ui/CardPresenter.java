package jacklin.com.youtubefxc.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jacklin.com.youtubefxc.R;

public class CardPresenter extends Presenter {
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        mContext = viewGroup.getContext();
        ImageCardView imageCardView = new ImageCardView(mContext);
        imageCardView.setCardType(ImageCardView.CARD_TYPE_INFO_UNDER);
        imageCardView.setInfoVisibility(BaseCardView.CARD_REGION_VISIBLE_ACTIVATED);
        imageCardView.setInfoAreaBackgroundColor(Color.RED);
        imageCardView.setFocusable(true);
        imageCardView.setFocusableInTouchMode(true);
        imageCardView.setTitleText("test");
        imageCardView.setContentText("testtest");
        imageCardView.setMainImage(mContext.getResources().getDrawable(R.drawable.ic_folder_white_24dp));
        imageCardView.setMainImageDimensions(150, 150);
        return new CardViewHolder(imageCardView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object o) {

    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }

    public class CardViewHolder extends Presenter.ViewHolder{

        private ImageCardView mImageCardView;
        public CardViewHolder(View view) {
            super(view);
            mImageCardView = (ImageCardView) view;
        }

        public ImageCardView getImageCardView() {
            return mImageCardView;
        }
    }
}
