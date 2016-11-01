package jackszm.androiddevtweets.api;

import android.content.Context;

import jackszm.androiddevtweets.R;
import jackszm.androiddevtweets.domain.api.ApiToken;
import jackszm.androiddevtweets.support.Optional;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class AccessTokenService implements AuthenticationService {

    private final AccessTokenStorage accessTokenStorage;
    private final Deserializer deserializer;
    private final AuthenticationApi authenticationApi;
    private final String authorizationKey;

    public static AccessTokenService newInstance(Context context) {
        AccessTokenStorage accessTokenStorage = AccessTokenStorage.newInstance(context);

        Deserializer deserializer = Deserializer.newInstance();
        String authorizationKey = context.getResources().getString(R.string.twitter_authorization_key);
        AuthenticationApi authenticationApi = AuthenticationApi.newInstance();
        return new AccessTokenService(accessTokenStorage, authenticationApi, deserializer, authorizationKey);
    }

    AccessTokenService(AccessTokenStorage accessTokenStorage, AuthenticationApi authenticationApi, Deserializer deserializer, String authorizationKey) {
        this.accessTokenStorage = accessTokenStorage;
        this.authenticationApi = authenticationApi;
        this.deserializer = deserializer;
        this.authorizationKey = authorizationKey;
    }

    @Override
    public Observable<String> getAccessToken() {
        Optional<String> cachedAccessToken = accessTokenStorage.getCachedAccessToken();
        if (cachedAccessToken.isPresent()) {
            return Observable.just(cachedAccessToken.get());
        } else {
            return apiAccessToken();
        }
    }

    private Observable<String> apiAccessToken() {
        return authenticationApi.getAccessTokenUsing(authorizationKey)
                .map(toAccessToken())
                .doOnNext(store());
    }

    private Func1<String, String> toAccessToken() {
        return new Func1<String, String>() {
            @Override
            public String call(String response) {
                ApiToken apiToken = deserializer.deserialize(response, ApiToken.class);
                return apiToken.accessToken();
            }
        };
    }

    private Action1<String> store() {
        return new Action1<String>() {
            @Override
            public void call(String accessToken) {
                accessTokenStorage.storeAccessToken(accessToken);
            }
        };
    }

}
