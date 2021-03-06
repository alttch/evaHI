package com.altertech.evahi;

/**
 * Created by oshevchuk on 20.03.2019
 */
public class AppConfig {

    public final static String NAME = "", VERSION = "", COPYRIGHT = "";

    public final static ServerConfig CONFIG = new ServerConfig(false, false, "", 443);

    public final static boolean AUTHENTICATION = true;

    public final static int evaHI_Build = 2019090501;

    public static class ServerConfig {

        final private boolean enabled, useHttps;
        final private String address;
        final private int port;

        /**
         * @param enabled
         * @param useHttps
         * @param address
         * @param port
         */
        ServerConfig(boolean enabled, boolean useHttps, String address, int port) {
            this.enabled = enabled;
            this.useHttps = useHttps;
            this.address = address;
            this.port = port;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public boolean useHttps() {
            return useHttps;
        }

        public String getAddress() {
            return address;
        }

        public int getPort() {
            return port;
        }
    }

    /**
     * in second
     */
    public final static int RELOAD_AFTER_SLEEP = 30;
}
