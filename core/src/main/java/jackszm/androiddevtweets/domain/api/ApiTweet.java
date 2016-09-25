package jackszm.androiddevtweets.domain.api;

import com.squareup.moshi.Json;

public class ApiTweet {
    @Json(name = "id")
    public String id;

    @Json(name = "retweeted_status")
    public ApiRetweetedStatus retweetedStatus;

    public static class ApiRetweetedStatus {
        @Json(name = "text")
        public String text;

        @Json(name = "user")
        public ApiUser user;

    }

    public static class ApiUser {
        @Json(name = "name")
        public String name;

        @Json(name = "screen_name")
        public String screenName;

        @Json(name = "profile_image_url")
        public String profileImageUrl;

    }

}
