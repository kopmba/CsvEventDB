package waz.api.entity;

public class EventData {

    private String entryType, machineName, category, source, message, username, recommendation, evType;

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getEvType() {
        return evType;
    }

    public void setEvType(String evType) {
        this.evType = evType;
    }

    @Override
    public String toString() {
        return "EventData{" +
                "entryType='" + entryType + '\'' +
                ", machineName='" + machineName + '\'' +
                ", category='" + category + '\'' +
                ", source='" + source + '\'' +
                ", message='" + message + '\'' +
                ", username='" + username + '\'' +
                ", recommendation='" + recommendation + '\'' +
                ", evType='" + evType + '\'' +
                '}';
    }
}
