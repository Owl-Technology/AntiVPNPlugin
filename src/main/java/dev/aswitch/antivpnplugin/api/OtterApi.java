package dev.aswitch.antivpnplugin.api;

import dev.aswitch.antivpnplugin.api.utils.NetworkUtils;
import dev.aswitch.antivpnplugin.api.utils.Settings;
import org.json.JSONObject;

import java.util.HashMap;

public class OtterApi {

    public final HashMap<String, Boolean> cachedResults = new HashMap<>();
    public final NetworkUtils networkUtils = new NetworkUtils();

    public boolean check(String ip) {
        if (cachedResults.containsKey(ip)) {
            return cachedResults.get(ip);
        }

        // Returning localhost ips
        if (ip.equals("127.0.0.1")) {
            return false;
        }

        try {
            final String response = networkUtils.getFromURL("https://owltech.digital/api/" + Settings.LICENSE + "/" + ip);
            final JSONObject json = new JSONObject(response);

            boolean vpn = json.getBoolean("proxy");
            cachedResults.put(ip, vpn);

            return vpn;
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        cachedResults.put(ip, false);
        return false;
    }


}
