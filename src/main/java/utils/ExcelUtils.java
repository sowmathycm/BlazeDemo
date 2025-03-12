package utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.IOException;
import java.io.InputStream;


import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtils {
    private static final String FILE_PATH = "src/main/resources/TestData.xlsx"; // Main resources


    public static List<List<String>> readExcelFile(String fileName) {

        List<List<String>> cityPairs = new ArrayList<>();
        try (InputStream is = ExcelUtils.class.getClassLoader().getResourceAsStream(fileName);
             Workbook workbook = new XSSFWorkbook(is)) {

            // Get the first sheet
            Sheet sheet = workbook.getSheetAt(0);

            // Iterate over rows
            for (int i=1; i<=sheet.getLastRowNum();i++) {
                Row row = sheet.getRow(i);
                List<String> cityPair = new ArrayList<>();

                for (Cell cell : row) {
                    cityPair.add(cell.getStringCellValue().trim());
                }
                cityPairs.add(cityPair);


            }



        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityPairs;
    }





}
