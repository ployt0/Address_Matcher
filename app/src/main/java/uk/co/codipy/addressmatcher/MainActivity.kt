package uk.co.codipy.addressmatcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import uk.co.codipy.addressmatcher.ui.theme.AppTheme
import org.json.JSONObject
import uk.co.codipy.addressmatcher.ui.screen.MainScreen
import java.io.BufferedReader
import java.io.InputStreamReader


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ocToL1s: Map<String, List<String>> = readJSONMapAsset("ocToL1s.json")
        val l1sToOCs: Map<String, List<String>> = readJSONMapAsset("l1sToOCs.json")
        setContent {
            MainContent(ocToL1s, l1sToOCs)
        }
    }

    private fun readJSONMapAsset(fileName: String): Map<String, List<String>> {
        val bufferedReader = BufferedReader(
            InputStreamReader(
                assets.open(fileName)
            )
        )
        val jsonString = bufferedReader.use { it.readText() }
        val jsonObj = JSONObject(jsonString)
        val ocToL1s: Map<String, List<String>> = jsonObj.toMap() as Map<String, List<String>>
        return ocToL1s
    }
}

@Composable
fun MainContent(
    ocToL1s: Map<String, List<String>>,
    l1sToOCs: Map<String, List<String>>
) {
    AppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            val vPadding = (innerPadding.calculateTopPadding() +
                    innerPadding.calculateBottomPadding()) / 24
            MainScreen(vPadding, ocToL1s, l1sToOCs)
        }
    }
}


