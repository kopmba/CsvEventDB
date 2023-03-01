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

public class EventRecord extends HashMap<String, String> {

    private final ObjectParser parser;
    private String[] properties;
    private Object[] values;
    private List<EventRecord> records;
    private String name;
    private String dir;

    public EventRecord(ObjectParser parser, String name) throws IOException, InterruptedException {
        this.parser = parser;
        Preferences node = Preferences.systemRoot().node(DBObject.svcevtkey);
        this.dir = new WazReg().readKeyValue(Preferences.systemRoot(), node.get("data_dir", "data_dir"), "data_dir");
        System.out.println(dir);
        this.name = name;
        this.properties = parser.getProperties();
        this.values = new Object[]{};
    }

    public EventRecord(ObjectParser parser, String name, String[] properties) throws IOException, InterruptedException {
        this.parser = parser;
        Preferences node = Preferences.systemRoot().node(DBObject.svcevtkey);
        this.dir = new WazReg().readKeyValue(Preferences.systemRoot(), node.get("data_dir", "data_dir"), "data_dir");
        System.out.println(dir);
        this.name = name;
        this.properties = parser.getProperties();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    /**
     * Retrieves list of csv records and build a new list of event record by serializing them
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public List<EventData> records() throws IOException, InterruptedException {
        List<CSVRecord> listRecords = parser.getParser().getRecords();
        List<EventData> data = new ArrayList();
        Set<EventData> filter = null;

        for(CSVRecord record : listRecords) {
            List<String> itList = new ArrayList();
            record.iterator().forEachRemaining(itList::add);
            String[] csvData = null;
            if(itList.size() == 1) {
                csvData = new String[]{""};
            } else {
                csvData = new String[]{"", "", "", "", "", "", "", ""};
            }

            for (int i= 0; i < itList.size(); i++) {
                csvData[i] = itList.get(i);
                System.out.println(csvData[i]);
            }

            if(csvData[0].equals(parser.getProperties()[0])) {
                continue;
            } else {
                if(csvData.length > 1) {
                    EventData ev = new EventData();
                    ev.setEntryType(csvData[0]);
                    ev.setMachineName(csvData[1]);
                    ev.setCategory(csvData[2]);
                    ev.setSource(csvData[3]);
                    ev.setMessage(csvData[4]);
                    ev.setUsername(csvData[5]);
                    ev.setRecommendation(csvData[6]);
                    ev.setEvType(name);
                    data.add(ev);
                }
            }
        }

        filter = (Set<EventData>) filter(data);
        values = filter.toArray();
        //DBUtil.serialize(dir + "\\" + name + ".json", filter);
        DBUtil.serialize(dir + "\\" + name, filter);
        return data;
    }

    public Set<EventData> filter(List<EventData> data) {
        Set<EventData> set = new HashSet();
        for (EventData ev : data) {
            if(!set.contains(ev)) {
                set.add(ev);
            }
        }
        return set;
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
