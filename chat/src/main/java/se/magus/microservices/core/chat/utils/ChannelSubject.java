package se.magus.microservices.core.chat.utils;

public class ChannelSubject {
    private static final String PUBLIC_CHANNEL = "chat.channel.public";

    public static String publicChannelSubject(String channelId) {
        return PUBLIC_CHANNEL + channelId;
    }

    public static String publicChannelOfSubject(String subject) {
        return subject.replace(PUBLIC_CHANNEL, "");
    }
}
