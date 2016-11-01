package jackszm.androiddevtweets.api;

import android.content.Context;

import jackszm.androiddevtweets.R;
import jackszm.androiddevtweets.domain.api.ApiToken;
import jackszm.androiddevtweets.support.Optional;

import static jackszm.androiddevtweets.api.TwitterApi.URL_POST_TOKEN;

public class AccessTokenService implements AuthenticationService {

    private final AccessTokenStorage accessTokenStorage;
    private final RequestExecutor requestExecutor;
    private final Deserializer deserializer;
    private final String authorizationKey;

    public static AccessTokenService newInstance(Context context) {
        AccessTokenStorage accessTokenStorage = AccessTokenStorage.newInstance(context);
        RequestExecutor requestExecutor = RequestExecutor.newInstance();
        Deserializer deserializer = Deserializer.newInstance();
        String authorizationKey = context.getResources().getString(R.string.twitter_authorization_key);
        return new AccessTokenService(accessTokenStorage, requestExecutor, deserializer, authorizationKey);
    }

    AccessTokenService(AccessTokenStorage accessTokenStorage, RequestExecutor requestExecutor, Deserializer deserializer, String authorizationKey) {
        this.accessTokenStorage = accessTokenStorage;
        this.requestExecutor = requestExecutor;
        this.deserializer = deserializer;
        this.authorizationKey = authorizationKey;
    }

    @Override
    public String getAccessToken() {
        Optional<String> cachedAccessToken = accessTokenStorage.getCachedAccessToken();
        if (cachedAccessToken.isPresent()) {
            return cachedAccessToken.get();
        }

        Request request = Request.builder(TwitterApi.BASE_URL)
                .path(URL_POST_TOKEN)
                .basicAuthorization(authorizationKey)
                .build();

        String response = requestExecutor.executeRequest(request);
        ApiToken apiToken = deserializer.deserialize(response, ApiToken.class);
        String accessToken = apiToken.accessToken();
        accessTokenStorage.storeAccessToken(accessToken);
        return accessToken;
    }
}
