package uk.co.codipy.addressmatcher.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.co.codipy.addressmatcher.AutoCompleteTextField


@Composable
fun OCToL1Screen(
    vPadding: Dp,
    typedOCArea: String,
    onTypedOCAreaChange: (String) -> Unit,
    resultText: String,
    onResultChange: (String) -> Unit,
    ocToL1s: Map<String, List<String>>
) {
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 16.dp)) {

        AutoCompleteTextField(
            modifier = Modifier.padding(top = vPadding),
            fieldLabel = "OC Area",
            value = typedOCArea,
            onSuggestionSelected = {
                onTypedOCAreaChange(it)
                onResultChange(ocToL1s[it]?.joinToString("\n") ?: "No results")
            },
            suggestions = ocToL1s.keys.toList(),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                capitalization = KeyboardCapitalization.Characters
            )
        )

        TextField(
            label = { Text("Result") },
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
