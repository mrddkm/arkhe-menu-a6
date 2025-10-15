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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import com.arkhe.menu.presentation.screen.settings.account.components.DetailPersonalAccordions
import com.arkhe.menu.presentation.ui.components.edit.EditBirthdayField
import com.arkhe.menu.presentation.ui.components.edit.EditGenderDropdown
import com.arkhe.menu.presentation.ui.components.edit.EditInitialFields
import com.arkhe.menu.presentation.ui.components.edit.EditNameField
import com.arkhe.menu.presentation.ui.components.edit.EditNicknameFields
import com.arkhe.menu.presentation.ui.components.edit.EditableField
import com.arkhe.menu.presentation.ui.components.edit.EditableInfoScreenBase
import com.arkhe.menu.presentation.ui.theme.ArkheTheme
import com.arkhe.menu.presentation.viewmodel.LanguageViewModel
import com.arkhe.menu.utils.sampleUser
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Close
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@Composable
fun PersonalInfoScreen(
    onBackClick: () -> Unit,
    navController: NavController? = null,
    user: User,
    onUserUpdate: (User) -> Unit
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
            user = user,
            onUserUpdate = onUserUpdate
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoContent(
    modifier: Modifier = Modifier,
    user: User,
    onUserUpdate: (User) -> Unit,
    onHandleBackNavigation: () -> Unit = { },
    langViewModel: LanguageViewModel = koinViewModel()
) {
    var textLabelName by remember { mutableStateOf("Name") }
    var name by remember { mutableStateOf(user.name) }
    var textLabelInitial by remember { mutableStateOf("Initial") }
    var initial by remember { mutableStateOf(user.initial) }
    var textLabelNickname by remember { mutableStateOf("Nickname") }
    var nickname by remember { mutableStateOf(user.nickName) }
    var textLabelGender by remember { mutableStateOf("Gender") }
    var gender by remember { mutableStateOf(user.gender) }
    var textLabelBirthday by remember { mutableStateOf("Birthday") }
    var birthday by remember { mutableStateOf(user.birthday) }

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
                modifier = Modifier.fillMaxWidth(0.8f)
            )
            Spacer(Modifier.height(16.dp))
            DetailPersonalAccordions(
                title = "tripkeun",
                user = user
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
                EditableInfoScreenBase(
                    title = "Changes to your $textLabelName will be reflected across your Account.",
                    userData = user,
                    onUserUpdate = onUserUpdate,
                    fields = listOf(
                        EditableField(
                            label = textLabelName,
                            valueLabel = name,
                            getValue = { it.name },
                            applyChange = { old, new -> old.copy(name = new) },
                            isValid = { it.isNotEmpty() },
                            editor = { value, onValueChange ->
                                EditNameField(
                                    label = textLabelName,
                                    value = value,
                                    onValueChange = onValueChange
                                )
                            }
                        )
                    )
                )
                EditableInfoScreenBase(
                    title = "Changes to your $textLabelInitial will be reflected across your Account.",
                    userData = user,
                    onUserUpdate = onUserUpdate,
                    fields = listOf(
                        EditableField(
                            label = textLabelInitial,
                            valueLabel = initial,
                            getValue = { it.initial },
                            applyChange = { old, new -> old.copy(initial = new) },
                            isValid = { it.isNotEmpty() },
                            editor = { value, onValueChange ->
                                EditInitialFields(
                                    initial = value,
                                    labelInitial = textLabelInitial,
                                    onInitialChange = onValueChange
                                )
                            }
                        )
                    )
                )
                EditableInfoScreenBase(
                    title = "Changes to your $textLabelNickname will be reflected across your Account.",
                    userData = user,
                    onUserUpdate = onUserUpdate,
                    fields = listOf(
                        EditableField(
                            label = textLabelNickname,
                            valueLabel = nickname,
                            getValue = { it.nickName },
                            applyChange = { old, new -> old.copy(nickName = new) },
                            isValid = { it.isNotEmpty() },
                            editor = { value, onValueChange ->
                                EditNicknameFields(
                                    nickname = nickname,
                                    labelNickname = textLabelNickname,
                                    onNicknameChange = onValueChange
                                )
                            }
                        )
                    )
                )
                EditableInfoScreenBase(
                    title = "Update your birthdate ($textLabelBirthday) to match your ID card, you never know, someone might plan a surprise for you!",
                    userData = user,
                    onUserUpdate = onUserUpdate,
                    fields = listOf(
                        EditableField(
                            label = textLabelBirthday,
                            valueLabel = birthday,
                            getValue = { it.birthday },
                            applyChange = { old, new -> old.copy(birthday = new) },
                            isValid = { it.isNotEmpty() },
                            editor = { value, onValueChange ->
                                EditBirthdayField(
                                    selectedDate = value,
                                    label = textLabelBirthday,
                                    onDateChange = onValueChange
                                )
                            }
                        )
                    )
                )
                EditableInfoScreenBase(
                    title = "Update your $textLabelGender to match your real information to complete your personal data.",
                    userData = user,
                    onUserUpdate = onUserUpdate,
                    fields = listOf(
                        EditableField(
                            label = textLabelGender,
                            valueLabel = gender,
                            showDivider = false,
                            getValue = { it.gender },
                            applyChange = { old, new -> old.copy(gender = new) },
                            isValid = { it.isNotEmpty() },
                            editor = { value, onValueChange ->
                                EditGenderDropdown(
                                    selected = value,
                                    label = textLabelGender,
                                    onSelect = onValueChange
                                )
                            }
                        )
                    )
                )
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
                user = sampleUser,
                onUserUpdate = {}
            )
        }
    }
}