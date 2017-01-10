package jackszm.androiddevtweets.api;

import android.content.Context;
import android.content.SharedPreferences;

import jackszm.androiddevtweets.support.Optional;

class AccessTokenStorage {

    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String UNUSED_ACCESS_TOKEN_DEFAULT = null;

    private static final String ACCESS_TOKEN_STORAGE_PREFERENCES = "access_token_storage_preferences";

    private final SharedPreferences preferences;
    private final String accessTokenKey;

    public static AccessTokenStorage newInstance(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(ACCESS_TOKEN_STORAGE_PREFERENCES, Context.MODE_PRIVATE);
        return new AccessTokenStorage(preferences, KEY_ACCESS_TOKEN);
    }

    AccessTokenStorage(SharedPreferences preferences, String accessTokenKey) {
        this.preferences = preferences;
        this.accessTokenKey = accessTokenKey;
    }

    public Optional<String> getCachedAccessToken() {
        if (preferences.contains(accessTokenKey)) {
            String accessToken = preferences.getString(accessTokenKey, UNUSED_ACCESS_TOKEN_DEFAULT);
            return Optional.of(accessToken);
        } else {
            return Optional.absent();
        }
    }

    public void storeAccessToken(String accessToken) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(accessTokenKey, accessToken);
        editor.apply();
    }

    public void invalidateCachedAccessToken() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(accessTokenKey);
        editor.apply();
    }
}
