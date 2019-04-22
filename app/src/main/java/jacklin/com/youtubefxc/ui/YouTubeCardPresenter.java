package jacklin.com.youtubefxc.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import jacklin.com.youtubefxc.R;
import jacklin.com.youtubefxc.data.YouTubeVideo;

public class YouTubeCardPresenter extends Presenter {
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        mContext = viewGroup.getContext();
        ImageCardView imageCardView = new ImageCardView(mContext);
//        imageCardView.getNextFocusUpId()
        imageCardView.setNextFocusLeftId(R.id.home_btn);
        imageCardView.setCardType(ImageCardView.CARD_TYPE_INFO_UNDER);
        imageCardView.setInfoVisibility(BaseCardView.CARD_REGION_VISIBLE_ACTIVATED);
//        imageCardView.setInfoAreaBackgroundColor(mContext.getResources().getColor(R.color.background));
        imageCardView.setFocusable(true);
        imageCardView.setFocusableInTouchMode(true);
        imageCardView.setMainImageDimensions(556, 312);

        ((TextView) imageCardView.findViewById(R.id.title_text)).setMaxLines(2);
        ((TextView) imageCardView.findViewById(R.id.content_text)).setLines(2);
        ((TextView) imageCardView.findViewById(R.id.content_text))
                .setTextColor(mContext.getResources().getColor(R.color.card_content));

        return new CardViewHolder(imageCardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object o) {
        ImageCardView imgCard = (ImageCardView) viewHolder.view;
        YouTubeVideo youTubeVideo = (YouTubeVideo) o;
        imgCard.setTitleText(Html.fromHtml(youTubeVideo.getTitle()));
        imgCard.setContentText(youTubeVideo.getChannel() + "\n"
                + youTubeVideo.getNumber_views() +" views ‧ "
                + youTubeVideo.getTime());

        Glide.with(mContext)
                .asBitmap()
                .load(mContext.getResources().getDrawable(R.drawable.ic_folder_white_24dp))
                .load("https://i.ytimg.com/vi/"+ youTubeVideo.getId() +"/0.jpg")
                .into(imgCard.getMainImageView());

    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
        // Nothing to unbind for TextView, but if this viewHolder had
        // allocated bitmaps, they can be released here.

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
