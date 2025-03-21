package com.example.addressmatcher

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCompleteTextField(
    modifier: Modifier = Modifier,
    fieldLabel: String,
    fieldError: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next,
        capitalization = KeyboardCapitalization.Words,
    ),
    onSuggestionSelected: (selectedSuggestion: String) -> Unit,
    suggestions: List<String>,
    value: String,
) {
//    val context = LocalContext.current
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
            textStyle = TextStyle(
                fontSize = 22.sp,
            ),
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
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            ,
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
