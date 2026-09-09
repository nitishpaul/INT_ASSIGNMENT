package hooks;

import base.BaseClass;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;

public class Hooks extends BaseClass {

    @BeforeAll
    public static void setUp(){
        initializeDriver("chrome");
    }

    @AfterAll
    public static void tearDown(){
        if(getDriver()!=null){
            getDriver().quit();
        }
    }

}
