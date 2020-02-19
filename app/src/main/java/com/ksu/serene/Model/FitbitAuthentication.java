package com.ksu.serene.Model;

public class FitbitAuthentication {


    private final String url = "https://www.fitbit.com/oauth2/authorize?response_type=token";
    private final String clientId = "22BK8G";
    private final String redirect_uri = "auth%3A%2F%2Fcallback";
    private final String scope = "activity%20heartrate%20sleep%20weight";
    private final String expires_in = "31536000";


    public String getUrl() {
        return url;
    }

    public String getClientId() {
        return clientId;
    }

    public String getRedirect_uri() {
        return redirect_uri;
    }

    public String getScope() {
        return scope;
    }

    public String getExpires_in() {
        return expires_in;
    }
}
