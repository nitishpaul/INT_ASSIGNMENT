# Test Cases: Login Functionality - https://www.saucedemo.com/

## TC_001 - Successful Login with Valid Credentials
- **Test Scenario:** Verify that a user with valid, active credentials can successfully log in.
- **Preconditions:** User is on the SauceDemo login page (https://www.saucedemo.com/). User account "standard_user" exists and is active.
- **Test Data:** Username: `standard_user`, Password: `secret_sauce`
- **Test Steps:**
  1. Navigate to https://www.saucedemo.com/
  2. Enter `standard_user` in the Username field.
  3. Enter `secret_sauce` in the Password field.
  4. Click the "Login" button.
- **Expected Result:** User is authenticated successfully and redirected to the Products (inventory) page displaying the list of products.
- **Priority:** High

## TC_002 - Login Attempt with Invalid Password
- **Test Scenario:** Verify that the system rejects login when a valid username is paired with an incorrect password.
- **Preconditions:** User is on the SauceDemo login page. User account "standard_user" exists.
- **Test Data:** Username: `standard_user`, Password: `wrong_password`
- **Test Steps:**
  1. Navigate to https://www.saucedemo.com/
  2. Enter `standard_user` in the Username field.
  3. Enter `wrong_password` in the Password field.
  4. Click the "Login" button.
- **Expected Result:** Login is rejected. An error message is displayed: "Epic sadface: Username and password do not match any user in this service." User remains on the login page.
- **Priority:** High

## TC_003 - Login Attempt with Locked Out User Account
- **Test Scenario:** Verify that the system prevents login for a user account that has been locked out, and displays an appropriate authentication error.
- **Preconditions:** User is on the SauceDemo login page. User account "locked_out_user" exists and is in a locked state.
- **Test Data:** Username: `locked_out_user`, Password: `secret_sauce`
- **Test Steps:**
  1. Navigate to https://www.saucedemo.com/
  2. Enter `locked_out_user` in the Username field.
  3. Enter `secret_sauce` in the Password field.
  4. Click the "Login" button.
- **Expected Result:** Login is rejected. An error message is displayed: "Epic sadface: Sorry, this user has been locked out." User remains on the login page and is not granted access.
- **Priority:** High

## TC_004 - Field Validation for Empty Username and/or Password
- **Test Scenario:** Verify that the system enforces mandatory field validation when the Username and/or Password fields are left empty on submission.
- **Preconditions:** User is on the SauceDemo login page. Both Username and Password fields are empty.
- **Test Data:**
  - Case A: Username = "" (blank), Password = "" (blank)
  - Case B: Username = "standard_user", Password = "" (blank)
- **Test Steps:**
  1. Navigate to https://www.saucedemo.com/
  2. Leave the Username field blank (Case A) or enter `standard_user` (Case B).
  3. Leave the Password field blank.
  4. Click the "Login" button.
- **Expected Result:**
  - Case A: Error message displayed: "Epic sadface: Username is required."
  - Case B: Error message displayed: "Epic sadface: Password is required."
  - In both cases, login is not processed and the user remains on the login page.
- **Priority:** Medium

## TC_005 - Login Field Security and Password Masking
- **Test Scenario:** Verify that the Password field masks user input and that credentials are not exposed in plain text, ensuring basic authentication data security.
- **Preconditions:** User is on the SauceDemo login page.
- **Test Data:** Username: `standard_user`, Password: `secret_sauce`
- **Test Steps:**
  1. Navigate to https://www.saucedemo.com/
  2. Enter `standard_user` in the Username field.
  3. Enter `secret_sauce` in the Password field.
  4. Observe the characters displayed in the Password field.
  5. Inspect the page source/HTML attribute of the Password input field.
- **Expected Result:** Password field displays masked characters (e.g., dots/asterisks) instead of plain text as it is typed. The input field's `type` attribute is set to `password`, confirming credentials are not visible on screen.
- **Priority:** Low
