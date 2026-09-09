package stepDefinitions;

import base.BaseClass;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.pages.LoginPageComponents;

import static org.junit.Assert.assertEquals;

public class LoginSteps extends BaseClass {

    private LoginPageComponents loginComponent;

    @Given("User is on the SauceDemo login page")
    public void openBrowserAndNavigate(){
        navigateTo("https://www.saucedemo.com/");
        loginComponent = new LoginPageComponents(getDriver());
    }

    @When("I enter username {string}")
    public void enterUsername(String userName) {
        loginComponent.enterUsername(userName);
    }

    @And("I enter password {string}")
    public void enterPassword(String password) {
        loginComponent.enterPassword(password);
    }

    @And("I click on the Login button")
    public void clickOnTheLoginButton() {
        loginComponent.clickLogin();
    }

    @Then("I should be redirected to the Products page")
    public void checkRedirection() {
        assertEquals("Products", loginComponent.getProductPageText());
    }

    @Then("I should see the error message {string}")
    public void verifyErrorMessage(String expectedMessage) {
        assertEquals(expectedMessage, loginComponent.getErrorMessage());
    }

    @Then("the password field should mask the entered characters")
    public void verifyPasswordFieldIsMasked() {
        assertEquals("password", loginComponent.getPasswordFieldType());
    }
}
