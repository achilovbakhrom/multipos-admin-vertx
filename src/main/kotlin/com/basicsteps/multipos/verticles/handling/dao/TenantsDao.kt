package com.basicsteps.multipos.verticles.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.model.Tenants
import com.basicsteps.multipos.utils.GenratorUtils
import com.basicsteps.multipos.utils.ValidationUtils
import com.sun.tools.internal.ws.processor.generator.GeneratorUtil
import de.braintags.io.vertx.pojomapper.dataaccess.query.IQueryResult
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable
import io.vertx.core.AsyncResult


class TenantsDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Tenants>(dbManager, dataStore, Tenants::class.java) {

    fun isTenantUnique(tenantId: String) : Observable<Boolean> {
        return Observable.create({ event ->
            val query = dataStore?.createQuery(Tenants::class.java)
            query?.field("tenant")?.`is`(tenantId)
            query?.execute({ result: AsyncResult<IQueryResult<Tenants>>? ->
                if (result?.succeeded()!!) {
                    val res = result.result()
                    val iterator = res.iterator()
                    event.onNext(iterator.hasNext())
                } else {
                    event.onError(ReadDbFailedException())
                }
            })
        })
    }

    fun generateTenant() : Observable<String> {
        return Observable.create({event ->
            dbManager
                    ?.tenantsDao
                    ?.findAll()
                    ?.map({ tenants ->
                        val result = mutableListOf<String>()
                        tenants.mapTo(result) { it.tenant }
                        result
                    })
                    ?.map({ tenants ->
                        var tenant: String
                        while (true) {
                            tenant = GenratorUtils.generateTenancyId()
                            if (!tenants.contains(tenant)) { break }
                        }
                        tenant
                    })?.subscribe({ uniqueTenant ->
                event.onNext(GenratorUtils.generateTenancyId())
            })
        })
    }
}