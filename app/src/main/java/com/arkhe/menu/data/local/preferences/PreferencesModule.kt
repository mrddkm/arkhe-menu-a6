package com.arkhe.menu.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.arkhe.menu.utils.Constants

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.Database.DATASTORE_NAME)