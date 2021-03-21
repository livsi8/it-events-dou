package Core;

import Google.GoogleSheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.openqa.selenium.JavascriptExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static Core.DriverFactory.getDriver;

public class Helper {
    private static String header = "";
    private static String[] forbiddenArray = {
            "запропонована позиція",
            "трудоустройств",
            "удобный график",
            "отпуск",
            "больничн",
            "испытательн",
            "% от зп",
            "% від зп",
            "% від майбутньої зарплати",
            "% від зарплати",
            "працевлаштуван",
            "резюме",
            "в нашей компании",
            "предлагаем работу ",
            "работа в нашей",
            "кращих випускників",
            "приєднатися до команди",
            "рекрутер",
            "успешно трудоустроены",
            "присоединяйтесь к нашей команде",
            "остались работать",
            " зарплата ",
            " зарплате ",
            "под действующие проекты",
            "[скасовано]",
            " наш HR ",
            "с зарплатой",
            "должность Trainee",
            "отримують роботу у",
            "пройти спiвбесiду",
            "работе в",
            "Пройди техническое интервью",
            "Получи приглашение на личную беседу",
            "співбесіда з ментором",
            "працювати у проєктах",
            "у менторських програмах",
//            "долучит",
//            "собеседовани",
//            "після закінчення",
//            "кар’єр",
//            "працівник",
//            "роботу",
//            "робота ",
    };

    public static Boolean getForbiddenContent(String str) {
        header = str.length() < 150 ? str : "";
        List<String> forbidden = Arrays.stream(forbiddenArray)
                                       .map(String::toLowerCase)
                                       .filter(str.toLowerCase()::contains)
                                       .collect(Collectors.toList());
        if (!forbidden.isEmpty()) {
            System.out.println(
                "\n=====================\n" +
                "header = \"" + header + "\"\n" +
                "body   = \"" + str.substring(0, Math.min(str.length(), 150)).replaceAll("\n|\r\n", "") +
                "\"\n~~~~~~~~~~~~~~~~~~~~~");
            forbidden.stream().forEach(e-> System.out.println("\"" + e + "\""));
        }
        return Arrays.stream(forbiddenArray)
                .map(String::toLowerCase)
                .anyMatch(str.toLowerCase()::contains);
    }

    public static void goToNewTabAndOpenUrl (String urlWithNews) {
        ((JavascriptExecutor) getDriver()).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<> (getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.get(1)); //switches to new tab
        getDriver().get(urlWithNews);

        getDriver().manage().timeouts().pageLoadTimeout(
            300,
            TimeUnit.SECONDS
        );
    }

    public static void closeCurrentTabAndBackToBeforeTab () {
        ArrayList<String> tabs = new ArrayList<> (getDriver().getWindowHandles());
        getDriver().close();
//        getDriver().switchTo().defaultContent();
        getDriver().switchTo().window(tabs.get(0)); // switch back to main screen
    }

    public static String getShortMonth (String inMonth) {
        String month = "";
        if (inMonth.matches("января|Января|Січня|січня|January|january")) {
            month = "01";
        } else if (inMonth.matches("февраля|Февраля|Лютого|лютого|February|february")) {
            month = "02";
        } else if (inMonth.matches("марта|Марта|Березня|березня|March|march")) {
            month = "03";
        } else if (inMonth.matches("апреля|Апреля|Квітня|квітня|April|april")) {
            month = "04";
        } else if (inMonth.matches("мая|Мая|Травня|травня|May|may")) {
            month = "05";
        } else if (inMonth.matches("июня|Июня|Червня|червня|June|june")) {
            month = "06";
        } else if (inMonth.matches("июля|Июля|Липня|липня|July|july")) {
            month = "07";
        } else if (inMonth.matches("августа|Августа|Серпня|серпня|August|august")) {
            month = "08";
        } else if (inMonth.matches("сентября|Сентября|Вересня|вересня|September|september")) {
            month = "09";
        } else if (inMonth.matches("октября|Октября|Жовтня|жовтня|October|october")) {
            month = "10";
        } else if (inMonth.matches("ноября|Ноября|Листопада|листопада|November|november")) {
            month = "11";
        } else if (inMonth.matches("декабря|Декабря|Грудня|грудня|December|december")) {
            month = "12";
        }
        return month;
    }

    public static String getCity(String place) {
        String city = place + "*";
        if (place.matches(".*Kharkiv.*|.*Харків.*|.*Харьков.*")) {
            city = "Харьков";
        } else if (place.matches(".*Kyiv.*|.*Kiev.*|.*Київ.*|.*Киев.*")) {
            city = "Киев";
        } else if (place.matches(".*Lviv.*|.*Львів.*|.*Львов.*")) {
            city = "Львов";
        } else if (place.matches(".*Dnipro.*|.*Дніпро.*|.*Днепр.*")) {
            city = "Днепр";
        } else if (place.matches(".*Vinnitsa.*|.*Вінниця.*|.*Винница.*")) {
            city = "Винница";
        }
        return city;
    }

    public static void setToGoogleSheets(ValueRange content) {
        try {
            GoogleSheets.setGoogleSheetsTable(
                    Config.getProperty("resourcesSpreadsheetId"),
                    Config.getProperty("resourcesRange"),
                    content
            );
//            Buffer.setResourcesFromGoogleSheets(sheet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
