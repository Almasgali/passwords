package ru.almasgali.passwords;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;

@SpringBootApplication
@Slf4j
public class PasswordsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PasswordsApplication.class, args);
	}

}
