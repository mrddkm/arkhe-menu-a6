package com.arkhe.menu.domain.repository

import com.arkhe.menu.data.remote.api.SafeApiResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class BaseRepository<Entity, Domain>(
    private val daoFlow: Flow<List<Entity>>,
    private val insertEntities: suspend (List<Entity>) -> Unit,
    private val clearEntities: suspend () -> Unit,
    private val mapperToDomain: (Entity) -> Domain,
    private val fetchRemote: suspend (String) -> List<Entity>
) {
    fun getAll(): Flow<List<Domain>> =
        daoFlow.map { entities -> entities.map(mapperToDomain) }

    suspend fun sync(token: String): SafeApiResult<List<Domain>> {
        return try {
            val remoteEntities = fetchRemote(token)
            clearEntities()
            insertEntities(remoteEntities)
            SafeApiResult.Success(remoteEntities.map(mapperToDomain))
        } catch (e: Exception) {
            SafeApiResult.Error(e)
        }
    }
}