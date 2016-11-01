package jackszm.androiddevtweets.api;

import android.content.Context;

public class HttpRequestExecutor {

    private final AccessTokenStorage accessTokenStorage;

    public static HttpRequestExecutor newInstance(Context context) {

        return new HttpRequestExecutor(null);
    }

    HttpRequestExecutor(AccessTokenStorage accessTokenStorage) {
        this.accessTokenStorage = accessTokenStorage;
    }

}
