package constructor_classes

data class LocatorsTypesConstructor constructor(
    val id: String = "id",
    val xpath: String = "xpath"
)

val locatorsTypes = LocatorsTypesConstructor()
