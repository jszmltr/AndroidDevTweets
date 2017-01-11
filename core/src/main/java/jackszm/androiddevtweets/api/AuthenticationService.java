package jackszm.androiddevtweets.api;

import rx.Observable;

public interface AuthenticationService {

    Observable<String> retrieveAccessToken();

    void invalidateAccessToken();
}
