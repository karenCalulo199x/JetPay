import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

sealed class TextFieldType {
    object Simple : TextFieldType()
    object Password : TextFieldType()
    data class LeadingIcon(val icon: @Composable () -> Unit) : TextFieldType()
    data class TrailingIcon(val icon: @Composable () -> Unit) : TextFieldType()
    data class TrailingButtonIcon(val icon: @Composable () -> Unit, val onClick: () -> Unit) : TextFieldType()
    data class Phone(val countryCode: String, val onCountryCodeClick: () -> Unit) : TextFieldType()
}

data class TextFieldState(
    val type: TextFieldType,
    val errorMessage: String? = null
)

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    textFieldState: TextFieldState = TextFieldState(TextFieldType.Simple),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    enabled: Boolean = true,
    outlineColor: Color = Color.Transparent,
    errorOutlineColor: Color = MaterialTheme.colorScheme.error
) {
    var passwordVisible by remember { mutableStateOf(false) }

    val leadingIcon: (@Composable (() -> Unit))? = when (textFieldState.type) {
        is TextFieldType.LeadingIcon -> textFieldState.type.icon
        is TextFieldType.Phone -> {
            { Text(textFieldState.type.countryCode) }
        }
        else -> null
    }

    val trailingIcon: (@Composable (() -> Unit))? = when (textFieldState.type) {
        is TextFieldType.Password -> {
            {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        }

        is TextFieldType.TrailingIcon -> textFieldState.type.icon
        is TextFieldType.TrailingButtonIcon -> {
            {
                IconButton(onClick = textFieldState.type.onClick) {
                    textFieldState.type.icon()
                }
            }
        }

        is TextFieldType.Phone -> {
            {
                IconButton(onClick = textFieldState.type.onCountryCodeClick) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Country Code Dropdown")
                }
            }
        }

        else -> null
    }

    val isError = textFieldState.errorMessage != null

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.LightGray, shape = RoundedCornerShape(4.dp)),
        placeholder = { Text(hint) },
        isError = isError,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        visualTransformation = if (textFieldState.type is TextFieldType.Password && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        enabled = enabled,
        colors = if (textFieldState.type == TextFieldType.Simple) {
            TextFieldDefaults.colors(
                focusedIndicatorColor = if (isError) errorOutlineColor else outlineColor,
                unfocusedIndicatorColor = if (isError) errorOutlineColor else outlineColor,
                errorIndicatorColor = errorOutlineColor,
                focusedContainerColor = Color.LightGray,
                unfocusedContainerColor = Color.LightGray
            )
        } else {
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (isError) errorOutlineColor else outlineColor,
                unfocusedBorderColor = if (isError) errorOutlineColor else outlineColor,
                errorBorderColor = errorOutlineColor
            )
        }
    )

    if (isError && !textFieldState.errorMessage.isEmpty()) {
        Text(
            text = textFieldState.errorMessage,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CustomTextFieldShowcase() {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        var simpleText by remember { mutableStateOf("") }
        CustomTextField(
            value = simpleText,
            onValueChange = { simpleText = it },
            hint = "Enter text",
            textFieldState = TextFieldState(TextFieldType.Simple)
        )

        var passwordText by remember { mutableStateOf("") }
        CustomTextField(
            value = passwordText,
            onValueChange = { passwordText = it },
            hint = "Enter password",
            textFieldState = TextFieldState(TextFieldType.Password)
        )

        var leadingText by remember { mutableStateOf("") }
        CustomTextField(
            value = leadingText,
            onValueChange = { leadingText = it },
            hint = "With leading icon",
            textFieldState = TextFieldState(TextFieldType.LeadingIcon(icon = {
                Icon(Icons.Default.AccountCircle, contentDescription = "User Icon")
            }))
        )

        var trailingText by remember { mutableStateOf("") }
        CustomTextField(
            value = trailingText,
            onValueChange = { trailingText = it },
            hint = "With trailing icon",
            textFieldState = TextFieldState(TextFieldType.TrailingIcon(icon = {
                Icon(Icons.Default.Info, contentDescription = "Info Icon")
            }))
        )

        var buttonTrailingText by remember { mutableStateOf("") }
        CustomTextField(
            value = buttonTrailingText,
            onValueChange = { buttonTrailingText = it },
            hint = "With button icon",
            textFieldState = TextFieldState(
                TextFieldType.TrailingButtonIcon(
                    icon = {
                        Icon(Icons.Default.Search, contentDescription = "Search Icon")
                    },
                    onClick = { /* Action */ }
                )
            )
        )

        var phoneText by remember { mutableStateOf("") }
        CustomTextField(
            value = phoneText,
            onValueChange = { phoneText = it },
            hint = "Enter phone number",
            textFieldState = TextFieldState(
                TextFieldType.Phone(
                    countryCode = "+639",
                    onCountryCodeClick = { /* Action */ }
                )
            )
        )

        var errorText by remember { mutableStateOf("") }
        CustomTextField(
            value = errorText,
            onValueChange = { errorText = it },
            hint = "Enter text",
            textFieldState = TextFieldState(
                TextFieldType.Simple,
                errorMessage = "This field is required"
            )
        )

        var combinedText by remember { mutableStateOf("") }
        CustomTextField(
            value = combinedText,
            onValueChange = { combinedText = it },
            hint = "Enter password",
            textFieldState = TextFieldState(
                TextFieldType.Password,
                errorMessage = "Invalid password"
            )
        )

        var phoneErrorText by remember { mutableStateOf("") }
        CustomTextField(
            value = phoneErrorText,
            onValueChange = { phoneErrorText = it },
            hint = "Enter phone number",
            textFieldState = TextFieldState(
                TextFieldType.Phone(
                    countryCode = "+639",
                    onCountryCodeClick = { /* Action */ }
                ),
                errorMessage = "Invalid phone number"
            )
        )
    }
}
