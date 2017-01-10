package jackszm.androiddevtweets.api;

import com.google.auto.value.AutoValue;

import java.util.concurrent.Callable;

import jackszm.androiddevtweets.api.RequestExecutor.HttpException;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;

public class AuthenticationInterceptor {

    private static final int RETRY_COUNT = 1;
    private static final int RETRY_COUNT_PLUS_FINAL_ERROR_EMISSION = RETRY_COUNT + 1;

    private final AuthenticationService authenticationService;

    public AuthenticationInterceptor(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public Func1<Observable<? extends Throwable>, Observable<?>> retryRule() {
        return new Func1<Observable<? extends Throwable>, Observable<?>>() {
            @Override
            public Observable<?> call(Observable<? extends Throwable> attempt) {
                return attempt.zipWith(retryCounts(), tick()).flatMap(checkForAuthenticationErrors());
            }
        };
    }

    private Observable<Integer> retryCounts() {
        return Observable.range(0, RETRY_COUNT_PLUS_FINAL_ERROR_EMISSION);
    }

    private Func2<Throwable, Integer, RetryMetadata> tick() {
        return new Func2<Throwable, Integer, RetryMetadata>() {
            @Override
            public RetryMetadata call(Throwable throwable, Integer integer) {
                return RetryMetadata.create(throwable, integer);
            }
        };
    }

    private Func1<RetryMetadata, Observable<?>> checkForAuthenticationErrors() {
        return new Func1<RetryMetadata, Observable<?>>() {
            @Override
            public Observable<?> call(RetryMetadata retryMetadata) {
                if (retryMetadata.retry() < RETRY_COUNT && isUnauthorized(retryMetadata.throwable())) {
                    return retry();
                } else {
                    return doNotRetry(retryMetadata.throwable());
                }
            }
        };
    }

    private Observable<Void> retry() {
        authenticationService.invalidateAccessToken();
        return Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() {
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

    @AutoValue
    abstract static class RetryMetadata {

        static RetryMetadata create(Throwable throwable, Integer retry) {
            return new AutoValue_AuthenticationInterceptor_RetryMetadata(throwable, retry);
        }

        abstract Throwable throwable();

        abstract Integer retry();
    }

}
