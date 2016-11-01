package jackszm.androiddevtweets.domain.api;

import com.google.auto.value.AutoValue;
import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

@AutoValue
public abstract class ApiToken {

    public static JsonAdapter<ApiToken> jsonAdapter(Moshi moshi) {
        return new AutoValue_ApiToken.MoshiJsonAdapter(moshi);
    }

    @Json(name = "access_token")
    public abstract String accessToken();

}
