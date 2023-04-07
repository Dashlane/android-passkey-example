package com.dashlane.dashlanepasskeydemo.injection

import android.content.Context
import android.content.SharedPreferences
import androidx.credentials.CredentialManager
import androidx.preference.PreferenceManager
import com.dashlane.dashlanepasskeydemo.repository.AccountRepository
import com.dashlane.dashlanepasskeydemo.repository.AccountRepositoryLocal
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
abstract class DashlanePasskeyModule {
    @Binds
    abstract fun bindAccountRepository(impl: AccountRepositoryLocal): AccountRepository

    companion object {
        @Provides
        fun provideCredentialManager(@ApplicationContext context: Context): CredentialManager {
            return CredentialManager.create(context)
        }

        @Provides
        fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }

        @Provides
        fun provideGson(): Gson {
            return Gson()
        }
    }
}