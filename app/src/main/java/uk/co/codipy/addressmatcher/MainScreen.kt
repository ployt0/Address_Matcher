package uk.co.codipy.addressmatcher

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
internal fun MainScreen(
    vPadding: Dp,
    ocToL1s: Map<String, List<String>>,
    l1sToOCs: Map<String, List<String>>
) {
    var resultText by remember { mutableStateOf("") }
    var typedLocality by remember { mutableStateOf("") }
    var typedOCArea by remember { mutableStateOf("") }
    LazyColumn {
        item {
            AutoCompleteTextField(
                modifier = Modifier.padding(12.dp, vPadding),
                fieldLabel = "OC area",
                value = typedOCArea,
                onSuggestionSelected = {
                    typedOCArea = it
                    resultText = ocToL1s.getValue(it).toTypedArray()
                        .joinToString("\n")
                },
                suggestions = ocToL1s.keys.toList(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    capitalization = KeyboardCapitalization.Characters
                )
            )
        }
        item {
            AutoCompleteTextField(
                modifier = Modifier.padding(12.dp, vPadding),
                fieldLabel = "L1",
                value = typedLocality,
                onSuggestionSelected = {
                    typedLocality = it
                    resultText = l1sToOCs.getValue(it).toTypedArray()
                        .joinToString("\n")
                },
                suggestions = l1sToOCs.keys.toList(),
            )
        }

        item {
            TextField (
                label = { Text("Result") },
                readOnly = true,
                textStyle = TextStyle(fontSize = 24.sp),
                placeholder = { Text("result") },
                value = resultText,
                onValueChange = { resultText = it },
                modifier = Modifier.padding(12.dp, vPadding)
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