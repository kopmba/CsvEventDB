package waz.api.entity;

public class ServiceData {
    @Override
    public String toString() {
        return "ServiceData{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", status='" + status + '\'' +
                ", startupType='" + startupType + '\'' +
                '}';
    }

    private String name, displayName, status, startupType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartupType() {
        return startupType;
    }

    public void setStartupType(String startupType) {
        this.startupType = startupType;
    }
}
