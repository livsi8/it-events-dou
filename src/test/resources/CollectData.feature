Feature: I collect IT news and save to files

  @test1
  Scenario: open dou.ua by category collect news and save to files
    And I go to platform https://dou.ua/calendar/tags/DevOps/ by url
    And I collect all founded IT news at new tab with DevOps section
    And I save to DevOps csv

    And I go to platform https://dou.ua/calendar/tags/Java/ by url
    And I collect all founded IT news at new tab with Java section
    And I save to Java csv

    And I go to platform https://dou.ua/calendar/tags/JavaScript/ by url
    And I collect all founded IT news at new tab with JS section
    And I save to JS csv

    And I go to platform https://dou.ua/calendar/tags/Front-end/ by url
    And I collect all founded IT news at new tab with FrontEnd section
    And I save to FrontEnd csv

    And I go to platform https://dou.ua/calendar/tags/QA/ by url
    And I collect all founded IT news at new tab with QA section
    And I save to QA csv