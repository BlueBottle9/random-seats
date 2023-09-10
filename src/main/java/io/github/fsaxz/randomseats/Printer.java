package io.github.fsaxz.randomseats;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;

public class Printer {
	protected static Workbook template;
			public static void loadTemplate(File file) {
							try {
								Main.logger.info("Loading the template...");
								template = new XSSFWorkbook(file);
							} catch (InvalidFormatException | IOException e) {
								Main.logger.log(Level.WARNING, "", e);
							}
			}
			
			public static void print(HashMap<Seat, Student> assignment) {

				Main.logger.info("Outputing the result...");
				
				File resultFolder = new File("./result/");
				if (!resultFolder.exists()) {
					resultFolder.mkdirs();
				}
				if (!resultFolder.isDirectory()) {
					resultFolder.delete();
					resultFolder.mkdirs();
				}
				
				File resultStorage = new File(resultFolder, new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date()));
				if (!resultStorage.exists()) {
					resultStorage.mkdirs();
				}
				if (!resultStorage.isDirectory()) {
					resultStorage.delete();
					resultStorage.mkdirs();
				}
				
				File excelFile = new File(resultStorage, "result.xlsx");
				if (excelFile.exists()) {
					excelFile.delete();
				}
				
				File jsonFile = new File(resultStorage, "result.json");
				if (jsonFile.exists()) {
					jsonFile.delete();
				}
				
				printJson(assignment, jsonFile);
				printExcel(assignment, excelFile);
				
				Main.logger.info("Assignment completed");
				Main.logger.info("Opening result...");
				
				try {
					Runtime.getRuntime().exec("explorer.exe " + excelFile.getAbsolutePath());
				} catch (IOException e) {
					Main.logger.log(Level.WARNING, "", e);
				}
			}
			
			private static void printExcel(HashMap<Seat, Student> assignment, File target) {
				try {
					Sheet sheet = template.getSheet("Main");
					for (int row =0;row < sheet.getLastRowNum() + 1;row++) {
						Row currentRow = sheet.getRow(row);
						if (currentRow == null) {
							continue;
						}
						
						for (int column = 0;column < 100;column++) {
							Cell cell = currentRow.getCell(column);
							if (cell == null) {
								continue;
							}
							for (Seat seat : assignment.keySet()) {
								String value = cell.getStringCellValue().replaceFirst("#","");
								if (seat.getFullID().equals(value)) {
									cell.setCellValue(assignment.get(seat).getName());
								}
							}
						}
						
					}
					
					template.write(new FileOutputStream(target));
					
					Main.logger.info("Result formatted with Excel has saved to " + target.getPath());
				}catch (Exception e) {
					Main.logger.log(Level.WARNING, "", e);
				}
				
			}
			
			private static void printJson(HashMap<Seat, Student> assignment, File target) {
					Gson gson = new Gson();
					try {
						gson.toJson(assignment);
						Files.write(target.toPath(), gson.toJson(assignment).getBytes(StandardCharsets.UTF_8));
						
						Main.logger.info("Result formatted with Json has saved to " + target.getPath());
					} catch (JsonIOException | IOException e) {
						Main.logger.log(Level.WARNING, "", e);
					}
			}
}
