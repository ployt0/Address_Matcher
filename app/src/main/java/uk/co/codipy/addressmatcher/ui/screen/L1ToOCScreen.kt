package uk.co.codipy.addressmatcher.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.co.codipy.addressmatcher.ui.widget.AutoCompleteTextField


@Composable
fun L1ToOCScreen(
    vPadding: Dp,
    typedLocality: String,
    onTypedLocalityChange: (String) -> Unit,
    resultText: String,
    onResultChange: (String) -> Unit,
    l1sToOCs: Map<String, List<String>>
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)) {

        AutoCompleteTextField(
            modifier = Modifier.padding(top = vPadding),
            fieldLabel = "L1",
            value = typedLocality,
            onSuggestionSelected = {
                onTypedLocalityChange(it)
                onResultChange(l1sToOCs[it]?.joinToString("\n") ?: "No results")
                keyboardController?.hide()
            },
            suggestions = l1sToOCs.keys.toList()
        )

        TextField(
            label = { Text("OCs") },
            readOnly = true,
            textStyle = TextStyle(fontSize = 24.sp),
            placeholder = { Text("Result") },
            value = resultText,
            onValueChange = { onResultChange(it) },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}


@Preview
@Composable
fun PreviewL1ToOCScreen() {
    L1ToOCScreen(
        16.dp,
        typedLocality = "Por",
        onTypedLocalityChange = { it },
        resultText = "placeholder",
        onResultChange = { it },
        l1sToOCs = mapOf<String, List<String>>("Exeter" to listOf("EX"), "Portsmouth" to listOf("PO")),
    )
}