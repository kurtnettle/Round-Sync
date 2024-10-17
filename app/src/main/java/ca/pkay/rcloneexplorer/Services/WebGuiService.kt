package ca.pkay.rcloneexplorer.Services

import android.content.Context
import android.util.Log
import ca.pkay.rcloneexplorer.Rclone
import ca.pkay.rcloneexplorer.data.model.WebGuiSettingsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import java.io.InterruptedIOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class WebGuiService(context: Context) {
    private val TAG = "WebGuiService"

    private var rcloneProcess: Process? = null
    private val rcloneInstance: Rclone = Rclone(context.applicationContext)

    private val _isRunning: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private val _commandOutput = MutableStateFlow("")
    val commandOutput: StateFlow<String> = _commandOutput

    companion object {
        @Volatile
        private var INSTANCE: WebGuiService? = null
        fun getInstance(context: Context): WebGuiService {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: WebGuiService(context.applicationContext).also { INSTANCE = it }
            }
        }
    }

    suspend fun start(data: WebGuiSettingsState?, addr: String?) {
        if (isRunning.value) {
            Log.d(TAG, "rclone web already running.")
            return
        }
        withContext(Dispatchers.IO) {
            try {
                val command = rcloneInstance.webGui(data, addr)
                val processBuilder = ProcessBuilder(*command).apply {
                    redirectErrorStream(true)
                }

                rcloneProcess = processBuilder.start().apply {
                    _isRunning.value = true
                }

                rcloneProcess?.inputStream?.bufferedReader()?.use { reader ->
                    reader.lineSequence().forEach { line ->
                        _commandOutput.update { it + "$line\n" }
                    }
                }
            } catch (e: InterruptedIOException) {
                //
            } catch (e: Exception) {
                Log.d(TAG, "Failed to start rclone: ${e.message}")
                _commandOutput.update {
                    it + getLogMsg("ERROR", "Failed to start rclone. Error: ${e.message}\n")
                }
            } finally {
                _isRunning.value = false
            }
        }
    }

    fun stop() {
        if (!isRunning.value || rcloneProcess == null) {
            Log.d(TAG, "nothing to stop, rclone web is not running.")
            return
        }

        rcloneProcess?.let { process ->
            try {
                process.destroy()
                process.inputStream.close()
                process.errorStream.close()

                val exitCode = process.waitFor()
                Log.d(TAG, "rclone process exited with code $exitCode")
                _commandOutput.update { it + getLogMsg("NOTICE", "Stopped rclone web.\n") }
            } catch (e: Exception) {
                Log.d(TAG, "Failed to stop rclone: ${e.message}")
                _commandOutput.update {
                    it + getLogMsg("ERROR", "Failed to stop rclone: ${e.message}\n")
                }
            } finally {
                rcloneProcess = null
                _isRunning.value = false
            }
        }
    }


    fun clear() {
        _commandOutput.update({ "" })
    }

    private fun getLogMsg(level: String, text: String): String {
        val dateTxt = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date())
        return "$dateTxt $level: $text"
    }
}