package com.fakepepsilol.grasp.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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


    var counter: Int = 0
        set(value) {
            field = value
            CoroutineScope(Dispatchers.IO).launch {
                saveCounter()
            }
        }

    suspend fun preload() {
        Log.i("fpllog", "preload")
        counter = context.dataStore.data.map { prefs ->
            prefs[KEY_COUNT] ?: 0
        }.first()
        Log.i("fpllog", "counter: ${counter}")
    }

    companion object {
        val KEY_COUNT = intPreferencesKey("counter")
    }

    val count: Flow<Int> = context.dataStore.data.map { prefs ->
        prefs[KEY_COUNT] ?: 0
    }

    suspend fun saveCounter() {
        context.dataStore.edit {
            it[KEY_COUNT] = counter
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