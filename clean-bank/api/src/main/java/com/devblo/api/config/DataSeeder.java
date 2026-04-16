package com.devblo.api.config;

import com.devblo.account.Account;
import com.devblo.account.AccountNumber;
import com.devblo.account.AccountType;
import com.devblo.account.repository.IAccountWriteRepository;
import com.devblo.common.security.PasswordEncoderPort;
import com.devblo.customer.Address;
import com.devblo.customer.Customer;
import com.devblo.customer.PersonalInfo;
import com.devblo.customer.repository.ICustomerWriteRepository;
import com.devblo.shared.Email;
import com.devblo.shared.Money;
import com.devblo.transaction.Transaction;
import com.devblo.transaction.repository.ITransactionWriteRepository;
import com.devblo.user.Role;
import com.devblo.user.User;
import com.devblo.user.repository.IUserReadRepository;
import com.devblo.user.repository.IUserWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;

@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final IUserReadRepository userReadRepository;
    private final IUserWriteRepository userWriteRepository;
    private final ICustomerWriteRepository customerWriteRepository;
    private final IAccountWriteRepository accountWriteRepository;
    private final ITransactionWriteRepository transactionWriteRepository;
    private final PasswordEncoderPort passwordEncoderPort;

    private static final String SEED_CHECK_EMAIL = "elif.yilmaz@cleanbank.com";

    @Override
    @Transactional
    public void run(String... args) {
        if (userReadRepository.findUserByEmail(SEED_CHECK_EMAIL).isPresent()) {
            log.info("Seed data already exists. Skipping.");
            return;
        }

        log.info("Seeding demo data...");
        String defaultPassword = passwordEncoderPort.encode("demo123");

        // ─── Employee ────────────────────────────────────────────────
        seedEmployee("elif.yilmaz@cleanbank.com", "Elif", "Yılmaz",
                LocalDate.of(1992, 3, 15), "Bağdat Caddesi No:42", "Kadıköy", defaultPassword);

        seedEmployee("can.demir@cleanbank.com", "Can", "Demir",
                LocalDate.of(1988, 7, 22), "İstiklal Caddesi No:105", "Beyoğlu", defaultPassword);

        // ─── Customers ───────────────────────────────────────────────

        // Customer 1: Ayşe Kara — active customer, multiple accounts, transaction history
        var ayse = seedCustomer("ayse.kara@gmail.com", "Ayşe", "Kara",
                LocalDate.of(1995, 5, 12), "Atatürk Bulvarı No:78", "Çankaya", defaultPassword);

        var ayseChecking = seedAccount(ayse, AccountType.CHECKING, "TRY");
        seedDeposit(ayseChecking, "15000.00", "TRY", "Maaş yatırma");
        seedDeposit(ayseChecking, "2500.00", "TRY", "Freelance ödeme");
        seedWithdraw(ayseChecking, "3200.00", "TRY", "Kira ödemesi");
        seedWithdraw(ayseChecking, "450.00", "TRY", "Market alışverişi");

        var ayseSavings = seedAccount(ayse, AccountType.SAVINGS, "TRY");
        seedDeposit(ayseSavings, "50000.00", "TRY", "Birikim transferi");

        var ayseUsd = seedAccount(ayse, AccountType.CHECKING, "USD");
        seedDeposit(ayseUsd, "2000.00", "USD", "Yurtdışı ödeme");

        // Customer 2: Mehmet Öztürk — active customer, investment account
        var mehmet = seedCustomer("mehmet.ozturk@hotmail.com", "Mehmet", "Öztürk",
                LocalDate.of(1980, 11, 3), "Tunalı Hilmi Caddesi No:22", "Kavaklıdere", defaultPassword);

        var mehmetChecking = seedAccount(mehmet, AccountType.CHECKING, "TRY");
        seedDeposit(mehmetChecking, "45000.00", "TRY", "Maaş yatırma");
        seedWithdraw(mehmetChecking, "12000.00", "TRY", "Araç taksit ödemesi");
        seedWithdraw(mehmetChecking, "2800.00", "TRY", "Fatura ödemeleri");

        var mehmetInvestment = seedAccount(mehmet, AccountType.INVESTMENT, "TRY");
        seedDeposit(mehmetInvestment, "120000.00", "TRY", "Yatırım fonu alımı");

        var mehmetEur = seedAccount(mehmet, AccountType.SAVINGS, "EUR");
        seedDeposit(mehmetEur, "5000.00", "EUR", "Avrupa seyahati birikimleri");

        // Customer 3: Zeynep Aydın — a few accounts
        var zeynep = seedCustomer("zeynep.aydin@outlook.com", "Zeynep", "Aydın",
                LocalDate.of(1998, 2, 28), "Kızılay Meydanı No:5/A", "Çankaya", defaultPassword);

        var zeynepChecking = seedAccount(zeynep, AccountType.CHECKING, "TRY");
        seedDeposit(zeynepChecking, "8500.00", "TRY", "Maaş yatırma");
        seedWithdraw(zeynepChecking, "1200.00", "TRY", "Online alışveriş");

        // Customer 4: Ali Şahin — active, single account
        var ali = seedCustomer("ali.sahin@gmail.com", "Ali", "Şahin",
                LocalDate.of(1975, 9, 17), "Konur Sokak No:14", "Kızılay", defaultPassword);

        var aliChecking = seedAccount(ali, AccountType.CHECKING, "TRY");
        seedDeposit(aliChecking, "32000.00", "TRY", "Emekli maaşı");
        seedWithdraw(aliChecking, "5500.00", "TRY", "Sağlık harcaması");
        seedDeposit(aliChecking, "32000.00", "TRY", "Emekli maaşı");

        var aliSavings = seedAccount(ali, AccountType.SAVINGS, "TRY");
        seedDeposit(aliSavings, "85000.00", "TRY", "Vadeli hesap transferi");

        // Customer 5: Fatma Çelik — small account
        var fatma = seedCustomer("fatma.celik@yahoo.com", "Fatma", "Çelik",
                LocalDate.of(2001, 12, 5), "Bahçelievler 7. Cadde No:33", "Bahçelievler", defaultPassword);

        var fatmaChecking = seedAccount(fatma, AccountType.CHECKING, "TRY");
        seedDeposit(fatmaChecking, "4200.00", "TRY", "Part-time maaş");

        // Transfer between two customers
        var transferAmount = Money.of(new BigDecimal("1500.00"), Currency.getInstance("TRY").toString());
        ayseChecking.withdraw(transferAmount);
        mehmetChecking.deposit(transferAmount);
        accountWriteRepository.save(ayseChecking);
        accountWriteRepository.save(mehmetChecking);
        var tx = Transaction.transfer(ayseChecking.getId(), mehmetChecking.getId(),
                transferAmount, "Borç ödemesi");
        transactionWriteRepository.save(tx.getValue());

        log.info("Demo data seeded: 2 employees, 5 customers with accounts and transactions.");
    }

    private void seedEmployee(String email, String firstName, String lastName,
                              LocalDate dob, String street, String district, String encodedPassword) {
        var personalInfo = PersonalInfo.of(firstName, lastName, email, dob, encodedPassword);
        var address = Address.of(street, district);
        var customer = Customer.register(personalInfo, address);
        customerWriteRepository.save(customer);

        var user = User.create(Email.of(email), encodedPassword, Role.EMPLOYEE, customer.getId());
        userWriteRepository.save(user);
        log.info("  Employee: {} {}", firstName, lastName);
    }

    private Customer seedCustomer(String email, String firstName, String lastName,
                                  LocalDate dob, String street, String district, String encodedPassword) {
        var personalInfo = PersonalInfo.of(firstName, lastName, email, dob, encodedPassword);
        var address = Address.of(street, district);
        var customer = Customer.register(personalInfo, address);
        customerWriteRepository.save(customer);

        var user = User.create(Email.of(email), encodedPassword, Role.CUSTOMER, customer.getId());
        userWriteRepository.save(user);

        // Default checking account (same as RegisterCustomerCommandHandler)
        var defaultAccount = Account.open(AccountNumber.generate(), customer.getId(),
                AccountType.CHECKING, Currency.getInstance("TRY"));
        accountWriteRepository.save(defaultAccount);

        log.info("  Customer: {} {}", firstName, lastName);
        return customer;
    }

    private Account seedAccount(Customer customer, AccountType type, String currencyCode) {
        var account = Account.open(AccountNumber.generate(), customer.getId(),
                type, Currency.getInstance(currencyCode));
        accountWriteRepository.save(account);
        return account;
    }

    private void seedDeposit(Account account, String amount, String currency, String description) {
        var money = Money.of(new BigDecimal(amount), currency);
        account.deposit(money);
        accountWriteRepository.save(account);

        var tx = Transaction.deposit(account.getId(), money, description);
        transactionWriteRepository.save(tx.getValue());
    }

    private void seedWithdraw(Account account, String amount, String currency, String description) {
        var money = Money.of(new BigDecimal(amount), currency);
        account.withdraw(money);
        accountWriteRepository.save(account);

        var tx = Transaction.withdraw(account.getId(), money, description);
        transactionWriteRepository.save(tx.getValue());
    }
}
