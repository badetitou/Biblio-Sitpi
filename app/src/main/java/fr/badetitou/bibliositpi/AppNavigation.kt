package fr.badetitou.bibliositpi

enum class Screen {
    HOME,
    LOGIN,
    RESOURCE_DETAIL
}
sealed class NavigationItem(val route: String) {
    data object Home : NavigationItem(Screen.HOME.name)
    data object Login : NavigationItem(Screen.LOGIN.name)
    data object ResourceDetail : NavigationItem(Screen.RESOURCE_DETAIL.name)
}