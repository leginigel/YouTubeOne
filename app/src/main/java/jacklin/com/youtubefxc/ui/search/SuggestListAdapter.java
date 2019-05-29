package jacklin.com.youtubefxc.ui.search;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import jacklin.com.youtubefxc.R;

public class SuggestListAdapter extends RecyclerView.Adapter<SuggestListAdapter.ViewHolder> {

    Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.suggest_viewholder, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
//        Log.d("onBindViewHolder", "" + i);
        viewHolder.textView.setText("CHECK "+ i);
    }

    @Override
    public int getItemCount() {
        return 10;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.suggest_card);
            cardView.setOnFocusChangeListener((v, focus)->{
                if(focus) cardView.setCardElevation(20);
                else cardView.setCardElevation(0);
            });
            imageView = itemView.findViewById(R.id.img);
//            imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_youtube_color_icon));
            textView = itemView.findViewById(R.id.suggest_text);
        }
    }
}
