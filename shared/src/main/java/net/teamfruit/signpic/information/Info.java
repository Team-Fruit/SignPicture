package net.teamfruit.signpic.information;

import javax.annotation.Nullable;
import java.util.List;

public class Info {
    public @Nullable String analytics;
    public @Nullable Api apis;

    public static class Api {
        public @Nullable Image image;
        public @Nullable Shortener shortener;

        public static class Image {
            public @Nullable Gyazo gyazo;
            public @Nullable Imgur imgur;

            public static class Gyazo {
                public @Nullable List<Config> config;

                public static class Config {
                    public @Nullable String key;
                }
            }

            public static class Imgur {
                public @Nullable List<Config> config;

                public static class Config {
                    public @Nullable String clientid;
                }
            }
        }

        public static class Shortener {
            public @Nullable Bitly bitly;
            public @Nullable Googl googl;

            public static class Bitly {
                public @Nullable List<Config> config;

                public static class Config {
                    public @Nullable String key;
                }
            }

            public static class Googl {
                public @Nullable List<Config> config;

                public static class Config {
                    public @Nullable String key;
                }
            }
        }
    }
}
