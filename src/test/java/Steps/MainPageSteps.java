package Steps;

import CSV.CSVWriter;
import Core.Buffer;
import Core.Helper;
import com.google.api.services.sheets.v4.model.ValueRange;
import cucumber.api.java.en.And;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainPageSteps extends Steps {
    @And("^I collect all founded IT news at new tab with (.*) section$")
    public void iСollectAllFoundedITNewsAtNewTab(String section) throws InterruptedException {
        log.info("I open all founded IT news at new tab");
        ArrayList<HashMap<String, String>> newsMapList = (Buffer.getNewsMap() == null
                ? new ArrayList<>()
                : (ArrayList<HashMap<String, String>>) Buffer.getNewsMap());
        int count = 0;
        do {
            for (WebElement titleNew : iMainPage.getNewsTitle()) {
                Helper.goToNewTabAndOpenUrl(titleNew.getAttribute("href"));

                HashMap<String, String> newsMap = new HashMap<>();
                List<WebElement> eventInfoRowDtList = iMainPage.getEventInfoRowDt();
                List<WebElement> eventInfoRowDdList = iMainPage.getEventInfoRowDd();
                String dateStart = "";
                String dateEnd = "";
                String timeStart = "";
                String timeEnd = "";
                String place = "";
                String price = "";
                String city = "";
                for (int j = 0; j < eventInfoRowDtList.size(); j++) {
                    String dt = eventInfoRowDtList.get(j).getText().trim();
                    if (dt.matches("Відбудеться|Date|Пройдет")) {
                        String dd = eventInfoRowDdList.get(j).getText().trim();
                        String yearEnd = "";
                        String yearStart = "";
                        Calendar cal = Calendar.getInstance();
                        int currentYear = cal.get(Calendar.YEAR);
                        int currentMonth = cal.get(Calendar.MONTH);
                        if (dd.contains("(")) {
                            dd = dd.replace(dd.substring(dd.indexOf("(")), "").trim();
                        }
                        // === Year event ===
                        if (dd.matches(".*\\d{4}")) {
                            yearStart = String.valueOf(currentYear);
                            if (dd.contains(String.valueOf(currentYear))) {
                                yearEnd = yearStart;
                                dd = dd.replace(dd.substring(dd.indexOf(yearStart)).trim(), "").trim();
                            } else {
                                yearEnd = String.valueOf(currentYear + 1);
                                dd = dd.replace(dd.substring(dd.indexOf(yearEnd)).trim(), "").trim();
                            }
                        }
                        // === Month event ===
                        int lastIndStart;
                        String monthStart;
                        String monthEnd;
                        if (dd.matches(".*[A-Za-zА-Яа-я]\\s—\\s\\d{1,2}\\s[A-Za-zА-Яа-я].*")) {
                            String[] doubleDate = dd.split(" — ");
                            lastIndStart = doubleDate[0].trim().indexOf(" ");
                            int lastIndEnd = doubleDate[1].trim().indexOf(" ");
                            monthStart = Helper.getShortMonth(doubleDate[0].substring(lastIndStart).trim());
                            monthEnd = Helper.getShortMonth(doubleDate[1].substring(lastIndEnd).trim());
                        } else {
                            lastIndStart = dd.trim().lastIndexOf(" ");
                            monthStart = Helper.getShortMonth(dd.substring(lastIndStart).trim());
                            monthEnd = monthStart;
                        }

                        yearStart = (yearStart.length() > 0 ? yearStart : String.valueOf(currentYear + ((Integer.parseInt(monthStart) < currentMonth) ? 1 : 0)));
                        // === Day event ===
                        String dayStart, dayEnd = "";
                        if (dd.matches(".*[A-Za-zА-Яа-я]\\s—\\s\\d{1,2}\\s[A-Za-zА-Яа-я].*")) {
                            String[] doubleDate = dd.split(" — ");
                            dayStart = doubleDate[0].split(" ")[0];
                            dayEnd = doubleDate[1].split(" ")[0];
                        } else {
                            String days = dd.substring(0, lastIndStart);
                            if (days.contains(" — ")) {
                                dayStart = days.split(" — ")[0];
                                dayEnd = days.split(" — ")[1];
                            } else {
                                dayStart = days;
                            }
                        }
                        dayStart = (dayStart.length() > 1 ? dayStart : "0" + dayStart);
                        dateStart = dayStart + "." + monthStart + "." + yearStart;
                        dateEnd = (dayEnd.length() > 0 ? (dayEnd.length() > 1 ? dayEnd : "0" + dayEnd) : dayStart) + "." + monthEnd + "." + (yearEnd.length() > 0 ? yearEnd : yearStart);
                    }
                    // === Time event ===
                    if (dt.matches("Time|Час|Время")) {
                        String dd = eventInfoRowDdList.get(j).getText().trim();
                        if (dd.contains(" — ")) {
                            timeStart = dd.split(" — ")[0];
                            timeEnd = dd.split(" — ")[1];
                        } else {
                            timeStart = dd;
                        }
                    }
                    // === Place event ===
                    if (dt.matches("Place|Місце|Место")) {
                        place = eventInfoRowDdList.get(j).getText().trim();
                        city = Helper.getCity(place);
                    }
                    if (dt.matches("Price|Вартість|Стоимость")) {
                        price = eventInfoRowDdList.get(j).getText().trim();
                    }
                }
                // === Body event ===
                String body = iMainPage.getNewsBody().getAttribute("textContent").trim();
                List<WebElement> linksBody = iMainPage.getNewsLinksBody();
                StringBuilder newsBody = new StringBuilder();
                String newsBodyHeader = "";
                String newsBodyTail = body.replaceAll("\\u00A0", " ")
                                          .replaceAll("\\u200B","");
                for (WebElement webElement : linksBody) {
                    int lastIndex = newsBodyTail.indexOf(webElement.getText());
                    String linksText = webElement.getText();
                    newsBodyHeader = (newsBodyTail.length() > 1 && lastIndex >= 0 ? newsBodyTail.substring(0, lastIndex) : "");
                    newsBodyTail = newsBodyTail.substring(lastIndex + linksText.length());
                    String link = webElement.getAttribute("href")
                                            .replace("utm_source=dou","")
                                            .replace("?&","?")
                                            .replace("&&","&");
                    newsBody.append(newsBodyHeader)
                            .append(" ")
                            .append(linksText.length() > 0
                                ? linksText
                                : "[picture]")
                            .append(" (Link: ")
                            .append(link)
                            .append(") ");
                }
                newsBody.append(newsBodyTail.length() > 1 ? newsBodyTail : "");
                newsMap.put("newsTitle", iMainPage.getNewsHead().getText());
                newsMap.put("dateStart", dateStart);
                newsMap.put("timeStart", timeStart);
                newsMap.put("newsBody", newsBody.toString());
                newsMap.put("dateEnd", dateEnd);
                newsMap.put("section", section);
                newsMap.put("timeEnd", timeEnd);
                newsMap.put("location", place);
                newsMap.put("price", price);
                newsMap.put("city", city);
                newsMapList.add(newsMap);
                Helper.closeCurrentTabAndBackToBeforeTab();
            }
            // === Next page ===
            if (count < iMainPage.getNextList().size()){
                iMainPage.getNextList().get(count).click();
                TimeUnit.SECONDS.sleep(5);
            }
        } while (count++ < iMainPage.getNextList().size());
        Buffer.setNewsMap(newsMapList);
    }
    @And("^I open all founded IT news at new tab with (.*) section test$")
    public void iOpenAllFoundedITNewsAtNewTabTest(String section) {
        log.info("I open all founded IT news at new tab");
        List<WebElement>  titleNews   = iMainPage.getNewsTitle();
        ArrayList<HashMap<String, String>> newsMapList = (Buffer.getNewsMap() == null
                ? new ArrayList<>()
                : (ArrayList<HashMap<String, String>>) Buffer.getNewsMap());
//        for (int i = 0; i < 10; i++) {
        HashMap<String, String> newsMap = new HashMap<>();
        List<WebElement> eventInfoRowDtList = iMainPage.getEventInfoRowDt();
        List<WebElement> eventInfoRowDdList = iMainPage.getEventInfoRowDd();
//        String dateStart = "dateStart-";
//        String dateEnd = "dateEnd-";
//        String newsTitle = "newsTitle-";
//        String timeStart = "timeStart-";
//        String newsBody = "newsBody-";
//        String timeEnd = "timeEnd-";
//        String place = "place-";
//        String price = "price-";
//        String city = "city-";
//        for (int j = 0; j < 10; j++) {
//            newsMap.put( "newsTitle", newsTitle + j);
//            newsMap.put( "dateStart", dateStart + j);
//            newsMap.put( "timeStart", timeStart + j);
//            newsMap.put( "newsBody",  newsBody  + j);
//            newsMap.put( "dateEnd",   dateEnd   + j);
//            newsMap.put( "section",   section   + j);
//            newsMap.put( "timeEnd",   timeEnd   + j);
//            newsMap.put( "place",     place     + j);
//            newsMap.put( "price",     price     + j);
//            newsMap.put( "city",      city      + j);
//
//            newsMapList.add(newsMap);
////            Helper.closeCurrentTabAndBackToBeforeTab();
//        }
        Helper.closeCurrentTabAndBackToBeforeTab();
        Buffer.setNewsMap(newsMapList);
    }

    @And("^I set NewsMap to Google sheet$")
    public void iSetNewsMapToGoogleSheet() {
        log.info("I set NewsMap to Google sheet");
        List<List<Object>> values = new ArrayList<>();
        List<HashMap<String, String>> newsMap = Buffer.getNewsMap();
        for (HashMap<String, String> element : newsMap) {
            values.add(Arrays.asList(
                    element.get("dateStart"),
                    element.get("timeStart"),
                    element.get("dateEnd"),
                    element.get("timeEnd"),
                    "[" + element.get("section") + " - " + element.get("city") + "] " + element.get("newsTitle"),
                    element.get("price") + ". \n" + element.get("newsBody"),
                    element.get("location")
            ));
        }
        Helper.setToGoogleSheets(new ValueRange().setValues(values));
    }

    @And("^I save to (.*) csv$")
    public void iSaveToCsv(String name) {
        log.info("I save to " + name + " csv");
        String[] header = {"dateStart", "timeStart", "dateEnd", "timeEnd", "newsTitle", "newsBody", "location"};
        List<String[]> correctNews = new ArrayList<>();
        List<String[]> inCorrectNews = new ArrayList<>();
        for (int i = 0; i < Buffer.getNewsMap().size(); i++) {
            HashMap<String, String> element = Buffer.getNewsMap().get(i);
            (!Helper.getForbiddenContent(element.get("newsTitle"))
                    && !Helper.getForbiddenContent(element.get("newsBody"))
                ? correctNews
                : inCorrectNews).add(new String[]{
                    element.get("dateStart"),
                    element.get("timeStart"),
                    element.get("dateEnd"),
                    element.get("timeEnd"),
                    "[" + element.get("section") + " - " + element.get("city") + "] " + element.get("newsTitle"),
                    (element.get("price").length() > 0
                            ? element.get("price") + ". \n"
                            : "") +
                        element.get("newsBody"),
                    element.get("location"),
                });
        }
        new CSVWriter().writerCSV(header, correctNews, name + "CorrectNews");
        if (inCorrectNews.size() > 0) {
            new CSVWriter().writerCSV(header, inCorrectNews, name + "_______InCorrectNews");
        }
        Buffer.setNewsMap(new ArrayList<>());
    }
}
