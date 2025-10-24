package com.arkhe.menu.presentation.screen.auth.activation

import android.content.Context
import android.os.Build
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkhe.menu.data.local.preferences.Lang
import com.arkhe.menu.presentation.ui.components.HeaderTitleSecondary
import com.arkhe.menu.presentation.viewmodel.AuthUiState
import com.arkhe.menu.presentation.viewmodel.AuthViewModel
import com.arkhe.menu.presentation.viewmodel.LanguageViewModel
import com.arkhe.menu.presentation.viewmodel.SuccessType
import com.arkhe.menu.utils.Constants.ActivationFlow.ACT_ACTIVATION_CODE_STEP
import com.arkhe.menu.utils.Constants.ActivationFlow.ACT_ACTIVATION_STEP
import com.arkhe.menu.utils.Constants.ActivationFlow.ACT_CREATE_PASSWORD_STEP
import com.arkhe.menu.utils.Constants.ActivationFlow.ACT_VERIFICATION_STEP
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Lock
import compose.icons.evaicons.outline.CheckmarkCircle2
import compose.icons.evaicons.outline.Close
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivationBottomSheet(
    onDismiss: () -> Unit,
    onActivated: () -> Unit,
    authViewModel: AuthViewModel = koinViewModel(),
    langViewModel: LanguageViewModel = koinViewModel()
) {
    val state = rememberActivationState()
    val uiState by authViewModel.uiState.collectAsState()

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )

    LaunchedEffect(uiState) {
        when (val currentState = uiState) {
            is AuthUiState.Success -> {
                state.onMessageChange(currentState.message)
                currentState.data?.name?.let { userName ->
                    state.onUserNameChange(userName)
                }
                val message = currentState.message.lowercase()
                when (currentState.type) {
                    SuccessType.ACTIVATION -> {
                        when {
                            message.contains("4-digit activation code") || message.contains("sent to your email") -> {
                                state.onStepChange(2)
                            }

                            message.contains("proceed to create password") -> {
                                state.onStepChange(3)
                            }

                            message.contains("proceed to device activation") -> {
                                state.onStepChange(4)
                            }

                            message.contains("activation successfully") -> {
                                onActivated()
                                onDismiss()
                            }
                        }
                    }

                    else -> Unit
                }
            }

            is AuthUiState.Failed -> {}

            else -> Unit
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = {
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(20.dp)
                    .padding(top = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = EvaIcons.Fill.Lock,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 0.dp, start = 16.dp, end = 16.dp, bottom = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onDismiss() }) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = EvaIcons.Outline.Close,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                HeaderTitleSecondary(
                    title = langViewModel.getLocalized(Lang.ACTIVATION),
                )
                Spacer(Modifier.width(48.dp))
            }
            ActivationProgressIndicator(currentStep = state.step)
        }

        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 32.dp)
        ) {
            when (state.step) {
                1 -> ActivationContentStepOne(
                    state = state,
                    uiState = uiState,
                    authViewModel = authViewModel,
                    onDismiss = onDismiss,
                    onNext = {
                        state.scope.launch {
                            authViewModel.performActivationStep(
                                step = ACT_VERIFICATION_STEP,
                                userId = state.userId,
                                phone = state.phone,
                                mail = state.mail
                            )
                        }
                    }
                )

                2 -> ActivationContentStepTwo(
                    state = state,
                    uiState = uiState,
                    authViewModel = authViewModel,
                    onVerify = {
                        state.scope.launch {
                            authViewModel.performActivationStep(
                                step = ACT_ACTIVATION_CODE_STEP,
                                userId = state.userId,
                                activationCode = state.code
                            )
                        }
                    },
                    onBack = {
                        state.onStepChange(1)
                    }
                )

                3 -> ActivationContentStepThree(
                    state = state,
                    uiState = uiState,
                    authViewModel = authViewModel,
                    onContinue = {
                        state.scope.launch {
                            authViewModel.performActivationStep(
                                step = ACT_CREATE_PASSWORD_STEP,
                                userId = state.userId,
                                newPassword = state.password
                            )
                        }
                    },
                    onBack = {
                        state.onStepChange(2)
                    }
                )

                4 -> ActivationContentStepFour(
                    state = state,
                    uiState = uiState,
                    onFinish = {
                        state.scope.launch {
                            if (state.pin == state.confirmPin && state.pin.length == 4) {
                                val deviceInfo = mapOf(
                                    "deviceId" to Build.ID,
                                    "manufacturer" to Build.MANUFACTURER,
                                    "brand" to Build.BRAND,
                                    "model" to Build.MODEL,
                                    "device" to Build.DEVICE,
                                    "product" to Build.PRODUCT,
                                    "osVersion" to Build.VERSION.RELEASE,
                                    "sdkLevel" to Build.VERSION.SDK_INT.toString(),
                                    "securityPatch" to Build.VERSION.SECURITY_PATCH,
                                    "deviceType" to "smartphone",
                                    "appVersionName" to "1.0.0",
                                    "appVersionCode" to "1"
                                )

                                authViewModel.performActivationStep(
                                    step = ACT_ACTIVATION_STEP,
                                    isPinActive = true,
                                    deviceInfo = deviceInfo
                                )

                                onActivated
                                onDismiss
                            }
                        }
                    },
                    onBack = {
                        state.onStepChange(3)
                    }
                )
            }
        }
    }
}

@Composable
fun rememberActivationState(): ActivationState {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var step by remember { mutableIntStateOf(1) }
    var userId by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }

    var successMessage by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf("") }

    return ActivationState(
        step = step, onStepChange = { step = it },
        userId = userId, onUserIdChange = { userId = it },
        phone = phone, onPhoneChange = { phone = it },
        mail = email, onEmailChange = { email = it },
        code = code, onCodeChange = { code = it },
        password = password, onPasswordChange = { password = it },
        confirmPassword = confirmPassword, onConfirmPasswordChange = { confirmPassword = it },
        pin = pin, onPinChange = { pin = it },
        confirmPin = confirmPin, onConfirmPinChange = { confirmPin = it },
        userName = userName, onUserNameChange = { userName = it },
        successMessage = successMessage, onMessageChange = { successMessage = it },
        scope = scope, context = context
    )
}

data class ActivationState(
    val step: Int,
    val onStepChange: (Int) -> Unit,

    val userId: String,
    val onUserIdChange: (String) -> Unit,
    val phone: String,
    val onPhoneChange: (String) -> Unit,
    val mail: String,
    val onEmailChange: (String) -> Unit,

    val code: String,
    val onCodeChange: (String) -> Unit,

    val password: String,
    val onPasswordChange: (String) -> Unit,
    val confirmPassword: String,
    val onConfirmPasswordChange: (String) -> Unit,

    val pin: String,
    val onPinChange: (String) -> Unit,
    val confirmPin: String,
    val onConfirmPinChange: (String) -> Unit,

    val successMessage: String,
    val onMessageChange: (String) -> Unit,
    val userName: String,
    val onUserNameChange: (String) -> Unit,

    val scope: CoroutineScope,
    val context: Context
)

@Composable
private fun ActivationProgressIndicator(currentStep: Int) {
    val totalSteps = 4
    val progress by animateFloatAsState(
        targetValue = currentStep / totalSteps.toFloat(),
        animationSpec = tween(durationMillis = 400),
        label = "progressAnimation"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 8.dp)
    ) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(MaterialTheme.shapes.small),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            repeat(totalSteps) { index ->
                val stepNumber = index + 1
                val isCompleted = stepNumber < currentStep
                val isCurrent = stepNumber == currentStep

                StepIndicatorItem(
                    stepNumber = stepNumber,
                    label = getStepLabel(stepNumber),
                    isCompleted = isCompleted,
                    isCurrent = isCurrent
                )
            }
        }
    }
}

@Composable
private fun StepIndicatorItem(
    stepNumber: Int,
    label: String,
    isCompleted: Boolean,
    isCurrent: Boolean
) {
    val backgroundColor by animateColorAsState(
        targetValue = when {
            isCompleted -> MaterialTheme.colorScheme.primary
            isCurrent -> MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            else -> MaterialTheme.colorScheme.surfaceVariant
        },
        animationSpec = tween(300),
        label = "bgColor"
    )

    val textColor by animateColorAsState(
        targetValue = when {
            isCompleted || isCurrent -> MaterialTheme.colorScheme.onPrimary
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(300),
        label = "textColor"
    )

    val labelColor by animateColorAsState(
        targetValue = when {
            isCurrent -> MaterialTheme.colorScheme.primary
            isCompleted -> MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
            else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
        },
        animationSpec = tween(300),
        label = "labelColor"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.width(70.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = EvaIcons.Outline.CheckmarkCircle2,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(18.dp)
                )
            } else {
                Text(
                    text = stepNumber.toString(),
                    color = textColor,
                    fontSize = 14.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                )
            }
        }

        Text(
            text = label,
            color = labelColor,
            fontSize = 10.sp,
            fontWeight = if (isCurrent) FontWeight.SemiBold else FontWeight.Normal,
            maxLines = 2,
            lineHeight = 12.sp
        )
    }
}

private fun getStepLabel(step: Int): String {
    return when (step) {
        1 -> "Verification"
        2 -> "Code"
        3 -> "Password"
        4 -> "PIN"
        else -> "Other"
    }
}

