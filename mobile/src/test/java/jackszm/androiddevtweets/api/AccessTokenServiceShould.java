package jackszm.androiddevtweets.api;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import jackszm.androiddevtweets.support.Optional;
import rx.Observable;
import rx.observers.TestObserver;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class AccessTokenServiceShould {

    private static final String CACHED_ACCESS_TOKEN = "dOsk36sp6WMZFVHCLcQ1d6vuvVPBxHR3jIhWh8hDJIfcYJoDA";
    private static final String API_ACCESS_TOKEN = "ZBlRHUeYtUq8w8ptwlTtaOpc%3D4dOsk36sp6WMZFVHCLcQ1d";
    private static final String AUTHORIZATION_KEY = "authorizationSecretKey";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    AccessTokenStorage accessTokenStorage;
    @Mock
    AuthenticationApi authenticationApi;

    private AccessTokenService accessTokenService;

    @Before
    public void setUp() {
        accessTokenService = new AccessTokenService(
                accessTokenStorage,
                authenticationApi,
                Deserializer.newInstance(),
                AUTHORIZATION_KEY
        );
    }

    @Test
    public void returnCachedAccessToken_whenItIsPresentInTheStorage() {
        given(accessTokenStorage.getCachedAccessToken()).willReturn(Optional.of(CACHED_ACCESS_TOKEN));

        TestObserver<String> observer = new TestObserver<>();
        accessTokenService.getAccessToken().subscribe(observer);

        assertThat(observer.getOnNextEvents().get(0)).isEqualTo(CACHED_ACCESS_TOKEN);
    }

    @Test
    public void returnAccessTokenFromApi_whenCachedAccessTokenIsAbsent() {
        given(accessTokenStorage.getCachedAccessToken()).willReturn(Optional.<String>absent());
        given(authenticationApi.getAccessTokenUsing(AUTHORIZATION_KEY)).willReturn(accessTokenResponse());

        TestObserver<String> observer = new TestObserver<>();
        accessTokenService.getAccessToken().subscribe(observer);

        assertThat(observer.getOnNextEvents().get(0)).isEqualTo(API_ACCESS_TOKEN);
    }

    @Test
    public void saveApiAccessToken_whenRetrievedFromTheApi() {
        given(accessTokenStorage.getCachedAccessToken()).willReturn(Optional.<String>absent());
        given(authenticationApi.getAccessTokenUsing(AUTHORIZATION_KEY)).willReturn(accessTokenResponse());

        accessTokenService.getAccessToken().subscribe();

        verify(accessTokenStorage).storeAccessToken(API_ACCESS_TOKEN);
    }

    private Observable<String> accessTokenResponse() {
        return Observable.just("{\"access_token\": \"" + API_ACCESS_TOKEN + "\"}");
    }

}
