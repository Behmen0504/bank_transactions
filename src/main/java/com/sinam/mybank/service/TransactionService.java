package com.sinam.mybank.service;

import com.sinam.mybank.dao.entity.BankAccountEntity;
import com.sinam.mybank.dao.entity.TransactionEntity;
import com.sinam.mybank.dao.repository.BankAccountRepository;
import com.sinam.mybank.dao.repository.TransactionRepository;
import com.sinam.mybank.mapper.TransactionMapper;
import com.sinam.mybank.model.TransactionDTO;
import com.sinam.mybank.model.exception.NotFoundException;
import com.sinam.mybank.model.exception.TransactionException;
import com.sinam.mybank.model.requests.TransactionRequestDTO;
import com.sinam.mybank.model.specifications.TransactionSpecifications;
import com.sinam.mybank.service.auth.AuthService;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;

    public TransactionService(TransactionRepository transactionRepository, BankAccountRepository bankAccountRepository) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    public List<TransactionDTO> getTransactions(Long id, Long senderAccountId,String fin) {
        Specification<TransactionEntity> spec = Specification.where(TransactionSpecifications.hasTransactionId(id))
                .and(TransactionSpecifications.hasSenderAccountId(senderAccountId))
                .and(TransactionSpecifications.hasUserFin(fin));

        return TransactionMapper.INSTANCE.mapEntitiesToDtos(transactionRepository.findAll(spec));
    }

    public List<TransactionDTO> getAllTransactionsByUserId() {
        return TransactionMapper.INSTANCE.mapEntitiesToDtos(transactionRepository.findAllByUserId(AuthService.getUser().getId()));
    }

    public List<TransactionDTO> getTransactionsByUserId() {
        return TransactionMapper.INSTANCE.mapEntitiesToDtos(transactionRepository.findTransactionsByUserId(AuthService.getUser().getId()));
    }

    @Transactional
    public void addTransaction(TransactionRequestDTO transactionRequestDTO) {
        Long userId = AuthService.getUser().getId();
        BankAccountEntity senderBankAccountEntity = bankAccountRepository.findByIdForTransfer(transactionRequestDTO.getSenderAccountId()).orElseThrow(
                () -> new NotFoundException("BANK_ACCOUNT_NOT_FOUND")
        );

        if (!Objects.equals(userId, senderBankAccountEntity.getUserEntity().getId())) {
            throw new TransactionException("ACCOUNT_IS_NOT_CLOSED_TO_USER");
        }

        BankAccountEntity receiverrBankAccountEntity = bankAccountRepository.findByIdForTransfer(transactionRequestDTO.getReceiverAccountId()).orElseThrow(
                () -> new NotFoundException("BANK_ACCOUNT_NOT_FOUND")
        );

        if (!(senderBankAccountEntity.getBalance().compareTo(transactionRequestDTO.getAmount()) >= 0)) {
            throw new TransactionException("AMOUNT_GREATER_THAN_BALANCE");
        }
        senderBankAccountEntity.setBalance(senderBankAccountEntity.getBalance().subtract(transactionRequestDTO.getAmount()));
        receiverrBankAccountEntity.setBalance(receiverrBankAccountEntity.getBalance().add(transactionRequestDTO.getAmount()));
        List<BankAccountEntity> bankAccountEntities = new ArrayList<>();
        bankAccountEntities.add(senderBankAccountEntity);
        bankAccountEntities.add(receiverrBankAccountEntity);
        bankAccountRepository.saveAll(bankAccountEntities);
        transactionRepository.save(TransactionMapper.INSTANCE.mapTransactionRequestDtoToEntity(transactionRequestDTO));
    }
}
