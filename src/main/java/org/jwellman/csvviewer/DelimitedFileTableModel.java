package org.jwellman.csvviewer;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

public class DelimitedFileTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private List<String> columns = new ArrayList<>();
    
    private List<String[]> records;
    
    public DelimitedFileTableModel(File file, String delimiter) {
        Reader reader = null;
        CSVReader csvReader = null;
        try {
            reader = Files.newBufferedReader(file.toPath());
            
            final CSVParser parser = new CSVParserBuilder()
                    .withSeparator(delimiter.charAt(0))
                    .withIgnoreQuotations(true)
                    .build();
            
            csvReader = new CSVReaderBuilder(reader)
                    .withSkipLines(0)
                    .withCSVParser(parser)
                    .build();
            
            records = csvReader.readAll();
            columns.addAll(Arrays.asList(records.get(0))); // assume first record is column headings
            records.remove(0);
            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (csvReader != null) csvReader.close();
                if (csvReader != null) reader.close();            
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    @Override
    public int getRowCount() {
        return records.size();
    }

    @Override
    public int getColumnCount() {
        return records.get(0).length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return ((String[])records.get(rowIndex))[columnIndex];
    }

    @Override
    public String getColumnName(int col) {
        return (columns != null) ? columns.get(col) : "TBD";
    }
    
}
