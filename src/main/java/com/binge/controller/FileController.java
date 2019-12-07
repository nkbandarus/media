package com.binge.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/v1/data")
public class FileController {

	public static final String INPUT_FILE_PATH = "src/main/resources/reader.txt";
	public static final String OUTPUT_FILE_PATH = "src/main/resources/reader.txt";
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<?> getData() {
		String file ="src/main/resources/reader.txt";
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try{
			reader = new BufferedReader(new FileReader(INPUT_FILE_PATH));
			writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH));
			List<String> lines = reader.lines().collect(Collectors.toList());
			for(String line : lines) {
				writer.write(line);
				writer.write("\n");
			}
			writer.close();
			reader.close();
		} catch(IOException ie){
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {

		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteData() throws Exception {
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try{
			reader = new BufferedReader(new FileReader(INPUT_FILE_PATH));
			writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH));
			List<String> lines = reader.lines().collect(Collectors.toList());
			for(String line : lines) {
				writer.write(line);
				writer.write("\n");
			}
			writer.close();
			reader.close();
		} catch(IOException ie){
			return new ResponseEntity<>(ie.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {

		}
		return new ResponseEntity<>(HttpStatus.OK);
	}


	@RequestMapping(value = "/", method = RequestMethod.PUT)
	public ResponseEntity<?> updateData() {
		String stringList = "11,22,33,44,55,66";
		System.out.println(Stream.of(stringList.split(",")).map(String::trim).collect(Collectors.toList()));
		Path path = Paths.get(INPUT_FILE_PATH);
		Path writerFilePath = Paths.get(OUTPUT_FILE_PATH);
		try {
			Stream<String> lines = Files.lines(path);
			List <String> replaced = lines.map(line -> line.replaceAll("FirstLine", "Replaced")).collect(Collectors.toList());
			Files.write(writerFilePath, replaced);
		} catch(IOException ie) {
			return new ResponseEntity<>(ie, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
