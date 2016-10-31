package jackszm.androiddevtweets.tweets;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jackszm.androiddevtweets.domain.Tweet;
import jackszm.androiddevtweets.domain.api.ApiTweet;

class TweetsConverter {

    List<Tweet> convert(List<ApiTweet> apiTweets) {
        List<Tweet> tweets = new ArrayList<>(apiTweets.size());
        for (ApiTweet apiTweet : apiTweets) {
            tweets.add(createTweet(apiTweet));
        }
        return tweets;
    }

    private Tweet createTweet(ApiTweet apiTweet) {
        return Tweet.create(
                apiTweet.id(),
                apiTweet.retweetedStatus().text(),
                apiTweet.retweetedStatus().user().name(),
                apiTweet.retweetedStatus().user().screenName(),
                URI.create(apiTweet.retweetedStatus().user().profileImageUrl())
        );
    }
}
