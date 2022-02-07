package main

import io.appium.java_client.AppiumDriver
import io.appium.java_client.MobileElement
import io.appium.java_client.android.AndroidDriver
import io.appium.java_client.ios.IOSDriver
import io.appium.java_client.remote.AndroidMobileCapabilityType
import io.appium.java_client.remote.MobileCapabilityType
import org.openqa.selenium.remote.DesiredCapabilities
import org.testng.annotations.*
import utils.appPath
import java.net.URL
import java.util.concurrent.TimeUnit

open class BaseClass {
    lateinit var driver: AppiumDriver<MobileElement>

    @BeforeSuite
    @Parameters(
        value = ["paramPlatformVersion", "paramDeviceName",
            "paramPlatformName", "paramTimeToDelay", "paramUDID"]
    )
    fun setupDriver(
        paramPlatformVersion: String, paramDeviceName: String,
        paramPlatformName: String, paramTimeToDelay: Long, paramUDID: String
    ) {

        val url = URL("http://127.0.0.1:4723/wd/hub")
        val caps = DesiredCapabilities()

        caps.setCapability(MobileCapabilityType.PLATFORM_NAME, paramPlatformName) // название платформы
        caps.setCapability(MobileCapabilityType.PLATFORM_VERSION, paramPlatformVersion) // верся ОС
        caps.setCapability(MobileCapabilityType.DEVICE_NAME, paramDeviceName) // имя устройства

        caps.setCapability(MobileCapabilityType.NO_RESET, true) // не сбрасывать приложение в 0 перед новый запуском
        caps.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "7200")
        caps.setCapability(MobileCapabilityType.UDID, paramUDID)

        when (paramPlatformName) {
            "Android" -> {
                caps.setCapability(AndroidMobileCapabilityType.APP_PACKAGE, "ru.sportmaster.app.handh.dev")
                caps.setCapability(
                    AndroidMobileCapabilityType.APP_ACTIVITY,
                    "ru.sportmaster.app.presentation.start.StartActivity"
                )
                caps.setCapability(
                    MobileCapabilityType.APP, appPath.fullAppLocalPathAndroid
                )
                driver = AndroidDriver(url, caps) // установка драйвера и приложения на Android устройство
            }
            "iOS" -> {
                caps.setCapability(
                    MobileCapabilityType.APP, appPath.fullLocalAppLocalPathIOS
                )
                driver = IOSDriver(url, caps) // установка драйвера и приложения на iOS устройство
            }
        }

        driver.manage().timeouts().implicitlyWait(paramTimeToDelay, TimeUnit.SECONDS)
        println("Драйвер установлен")

        // проверка наличия онбординга + прохождение до главного экрана, минуя авторизацию, если онбординг найден


    }

    @AfterSuite
    fun end() {
        println("тест окончен")
        driver.quit()
    }

    @BeforeClass
    fun beforeClass() {
        // заново инициализирвать драйвер
        // закрыть приложение
    }

    @AfterClass
    fun afterClass() {
        // закрыть сессию драйвера
        driver.quit()
    }

    @BeforeMethod
    fun beforeMethod() {
        // запустить приложение
        driver.launchApp()
        TimeUnit.SECONDS.sleep(4)
    }

    @AfterMethod
    fun afterMethod() {
        // закрыть приложение
    }


}