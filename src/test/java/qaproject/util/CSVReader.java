package qaproject.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading data from CSV files.
 * Provides methods to extract test data from CSV files for data-driven testing.
 */
public class CSVReader {
    
    /**
     * Reads all data from a CSV file.
     * 
     * @param filePath Path to the CSV file
     * @param delimiter Character used to separate values (default is comma)
     * @return List of maps where each map represents a row with column headers as keys
     * @throws IOException If file cannot be read
     */
    public static List<Map<String, String>> readCSV(String filePath, String delimiter) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            // Read header line
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new IOException("CSV file is empty");
            }
            
            // Parse headers
            String[] headerArray = headerLine.split(delimiter);
            for (String header : headerArray) {
                headers.add(header.trim());
            }
            
            // Read data rows
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Parse row data
                String[] values = parseCSVLine(line, delimiter);
                Map<String, String> rowData = new HashMap<>();
                
                // Map values to headers
                for (int i = 0; i < headers.size(); i++) {
                    if (i < values.length) {
                        rowData.put(headers.get(i), values[i].trim());
                    } else {
                        rowData.put(headers.get(i), ""); // Empty value for missing columns
                    }
                }
                
                data.add(rowData);
            }
        }
        
        return data;
    }
    
    /**
     * Reads all data from a CSV file using comma as the default delimiter.
     * 
     * @param filePath Path to the CSV file
     * @return List of maps where each map represents a row with column headers as keys
     * @throws IOException If file cannot be read
     */
    public static List<Map<String, String>> readCSV(String filePath) throws IOException {
        return readCSV(filePath, ",");
    }
    
    /**
     * Reads data from a CSV file and returns it as a two-dimensional array.
     * Suitable for use with TestNG DataProvider.
     * 
     * @param filePath Path to the CSV file
     * @param delimiter Character used to separate values
     * @return Two-dimensional Object array where each inner array represents a row of data
     * @throws IOException If file cannot be read
     */
    public static Object[][] readCSVAsDataProvider(String filePath, String delimiter) throws IOException {
        List<Map<String, String>> csvData = readCSV(filePath, delimiter);
        Object[][] data = new Object[csvData.size()][1];
        
        for (int i = 0; i < csvData.size(); i++) {
            data[i][0] = csvData.get(i);
        }
        
        return data;
    }
    
    /**
     * Reads data from a CSV file and returns it as a two-dimensional array using comma as default delimiter.
     * 
     * @param filePath Path to the CSV file
     * @return Two-dimensional Object array where each inner array represents a row of data
     * @throws IOException If file cannot be read
     */
    public static Object[][] readCSVAsDataProvider(String filePath) throws IOException {
        return readCSVAsDataProvider(filePath, ",");
    }
    
    /**
     * Reads data from a CSV file and returns it as a two-dimensional array with specific columns only.
     * 
     * @param filePath Path to the CSV file
     * @param delimiter Character used to separate values
     * @param columns Array of column names to include
     * @return Two-dimensional Object array where each inner array contains values for specified columns
     * @throws IOException If file cannot be read
     */
    public static Object[][] readCSVAsDataProvider(String filePath, String delimiter, String[] columns) throws IOException {
        List<Map<String, String>> csvData = readCSV(filePath, delimiter);
        Object[][] data = new Object[csvData.size()][columns.length];
        
        for (int i = 0; i < csvData.size(); i++) {
            Map<String, String> row = csvData.get(i);
            for (int j = 0; j < columns.length; j++) {
                data[i][j] = row.get(columns[j]);
            }
        }
        
        return data;
    }
    
    /**
     * Parses a CSV line considering quoted values that may contain delimiters.
     * 
     * @param line The CSV line to parse
     * @param delimiter The delimiter character
     * @return Array of values from the line
     */
    private static String[] parseCSVLine(String line, String delimiter) {
        List<String> values = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                // Toggle quote state
                inQuotes = !inQuotes;
            } else if (c == delimiter.charAt(0) && !inQuotes) {
                // End of value
                values.add(currentValue.toString());
                currentValue = new StringBuilder();
            } else {
                // Part of value
                currentValue.append(c);
            }
        }
        
        // Add the last value
        values.add(currentValue.toString());
        
        return values.toArray(new String[0]);
    }
}