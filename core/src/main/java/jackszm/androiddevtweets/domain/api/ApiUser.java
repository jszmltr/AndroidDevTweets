package jackszm.androiddevtweets.domain.api;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class ApiUser {

    public static JsonAdapter<ApiUser> jsonAdapter(Moshi moshi) {
        return new AutoValue_ApiUser.MoshiJsonAdapter(moshi);
    }

    @Json(name = "name")
    public abstract String name();

    @Json(name = "screen_name")
    public abstract String screenName();

    @Json(name = "profile_image_url")
    public abstract String profileImageUrl();

}
