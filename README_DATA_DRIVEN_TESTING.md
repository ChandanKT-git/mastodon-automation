# Data-Driven Testing in QAMonstoddon

This document describes the data-driven testing functionality implemented in the Mastodon test suite.

## Overview

Data-driven testing allows test cases to be executed multiple times with different input data. This approach separates test logic from test data, making tests more maintainable and enabling comprehensive test coverage with minimal code duplication.

The implementation supports two common data formats:

- Excel (XLSX) files using Apache POI
- CSV files using standard Java I/O

## Components

### 1. ExcelReader

A utility class that handles reading test data from Excel (XLSX) files:

- `readSheet(String filePath, String sheetName)`: Reads all data from a specific sheet
- `readSheetAsDataProvider(String filePath, String sheetName)`: Formats data for TestNG DataProvider
- `readSheetAsDataProvider(String filePath, String sheetName, String[] columns)`: Extracts specific columns

### 2. CSVReader

A utility class for reading test data from CSV files:

- `readCSV(String filePath, String delimiter)`: Reads all data from a CSV file
- `readCSVAsDataProvider(String filePath)`: Formats data for TestNG DataProvider
- `readCSVAsDataProvider(String filePath, String delimiter, String[] columns)`: Extracts specific columns

### 3. DataDrivenDemoTest

A demonstration test class that shows how to use the data readers with TestNG:

- Uses `@DataProvider` to supply test data from Excel and CSV files
- Demonstrates login testing with Excel data
- Demonstrates search testing with CSV data
- Integrates with the existing screenshot functionality

## Usage Examples

### Setting Up Data Providers

```java
@DataProvider(name = "excelDataProvider")
public Object[][] getExcelData() throws IOException {
    String excelFilePath = "src/test/resources/testdata/login_data.xlsx";
    return ExcelReader.readSheetAsDataProvider(excelFilePath, "LoginTests");
}

@DataProvider(name = "csvDataProvider")
public Object[][] getCSVData() throws IOException {
    String csvFilePath = "src/test/resources/testdata/search_data.csv";
    return CSVReader.readCSVAsDataProvider(csvFilePath);
}
```

### Using Data Providers in Tests

```java
@Test(dataProvider = "excelDataProvider")
public void testLoginWithExcelData(Map<String, String> testData) {
    String username = testData.get("username");
    String password = testData.get("password");
    String expectedResult = testData.get("expectedResult");

    // Test logic using the data...
}

@Test(dataProvider = "csvDataProvider")
public void testSearchWithCSVData(Map<String, String> testData) {
    String searchTerm = testData.get("searchTerm");
    String expectedResults = testData.get("expectedResults");

    // Test logic using the data...
}
```

## Data File Structure

### Excel Files

Excel files should have:

- A header row with column names
- Data rows with values corresponding to each column
- Sheets organized by test category

Example structure for `login_data.xlsx`:

| username | password  | expectedResult |
| -------- | --------- | -------------- |
| admin    | admin123  | success        |
| user1    | password1 | success        |
| invalid  | wrongpass | failure        |

### CSV Files

CSV files should have:

- A header row with column names
- Data rows with values separated by commas

Example structure for `search_data.csv`:

```
searchTerm,expectedResults
mastodon,5
selenium,10
automation,7
testing,15
```

## Best Practices

1. **Organize Data Files**: Keep test data files in a dedicated directory structure
2. **Meaningful Column Names**: Use descriptive names for data columns
3. **Data Validation**: Include validation in data providers to handle missing files gracefully
4. **Selective Data**: Extract only the columns needed for specific tests
5. **Error Handling**: Implement proper exception handling for file operations

## Limitations

- Excel files require Apache POI dependencies
- Large data sets may impact test execution time
- CSV files have limited support for complex data types

## Integration with Screenshot Functionality

The data-driven testing functionality integrates with the existing screenshot capabilities:

- Screenshots can be taken at key points in data-driven tests
- Screenshot filenames can include data values for easy identification
- Test failures automatically trigger screenshots through the CustomTestListener

Example:

```java
// Take screenshot with data-specific name
ScreenshotUtil.takeStepScreenshot(driver, "loginTest", username + "_result");
```
