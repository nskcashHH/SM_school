package tests

import constructor_classes.locatorsTypes
import io.appium.java_client.MobileBy
import io.appium.java_client.MobileElement
import io.qameta.allure.Description
import locators.SplashScreenLocators
import main.TestMethods
import org.testng.annotations.Test
import java.util.concurrent.TimeUnit

class TestOne : TestMethods() {

    @Description(value = "Текст")
    @Test(description = "Текст", priority = 1)
    fun testOne() {


        when (platfonm){
            "iOS" ->{

            }
            "Android" ->{

            }
        }
        // описать логику, нужен ли нам авторизованный пользователь

      try {
            // нажатие на крестик на сплеше
            clickToElement(
                locatorType = locatorsTypes.xpath,
                locator = SplashScreenLocators().exitButtonOnSplashScreen.androidXpath
            )
        } catch (e: org.openqa.selenium.NoSuchElementException) {
            println("Элемент не найден, идем дальше")
        }


        val buttonExitProfile11 = driver.findElement(MobileBy.AndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true))" +
                ".scrollIntoView(new UiSelector().resourceIdMatches(\".*${SplashScreenLocators().exitButtonOnSplashScreen.androidXpath}.*\"))"))
        buttonExitProfile11.sendKeys("9999999905")
        // ввод текста в поле номера телефона
        inputTextInField(
            locatorType = locatorsTypes.id,
            locator = "ru.sportmaster.app.handh.dev:id/editTextPhone",
            inputText = "9999999905"
        )

        //клик на кнопку Получить код
        clickToElement(
            locatorType = locatorsTypes.id,
            locator = "ru.sportmaster.app.handh.dev:id/buttonGetCode"
        )

        // ввод текста в поле номера телефона
        inputTextInField(
            locatorType = locatorsTypes.id,
            locator = "ru.sportmaster.app.handh.dev:id/pinCodeEditText",
            inputText = "1111"
        )

        // Нажатие на пермищн геолокации
        clickToElement(
            locatorType = locatorsTypes.id,
            locator = "com.android.permissioncontroller:id/permission_allow_foreground_only_button"
        )

        // Нажатие на кнопку Да, при подтверждении города
        clickToElement(
            locatorType = locatorsTypes.id,
            locator = "android:id/button1"
        )

        swipeOnScreen(
            startCordX = 12,
            startCordY = 23,
            moveCordX = 23,
            moveCordY = 43
        )

    }

}