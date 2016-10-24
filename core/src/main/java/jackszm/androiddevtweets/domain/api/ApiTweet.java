package jackszm.androiddevtweets.domain.api;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class ApiTweet {

    public static ApiTweet create(String id, ApiRetweetedStatus apiRetweetedStatus) {
        return new AutoValue_ApiTweet(id, apiRetweetedStatus);
    }

    public static JsonAdapter<ApiTweet> jsonAdapter(Moshi moshi) {
        return new AutoValue_ApiTweet.MoshiJsonAdapter(moshi);
    }

    @Json(name = "id")
    public abstract String id();

    @Json(name = "retweeted_status")
    public abstract ApiRetweetedStatus retweetedStatus();

}
