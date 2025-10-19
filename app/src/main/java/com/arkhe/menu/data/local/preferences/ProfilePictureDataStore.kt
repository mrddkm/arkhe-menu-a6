package com.arkhe.menu.data.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import com.arkhe.menu.utils.Constants.Database.KEY_PROFILE_PICTURE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProfilePicturePrefs(private val context: Context) {
    fun getProfilePicture(): Flow<String?> =
        context.dataStore.data.map { it[KEY_PROFILE_PICTURE] }

    suspend fun saveProfilePicture(uri: String?) {
        context.dataStore.edit { prefs ->
            if (uri == null) prefs.remove(KEY_PROFILE_PICTURE)
            else prefs[KEY_PROFILE_PICTURE] = uri
        }
    }
}
