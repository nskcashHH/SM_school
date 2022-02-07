package constructor_classes

data class LocatorsConstructor constructor(
    val androidAccessibilityId: String = "",
    val androidId: String = "",
    val androidXpath: String = "",
)

val example = LocatorsConstructor(
    androidAccessibilityId = "",
    androidId = "",
    androidXpath = "",
)