package jackszm.androiddevtweets.tweets;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jackszm.androiddevtweets.domain.Tweet;
import jackszm.androiddevtweets.domain.api.ApiTweet;
import jackszm.androiddevtweets.api.Deserializer;
import jackszm.androiddevtweets.api.TwitterApi;
import rx.Observable;
import rx.functions.Func1;

public class TweetsService {

    private final TwitterApi twitterApi;
    private final Deserializer deserializer;

    public static TweetsService newInstance() {
        TwitterApi twitterApi = TwitterApi.newInstance();
        Deserializer deserializer = Deserializer.newInstance();
        return new TweetsService(twitterApi, deserializer);
    }

    TweetsService(TwitterApi twitterApi, Deserializer deserializer) {
        this.twitterApi = twitterApi;
        this.deserializer = deserializer;
    }

    public Observable<List<Tweet>> loadTweets(String accessToken) {
        return twitterApi.getAndroidDevTweets(accessToken)
                .map(deserialize())
                .map(marshal());
    }

    private Func1<List<ApiTweet>, List<Tweet>> marshal() {
        return new Func1<List<ApiTweet>, List<Tweet>>() {
            @Override
            public List<Tweet> call(List<ApiTweet> apiTweets) {
                List<Tweet> tweets = new ArrayList<>(apiTweets.size());
                for (ApiTweet apiTweet : apiTweets) {
                    Tweet tweet = Tweet.create(
                            apiTweet.id,
                            apiTweet.retweetedStatus.text,
                            apiTweet.retweetedStatus.user.name,
                            apiTweet.retweetedStatus.user.screenName,
                            URI.create(apiTweet.retweetedStatus.user.profileImageUrl)
                    );
                    tweets.add(tweet);
                }
                return tweets;
            }
        };
    }

    private Func1<String, List<ApiTweet>> deserialize() {
        return new Func1<String, List<ApiTweet>>() {
            @Override
            public List<ApiTweet> call(String json) {
                return deserializer.deserializeList(json, ApiTweet.class);
            }
        };
    }

}
