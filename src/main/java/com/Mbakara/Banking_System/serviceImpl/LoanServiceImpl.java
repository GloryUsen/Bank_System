package com.Mbakara.Banking_System.serviceImpl;
import com.Mbakara.Banking_System.dto.*;
import com.Mbakara.Banking_System.entity.BankCustomers;
import com.Mbakara.Banking_System.entity.CustomersLoan;
import com.Mbakara.Banking_System.repository.BankCustomersRepository;
import com.Mbakara.Banking_System.repository.LoanRepository;
import com.Mbakara.Banking_System.service.EmailService;
import com.Mbakara.Banking_System.service.LoanService;
import com.Mbakara.Banking_System.service.TransactionService;
import com.Mbakara.Banking_System.util.AccountUtils;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor

public class LoanServiceImpl implements LoanService {


    @Autowired
    LoanRepository loanRepository;

    @Autowired
    TransactionService transactionService;


    @Autowired
    EmailService emailService;

    @Autowired
    private BankCustomersRepository createUserRepository;


    @Override
    @Transactional
    public CustomersLoanResponseDTO customerAppliesForLoan(CustomersLoanRequestDTO request) {
        // check if User exists
        BankCustomers newUser = createUserRepository.findByAccountNumber(request.getAccountNumber());
        if (newUser == null){
            return CustomersLoanResponseDTO.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .loanInfo(null)
                    .build();
        }

        // Check if the user already has an active loan.
        boolean hasActiveLoanAccount = loanRepository.existsByAccountNumberAndStatus(request.getAccountNumber(), "ACTIVE");
        if (hasActiveLoanAccount){
            return CustomersLoanResponseDTO.builder()
                    .responseCode(AccountUtils.LOAN_ALREADY_EXISTS_CODE)
                    .responseMessage(AccountUtils.LOAN_ALREADY_EXISTS_MESSAGE)
                    .loanInfo(null)
                    .build();
        }

        // Validate loan amount example must be greater than zero.
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0){
            return CustomersLoanResponseDTO.builder()
                    .responseCode(AccountUtils.INVALID_LOAN_AMOUNT_CODE)
                    .responseMessage(AccountUtils.INVALID_LOAN_AMOUNT_MESSAGE)
                    .loanInfo(null)
                    .build();
        }

        // Calculate loan repayment terms
        BigDecimal interestRate = new BigDecimal("0.05"); // 5% interest
        BigDecimal totalAmountRepayment = request.getAmount().multiply(BigDecimal.ONE.add(interestRate));
        LocalDateTime dueDate = LocalDateTime.now().plusMonths(request.getRepaymentPeriod());


        // Save Loan to Database
        CustomersLoan newLoan = CustomersLoan.builder()
                .accountNumber(request.getAccountNumber())
                .amountBorrowed(request.getAmount())
                .totalAmountRepayment(totalAmountRepayment)
                .balanceToPay(totalAmountRepayment)
                .dueDate(dueDate)
                .status("ACTIVE")
                .build();

       loanRepository.save(newLoan);

        // Crediting the user's account with loan amount

        newUser.setAccountBalance(newUser.getAccountBalance().add(request.getAmount()));
        createUserRepository.save(newUser);

        // Sending Email Notification
        try {
            emailService.sendEmailAlert(EmailDetailsDTO.builder()
                            .recipient(newUser.getEmail())
                            .subject("LOAN APPROVED")
                            .messageBody("Your loan request of " + request.getAmount() + "has been approved.\n" +
                                    "Total repayment: " + totalAmountRepayment + "\nDue Date:" + dueDate)
                    .build());

        } catch (MailException e) {
            System.out.println("Failed to send loan approval email.");
        }


        return CustomersLoanResponseDTO.builder()
                .responseCode(AccountUtils.LOAN_APPROVAL_CODE)
                .responseMessage(AccountUtils.LOAN_APPROVAL_MESSAGE)
//                .loanInfo(CustomersLoan.builder()
//                        .accountNumber(request.getAccountNumber())
//                        .amountBorrowed(request.getAmount())
//                        .totalAmountRepayment(totalAmountRepayment)
//                        .balanceToPay(totalAmountRepayment)
//                        .dueDate(dueDate)
//                        .build())
                .loanInfo(CustomersLoanInfoDTO.builder()
                        .accountNumber(request.getAccountNumber())
                        .amountBorrowed(request.getAmount())
                        .totalAmountRepayment(totalAmountRepayment)
                        .balanceToPay(totalAmountRepayment)
                        .dueDate(dueDate)
                        .status("ACTIVE")
                        .build())
                .build();

    }


    @Override
    @Transactional
    public CustomersLoanResponseDTO customerRepayLoan(CustomersLoanRepaymentDTO request) {
        // Check if the customer exists
                BankCustomers newUser = createUserRepository.findByAccountNumber(request.getAccountNumber());
                if (newUser == null){
                    return CustomersLoanResponseDTO.builder()
                            .responseCode(AccountUtils.ACCOUNT_EXIST_CODE)
                            .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                            .loanInfo(null)
                            .build();
                }

                // Fetching an active Loan status of a user
        CustomersLoan newLoan = loanRepository.findByAccountNumberAndStatus(request.getAccountNumber(), "ACTIVE");
                if (newLoan == null){
                    return CustomersLoanResponseDTO.builder()
                            .responseCode(AccountUtils.NO_ACTIVE_LOAN_CODE)
                            .responseMessage(AccountUtils.NO_ACTIVE_LOAN_MESSAGE)
                            .loanInfo(null)
                            .build();
                }

                // Checking if the Repayment Amount is valid (Validating Repayment)
        //please note the next line of code
       // if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0 || request.getAmount().compareTo(newUser.getAccountNumber()) > 0){
        if (request.getAmount().compareTo(BigDecimal.ZERO) < 0 || request.getAmount().compareTo(newLoan.getBalanceToPay()) > 0){
            return CustomersLoanResponseDTO.builder()
                    .responseCode(AccountUtils.INVALID_REPAYMENT_AMOUNT_CODE)
                    .responseMessage(AccountUtils.INVALID_REPAYMENT_AMOUNT_MESSAGE)
                    .loanInfo(null)
                    .build();
        }

        // Checking if the customer has insufficient balance

        if (newUser.getAccountBalance().compareTo(request.getAmount()) < 0){
            return CustomersLoanResponseDTO.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .loanInfo(null)
                    .build();
        }

        // Deducting the repayment amount
        newUser.setAccountBalance(newUser.getAccountBalance().subtract(request.getAmount()));
        createUserRepository.save(newUser);

        // Update Loan Balance

        newLoan.setBalanceToPay(newLoan.getBalanceToPay().subtract(request.getAmount()));
        if (newLoan.getBalanceToPay().compareTo(BigDecimal.ZERO) == 0){
            newLoan.setStatus("PAID"); // Marked as fully paid
        }
        loanRepository.save(newLoan);

        // Sending a repayment email confirmation to customers.
        try{
            emailService.sendEmailWithAttachment(EmailDetailsDTO.builder()
                            .recipient(newUser.getEmail())
                            .subject("LOAN REPAYMENT CONFIRMATION")
                            .messageBody("You have successfully repaid " + request.getAmount() + ".\n" +
                                    "Remaining balance: " + newLoan.getBalanceToPay())
                    .build());

        } catch (MailException e){
            System.out.println("Failed to to send repayment confirmation email.");
        }
        return CustomersLoanResponseDTO.builder()
                .responseCode(AccountUtils.LOAN_REPAID_SUCCESS_CODE)
                .responseMessage(AccountUtils.LOAN_REPAID_SUCCESS_MESSAGE)
                .loanInfo(CustomersLoanInfoDTO.builder()
                        .accountNumber(request.getAccountNumber())
                        .amountBorrowed(newLoan.getAmountBorrowed())
                        .totalAmountRepayment(newLoan.getTotalAmountRepayment())
                        .balanceToPay(newLoan.getBalanceToPay())
                        .dueDate(newLoan.getDueDate())
                        .status(newLoan.getStatus())
                        .build())
                .build();
    }

}
