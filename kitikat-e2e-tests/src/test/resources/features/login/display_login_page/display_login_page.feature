Feature: Display Login page when not authenticated
  In order to view the login page
  As an guest user
  I want to access the login url and expect to have a login form

  Scenario Outline: With given login url I want to have a login form
    Given a known <login_url>
    When sending a GET request
    Then the HTTP status should be <status>
    Then the contents should contain a login form

    Examples:
      | login_url | status |
      | 'http://localhost:8080/login' | '200' |
      | 'http://localhost:8080/profile' | '200' |
