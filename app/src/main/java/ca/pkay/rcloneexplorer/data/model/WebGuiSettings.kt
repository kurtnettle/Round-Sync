package ca.pkay.rcloneexplorer.data.model

data class WebGuiSettingsState(
    val user: String = DefaultWebGuiSettings.USERNAME,
    val pass: String = DefaultWebGuiSettings.PASSWORD,
    val addr: String = DefaultWebGuiSettings.ADDRESS,
    val isAnonymousLogin: Boolean = DefaultWebGuiSettings.ANONYMOUS_LOGIN,
    val guiReleaseUrl: String = DefaultWebGuiSettings.GUI_RELEASE_URL
)

object DefaultWebGuiSettings {
    const val ANONYMOUS_LOGIN = true
    const val ADDRESS = "localhost:5572"
    const val USERNAME = ""
    const val PASSWORD = ""
    const val GUI_RELEASE_URL =
        "https://api.github.com/repos/rclone/rclone-webui-react/releases/latest"
}