Feature: Login with Email and password
  In order to access the application
  As an guest user
  I want to provide an email and a password and expect to have a predictable outcome from login page

  Scenario Outline: With given email and password I want to have predictable outcome from login page
    Given '<email>' as E-mail and <password> as password
    When sending a POST request to <login_url>
    Then the HTTP status should be <status>

    Examples:
      | email | password | login_url | status |
      | ciutz@ciutz.com | '11' | 'http://localhost:8080/login' | '302' |
      | ciutz@ciutz.com | '12' | 'http://localhost:8080/login' | '200' |
