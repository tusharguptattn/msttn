package com.ttn.WebAutomation.utillib;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExcelToCsvConvert {

    public String convertExcelToCSV(File excelFile) throws IOException, InvalidFormatException {
        String csvFilePath = excelFile.getAbsolutePath().replace(".xlsx", ".csv");
        try (Workbook workbook = new XSSFWorkbook(excelFile);
             FileWriter fileWriter = new FileWriter(csvFilePath);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    String cellValue = getCellValue(cell);
                    bufferedWriter.write(cellValue + ",");
                }
                bufferedWriter.newLine();
            }
        }
        return csvFilePath;
    }

    private String getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return "";
        }
    }
}
