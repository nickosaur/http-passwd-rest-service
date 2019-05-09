package org.springframework.httprestservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

@SpringBootApplication
public class HttpRestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HttpRestServiceApplication.class, args);
	}
}
