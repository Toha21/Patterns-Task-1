package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeAll
    static void setUpAll(){
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll(){
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setup() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
    }

    @DisplayName("Should be successful assign and reassign meeting")
    @Test
    void shouldBeSuccessfulAssignReassignMeeting() {
        var validUser = DataGenerator.Registration.generateUser("ru");
        var daysToAddForFirstMeeting = 6;
        var firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        var daysToAddForSecondMeeting = 7;
        var secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $("[placeholder=Город]").setValue(validUser.getCity());
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id=\"name\"] input").setValue(validUser.getName());
        $("[data-test-id=\"phone\"] input").setValue(validUser.getPhone());
        $("[data-test-id=\"agreement\"]").click();
        $x("//span[@class='button__text']").click();
        $("[data-test-id=\"success-notification\"]").shouldHave(Condition.text("Встреча успешно " +
                "запланирована на " + firstMeetingDate), Duration.ofSeconds(15));
        $("[placeholder=\"Дата встречи\"]").doubleClick().sendKeys(Keys.DELETE);
        $("[data-test-id='date'] input").setValue(secondMeetingDate);
        $(withText("Запланировать")).click();
        $("[data-test-id=\"replan-notification\"]").shouldHave(Condition.text("У вас уже запланирована " +
                "встреча на другую дату."));
        $(withText("Перепланировать")).click();
        $("[data-test-id=\"success-notification\"]").shouldHave(Condition.text("Встреча успешно" +
                " запланирована на " + secondMeetingDate), Duration.ofSeconds(15));

    }
}