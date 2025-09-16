package uk.co.codipy.addressmatcher

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import uk.co.codipy.addressmatcher.ui.screen.L1ToOCScreen
import uk.co.codipy.addressmatcher.ui.screen.OCToL1Screen


@Composable
internal fun MainScreen(
    vPadding: Dp,
    ocToL1s: Map<String, List<String>>,
    l1sToOCs: Map<String, List<String>>
) {
    val pagerState = rememberPagerState(initialPage = 0, pageCount = { 2 })

    var typedOCArea by rememberSaveable { mutableStateOf("") }
    var ocToL1Result by rememberSaveable { mutableStateOf("") }

    var typedLocality by rememberSaveable { mutableStateOf("") }
    var l1ToOCResult by rememberSaveable { mutableStateOf("") }

    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
        when (page) {
            0 -> OCToL1Screen(
                vPadding = vPadding,
                typedOCArea = typedOCArea,
                onTypedOCAreaChange = { typedOCArea = it },
                resultText = ocToL1Result,
                onResultChange = { ocToL1Result = it },
                ocToL1s = ocToL1s
            )

            1 -> L1ToOCScreen(
                vPadding = vPadding,
                typedLocality = typedLocality,
                onTypedLocalityChange = { typedLocality = it },
                resultText = l1ToOCResult,
                onResultChange = { l1ToOCResult = it },
                l1sToOCs = l1sToOCs
            )
        }
    }
}



@Preview
@Composable
fun Preview() {
    MainScreen(
        16.dp,
        mapOf<String, List<String>>("EX" to listOf("Exeter", "Exmouth"), "PO" to listOf("Portsmouth", "Southsea", "Havant", "Gosport", "Fareham", "Isle of Wight")),
        mapOf<String, List<String>>("Exeter" to listOf("EX"), "Portsmouth" to listOf("PO")),
    )
}