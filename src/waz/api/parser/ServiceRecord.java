package waz.api.parser;

import com.sun.jna.platform.win32.WinReg;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import waz.api.db.DBObject;
import waz.api.db.DBUtil;
import waz.api.entity.EventData;
import waz.api.entity.ServiceData;
import wazsvcreg.WazReg;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.prefs.Preferences;

public class ServiceRecord extends HashMap<String, String> {

    private final ObjectParser parser;
    private String[] properties;
    private Object[] values;
    private List<ServiceRecord> records;
    private String fname;
    private String dir;

    public ServiceRecord(ObjectParser parser) throws IOException, InterruptedException {
        this.parser = parser;
        Preferences node = Preferences.systemRoot().node(DBObject.svcevtkey);
        this.dir = new WazReg().readKeyValue(Preferences.systemRoot(), node.get("data_dir", "data_dir"), "data_dir");
        this.fname = "";
        this.properties = parser.getProperties();
        this.values = new Object[]{};
    }

    public ServiceRecord(ObjectParser parser, String[] properties) throws IOException, InterruptedException {
        this.parser = parser;
        Preferences node = Preferences.systemRoot().node(DBObject.svcevtkey);
        this.dir = new WazReg().readKeyValue(Preferences.systemRoot(), node.get("data_dir", "data_dir"), "data_dir");
        this.fname = "";
        this.properties = parser.getProperties();
    }

    public String getFName() {
        return fname;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getDir() {
        return dir;
    }

    public String[] getProperties() {
        return properties;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }

    public ObjectParser getParser() {
        return parser;
    }

    /**
     * Retrieves list of csv records and build a new list of event record by serializing them
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public List<ServiceData> records() throws IOException, InterruptedException {
        List<CSVRecord> listRecords = parser.getParser().getRecords();
        List<ServiceData> data = new ArrayList();
        List<ServiceData> filter = null;
        for(CSVRecord record : parser.getParser().getRecords()) {
            List<String> itList = new ArrayList();
            record.iterator().forEachRemaining(itList::add);
            String[] csvData = null;
            if(itList.size() == 1) {
                csvData = new String[]{""};
            } else {
                csvData = new String[]{"", "", "", "", "", "", ""};
            }

            for (int i= 0; i < itList.size(); i++) {
                csvData[i] = itList.get(i);
                System.out.println(csvData[i]);
            }

            if(csvData.equals(parser.getProperties())) {
                continue;
            } else {
                ServiceData ev = new ServiceData();
                ev.setName(csvData[0]);
                ev.setDisplayName(csvData[1]);
                ev.setStatus(csvData[2]);
                ev.setStartupType(csvData[3]);
                data.add(ev);
            }
        }
        values = data.toArray();
        //DBUtil.serialize(dir + "\\" + fname + ".json", filter);
        DBUtil.serialize(dir + "\\" + fname, filter);
        return data;
    }

    /**
     * Get the values of record and print the values on the console
     * @param properties
     * @param parser
     * @return
     * @throws IOException
     */
    public String[] getValuesFromProperties(String filename, String[] properties, ObjectParser parser) throws IOException {
        String[] values = {};
        parser.setFilename(filename);
        CSVPrinter printer = new CSVPrinter(new FileWriter(parser.getFilename()), CSVFormat.EXCEL.withHeader(properties));
        printer.printRecords((Object[]) properties);
        for(CSVRecord record : parser.getParser().getRecords()) {
            for (int i = 0; i < properties.length ; i++) {
                values[i] = record.get(properties[i]);
                printer.printRecords((Object[])values);
            }
        }
        return values;
    }


}
