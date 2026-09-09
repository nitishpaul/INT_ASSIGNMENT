Feature: Login Feature Functionality

  @TC_001
  Scenario: Successful Login with Valid Credentials
    Given User is on the SauceDemo login page
    When I enter username "standard_user"
    And I enter password "secret_sauce"
    And I click on the Login button
    Then I should be redirected to the Products page

  @TC_002 @TC_003 @TC_004
  Scenario Outline: Unsuccessful Login Attempts
    Given User is on the SauceDemo login page
    When I enter username "<username>"
    And I enter password "<password>"
    And I click on the Login button
    Then I should see the error message "<errorMessage>"

    Examples:
      | username         | password       | errorMessage                                                                |
      | standard_user    | wrong_password | Epic sadface: Username and password do not match any user in this service  |
      | locked_out_user  | secret_sauce   | Epic sadface: Sorry, this user has been locked out.                        |
      |                  |                | Epic sadface: Username is required                                         |
      | standard_user    |                | Epic sadface: Password is required                                         |

  @TC_005
  Scenario: Login Field Security and Password Masking
    Given User is on the SauceDemo login page
    When I enter username "standard_user"
    And I enter password "secret_sauce"
    Then the password field should mask the entered characters
