package waz.api.parser;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class ObjectParser {

    private CSVParser parser;
    private String filename;
    private String[] properties;
    private CSVPrinter printer;

    public ObjectParser(String filename, String[] properties) throws IOException {
        this.filename = filename;
        this.properties = properties;
        parser = CSVParser.parse(new File(filename), Charset.defaultCharset(), CSVFormat.EXCEL.withHeader(properties));
    }

    public CSVParser getParser() {
        return parser;
    }

    public void setParser(CSVParser parser) {
        this.parser = parser;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String[] getProperties() {
        return properties;
    }

    public void setProperties(String[] properties) {
        this.properties = properties;
    }
}
