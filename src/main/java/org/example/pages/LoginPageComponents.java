package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPageComponents {

    final private WebDriver driver;

    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By productPageTitle = By.xpath("//*[@data-test='title']");
    private final By errorMessage = By.xpath("//*[@data-test='error']");


    public LoginPageComponents(WebDriver driver){
        this.driver = driver;
    }

    public void enterUsername(String username) {
        driver.findElement(usernameField).sendKeys(username);
    }

    public void enterPassword(String password) {
        driver.findElement(passwordField).sendKeys(password);
    }

    public void clickLogin() {
        driver.findElement(loginButton).click();
    }

    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLogin();
    }

    public String getProductPageText(){
        return driver.findElement(productPageTitle).getText();
    }

    public String getErrorMessage(){
        return driver.findElement(errorMessage).getText();
    }

    public String getPasswordFieldType(){
        return driver.findElement(passwordField).getAttribute("type");
    }

}
