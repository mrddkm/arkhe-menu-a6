package com.arkhe.menu.data.repository

import com.arkhe.menu.data.local.dao.CategoryDao
import com.arkhe.menu.data.local.entity.CategoryEntity
import com.arkhe.menu.data.mapper.toDomain
import com.arkhe.menu.data.remote.api.SafeApiResult
import com.arkhe.menu.data.remote.api.TripkeunApiService
import com.arkhe.menu.domain.model.Category
import com.arkhe.menu.domain.repository.BaseRepository
import com.arkhe.menu.domain.repository.CategoryRepository
import com.arkhe.menu.data.mapper.toEntityList
import kotlinx.coroutines.flow.Flow

import kotlinx.coroutines.flow.map

class CategoryRepositoryImpl(
    private val api: TripkeunApiService,
    private val dao: CategoryDao
) : BaseRepository<CategoryEntity, Category>(
    daoFlow = dao.getAllCategories(),
    insertEntities = { dao.insertCategories(it) },
    clearEntities = { dao.deleteAllCategories() },
    mapperToDomain = { entity -> entity.toDomain() },
    fetchRemote = { token -> api.getCategories(token).toEntityList() }
), CategoryRepository {

    override fun getCategories(
        sessionToken: String,
        forceRefresh: Boolean
    ): Flow<SafeApiResult<List<Category>>> {
        return super.getAll().map { categories ->
            if (categories.isNotEmpty()) {
                SafeApiResult.Success(categories)
            } else {
                SafeApiResult.Loading
            }
        }
    }

    override suspend fun getCategory(id: String): Category? {
        return dao.getCategory(id)?.toDomain()
    }

    override suspend fun refreshCategories(sessionToken: String): SafeApiResult<List<Category>> {
        return super.sync(sessionToken)
    }
}