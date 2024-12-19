package com.example.addressmatcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.addressmatcher.ui.theme.AddressMatcherTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader


fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
    when (val value = this[it]) {
        is JSONArray -> {
            val map = (0 until value.length()).associate {
                Pair(it.toString(), value[it])
            }
            // We'd cast map to JSONObject(map).toMap() if recursing.
            map.values.toList()
        }
        is JSONObject -> value.toMap()
        JSONObject.NULL -> null
        else            -> value
    }
}


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ocToL1s: Map<String, List<String>> = readJSONMapAsset("ocToL1s.json")
        val l1sToOCs: Map<String, List<String>> = readJSONMapAsset("l1sToOCs.json")
        enableEdgeToEdge()
        setContent {
            AddressMatcherTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    val vPadding = (innerPadding.calculateTopPadding() +
                            innerPadding.calculateBottomPadding()) / 3
                    Column {
                        var resultText by remember { mutableStateOf("") }
                        var typedLocality by remember { mutableStateOf("") }
                        var typedOCArea by remember { mutableStateOf("") }

                        AutoCompleteTextField(
                            modifier = Modifier.padding(0.dp, vPadding),
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

                        AutoCompleteTextField(
                            modifier = Modifier.padding(0.dp, vPadding),
                            fieldLabel = "L1",
                            value = typedLocality,
                            onSuggestionSelected = {
                                typedLocality = it
                                resultText = l1sToOCs.getValue(it).toTypedArray()
                                    .joinToString("\n")
                                                   },
                            suggestions = l1sToOCs.keys.toList(),
                        )

                        TextField(
                            label = { Text("Result")},
                            readOnly = true,
                            placeholder = {Text("result")},
                            value = resultText,
                            onValueChange = { resultText = it },
                            modifier = Modifier.padding(0.dp, vPadding)
                        )
                    }
                }
            }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCompleteTextField(
    modifier: Modifier = Modifier,
    fieldLabel: String,
    fieldError: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next,
        capitalization = KeyboardCapitalization.Words
    ),
    onSuggestionSelected: (selectedSuggestion: String) -> Unit,
    suggestions: List<String>,
    value: String,
) {
    val context = LocalContext.current
    var text by remember { mutableStateOf(value) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    var filteredSuggestions by remember { mutableStateOf(emptyList<String>()) }
    var debounceJob by remember { mutableStateOf<Job?>(null) }

    LaunchedEffect(text) {
        debounceJob?.cancel()
        debounceJob = launch {
            delay(500)
            filteredSuggestions = suggestions.filter {
                text.isNotEmpty() && it.contains(text, ignoreCase = false)
            }
        }
    }

    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = isDropdownExpanded,
        onExpandedChange = { expanded ->
            isDropdownExpanded = expanded
        }
    ) {
        OutlinedTextField(
            placeholder = { Text(fieldLabel) },
            isError = !fieldError.isNullOrEmpty(),
            keyboardOptions = keyboardOptions,
            label = { Text(fieldLabel) },
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged {
                    if (!it.isFocused) {
                        isDropdownExpanded = false
                    }
                }
                .menuAnchor(),
            onValueChange = {
                text = it
                isDropdownExpanded = it.isNotEmpty()
            },
            readOnly = false,
            supportingText = {
                if (!fieldError.isNullOrEmpty()) {
                    Text(
                        text = fieldError,
                        color = Color.Red,
                        style = TextStyle(fontSize = 12.sp)
                    )
                }
            },
            trailingIcon = {
                if (!fieldError.isNullOrEmpty()) {
                    Icon(Icons.Filled.Warning, contentDescription = "Error", tint = Color.Red)
                }
            },
            value = text
        )

        if (filteredSuggestions.isNotEmpty()) {
            DropdownMenu(
                modifier = Modifier.exposedDropdownSize(),
                expanded = isDropdownExpanded,
                onDismissRequest = {
                    isDropdownExpanded = false
                },
                properties = PopupProperties(focusable = false)
            ) {
                filteredSuggestions.forEach { suggestion ->
                    DropdownMenuItem(
                        text = { Text(suggestion) },
                        onClick = {
                            onSuggestionSelected(suggestion)
                            text = suggestion
                            isDropdownExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}
