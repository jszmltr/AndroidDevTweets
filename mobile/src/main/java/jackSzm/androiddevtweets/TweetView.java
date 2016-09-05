package jackszm.androiddevtweets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TweetView extends LinearLayout {

    private ImageView authorAvatarImageView;
    private TextView authorHandleTextView;
    private TextView authorNameTextView;
    private TextView contentTextView;

    private ImageLoader imageLoader;

    public TweetView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_tweet_view, this);

        imageLoader = new ImageLoader();

        authorAvatarImageView = ((ImageView) findViewById(R.id.tweet_author_avatar));
        authorHandleTextView = ((TextView) findViewById(R.id.tweet_author_handle));
        authorNameTextView = ((TextView) findViewById(R.id.tweet_author_name));
        contentTextView = ((TextView) findViewById(R.id.tweet_content));
    }

    public void bind(Tweet tweet) {
        authorHandleTextView.setText(tweet.authorHandle());
        authorNameTextView.setText(tweet.authorDisplayName());
        contentTextView.setText(tweet.content());

        imageLoader.load(authorAvatarImageView, tweet.authorAvatar());
    }
}
