package com.fakepepsilol.grasp.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "config")

@Singleton
class Config @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val TAG: String = "fpl->Config"

    var counter: Int = 0
        set(value) {
            field = value
            CoroutineScope(Dispatchers.IO).launch {
                saveCounter()
            }
        }

    var urls: ObservableList<String> = ObservableList(onChange = {
        
    })


    suspend fun preload() {
        Log.d(TAG, "preload")
        counter = context.dataStore.data.map { prefs ->
            prefs[I_COUNTER] ?: 0
        }.first()
        Log.i(TAG, "counter: ${counter}") // TODO: remove
    }

    companion object {
        val I_COUNTER = intPreferencesKey("counter")
        val L_URLS = stringPreferencesKey("urls")
    }

    val count: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[I_COUNTER] ?: 0
    }

    suspend fun saveCounter() {
        context.dataStore.edit {
            it[I_COUNTER] = counter
        }
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ConfigModule {

    @Provides
    @Singleton
    fun getConfig(@ApplicationContext context: Context): Config {
        return Config(context)
    }
}