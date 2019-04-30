package jacklin.com.youtubefxc.ui;

import android.content.Context;
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

//        CustomCardView customCardView = new CustomCardView(mContext);
//        customCardView.setNextFocusLeftId(R.id.home_btn);
//        customCardView.setCardType(ImageCardView.CARD_TYPE_INFO_UNDER);
//        customCardView.setInfoVisibility(BaseCardView.CARD_REGION_VISIBLE_ACTIVATED);
//        customCardView.setFocusable(true);
//        customCardView.setFocusableInTouchMode(true);
//        customCardView.setMainImageDimensions(500, 281);
//        customCardView.setInfoAreaBackgroundColor(mContext.getResources().getColor(R.color.background));
//        customCardView.setBackgroundColor(mContext.getResources().getColor(R.color.card_loading));
//
//        ((TextView) customCardView.findViewById(R.id.title_text)).setMaxLines(2);
//        ((TextView) customCardView.findViewById(R.id.title_text)).setTextSize(20);
//        ((TextView) customCardView.findViewById(R.id.content_text)).setLines(2);
//        ((TextView) customCardView.findViewById(R.id.content_text)).setTextSize(14);
//        ((TextView) customCardView.findViewById(R.id.content_text))
//                .setTextColor(mContext.getResources().getColor(R.color.card_content));
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
//        CustomCardView imgCard = cardViewHolder.mImageCardView;

        imgCard.setTitleText(Html.fromHtml(youTubeVideo.getTitle()));
        imgCard.setContentText(youTubeVideo.getChannel() + "\n"
                + Utils.CountConverter(youTubeVideo.getNumber_views()) +" views ‧ "
                + Utils.TimeConverter(youTubeVideo.getTime()) + "ago");

        Glide.with(mContext)
                .asBitmap()
//                .placeholder(mContext.getResources().getDrawable(R.drawable.ic_folder_24dp))
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
//        private CustomCardView mImageCardView;
        public CardViewHolder(View view) {
            super(view);
//            mImageCardView = (CustomCardView) view;
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
