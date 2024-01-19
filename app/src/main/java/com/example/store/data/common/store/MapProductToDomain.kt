package com.example.store.data.common.store

import com.example.store.data.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <any, DomainType> Flow<Resource<List<any>>>.mapToDomain(mapper: (any) -> DomainType): Flow<Resource<List<DomainType>>> {
    return this.map { resource ->
        when (resource) {
            is Resource.Success -> Resource.Success(resource.data.map(mapper))
            is Resource.Error -> Resource.Error(resource.errorMessage)
            is Resource.Loading -> Resource.Loading(resource.loading)
        }
    }
}
