package com.sinam.mybank.controller;

import com.sinam.mybank.model.BankAccountDTO;
import com.sinam.mybank.model.UserDTO;
import com.sinam.mybank.service.BankAccountService;
import com.sinam.mybank.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/user")
@Tag(name = "User Controller")
public class UserController {
    private final UserService userService;
    private final BankAccountService bankAccountService;

    public UserController(UserService userService, BankAccountService bankAccountService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserDTO getUser() {
        return userService.getUser();
    }

    @GetMapping("/accounts/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BankAccountDTO getUserBankAccountByBankAccountId(@PathVariable Long id) {
        return bankAccountService.getUserBankAccountByBankAccountId(id);
    }

    @GetMapping("/accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<BankAccountDTO> getUserBankAccounts() {
        return bankAccountService.getUserBankAccounts();
    }

    @GetMapping("/active-accounts")
    @ResponseStatus(HttpStatus.OK)
    public List<BankAccountDTO> getUserActiveBankAccounts(@RequestParam String fin) {
        return bankAccountService.getUserActiveBankAccounts(fin);
    }

}
