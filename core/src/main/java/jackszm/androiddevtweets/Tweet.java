package jackszm.androiddevtweets;

import com.google.auto.value.AutoValue;

import java.net.URI;

@AutoValue
public abstract class Tweet {

    public static Tweet create(String id, String content, String authorDisplayName, String authorHandle, URI authorAvatar) {
        return new AutoValue_Tweet(id, content, authorDisplayName, authorHandle, authorAvatar);
    }

    public abstract String id();

    public abstract String content();

    public abstract String authorDisplayName();

    public abstract String authorHandle();

    public abstract URI authorAvatar();

}
