package com.arkhe.menu.presentation.screen.settings.account

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arkhe.menu.data.local.preferences.Lang
import com.arkhe.menu.di.appModule
import com.arkhe.menu.di.dataModule
import com.arkhe.menu.di.domainModule
import com.arkhe.menu.di.previewModule
import com.arkhe.menu.domain.model.User
import com.arkhe.menu.presentation.navigation.NavigationRoute
import com.arkhe.menu.presentation.screen.settings.account.components.AccountEditItem
import com.arkhe.menu.presentation.screen.settings.account.components.DetailPersonalAccordions
import com.arkhe.menu.presentation.ui.components.edit.EditBirthdayField
import com.arkhe.menu.presentation.ui.components.edit.EditBottomSheetBase
import com.arkhe.menu.presentation.ui.components.edit.EditGenderDropdown
import com.arkhe.menu.presentation.ui.components.edit.EditNameField
import com.arkhe.menu.presentation.ui.components.edit.EditNicknameFields
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.presentation.viewmodel.LanguageViewModel
import com.arkhe.menu.utils.sampleUser
import compose.icons.EvaIcons
import compose.icons.evaicons.Fill
import compose.icons.evaicons.Outline
import compose.icons.evaicons.fill.Lock
import compose.icons.evaicons.outline.Close
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@Composable
fun PersonalInfoScreen(
    onBackClick: () -> Unit,
    navController: NavController? = null,
    user: User
) {
    val handleBackNavigation: () -> Unit = {
        navController?.let { nav ->
            val popSuccess = nav.popBackStack()
            if (!popSuccess) {
                nav.navigate(NavigationRoute.MAIN) {
                    popUpTo(NavigationRoute.MAIN) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        } ?: run {
            onBackClick()
        }
    }

    Scaffold { paddingValues ->
        PersonalInfoContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            onHandleBackNavigation = handleBackNavigation,
            user = user
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoContent(
    modifier: Modifier = Modifier,
    user: User,
    onHandleBackNavigation: () -> Unit = { },
    langViewModel: LanguageViewModel = koinViewModel()
) {
    var currentUser by remember { mutableStateOf(user) }

    var isSheetOpen by remember { mutableStateOf(false) }
    var editField by remember { mutableStateOf<String?>(null) }

    var textLabelOne by remember { mutableStateOf("") }
    var textLabelTwo by remember { mutableStateOf("") }

    var textValue by remember { mutableStateOf("") }
    var initial by remember { mutableStateOf(currentUser.initial) }
    var nickname by remember { mutableStateOf(currentUser.nickName) }
    var selectedGender by remember { mutableStateOf(currentUser.gender) }
    var selectedDate by remember { mutableStateOf(currentUser.birthday) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
        confirmValueChange = { newValue ->
            newValue != SheetValue.Hidden
        }
    )

    Column(
        modifier = modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onHandleBackNavigation) {
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
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = langViewModel.getLocalized(Lang.PERSONAL_INFO),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Info about you and your preferences",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
            Spacer(Modifier.height(16.dp))
            DetailPersonalAccordions(
                title = "tripkeun",
                user = currentUser
            )
        }

        /*--- List Editable Fields ---*/
        Surface(
            modifier = Modifier
                .padding(start = 16.dp, top = 0.dp, bottom = 0.dp, end = 16.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                AccountEditItem(
                    label = "Name",
                    value = currentUser.name,
                    info = "Use your legal name",
                    onClick = {
                        textLabelOne = "Name"
                        editField = "name"
                        textValue = currentUser.name
                        isSheetOpen = true
                    }
                )
                AccountEditItem(
                    label = "Initial/Nickname",
                    value = "${currentUser.initial} - ${currentUser.nickName}",
                    onClick = {
                        textLabelOne = "Initial"
                        textLabelTwo = "Nickname"
                        editField = "nickname"
                        initial = currentUser.initial
                        nickname = currentUser.nickName
                        isSheetOpen = true
                    }
                )
                AccountEditItem(
                    label = "Birthday",
                    value = currentUser.birthday,
                    onClick = {
                        textLabelOne = "Birthday"
                        editField = "birthday"
                        selectedDate = currentUser.birthday
                        isSheetOpen = true
                    }
                )
                AccountEditItem(
                    label = "Gender",
                    value = currentUser.gender,
                    onClick = {
                        textLabelOne = "Gender"
                        editField = "gender"
                        selectedGender = currentUser.gender
                        isSheetOpen = true
                    },
                    showDivider = false
                )
            }
        }
    }

    /*--- Dynamic BottomSheet ---*/
    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { },
            sheetState = sheetState,
            dragHandle = {
                Box(
                    modifier = Modifier
                        .height(22.dp)
                        .width(22.dp)
                        .padding(top = 12.dp),
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
            when (editField) {
                "name" -> EditBottomSheetBase(
                    title = "Changes to your $textLabelOne will be reflected across your Account.",
                    isChanged = textValue != currentUser.name,
                    isValid = textValue.isNotBlank(),
                    onCancel = { isSheetOpen = false },
                    onSave = {
                        currentUser = currentUser.copy(name = textValue)
                        true
                    }
                ) {
                    EditNameField(
                        value = textValue,
                        label = textLabelOne,
                        onValueChange = { textValue = it },
                        onClear = { textValue = "" }
                    )
                }

                "nickname" -> EditBottomSheetBase(
                    title = "Changes to your $textLabelOne & $textLabelTwo will be reflected across your Account.",
                    isChanged = initial != currentUser.initial || nickname != currentUser.nickName,
                    isValid = initial.isNotBlank() && nickname.isNotBlank(),
                    onCancel = { isSheetOpen = false },
                    onSave = {
                        currentUser = currentUser.copy(initial = initial, nickName = nickname)
                        true
                    }
                ) {
                    EditNicknameFields(
                        initial = initial,
                        labelInitial = textLabelOne,
                        nickname = nickname,
                        labelNickname = textLabelTwo,
                        onInitialChange = { initial = it },
                        onNicknameChange = { nickname = it },
                        onClearInitial = { initial = "" },
                        onClearNickname = { nickname = "" }
                    )
                }

                "gender" -> EditBottomSheetBase(
                    title = "Update your $textLabelOne to match your real information to complete your personal data.",
                    isChanged = selectedGender != currentUser.gender,
                    isValid = selectedGender.isNotBlank(),
                    onCancel = { isSheetOpen = false },
                    onSave = {
                        currentUser = currentUser.copy(gender = selectedGender)
                        true
                    }
                ) {
                    EditGenderDropdown(
                        selected = selectedGender,
                        label = textLabelOne,
                        onSelect = { selectedGender = it }
                    )
                }

                "birthday" -> EditBottomSheetBase(
                    title = "Update your birthdate ($textLabelOne) to match your ID card, you never know, someone might plan a surprise for you!",
                    isChanged = selectedDate != currentUser.birthday,
                    isValid = selectedDate.isNotBlank(),
                    onCancel = { isSheetOpen = false },
                    onSave = {
                        currentUser = currentUser.copy(birthday = selectedDate)
                        true
                    }
                ) {
                    EditBirthdayField(
                        selectedDate = selectedDate,
                        label = textLabelOne,
                        onDateChange = { selectedDate = it }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PersonalInfoScreenPreview() {
    val previewContext = LocalContext.current
    KoinApplicationPreview(
        application = {
            androidContext(previewContext)
            modules(
                dataModule,
                domainModule,
                appModule,
                previewModule
            )
        }
    ) {
        ArkheTheme {
            PersonalInfoScreen(
                onBackClick = {},
                user = sampleUser
            )
        }
    }
}