package net.jackszm.twittertagscanner;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.TweetViewHolder> implements MainActivityPresenter.TweetsDisplayer {

    private List<Tweet> tweets = new ArrayList<>();

    @Override
    public TweetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View tweetView = inflater.inflate(R.layout.view_tweet, parent, false);

        return new TweetViewHolder(tweetView);
    }

    @Override
    public void onBindViewHolder(TweetViewHolder holder, int position) {
        Tweet tweet = tweets.get(position);
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return tweets.size();
    }

    @Override
    public void displayTweets(List<Tweet> tweets) {
        this.tweets = tweets;
        notifyDataSetChanged();
    }

    static class TweetViewHolder extends RecyclerView.ViewHolder {

        public TweetViewHolder(View itemView) {
            super(itemView);
        }

        public void bind(Tweet tweet) {
            ((TweetView) itemView).bind(tweet);
        }
    }
}
