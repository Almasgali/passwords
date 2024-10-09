package ru.almasgali.passwords.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.almasgali.passwords.model.data.PasswordEntity;
import ru.almasgali.passwords.repository.PasswordRepository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class CsvService {

    public static final String FILE_NAME = "passwords_csv.txt";
    private static final String[] CSV_HEADERS_EXPORT = {"id", "name", "comment", "password", "updated_at"};
    private static final String[] CSV_HEADERS_IMPORT = {"name", "comment", "password", "updated_at"};
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final PasswordRepository passwordRepository;
    private final Path csvPath = Path.of(System.getProperty("java.io.tmpdir")).resolve(FILE_NAME);

    @Autowired
    public CsvService(PasswordRepository passwordRepository) {
        this.passwordRepository = passwordRepository;
    }

    public Resource getCsvResource() {
        try {
            exportToCsv();
            return new UrlResource(csvPath.toUri());
        } catch (IOException e) {
            log.error("Can't write csv to file: {}", e.getMessage());
            throw new RuntimeException("Csv export error.", e);
        }
    }

    private void exportToCsv() throws IOException {
        List<PasswordEntity> passwords = passwordRepository.findAll();
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(CSV_HEADERS_EXPORT)
                .build();
        try (final CSVPrinter printer = new CSVPrinter(
                new PrintWriter(Files.newOutputStream(csvPath), true),
                csvFormat)) {
            passwords.forEach(p -> {
                try {
                    printer.printRecord(
                            p.getId(),
                            p.getName(),
                            p.getComment(),
                            p.getPassword(),
                            formatter.format(p.getUpdatedAt()));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            });
        }
    }

    public void importFromCsv(MultipartFile file) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(CSV_HEADERS_IMPORT)
                .setSkipHeaderRecord(true)
                .build();
        try (Reader r = new InputStreamReader(file.getInputStream())) {
            Iterable<CSVRecord> records = csvFormat.parse(r);
            for (CSVRecord record : records) {
                PasswordEntity password = new PasswordEntity();
                password.setName(record.get("name"));
                password.setPassword(record.get("password"));
                password.setComment(record.get("comment"));
                password.setUpdatedAt(LocalDateTime.parse(record.get("updated_at"), formatter));
                passwordRepository.save(password);
            }
        } catch (IOException e) {
            log.error("Can't import from csv file: {}", e.getMessage());
            throw new RuntimeException("Csv export error.", e);
        }
    }
}
