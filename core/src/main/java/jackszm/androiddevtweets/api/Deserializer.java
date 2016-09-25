package jackszm.androiddevtweets.api;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Deserializer {

    private final Moshi moshi;

    public static Deserializer newInstance() {
        Moshi moshi = new Moshi.Builder().build();
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

    static class DeserializerException extends RuntimeException {

        DeserializerException(Throwable cause) {
            super(cause);
        }
    }
}
