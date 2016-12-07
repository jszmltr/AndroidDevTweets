package jackszm.androiddevtweets.api;

import android.content.SharedPreferences;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import jackszm.androiddevtweets.support.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;

public class AccessTokenStorageShould {

    private static final String ACCESS_TOKEN_KEY = "access_token_key";
    private static final String ACCESS_TOKEN = "fjasdkfjiqrewuqmcnvbfdwerjdvnafvnckjafequi";

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    SharedPreferences preferences;

    private AccessTokenStorage accessTokenStorage;

    @Before
    public void setUp() {
        accessTokenStorage = new AccessTokenStorage(preferences, ACCESS_TOKEN_KEY);
    }

    @Test
    public void returnCachedAccessToken_whenAccessTokenIsPresentInPreferences() {
        given(preferences.contains(ACCESS_TOKEN_KEY)).willReturn(true);
        given(preferences.getString(eq(ACCESS_TOKEN_KEY), any(String.class))).willReturn(ACCESS_TOKEN);

        Optional<String> accessToken = accessTokenStorage.getCachedAccessToken();

        assertThat(accessToken.isPresent()).isTrue();
        assertThat(accessToken.get()).isEqualTo(ACCESS_TOKEN);
    }

    @Test
    public void returnNoAccessToken_whenAccessTokenIsMissingInPreferences() {
        given(preferences.contains(ACCESS_TOKEN_KEY)).willReturn(false);

        Optional<String> accessToken = accessTokenStorage.getCachedAccessToken();

        assertThat(accessToken.isAbsent()).isTrue();
    }

    @Test
    public void storeAccessTokenInPreferences() {
        SharedPreferences.Editor editor = mock(SharedPreferences.Editor.class);
        given(preferences.edit()).willReturn(editor);
        given(editor.putString(anyString(), anyString())).willReturn(editor);

        accessTokenStorage.storeAccessToken(ACCESS_TOKEN);

        InOrder inOrder = inOrder(preferences, editor);
        inOrder.verify(preferences).edit();
        inOrder.verify(editor).putString(ACCESS_TOKEN_KEY, ACCESS_TOKEN);
        inOrder.verify(editor).apply();
        inOrder.verifyNoMoreInteractions();
    }

}
