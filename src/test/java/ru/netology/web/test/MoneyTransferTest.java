package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPageV2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.codeborne.selenide.Selenide.*;


class MoneyTransferTest {

    @BeforeEach
    public void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTransferFromSecondToFirstCard() {
        val amount = 9999;
        val loginPage = new LoginPageV2();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.checkHeadingYourCards();
        val initialBalanceToCard = dashboardPage.getFirstCardBalance();
        val initialBalanceFromCard = dashboardPage.getSecondCardBalance();
        val transferPage = dashboardPage.validChoosePay1();
        transferPage.checkHeadingPaymentCards();
        transferPage.setPayCardNumber(DataHelper.getSecondCard(), amount);
        val dashboardPage1 = transferPage.validPayCard();
        val actual = dashboardPage1.getFirstCardBalance();
        val expected = initialBalanceToCard + amount;
        val actual2 = dashboardPage1.getSecondCardBalance();
        val expected2 = initialBalanceFromCard - amount;
        assertEquals(expected, actual);
        assertEquals(expected2, actual2);
    }

    @Test
    void shouldTransferFromFirstToSecond() {
        val amount = 999;
        val loginPage = new LoginPageV2();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.checkHeadingYourCards();
        val initialBalanceToCard = dashboardPage.getSecondCardBalance();
        val initialBalanceFromCard = dashboardPage.getFirstCardBalance();
        val transferPage = dashboardPage.validChoosePay2();
        transferPage.checkHeadingPaymentCards();
        transferPage.setPayCardNumber(DataHelper.getFirstCard(), amount);
        val dashboardPage1 = transferPage.validPayCard();
        val actual1 = dashboardPage1.getSecondCardBalance();
        val expected1 = initialBalanceToCard + amount;
        val actual2 = dashboardPage1.getFirstCardBalance();
        val expected2 = initialBalanceFromCard - amount;
        assertEquals(expected1, actual1);
        assertEquals(expected2, actual2);
    }

    @Test
    void shouldNotTransferMoreLimit() {
        val loginPage = new LoginPageV2();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.checkHeadingYourCards();
        val initialBalanceFromCard = dashboardPage.getSecondCardBalance();
        val transferPage = dashboardPage.validChoosePay1();
        transferPage.checkHeadingPaymentCards();
        val amount = 1 + initialBalanceFromCard;
        transferPage.setPayCardNumber(DataHelper.getSecondCard(), amount);
        transferPage.validPayExtendAmount();
    }

    @Test
    void shouldNotTransferMoreLimit2() {
        val loginPage = new LoginPageV2();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCodeFor(authInfo);
        val dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.checkHeadingYourCards();
        val initialBalanceFromCard = dashboardPage.getFirstCardBalance();
        val transferPage = dashboardPage.validChoosePay2();
        transferPage.checkHeadingPaymentCards();
        val amount = 1 + initialBalanceFromCard;
        transferPage.setPayCardNumber(DataHelper.getFirstCard(), amount);
        transferPage.validPayExtendAmount();
    }
}