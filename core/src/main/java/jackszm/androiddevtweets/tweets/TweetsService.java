package jackszm.androiddevtweets.tweets;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import jackszm.androiddevtweets.Tweet;
import rx.Observable;

public class TweetsService {

    public Observable<List<Tweet>> loadTweets() {
        return Observable.fromCallable(load());
    }

    private Callable<List<Tweet>> load() {
        return new Callable<List<Tweet>>() {
            @Override
            public List<Tweet> call() throws Exception {
                return Arrays.asList(
                        Tweet.create("id-1", "Material-Animations is trending on Github right now! https://github.com/lgvalle/Material-Animations #materialdesign #androiddev", "Luis Valle", "lgvalle", URI.create("https://pbs.twimg.com/profile_images/752093150534070272/7PD9PUhe_400x400.jpg")),
                        Tweet.create("id-2", "Just published a quick post about how to improve your continuous delivery process thanks to @jenkinsci pipelines.", "Daniele Bonaldo", "danybony_", URI.create("https://pbs.twimg.com/profile_images/500605090965630976/ztUmaIkw_400x400.jpeg")),
                        Tweet.create("id-3", "🎉100+ Stars 🎉 If you haven’t, check out Google Material Icons for Sketch Repo https://github.com/LPZilva/Google-Material-Icons-for-Sketch … @GoogleDesign", "Luis da Silva  ", "LPZilva", URI.create("https://pbs.twimg.com/profile_images/744938574223532032/JhRHYd96_400x400.jpg")),
                        Tweet.create("id-4", "- Shall we create a Snapchat rip-off? - Nah.Let's call it \"Instagram Stories\"", "Luis da Silva  ", "LPZilva", URI.create("https://pbs.twimg.com/profile_images/744938574223532032/JhRHYd96_400x400.jpg")),
                        Tweet.create("id-5", ".@blundell_apps @novoda via the nearest pokestops near you", "Kevin McDonagh", "@kevinmcdonagh", URI.create("https://pbs.twimg.com/profile_images/2331994386/s0odku3nri6svys0gtrt_bigger.jpeg"))
                );
            }
        };
    }
}
