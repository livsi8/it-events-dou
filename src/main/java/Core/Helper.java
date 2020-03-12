package Core;

import static Core.DriverFactory.getDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Google.GoogleSheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.openqa.selenium.JavascriptExecutor;

public class Helper {
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

    public static Boolean getForbiddenContent(String str) {
        String[] forbiddenArray = {
            "кар’єр", "запропонована позиція", " зарплат", "трудоустройство", "собеседовани", "удобный график",
            "отпуск", "оплачиваемы", "больничн", "испытательн", "% от зп", "на протяжении", "працевлаштуван",
        };
        return Arrays.stream(forbiddenArray).anyMatch(str::contains);
    }
    
}
