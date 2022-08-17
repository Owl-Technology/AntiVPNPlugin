package dev.aswitch.antivpnplugin.api.utils;

import java.util.UUID;

public class Settings {
    public static String KICK_MESSAGE = "&cProxies/VPNs are not allowed on this server!";
    public static boolean KICK_PLAYERS = true;
    public static boolean OP_BYPASS = true;

    public static boolean CONSOLE_ALERTS = true;

    public static boolean ALERTS_ENABLED = true;
    public static String ALERT_MESSAGE = "&8[&cOtter&8] &4%player% &7has joined on a VPN/proxy!";

    public static String SERVER_ID = UUID.randomUUID().toString();

    /**
     * Ip limiting settings
     */

    public static boolean IP_LIMIT_ENABLED = true;
    public static int MAX_IP_CONNECTIONS = 2;
    public static String IP_LIMIT_KICK_MESSAGE = "&cIp connection limit reached!";


}
