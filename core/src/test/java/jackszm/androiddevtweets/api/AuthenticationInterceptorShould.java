package jackszm.androiddevtweets.api;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import rx.Observable;
import rx.Subscriber;
import rx.observers.TestSubscriber;

import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AuthenticationInterceptorShould {

    private static final RequestExecutor.HttpException UNAUTHORIZED_EXCEPTION = new RequestExecutor.HttpException(HTTP_UNAUTHORIZED);

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock
    AuthenticationService authenticationService;

    private AuthenticationInterceptor retryRule;

    @Before
    public void setUp() throws Exception {
        retryRule = new AuthenticationInterceptor(authenticationService);
    }

    @Test
    public void doesNotRepeatObservable_whenAnyException() {
        RetryCountErrorObservable errorObservable = anyException();
        TestSubscriber<String> subscriber = new TestSubscriber<>();

        Observable.create(errorObservable)
                .retryWhen(retryRule.retryRule())
                .subscribe(subscriber);

        assertThat(errorObservable.callCount).isEqualTo(1);
    }

    @Test
    public void emitsOriginalException_whenAnyException() {
        RetryCountErrorObservable errorObservable = anyException();
        TestSubscriber<String> subscriber = new TestSubscriber<>();

        Observable.create(errorObservable)
                .retryWhen(retryRule.retryRule())
                .subscribe(subscriber);

        verify(authenticationService, times(0)).invalidateAccessToken();
    }

    @Test
    public void repeatsObservable_thenEmitsException_whenUnauthorizedExceptionHappensMoreThanOnce() {
        RetryCountErrorObservable errorObservable = unAuthorizedException();
        TestSubscriber<String> subscriber = new TestSubscriber<>();

        Observable.create(errorObservable)
                .retryWhen(retryRule.retryRule())
                .subscribe(subscriber);

        assertThat(errorObservable.callCount).isEqualTo(2);
        assertThat(subscriber.getOnErrorEvents().get(0)).isEqualTo(UNAUTHORIZED_EXCEPTION);
        assertThat(subscriber.getOnNextEvents()).isEmpty();
    }

    @Test
    public void repeatsObservable_andCallsOnNext_whenUnauthorizedExceptionHappensOnce() {
        UnauthorizedSingleErrorObservable errorObservable = new UnauthorizedSingleErrorObservable();
        TestSubscriber<String> subscriber = new TestSubscriber<>();

        Observable.create(errorObservable)
                .retryWhen(retryRule.retryRule())
                .subscribe(subscriber);

        assertThat(errorObservable.callCount).isEqualTo(2);
        assertThat(subscriber.getOnErrorEvents()).isEmpty();
        assertThat(subscriber.getOnNextEvents()).isNotEmpty();
    }

    @Test
    public void invalidatesAccessToken_whenUnauthorizedException() {
        RetryCountErrorObservable errorObservable = unAuthorizedException();
        TestSubscriber<String> subscriber = new TestSubscriber<>();

        Observable.create(errorObservable)
                .retryWhen(retryRule.retryRule())
                .subscribe(subscriber);

        verify(authenticationService).invalidateAccessToken();
    }

    private RetryCountErrorObservable unAuthorizedException() {
        return new RetryCountErrorObservable(UNAUTHORIZED_EXCEPTION);
    }

    private RetryCountErrorObservable anyException() {
        return new RetryCountErrorObservable(new RuntimeException("Any Exception"));
    }

    private static class RetryCountErrorObservable implements Observable.OnSubscribe<String> {

        private final Throwable throwable;

        private int callCount = 0;

        private RetryCountErrorObservable(Throwable throwable) {
            this.throwable = throwable;
        }

        @Override
        public void call(Subscriber<? super String> subscriber) {
            callCount++;
            subscriber.onError(throwable);
        }
    }

    private static class UnauthorizedSingleErrorObservable implements Observable.OnSubscribe<String> {

        private int callCount = 0;

        private UnauthorizedSingleErrorObservable() {
        }

        @Override
        public void call(Subscriber<? super String> subscriber) {
            callCount++;
            if (callCount == 2) {
                subscriber.onNext("data");
                subscriber.onCompleted();
            } else {
                subscriber.onError(UNAUTHORIZED_EXCEPTION);
            }
        }
    }
}
