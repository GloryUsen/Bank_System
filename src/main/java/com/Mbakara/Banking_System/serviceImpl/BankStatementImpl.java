package com.Mbakara.Banking_System.serviceImpl;

import com.Mbakara.Banking_System.dto.EmailDetailsDTO;
import com.Mbakara.Banking_System.entity.BankCustomers;
import com.Mbakara.Banking_System.entity.CustomerTransactions;
import com.Mbakara.Banking_System.repository.BankCustomersRepository;
import com.Mbakara.Banking_System.repository.TransactionRepository;
import com.Mbakara.Banking_System.service.BankStatementService;
import com.Mbakara.Banking_System.service.EmailService;
import com.itextpdf.text.*;


import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;



import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;



@Component
@AllArgsConstructor
@Slf4j
public class BankStatementImpl implements BankStatementService {


    private final TransactionRepository transactionRepository;
    private final BankCustomersRepository createUserRepository;
    private final EmailService emailService;



//    private final String FILE = "C:\\Users\\Admin\\Documents\\MyStatement.pdf";
      private final String FILE = "MyStatement.pdf";


    /**
     * Retrieving the list of Transaction within a date range given an account number.
     * Generate a pdf file of transaction
     * Send the file via email
     */
//    @Override
//    public List<CustomerTransactions> generateAccountStatement(String accountNumber, String startDate, String endDate){
//
//        /** NextLine: Converting startDate and endDate from String to LocalDateTime at the start of the day(00:00)
//         */
//        LocalDateTime start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE).atStartOfDay(); // This line helps me to convert the above stringDate inside the parameter into Date Format.
//        LocalDateTime end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE).atTime(23, 59,59);

//        List<Transaction> transactionList = transactionRepository.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
//                .filter(transaction -> transaction.getCreatedAt().isEqual(start)).filter(transaction -> transaction.getCreatedAt()
//                        .isEqual(end)).toList();
//
//        return transactionList;

//        return transactionRepository.findAll().stream()
//                .filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
//                .filter(transaction ->!transaction.getCreatedAt().isBefore(start) && !transaction.getCreatedAt()
//                        .isAfter(end))
//                .toList();

//        return transactionRepository.findByAccountNumberAndCreatedAtBetween(accountNumber, start, end);
//    }


//    @Override
//    public List<CustomerTransactions> generateAccountStatement(String accountNumber, String startDate, String endDate) {
//        LocalDateTime start = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_DATE).withHour(0).withMinute(0);
//        LocalDateTime end = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_DATE).withHour(23).withMinute(59);
//
//        return transactionRepository.findByAccountNumberAndCreatedAtBetween(accountNumber, start, end);
//    }
    @Override
    public List<CustomerTransactions> generateAccountStatement(String accountNumber, String startDate, String endDate) throws FileNotFoundException, DocumentException {
        log.info("Generating statement for Account: {}, Start: {}, End: {}", accountNumber, startDate, endDate);

        LocalDateTime start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE).atStartOfDay();
        LocalDateTime end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE).atTime(23, 59, 59);

        List<CustomerTransactions> transactionsList = transactionRepository.findByAccountNumberAndCreatedAtBetween(accountNumber, start, end);
        log.info("Found {} transactions", transactionsList.size());

        // Finding Account Number to be able to get the number for a particular account.
        BankCustomers user = createUserRepository.findByAccountNumber(accountNumber); // Finding accountNo.
        String customerName = user.getFirstName() + " " + user.getLastName() + " " +  user.getOtherName();

        Rectangle statementSize = new Rectangle(PageSize.A4);
        //com.itextpdf.text.Rectangle statementSize = new com.itextpdf.text.Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("Setting Size Of Document ");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        // Content To Our PDF
        // Table
        PdfPTable bankInfoTable = new PdfPTable(1); // Table and Number columns
        PdfPCell bankName = new PdfPCell(new Phrase("The Glorious Bank Project")); // This cell will be inside the new Table.
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.BLUE);
        bankName.setPadding(20f);

        // Company Name
        PdfPCell bankAddress = new PdfPCell(new Phrase("70 Mowokekere, Lagos State Nigeria"));
        bankAddress.setBorder(0);
        bankInfoTable.addCell(bankName);// Inserting Cells into the Table.
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementTable = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
        customerInfo.setBorder(0);

        PdfPCell statementOfAccount = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT "));
        statementOfAccount.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("End Date: " + endDate));
        stopDate.setBorder(0);

        /** Getting a customer name and address, since my Transaction entity doesn't actually contain
         * customer name, I used the accountNumber to retrieve accountName from the customer Entity, also added a
         * new dependency(CreateUserRepo)
         */

        PdfPCell customerDetails = new PdfPCell(new Phrase("Customer Name: " + customerName)); //This is the concatenated string up.
        customerDetails.setBorder(0);
        PdfPCell space = new PdfPCell();// Spaces on where the address is located on the file.
        space.setBorder(0);

        // To get Customer Address
        PdfPCell address = new PdfPCell(new Phrase("Customer Address " + user.getAddress()));
        address.setBorder(0);

        //Transaction Tables.
        PdfPTable transactionsTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBackgroundColor(BaseColor.BLUE);
        date.setBorder(0);
        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.BLUE);
        transactionType.setBorder(0);

        // Amount Of Transactions
        PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        transactionAmount.setBackgroundColor(BaseColor.BLUE);
        transactionAmount.setBorder(0);


        // The Status
        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        status.setBackgroundColor(BaseColor.BLUE);
        status.setBorder(0);

        transactionsTable.addCell(date);
        transactionsTable.addCell(transactionType);
        transactionsTable.addCell(transactionAmount);
        transactionsTable.addCell(status);


        /** fetching or stream all the records gotten inside the ListTransaction, and then populate the Table
         * with all the records from the List.
         */

        // So making use of the Variable Name.
       transactionsList.forEach(transaction -> {
           transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString())); // Each phrase, add newCell.
           transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
           transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
           transactionsTable.addCell(new Phrase(transaction.getStatus()));
       }); // This method helps iterate through single transaction that is inside the filtering list.


        /** Statement Information, which is rep by statementTable below, add all cell that suppose to be inside.
         *
         */
        statementTable.addCell(customerInfo);
        statementTable.addCell(statementOfAccount);
        statementTable.addCell(endDate);
        statementTable.addCell(customerDetails);
        statementTable.addCell(startDate);
        statementTable.addCell(space);
        statementTable.addCell(address);


        // Adding Tables Into The Documents.
        document.add(bankInfoTable);
        document.add(statementTable);
        document.add(transactionsTable);
        document.close();

        // Building An Object Of EmailService To Send Email
        EmailDetailsDTO emailDetailsDTO = EmailDetailsDTO.builder()
                .recipient(user.getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Kindly Find Your Requested Account Statement Attached!")
                .attachment(FILE)
                .build();

        emailService.sendEmailWithAttachment(emailDetailsDTO);



        return transactionsList;


    }
}








