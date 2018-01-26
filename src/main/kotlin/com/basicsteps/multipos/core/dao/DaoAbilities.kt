package com.basicsteps.multipos.core.dao

import io.reactivex.Observable

interface Trash<T> {
    fun trash(t: T, id: String, companyId: String? = null) : Observable<T>
    fun trashAll(t: List<T>, id: String, companyId: String? = null) : Observable<List<T>>
    fun untrash(t: T, id: String, companyId: String? = null) : Observable<T>
    fun untrashAll(t: List<T>, id: String, companyId: String? = null) : Observable<List<T>>
}
interface CRUDDao<T> : Trash<T>, Active<T>, Pageable<T> {
    fun save(t: T, id: String, companyId: String? = null) : Observable<T>
    fun saveAll(t: List<T>, id: String, companyId: String? = null) : Observable<List<T>>
    fun update(t: T, id: String, companyId: String? = null) : Observable<T>
    fun updateAll(t: List<T>, id: String, companyId: String? = null) : Observable<List<T>>
    fun updateWithoutDuplicate(t: T, id: String, companyId: String? = null) : Observable<T>
    fun updateWithoutDuplicateAll(t: List<T>, id: String, companyId: String? = null) : Observable<List<T>>
    fun delete(t: T, id: String, companyId: String? = null) : Observable<Boolean>
    fun deleteAll(t: List<T>, id: String, companyId: String? = null) : Observable<Boolean>
    fun findById(id: String, companyId: String? = null) : Observable<T>
    fun findAll() : Observable<MutableList<T>>
}

interface Active<T> {
    fun activate(t: T, id: String, companyId: String? = null) : Observable<T>
    fun unactivate(t: T, id: String, companyId: String? = null) : Observable<T>
}

interface Pageable<T> {
    fun get(start: Int, limit: Int) : Observable<List<T>>
}