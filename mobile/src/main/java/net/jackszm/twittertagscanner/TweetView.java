package net.jackszm.twittertagscanner;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TweetView extends LinearLayout {

    private TextView authorAvatarTextView;
    private TextView authorHandleTextView;
    private TextView authorNameTextView;
    private TextView contentTextView;

    public TweetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_tweet_view, this);

        authorAvatarTextView = ((TextView) findViewById(R.id.tweet_author_avatar));
        authorHandleTextView = ((TextView) findViewById(R.id.tweet_author_handle));
        authorNameTextView = ((TextView) findViewById(R.id.tweet_author_name));
        contentTextView = ((TextView) findViewById(R.id.tweet_content));
    }

    public void bind(Tweet tweet) {
        authorAvatarTextView.setText(tweet.authorAvatar());
        authorHandleTextView.setText(tweet.authorHandle());
        authorNameTextView.setText(tweet.authorDisplayName());
        contentTextView.setText(tweet.content());
    }
}
