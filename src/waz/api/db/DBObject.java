package waz.api.db;

import com.sun.jna.platform.win32.WinReg;
import waz.api.entity.EventData;
import waz.api.entity.ServiceData;
import waz.api.parser.EventRecord;
import waz.api.parser.ServiceRecord;
import wazsvcreg.WazReg;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class DBObject extends Thread {

    private Set<Object> evt_app_queue;
    private Set<Object> evt_sys_queue;
    private Set<Object> svcqueue;
    private Logon logon;
    private WazReg registry;

    public String SRV_col = null;
    public String EVT_APP_col = null;
    public String EVT_SYS_col = null;

    private static final String key = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Authentication\\LogonUI\\SessionData\\1";
    public static final String svcevtkey = "SOFTWARE\\WazSvc\\SvcEvt";

    public DBObject() {
        evt_app_queue = new HashSet<>();
        evt_sys_queue = new HashSet<>();
        svcqueue = new HashSet<>();
        logon = new Logon();
        registry = new WazReg();
    }

    /**
     * At the run of DBObject, we set the value of the logon user
     */
    public void run() {
        logon.setLoggedOnUserId(registry.read(WinReg.HKEY_LOCAL_MACHINE, key, "LoggedOnUserSID"));
        logon.setLoggedOnUser(registry.read(WinReg.HKEY_LOCAL_MACHINE, key, "LoggedOnUser"));
       // logon.setLastLoggedOnProvider(registry.read(WinReg.HKEY_LOCAL_MACHINE, key, "LastLoggedOnProvider"));
        logon.setLoggedOnDisplayName(registry.read(WinReg.HKEY_LOCAL_MACHINE, key, "LoggedOnDisplayName"));

        SRV_col = registry.read(WinReg.HKEY_CURRENT_USER, svcevtkey, "svc_data");
        EVT_APP_col = registry.read(WinReg.HKEY_CURRENT_USER, svcevtkey, "evt_sys_data");
        EVT_SYS_col = registry.read(WinReg.HKEY_CURRENT_USER, svcevtkey, "evt_app_data");

        try {
            setEvt_app_queue(DBUtil.deserialize(EVT_APP_col));
            setEvt_sys_queue(DBUtil.deserialize(EVT_SYS_col));
            setSvcqueue(DBUtil.deserialize(SRV_col));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Set<Object> getEvt_app_queue() {
        return evt_app_queue;
    }

    public Set<Object> getEvt_sys_queue() {
        return evt_sys_queue;
    }

    public void setEvt_app_queue(Set<Object> evt_app_queue) {
        this.evt_app_queue = evt_app_queue;
    }

    public void setEvt_sys_queue(Set<Object> evt_sys_queue) {
        this.evt_sys_queue = evt_sys_queue;
    }

    public Set<Object> getSvcqueue() {
        return svcqueue;
    }

    public void setSvcqueue(Set<Object> svcqueue) {
        this.svcqueue = svcqueue;
    }

    public Set<Object> getByPredicat(String filename) throws FileNotFoundException, InterruptedException {
        if(filename.contains("evt_app")) {
            return (Set<Object>) DBUtil.deserialize(filename);
        } else if(filename.contains("evt_sys")) {
            return (Set<Object>) DBUtil.deserialize(filename);
        } else {
            return (Set<Object>) DBUtil.deserialize(filename);
        }
    }

    /**
     * Update an service value
     * @param data
     * @param key
     */
    public void update(ServiceData data, String key) throws IOException {
        for (Object svcdata : svcqueue) {
            if(svcdata instanceof ServiceData) {
                if (((ServiceData) svcdata).getName().equals(key)) {
                    ((ServiceData) svcdata).setName(data.getName());
                    ((ServiceData) svcdata).setDisplayName(data.getDisplayName());
                    ((ServiceData) svcdata).setStartupType(data.getStartupType());
                    ((ServiceData) svcdata).setStatus(data.getStatus());
                }
            }
        }
        DBUtil.serialize(this.SRV_col, svcqueue);
    }

    /**
     * List all services with non running status
     */
    public List<Object> history() {
        List<Object> data = new ArrayList<>();
        for (Object svcdata : svcqueue) {
            if(svcdata instanceof ServiceData) {
                if (!((ServiceData) svcdata).getStatus().equals("Running")) {
                    data.add(svcdata);
                }
            }
        }
        return data;
    }

    public static void main(String[] args) {
        new DBObject().start();
    }

}
