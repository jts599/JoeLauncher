package app.joelauncher.data

data class MultiSelectOption (
    //What gets displayed in the selection list
    val title: String,
    //Unique identifier for the option
    val key: String,
    //Whether or not the option is selected
    var selected: Boolean = false
) {}