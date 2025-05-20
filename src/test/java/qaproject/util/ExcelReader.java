package qaproject.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Utility class for reading data from Excel (XLSX) files.
 * Supports data-driven testing by providing methods to extract test data
 * from Excel spreadsheets.
 */
public class ExcelReader {
    
    /**
     * Reads all data from a specific sheet in an Excel file.
     * 
     * @param filePath Path to the Excel file
     * @param sheetName Name of the sheet to read
     * @return List of maps where each map represents a row with column headers as keys
     * @throws IOException If file cannot be read
     */
    public static List<Map<String, String>> readSheet(String filePath, String sheetName) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            
            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IOException("Sheet '" + sheetName + "' not found in the workbook");
            }
            
            // Get header row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IOException("Header row not found in sheet '" + sheetName + "'");
            }
            
            // Extract column headers
            List<String> headers = new ArrayList<>();
            Iterator<Cell> headerCellIterator = headerRow.cellIterator();
            while (headerCellIterator.hasNext()) {
                Cell cell = headerCellIterator.next();
                headers.add(getCellValueAsString(cell));
            }
            
            // Extract data rows
            Iterator<Row> rowIterator = sheet.rowIterator();
            // Skip header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                Map<String, String> rowData = new HashMap<>();
                
                for (int i = 0; i < headers.size(); i++) {
                    Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    rowData.put(headers.get(i), getCellValueAsString(cell));
                }
                
                data.add(rowData);
            }
        }
        
        return data;
    }
    
    /**
     * Reads data from a specific sheet in an Excel file and returns it as a two-dimensional array.
     * Suitable for use with TestNG DataProvider.
     * 
     * @param filePath Path to the Excel file
     * @param sheetName Name of the sheet to read
     * @return Two-dimensional Object array where each inner array represents a row of data
     * @throws IOException If file cannot be read
     */
    public static Object[][] readSheetAsDataProvider(String filePath, String sheetName) throws IOException {
        List<Map<String, String>> sheetData = readSheet(filePath, sheetName);
        Object[][] data = new Object[sheetData.size()][1];
        
        for (int i = 0; i < sheetData.size(); i++) {
            data[i][0] = sheetData.get(i);
        }
        
        return data;
    }
    
    /**
     * Reads data from a specific sheet in an Excel file and returns it as a two-dimensional array
     * with specific columns only.
     * 
     * @param filePath Path to the Excel file
     * @param sheetName Name of the sheet to read
     * @param columns Array of column names to include
     * @return Two-dimensional Object array where each inner array contains values for specified columns
     * @throws IOException If file cannot be read
     */
    public static Object[][] readSheetAsDataProvider(String filePath, String sheetName, String[] columns) throws IOException {
        List<Map<String, String>> sheetData = readSheet(filePath, sheetName);
        Object[][] data = new Object[sheetData.size()][columns.length];
        
        for (int i = 0; i < sheetData.size(); i++) {
            Map<String, String> row = sheetData.get(i);
            for (int j = 0; j < columns.length; j++) {
                data[i][j] = row.get(columns[j]);
            }
        }
        
        return data;
    }
    
    /**
     * Converts cell value to string regardless of cell type.
     * 
     * @param cell The cell to extract value from
     * @return String representation of cell value
     */
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                // Check if the numeric value is an integer
                double numericValue = cell.getNumericCellValue();
                if (numericValue == Math.floor(numericValue)) {
                    return String.valueOf((int) numericValue);
                }
                return String.valueOf(numericValue);
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                try {
                    return String.valueOf(cell.getStringCellValue());
                } catch (Exception e) {
                    try {
                        return String.valueOf(cell.getNumericCellValue());
                    } catch (Exception ex) {
                        return String.valueOf(cell.getBooleanCellValue());
                    }
                }
            default:
                return "";
        }
    }
}