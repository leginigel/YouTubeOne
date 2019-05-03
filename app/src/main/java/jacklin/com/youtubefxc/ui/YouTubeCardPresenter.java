package jacklin.com.youtubefxc.ui;

import android.content.Context;
import android.graphics.Color;
import android.support.v17.leanback.widget.BaseCardView;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import jacklin.com.youtubefxc.R;
import jacklin.com.youtubefxc.data.YouTubeVideo;
import jacklin.com.youtubefxc.util.Utils;

public class YouTubeCardPresenter extends Presenter {
    private Context mContext;
    CardViewHolder mCardViewHolder;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
        Log.d("YouTubeCardPresenter" , "onCreateViewHolder");
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
        mCardViewHolder = new CardViewHolder(view);
        return mCardViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object o) {
        Log.d("YouTubeCardPresenter" , "onBindViewHolder");
        CardViewHolder cardViewHolder = (CardViewHolder) viewHolder;
        YouTubeVideo youTubeVideo = (YouTubeVideo) o;

        TextView timeStamp = cardViewHolder.mTimeStamp;
        if(youTubeVideo.getDuration() == null){
//            timeStamp.setVisibility(View.GONE);
        }
        else{
            timeStamp.setText(Utils.DurationConverter(youTubeVideo.getDuration()));
        }

        ImageCardView imgCard = cardViewHolder.mImageCardView;
//        TextView title = cardViewHolder.mTitle;
//        TextView content = cardViewHolder.mContent;
//        CustomCardView imgCard = cardViewHolder.mImageCardView;

        if(youTubeVideo.getId() != null){
            Log.d("YouTubeCardPresenter" , "!cardViewHolder.isLoading");
            cardViewHolder.setCardInfo();
        }
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
        private TextView mTitle;
        private TextView mContent;
//        private CustomCardView mImageCardView;
        public CardViewHolder(View view) {
            super(view);
//            mImageCardView = (CustomCardView) view;
            mImageCardView = (ImageCardView) view.findViewById(R.id.img_card_view);
            mTimeStamp = view.findViewById(R.id.img_card_time_stamp);
            mTitle = (TextView) mImageCardView.findViewById(R.id.title_text);
            mContent = (TextView) mImageCardView.findViewById(R.id.content_text);

            mImageCardView.setNextFocusLeftId(R.id.home_btn);
            mImageCardView.setCardType(ImageCardView.CARD_TYPE_INFO_UNDER);
            mImageCardView.setInfoVisibility(BaseCardView.CARD_REGION_VISIBLE_ACTIVATED);
            mImageCardView.setFocusable(true);
            mImageCardView.setFocusableInTouchMode(true);
            mImageCardView.setMainImageDimensions(500, 281);
            mImageCardView.setInfoAreaBackgroundColor(mContext.getResources().getColor(R.color.background));
            mImageCardView.setBackgroundColor(mContext.getResources().getColor(R.color.card_loading));

            setLoadingCardInfo();
        }

        private void setLoadingCardInfo(){
            Log.d("YouTubeCardPresenter" , "setLoadingCardInfo");
//            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mTitle.setMaxLines(2);
            mTitle.setTextColor(mContext.getResources().getColor(R.color.card_loading));
            mTitle.setBackground(mContext.getDrawable(R.drawable.card_text_bg));

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.setMarginEnd(100);
            params.addRule(RelativeLayout.BELOW, mTitle.getId());
            mContent.setLayoutParams(params);

            mContent.setBackground(mContext.getDrawable(R.drawable.card_text_bg));
//            mContent.setTextSize(mTitle.getTextSize());
            mContent.setTextColor(mContext.getResources().getColor(R.color.card_loading));

        }

        public void setCardInfo(){
            Log.d("YouTubeCardPresenter" , "setCardInfo");
            mTitle.setTextSize(20);
            mTitle.setTextColor(Color.WHITE);
            mTitle.setBackgroundColor(Color.TRANSPARENT);

            mContent.setLines(2);
            mContent.setTextSize(12);
            mContent.setTextColor(mContext.getResources().getColor(R.color.card_content));
            mContent.setBackgroundColor(Color.TRANSPARENT);
        }

        public ImageCardView getImageCardView() {
            return mImageCardView;
        }
    }

    public CardViewHolder getCardViewHolder() {
        return mCardViewHolder;
    }
}
