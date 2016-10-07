package ch.hsr.gadgeothek.service;

public class LoginToken {
    private String customerId;
    private String securityToken;
    private boolean keepMeLoggedIn;

    public String getSecurityToken() {
        return securityToken;
    }

    public void setSecurityToken(String securityToken) {
        this.securityToken = securityToken;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setKeepMeLoggedIn(boolean keepMeLoggedIn) {this.keepMeLoggedIn = keepMeLoggedIn; }

    public boolean getKeepMeLoggedIn() { return keepMeLoggedIn; }
}
