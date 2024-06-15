package com.devstreeit.vickypracticaldevstreeit.di

import android.content.Context
import com.devstreeit.vickypracticaldevstreeit.database.AppDataBase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDataBase.getDatabase(appContext)

    @Singleton
    @Provides
    fun provideLocationDao(db: AppDataBase) = db.locationDao()
}