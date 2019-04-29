package jacklin.com.youtubefxc.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import jacklin.com.youtubefxc.R;
import jacklin.com.youtubefxc.data.YouTubeVideo;
import jacklin.com.youtubefxc.util.Utils;

public class YouTubeCardPresenter extends Presenter {
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        mContext = viewGroup.getContext();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.card_image, viewGroup, false);

        ImageCardView imageCardView = view.findViewById(R.id.img_card_view);
//        ImageCardView imageCardView = new ImageCardView(mContext);

        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object o) {
        CardViewHolder cardViewHolder = (CardViewHolder) viewHolder;
        YouTubeVideo youTubeVideo = (YouTubeVideo) o;

        TextView timeStamp = cardViewHolder.mTimeStamp;
        if(youTubeVideo.getDuration() == null){
            timeStamp.setVisibility(View.GONE);
        }
        else{
            timeStamp.setText(youTubeVideo.getDuration());
        }

        ImageCardView imgCard = cardViewHolder.mImageCardView;

        imgCard.setTitleText(Html.fromHtml(youTubeVideo.getTitle()));
        imgCard.setContentText(youTubeVideo.getChannel() + "\n"
                + Utils.CountConverter(youTubeVideo.getNumber_views()) +" views ‧ "
                + Utils.TimeConverter(youTubeVideo.getTime()) + "ago");

        Glide.with(mContext)
                .asBitmap()
//                .placeholder(mContext.getResources().getDrawable(R.drawable.ic_folder_white_24dp))
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
        private TextView mTimeStamp;
        public CardViewHolder(View view) {
            super(view);
//            mImageCardView = (ImageCardView) view;
            mImageCardView = (ImageCardView) view.findViewById(R.id.img_card_view);
            mTimeStamp = view.findViewById(R.id.img_card_time_stamp);

            mImageCardView.setNextFocusLeftId(R.id.home_btn);
            mImageCardView.setCardType(ImageCardView.CARD_TYPE_INFO_UNDER);
            mImageCardView.setInfoVisibility(BaseCardView.CARD_REGION_VISIBLE_ACTIVATED);
            mImageCardView.setFocusable(true);
            mImageCardView.setFocusableInTouchMode(true);
            mImageCardView.setMainImageDimensions(500, 281);
            mImageCardView.setInfoAreaBackgroundColor(mContext.getResources().getColor(R.color.background));
            mImageCardView.setBackgroundColor(mContext.getResources().getColor(R.color.card_loading));

            ((TextView) mImageCardView.findViewById(R.id.title_text)).setMaxLines(2);
            ((TextView) mImageCardView.findViewById(R.id.title_text)).setTextSize(20);
            ((TextView) mImageCardView.findViewById(R.id.content_text)).setLines(2);
            ((TextView) mImageCardView.findViewById(R.id.content_text)).setTextSize(14);
            ((TextView) mImageCardView.findViewById(R.id.content_text))
                    .setTextColor(mContext.getResources().getColor(R.color.card_content));
        }

        public ImageCardView getImageCardView() {
            return mImageCardView;
        }
    }
}
