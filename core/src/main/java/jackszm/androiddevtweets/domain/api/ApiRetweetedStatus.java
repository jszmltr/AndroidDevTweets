package jackszm.androiddevtweets.domain.api;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class ApiRetweetedStatus {

    public static JsonAdapter<ApiRetweetedStatus> jsonAdapter(Moshi moshi) {
        return new AutoValue_ApiRetweetedStatus.MoshiJsonAdapter(moshi);
    }

    @Json(name = "text")
    public abstract String text();

    @Json(name = "user")
    public abstract ApiUser user();

}
