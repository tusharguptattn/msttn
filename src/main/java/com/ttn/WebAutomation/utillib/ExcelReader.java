package com.ttn.WebAutomation.utillib;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Hashtable;


public class ExcelReader {
    private static XSSFSheet sheet;
    private static XSSFWorkbook workbook = null;
    static File file;
    static FileInputStream inputStream;

    private static XSSFRow row = null;
    private static XSSFCell cell = null;

    public static String path;


    static String TEST_CASES_DATA_SHEET_NAME = "sanity";

    public synchronized static Object[][] read_data(String... args) throws IOException {

        String testName = args[0];
        if (args.length == 2) {
            TEST_CASES_DATA_SHEET_NAME = args[1];
        }
        path = "/resources/xlfiles/TestData.xlsx";


        file = new File(ObjectReader.getResourcePath(path));
        inputStream = new FileInputStream(file);
        workbook = new XSSFWorkbook(inputStream);
//        sheet = workbook.getSheet("Shet1");


        int testStartRowNum = 0;
        try {
            System.out.println(getRowCount(TEST_CASES_DATA_SHEET_NAME));
            for (int rNum = 1; rNum <= getRowCount(TEST_CASES_DATA_SHEET_NAME); rNum++) {
                try {
                    System.out.println(getCellData(TEST_CASES_DATA_SHEET_NAME, 0, rNum));
                    if (getCellData(TEST_CASES_DATA_SHEET_NAME, 0, rNum).equals(testName)) {
                        testStartRowNum = rNum;
                        break;
                    }
                } catch (Exception ex) {
                    System.out.println("------++++Test Case Skipped+++++------" + testName + "," + TEST_CASES_DATA_SHEET_NAME + "," + rNum);
                    System.out.println("------Print Exception Message------" + ex);

                }
            }
        } catch (Exception e1) {
            System.out.println("=========Check for Test case Name Or Runmode==========" + e1.getMessage());
        }
        int colStartRowNum = testStartRowNum + 1;
        int totalCols = 0;
        while (!getCellData(TEST_CASES_DATA_SHEET_NAME, totalCols, colStartRowNum).equals("")) {
            totalCols++;
        }
        int dataStartRowNum = testStartRowNum + 2;
        int totalRows = 0;
        while (!getCellData(TEST_CASES_DATA_SHEET_NAME, 0, dataStartRowNum + totalRows)
                .equals("")) {
            totalRows++;
        }

        Object[][] data = new Object[totalRows][1];
        int index = 0;
        Hashtable<String, String> table = null;
        try {
            System.out.println(">>>>>>>>>>>>DataStartRowNum: " + dataStartRowNum);
            System.out.println(">>>>>>>>>>>>(dataStartRowNum + totalRows): " + (dataStartRowNum + totalRows));
            System.out.println(">>>>>>>>>>>>totalCols: " + totalCols);
            System.out.println("++--Test Case:--++" + testName);
            for (int rNum = dataStartRowNum; rNum < (dataStartRowNum + totalRows); rNum++) {
                table = new Hashtable<String, String>();
                //Thread.sleep(3000);

                for (int cNum = 0; cNum < totalCols; cNum++) {
                    table.put(getCellData(TEST_CASES_DATA_SHEET_NAME, cNum, colStartRowNum),
                            getCellData(TEST_CASES_DATA_SHEET_NAME, cNum, rNum));
                    //Thread.sleep(3000);
                    //System.out.println("==========++++++++++++++++++++++++++===========");

                }
                data[index][0] = table;
                index++;
            }
        } catch (Exception ex) {
            System.out.println("Exception caused by : " + ex.getMessage());
            return null;
        }

        return data;
    }


    public static int getRowCount(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1)
            return 0;
        else {
            sheet = workbook.getSheetAt(index);
            int number = sheet.getLastRowNum() + 1;
            return number;
        }

    }

    // returns the data from a cell
    public String getCellData(String sheetName, String colName, int rowNum) {
        try {
            if (rowNum <= 0) {

                return "";
            }

            int index = workbook.getSheetIndex(sheetName);
            int col_Num = -1;
            if (index == -1) {

                return "";
            }
            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(0);
            for (int i = 0; i < row.getLastCellNum(); i++) {
                // System.out.println(row.getCell(i).getStringCellValue().trim());
                try {
                    if (row.getCell(i).getStringCellValue().trim().equals(colName.trim()))
                        col_Num = i;
                } catch (Exception ex) {
                    System.out.println("Error in row : " + row.getRowNum() + " , column is : " + i);
                }

            }
            if (col_Num == -1) {
                //System.out.println("+++++Empty string returned conditon col_Num = -1");
                return "";
            }
            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum - 1);
            if (row == null) {
                //	System.out.println("+++++Empty string returned conditon row = null");
                return "";
            }

            cell = row.getCell(col_Num);
            if (cell == null) {
                //	System.out.println("+++++Empty string returned conditon cell = null");
                return "";
            }
            // System.out.println(cell.getCellType());
            if (cell.getCellType() == CellType.STRING) {
                //System.out.println("+++++ Print Cell Value for CELL_TYPE_STRING +++++"+cell.getStringCellValue());
                return cell.getStringCellValue();
            } else if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {

                String cellText = String.valueOf(cell.getNumericCellValue());
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // format in form of M/D/YY
                    double d = cell.getNumericCellValue();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(HSSFDateUtil.getJavaDate(d));
                    cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.DAY_OF_MONTH) + "/" + cal.get(Calendar.MONTH) + 1 + "/" + cellText;
                }
                return cellText;
            } else if (cell.getCellType() == CellType.BLANK) {
                //System.out.println("+++++Empty string returned conditon cell.getCellType() == Cell.CELL_TYPE_BLANK");
                return "";
            } else {
                throw new IllegalArgumentException("+++++++++Invalid cell value for Cell type :  " + cell.getCellType()
                        + " and row : " + rowNum + "Column name" + colName);
                //return String.valueOf(cell.getBooleanCellValue());
            }
        } catch (Exception e) {


            return "row " + rowNum + " or column " + colName + " does not exist in xls";
        }
    }

    public static String getCellData(String sheetName, int colNum, int rowNum) {
        try {
            //System.out.println("Reading value for row : " + rowNum + " , column : " + colNum);
            if (rowNum <= 0) {
                //	System.out.println("Empty string returned conditon rowNum <= 0");
                return "";
            }
            int index = workbook.getSheetIndex(sheetName);
            if (index == -1) {
                //	System.out.println("Empty string returned conditon index == -1");
                return "";
            }
            sheet = workbook.getSheetAt(index);
            row = sheet.getRow(rowNum - 1);
            if (row == null) {
                //	System.out.println("Empty string returned conditon row == null");
                return "";
            }
            cell = row.getCell(colNum);
            if (cell == null) {
                //	System.out.println("Empty string returned conditon cell == null");
                return "";
            }
            if (cell.getCellType() == CellType.STRING) {
                try {
                    // System.out.println("----value from
                    // XLREADER-----"+cell.getStringCellValue());
                    //System.out.println("Cell value : " + cell.getStringCellValue());
                    return cell.getStringCellValue();
                } catch (Exception ex) {
                    System.out.println("Cell value In catch block: " + cell.toString());
                    return cell.toString();
                }
            } else if (cell.getCellType() == CellType.NUMERIC || cell.getCellType() == CellType.FORMULA) {

                String cellText = String.valueOf(cell.getNumericCellValue());
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // format in form of M/D/YY
                    double d = cell.getNumericCellValue();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(HSSFDateUtil.getJavaDate(d));
                    cellText = (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.MONTH) + 1 + "/" + cal.get(Calendar.DAY_OF_MONTH) + "/" + cellText;

                    // System.out.println(cellText);

                }
                System.out.println("Above Cell Text : " + cellText);
                return cellText;
            } else if (cell.getCellType() == CellType.BLANK) {
                //System.out.println("Empty string returned conditon cell.getCellType() == Cell.CELL_TYPE_BLANK");
                return "";
            }

            if (cell.getCellType() == CellType.BOOLEAN) {
                if (cell.getBooleanCellValue()) {
                    return "True";
                } else {
                    return "False";
                }
            } else {
                throw new IllegalArgumentException("Invalid cell value. Cell type : " + cell.getCellType()
                        + " and row : " + rowNum + " , column : " + colNum);


            }
        } catch (Exception e) {


            return "row " + rowNum + " or column " + colNum + " does not exist  in xls";
        }
    }


    // find whether sheets exists
    public boolean isSheetExist(String sheetName) {
        int index = workbook.getSheetIndex(sheetName);
        if (index == -1) {
            index = workbook.getSheetIndex(sheetName.toUpperCase());
            if (index == -1)
                return false;
            else
                return true;
        } else
            return true;
    }

    // returns number of columns in a sheet
    public int getColumnCount(String sheetName) {
        // check if sheet exists
        if (!isSheetExist(sheetName))
            return -1;

        sheet = workbook.getSheet(sheetName);
        row = sheet.getRow(0);

        if (row == null)
            return -1;

        return row.getLastCellNum();

    }

    public int getCellRowNum(String sheetName, String colName, String cellValue) {

        for (int i = 2; i <= getRowCount(sheetName); i++) {
            if (getCellData(sheetName, colName, i).equalsIgnoreCase(cellValue)) {
                return i;
            }
        }
        return -1;

    }

    // Function to check if the file is downloaded
    public static boolean isFileDownloaded(String downloadDir, String fileName) {
        File dir = new File(downloadDir);
        File[] files = dir.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().equals(fileName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
