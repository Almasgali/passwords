package ru.almasgali.passwords.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.almasgali.passwords.model.data.PasswordEntity;
import ru.almasgali.passwords.model.dto.PasswordRequest;
import ru.almasgali.passwords.service.PasswordService;

import java.util.List;

@RestController
@RequestMapping("/password")
public class PasswordController {

    private final PasswordService passwordService;

    @Autowired
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @GetMapping
    public ResponseEntity<List<PasswordEntity>> getPassword(
            @RequestParam String password,
            @RequestParam boolean contains) {
        return ResponseEntity.ok(passwordService.getPassword(password, contains));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PasswordEntity> getPassword(@PathVariable long id) {
        return ResponseEntity.ok(passwordService.getPassword(id));
    }

    @GetMapping("/revs/{id}")
    public ResponseEntity<List<PasswordEntity>> getPasswordRevs(@PathVariable long id) {
        return ResponseEntity.ok(passwordService.getPasswordRevs(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void putPassword(@RequestBody PasswordRequest passwordRequest) {
        passwordService.savePassword(passwordRequest);
    }

    @PatchMapping("/{id}")
    public void updatePassword(@RequestBody PasswordRequest passwordRequest, @PathVariable long id) {
        passwordService.updatePassword(passwordRequest, id);
    }

    @DeleteMapping("/{id}")
    public void deletePassword(@PathVariable long id) {
        passwordService.deletePassword(id);
    }
}
