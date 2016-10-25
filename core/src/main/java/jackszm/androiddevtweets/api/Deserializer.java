package jackszm.androiddevtweets.api;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import jackszm.androiddevtweets.domain.api.ApiRetweetedStatus;
import jackszm.androiddevtweets.domain.api.ApiTweet;
import jackszm.androiddevtweets.domain.api.ApiUser;

public class Deserializer {

    private final Moshi moshi;

    public static Deserializer newInstance() {
        Moshi moshi = new Moshi.Builder()
                .add(new AutoValueAdapterFactory())
                .build();
        return new Deserializer(moshi);
    }

    public Deserializer(Moshi moshi) {
        this.moshi = moshi;
    }

    public <T> List<T> deserializeList(String json, Class<T> klass) {
        try {
            Type listMyData = Types.newParameterizedType(List.class, klass);
            JsonAdapter<List<T>> jsonAdapter = moshi.adapter(listMyData);
            return jsonAdapter.fromJson(json);
        } catch (IOException exception) {
            throw new DeserializerException(exception);
        }
    }

    private static class DeserializerException extends RuntimeException {

        DeserializerException(Throwable cause) {
            super(cause);
        }
    }

    private static final class AutoValueAdapterFactory implements JsonAdapter.Factory {
        @Override
        public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations, Moshi moshi) {
            if (type.equals(ApiRetweetedStatus.class)) {
                return ApiRetweetedStatus.jsonAdapter(moshi);
            } else if (type.equals(ApiTweet.class)) {
                return ApiTweet.jsonAdapter(moshi);
            } else if (type.equals(ApiUser.class)) {
                return ApiUser.jsonAdapter(moshi);
            } else {
                return null;
            }
        }
    }

}
