package lee.ashlyn.hudlu;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;

import lee.ashlyn.hudlu.lee.ashlyn.hudlu.models.MashableNewsItem;

/**
 * Created by ashlyn.lee on 3/8/16.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<MashableNewsItem> mListData;
    private OnAdapterInteractionListener mListener;
    private RequestQueue mRequestQueue;

    public MyAdapter(Context context, List<MashableNewsItem> data) {
        mListener = (OnAdapterInteractionListener) context;
        mListData = data;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        MashableNewsItem item = mListData.get(position);
        holder.mTitleTextView.setText(item.title);
        holder.mAuthorTextView.setText(item.author);

        ImageRequest request = new ImageRequest(item.feature_image,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        holder.mImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, ImageView.ScaleType.FIT_XY, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Request Error", "Image did not load");
                    }
                });

        mRequestQueue.add(request);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(v, position);
            }
        });

        Context context = holder.itemView.getContext();
        boolean isFavorite = FavoriteUtil.isFavorite(context, item);
        if (isFavorite) {
            holder.mFavoriteButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
            holder.mFavoriteButton.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        } else {
            holder.mFavoriteButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            holder.mFavoriteButton.setTextColor(ContextCompat.getColor(context, R.color.colorBlack));
        }

        holder.mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(v, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView mTitleTextView;
        TextView mAuthorTextView;
        ImageView mImageView;
        Button mFavoriteButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.item_title);
            mAuthorTextView = (TextView) itemView.findViewById(R.id.item_author);
            mImageView = (ImageView) itemView.findViewById(R.id.item_image);
            mFavoriteButton = (Button) itemView.findViewById(R.id.favorite_button);
        }
    }

    public interface OnAdapterInteractionListener {
        void onItemClicked(View view, int position);
    }
}
