package main

import io.appium.java_client.touch.offset.PointOption
import io.qameta.allure.Allure
import io.qameta.allure.Attachment
import io.qameta.allure.model.Status
import io.qameta.allure.model.StepResult
import org.openqa.selenium.OutputType
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.html5.Location
import org.testng.Assert
import utils.PlatformTouchAction
import utils.appPath
import java.util.concurrent.TimeUnit

class GeneralTestsFunc {
    /* тут я оставлю код, который возможно вам поможет, тут будет все подряд, если этот код находится в этом классе,
    это не значит, что у вас он тоже будет здесь.
     */


    // ==================== Для свайпов ===================
    // Вычисление максимальных координат экрана устройства, возвращает половину от координаты X и Y
    fun getDeviceScreenCords(): Array<Int> {
        val sizeDeviceScreenWidth = driver.manage().window().size.getWidth()
        val sizeDeviceScreenHeight = driver.manage().window().size.getHeight()
        val centerDeviceScreenHeight = sizeDeviceScreenHeight / 2
        val centerDeviceScreenWidth = sizeDeviceScreenWidth / 2
        return arrayOf(centerDeviceScreenHeight, centerDeviceScreenWidth)

    }

    //Вычисление координат на экране устройства  iOS
    private fun getDeviceScreenCordsForSwipeIOS(): Array<Int> {
        val (centerDeviceScreenHeight) = getDeviceScreenCords()
        val startCordYIOS = (centerDeviceScreenHeight * 1.11).toInt() //1.11
        val moveCordYIOS = (centerDeviceScreenHeight / 1.973).toInt() //1.973
        return arrayOf(startCordYIOS, moveCordYIOS)
    }

    //Вычисление координат на экране устройства Android
    private fun getDeviceScreenCordsForSwipeAndroid(): Array<Int> {
        val (centerDeviceScreenHeight) = getDeviceScreenCords()
        val startCordYAndroid = (centerDeviceScreenHeight * 1.6).toInt() // 1.5
        val moveCordYAndroid = (centerDeviceScreenHeight / 1.8).toInt() // 1.8
        return arrayOf(startCordYAndroid, moveCordYAndroid)
    }

    // Позволяет сделать свайп по экрану, нужно ввести координа начала и окончания свайпа
    fun swipeOnScreen(
        startCordX: Int,
        startCordY: Int,
        moveCordX: Int,
        moveCordY: Int,
    ) {
        println("============\nСвайп по экрану\n============")
        PlatformTouchAction(driver)
            .longPress(PointOption.point(startCordX, startCordY))
            .moveTo(PointOption.point(moveCordX, moveCordY))
            .release()
            .perform()
        println("============\nУспешно\n============")

    }


    // Реализация универсального свайпа на экране
    fun universalSwipeOnScreen() {
        val (centerDeviceScreenWidth) = getDeviceScreenCords()
        val (startCordYAndroid, moveCordYAndroid) = getDeviceScreenCordsForSwipeAndroid()
        val (startCordYIOS, moveCordYIOS) = getDeviceScreenCordsForSwipeIOS()
        when (platformName) {
            PlatformTypes.IOS -> {
                swipeOnScreen(
                    startCordX = centerDeviceScreenWidth,
                    startCordY = startCordYIOS,
                    moveCordX = centerDeviceScreenWidth,
                    moveCordY = moveCordYIOS
                )
            }
            PlatformTypes.ANDROID -> {
                swipeOnScreen(
                    startCordX = centerDeviceScreenWidth - 300, // почему - 300? =) практически вычислил, что для android так нужно))
                    startCordY = startCordYAndroid,
                    moveCordX = centerDeviceScreenWidth - 300,
                    moveCordY = moveCordYAndroid
                )
            }
        }
    }
    // ==================== Для свайпов - конец ===================

    // Делает скриншот и прикрепляет его к отчету в Allure
    @Attachment(value = "Screenshot - {dateOnSystem}", type = "image/png")
    private fun makeScreenshotOfScreen(dateOnSystem: String?): ByteArray {
        return (driver as TakesScreenshot?)!!.getScreenshotAs(OutputType.BYTES)
        // использование - makeScreenshotOfScreen(String.valueOf(new Date()))
    }


    // логика поиска элемента через свайп для iOS (как альтернатива свайпам по координатам)
    fun qwe() {
        val elementID = finalElement.id // finalElement = этот элемент, который мы получаем после поиска по локатору.
        val scrollObject = HashMap<String, String>()
        scrollObject["element"] = elementID //
        scrollObject["toVisible"] = "down" // тут главное, чтобы toVisible не являлся пустой строкой
        driver.executeScript("mobile:scroll", scrollObject) // скрипт, который скролит экран и ищет elementID
    }


    // переустановка приложения
    fun reinstallingApp() {

        // BUNDLE_ID_IOS = "ru.handh.sportmaster"
        // BUNDLE_ID_ANDROID = "ru.sportmaster.app.handh.dev"
        println("============\nПереустановка приложения началась\n============")
        when (platformName) {
            PlatformTypes.IOS -> {
                driver.removeApp(BUNDLE_ID_IOS)
                TimeUnit.SECONDS.sleep(2)
                driver.installApp(appPath.fullLocalAppLocalPathIOS)
            }
            PlatformTypes.ANDROID -> {
                driver.removeApp(BUNDLE_ID_ANDROID)
                TimeUnit.SECONDS.sleep(3)
                driver.installApp(appPath.fullAppLocalPathAndroid)
            }
        }
        TimeUnit.SECONDS.sleep(3)
        driver.launchApp()
        println("============\nУспешно, приложение запустилось\n============")
        TimeUnit.SECONDS.sleep(1)

    }

    // установка геолокации на устройство
    val location = Location(55.052146, 82.920077, 1.0)
    driver.setLocation(location)


    // Изменяет статус шага на (failed), если тот провалился
    fun changeStepStatusOnFailed() {
        Allure.getLifecycle().updateStep { stepResult: StepResult -> stepResult.status = Status.FAILED }
        Allure.getLifecycle().stopStep()
    }

    // Изменяет статус шага на (skipped), если тот провалится
    fun changeStepStatusOnSkipped() {
        Allure.getLifecycle().updateStep { stepResult: StepResult -> stepResult.status = Status.SKIPPED }
        Allure.getLifecycle().stopStep()
    }

    // Выделяет текст в статус (failed) при наличии ошибки в тесте
    fun checkTestStatus() {
        println("============\nПроверка статуса теста\n============")
        if (!stepStatus) Assert.fail("В каком-то из шагов возникла ошибка")
        println("============\nУспешно\n============")
    }


}