package net.jackszm.twittertagscanner;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Tweet {

    public static Tweet create(String id, String content, String authorDisplayName, String authorHandle, String authorAvatar) {
        return new AutoValue_Tweet(id, content, authorDisplayName, authorHandle, authorAvatar);
    }

    public abstract String id();

    public abstract String content();

    public abstract String authorDisplayName();

    public abstract String authorHandle();

    public abstract String authorAvatar();

}
