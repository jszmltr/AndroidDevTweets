package jackszm.androiddevtweets.api;

import android.content.Context;
import android.content.res.Resources;

import jackszm.androiddevtweets.R;
import jackszm.androiddevtweets.domain.api.ApiToken;
import jackszm.androiddevtweets.support.Optional;

import static jackszm.androiddevtweets.api.TwitterApi.URL_POST_TOKEN;

public class AccessTokenService implements AuthenticationService {

    private final AccessTokenStorage accessTokenStorage;
    private final Resources resources;
    private final RequestExecutor requestExecutor;
    private final Deserializer deserializer;

    public static AccessTokenService newInstance(Context context) {
        AccessTokenStorage accessTokenStorage = AccessTokenStorage.newInstance(context);
        Resources resources = context.getResources();
        RequestExecutor requestExecutor = RequestExecutor.newInstance();
        Deserializer deserializer = Deserializer.newInstance();
        return new AccessTokenService(accessTokenStorage, resources, requestExecutor, deserializer);
    }

    AccessTokenService(AccessTokenStorage accessTokenStorage, Resources resources, RequestExecutor requestExecutor, Deserializer deserializer) {
        this.accessTokenStorage = accessTokenStorage;
        this.resources = resources;
        this.requestExecutor = requestExecutor;
        this.deserializer = deserializer;
    }

    @Override
    public String getAccessToken() {
        Optional<String> cachedAccessToken = accessTokenStorage.getCachedAccessToken();
        if (cachedAccessToken.isPresent()) {
            return cachedAccessToken.get();
        }

        String authorizationKey = resources.getString(R.string.twitter_authorization_key);

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
