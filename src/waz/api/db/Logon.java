package waz.api.db;

public class Logon {

    private String loggedOnUserId;
    private String loggedOnUser;
    private String loggedOnDisplayName;
    private String lastLoggedOnProvider;
    private String computer;

    public String getLoggedOnUserId() {
        return loggedOnUserId;
    }

    public void setLoggedOnUserId(String loggedOnUserId) {
        this.loggedOnUserId = loggedOnUserId;
    }

    public String getLoggedOnUser() {
        return loggedOnUser;
    }

    public void setLoggedOnUser(String loggedOnUser) {
        this.loggedOnUser = loggedOnUser;
    }

    public String getLoggedOnDisplayName() {
        return loggedOnDisplayName;
    }

    public void setLoggedOnDisplayName(String loggedOnDisplayName) {
        this.loggedOnDisplayName = loggedOnDisplayName;
    }

    public String getLastLoggedOnProvider() {
        return lastLoggedOnProvider;
    }

    public void setLastLoggedOnProvider(String lastLoggedOnProvider) {
        this.lastLoggedOnProvider = lastLoggedOnProvider;
    }

    public String getComputer() {
        computer = getLoggedOnUser().substring(0, getLoggedOnUser().indexOf('\\'));
        return computer;
    }
}
