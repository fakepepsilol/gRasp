package com.fakepepsilol.grasp.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "config")

@Singleton
class Config @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val tag: String = "fpl->Config"

    val preloaded: Preloaded = Preloaded()
    class Preloaded{
        val urlEntries = ObservableList<UrlEntry>(mutableStateListOf(), onChange = {})
        var preloadFinished: Boolean = false
    }

    suspend fun preload() {
        Log.d(tag, "Starting preload.")

        val decodedEntries = Json.decodeFromString(
            ObservableListSerializer,
            loadString(KEY_UrlEntries, "[]")
        )
        preloaded.urlEntries.addAll(decodedEntries)
        preloaded.preloadFinished = true
    }


    companion object {
        val KEY_UrlEntries = stringPreferencesKey("UrlEntries")
    }



//    suspend fun saveInt(key: Preferences.Key<Int>, value: Int) {
//        Log.d(tag, "Saving: $key -> $value")
//        context.dataStore.edit {
//            it[key] = value
//        }
//        Log.d(tag, "Saving: $key -> done.")
//    }


    suspend fun loadString(key: Preferences.Key<String>, defaultValue: String): String {
        Log.d(tag, "Loading: $key")
        val ret = context.dataStore.data.map { prefs -> prefs[key] ?: defaultValue }.first()
        Log.d(tag, "Loading: $key done -> $ret")
        return ret
    }
    suspend fun saveString(key: Preferences.Key<String>, value: String) {
        Log.d(tag, "Saving: $key -> $value")
        context.dataStore.edit { prefs ->
            prefs[key] = value
        }
        Log.d(tag, "Saving: $key -> done.")
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