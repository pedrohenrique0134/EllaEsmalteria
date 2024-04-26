package com.app.ellaeamalteriasistemadelancamentos.di

import com.app.ellaeamalteriasistemadelancamentos.repositorys.Repository
import com.app.ellaeamalteriasistemadelancamentos.repositorys.RepositoryImpl
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(
        database: FirebaseFirestore
    ):Repository{
        return RepositoryImpl(database)
    }
}