package jackszm.androiddevtweets.api;

import java.util.concurrent.Callable;

import jackszm.androiddevtweets.api.RequestExecutor.HttpException;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

public class AuthenticationRetryRule {

    private static final int RETRY_COUNT = 1;
    private static final int RETRY_COUNT_PLUS_FINAL_ERROR_EMISSION = RETRY_COUNT + 1;

    private final AuthenticationService authenticationService;

    public AuthenticationRetryRule(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public Func1<Observable<? extends Throwable>, Observable<?>> rule() {
        return new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> attempt) {
                return attempt.zipWith(retryCounts(), tick()).flatMap(checkForAuthenticationErrors());
            }
        };
    }

    private Observable<Integer> retryCounts() {
        return Observable.range(1, RETRY_COUNT_PLUS_FINAL_ERROR_EMISSION);
    }

    private Func2<Throwable, Integer, Throwable> tick() {
        return new Func2<Throwable, Integer, Throwable>() {
            @Override
            public Throwable call(Throwable throwable, Integer integer) {
                return throwable;
            }
        };
    }

    private Func1<Throwable, Observable<?>> checkForAuthenticationErrors() {
        return new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable throwable) {
                if (isUnauthorized(throwable)) {
                    return retry();
                } else {
                    return doNotRetry(throwable);
                }
            }
        };
    }

    private Observable<Void> retry() {
        authenticationService.invalidateAccessToken();
        return Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                return null;
            }
        });
    }

    private Observable<Throwable> doNotRetry(Throwable throwable) {
        return Observable.error(throwable);
    }

    private boolean isUnauthorized(Throwable throwable) {
        return throwable instanceof HttpException && ((HttpException) throwable).httpCode() == HTTP_UNAUTHORIZED;
    }

}
