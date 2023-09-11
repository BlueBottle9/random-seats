package io.github.fsaxz.randomseats;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

public class Main {
	protected static Logger logger = LoggerFactory.getLogger("RandomSeat");
	public static ArrayList<Student> students;
	public static ArrayList<Student> males = new ArrayList<>();
	public static ArrayList<Student> females = new ArrayList<>();
	public static ArrayList<SeatGroup> seatGruops;
	public static TomlParseResult configuration;

	public static void main(String[] args) {
		logger.info("RandomSeats v0.0.3, by FsaxZ");
		loadResources();
		Printer.print(Assignment.assign(seatGruops, females,males));
	}

	public static void loadResources() {
		logger.info("Loading resources.....");
		try {
			File configFolder = new File("./config");
			if (!configFolder.exists()) {
				configFolder.mkdirs();
			} else if (!configFolder.isDirectory()) {
				configFolder.delete();
				configFolder.mkdirs();
			}

			File config = new File(configFolder, "list.json");
			boolean flag = false;
			if (!config.exists()) {
				flag = true;
			} else if (!config.isFile()) {
				config.delete();
				flag = true;
			}

			File seatsConfig = new File(configFolder, "seats.json");
			if (!seatsConfig.exists()) {
				flag = true;
			} else if (!seatsConfig.isFile()) {
				seatsConfig.delete();
				flag = true;
			}

			File seatTemplate = new File(configFolder, "template.xlsx");
			if (!seatTemplate.exists()) {
				flag = true;
			} else if (!seatTemplate.isFile()) {
				seatTemplate.delete();
				flag = true;
			}
			
			File configFile = new File(configFolder, "config.toml");
			if (!configFile.exists()) {
				flag = true;
			} else if (!configFile.isFile()) {
				configFile.delete();
				flag = true;
			}

			if (flag) {
				copyDefaultResources();
			}

			logger.info("Loading student list...");
			
			Gson gson = new Gson();
			students = gson.fromJson(new JsonReader(new FileReader(config)), new TypeToken<ArrayList<Student>>() {
			}.getType());

			logger.info(students.size() + " students loaded");

			for (Student student : students) {
				if (student.getSex() == Sex.MALE) {
					males.add(student);
				} else {
					females.add(student);
				}
			}

			logger.info("Loading seat groups...");
			
			seatGruops = gson.fromJson(new JsonReader(new FileReader(seatsConfig)),
					new TypeToken<ArrayList<SeatGroup>>() {
					}.getType());

			logger.info("Loading the Excel template...");
			Printer.loadTemplate(seatTemplate);

			logger.info("Loading config...");
			
			configuration = Toml.parse(configFile.toPath());
			
		} catch (Exception e) {
			logger.error("",e);
		}
	}

	public static void copyDefaultResources() {
		try {
			File config = new File("./config/list.json");
			File seatsConfig = new File("./config/seats.json");
			File seatsTemplate = new File("./config/template.xlsx");
			File configFile = new File("./config/config.toml");
			
			if (!config.exists()) {
				logger.info("list.json is not found, loading default config");
				copyResource("list.json");
			}

			if (!seatsConfig.exists()) {
				logger.info("seats.json is not found, loading default config");
				copyResource("seats.json");
			}

			if (!seatsTemplate.exists()) {
				logger.info("template.xlsx is not found, loading default config");
				copyResource("template.xlsx");
			}
			
			if (!configFile.exists()) {
				logger.info("config.toml is not found, loading default config");
				copyResource("config.toml");
			}

		} catch (Exception e) {
			logger.error("",e);
		}
	}
	
	private static void copyResource(String name) {
		try {
			Files.copy(Main.class.getResourceAsStream("/resources/" + name), new File("./config/" + name).toPath());
			//Files.copy(Main.class.getResourceAsStream("/" + name), new File("./config/" + name).toPath());
		}catch (Exception e) {
			logger.error("", e);
		}
	}

}
