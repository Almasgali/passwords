package ru.almasgali.passwords.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.almasgali.passwords.model.data.PasswordEntity;
import ru.almasgali.passwords.model.dto.PasswordRequest;
import ru.almasgali.passwords.service.CsvService;
import ru.almasgali.passwords.service.PasswordService;

import java.util.List;

@RestController
@RequestMapping("/password")
public class PasswordController {

    private final PasswordService passwordService;
    private final CsvService csvService;

    @Autowired
    public PasswordController(PasswordService passwordService, CsvService csvService) {
        this.passwordService = passwordService;
        this.csvService = csvService;
    }

    @Operation(summary = "Получить пароль по полному или частичному совпадению")
    @GetMapping
    public ResponseEntity<List<PasswordEntity>> getPassword(
            @Parameter(description = "Искомый пароль")
            @RequestParam
            String password,
            @Parameter(description = "true - выдавать по частичному совпадению, иначе по полному")
            @RequestParam
            boolean contains) {
        return ResponseEntity.ok(passwordService.getPassword(password, contains));
    }

    @Operation(summary = "Получить пароль по id")
    @GetMapping("/{id}")
    public ResponseEntity<PasswordEntity> getPassword(@PathVariable long id) {
        return ResponseEntity.ok(passwordService.getPassword(id));
    }

    @Operation(summary = "Получить историю изменений пароля по его id (от нового к старому).")
    @GetMapping("/revs/{id}")
    public ResponseEntity<List<PasswordEntity>> getPasswordRevs(@PathVariable long id) {
        return ResponseEntity.ok(passwordService.getPasswordRevs(id));
    }

    @Operation(summary = "Создать новый пароль")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void putPassword(@RequestBody PasswordRequest passwordRequest) {
        passwordService.savePassword(passwordRequest);
    }

    @Operation(summary = "Изменить существующий пароль")
    @PatchMapping("/{id}")
    public void updatePassword(@RequestBody PasswordRequest passwordRequest, @PathVariable long id) {
        passwordService.updatePassword(passwordRequest, id);
    }

    @Operation(summary = "Удалить пароль по id")
    @DeleteMapping("/{id}")
    public void deletePassword(@PathVariable long id) {
        passwordService.deletePassword(id);
    }

    @Operation(summary = "Экспортировать пароли в csv")
    @ResponseBody
    @GetMapping("/csv")
    public ResponseEntity<Resource> exportCsv() {

        Resource file = csvService.getCsvResource();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + CsvService.FILE_NAME + "\"").body(file);
    }

    @Operation(summary = "Импортировать пароли из csv")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void importCsv(
            @Parameter(description = "Файл для загрузки и импорта")
            @RequestParam("file")
            MultipartFile file) {
        csvService.importFromCsv(file);
    }
}
