package com.basicsteps.multipos.verticles.handling.dao

import com.basicsteps.multipos.config.KeycloakConfig
import com.basicsteps.multipos.core.DbManager
import io.reactivex.Observable
import org.keycloak.representations.idm.UserRepresentation

class UserDao(val dbManager: DbManager) {

    fun save(user: UserRepresentation) : Observable<UserRepresentation> {
        return Observable.create({ event ->
            val client = dbManager.keycloak!!
            client
                    .realm(KeycloakConfig.REALM)
                    .users()
                    .create(user)
            event.onNext(user)
        })
    }

    fun remove(user: UserRepresentation) : Observable<Boolean> {
        return Observable.create({ event ->
            TODO("Implementation")
        })
    }

    fun update(user: UserRepresentation) : Observable<UserRepresentation> {
        return Observable.create({ event ->
            TODO("Implementation")
        })
    }

    fun findByEMail(email: String) : Observable<UserRepresentation> {
        return Observable.create({ event ->
            TODO("Implementation")
        })
    }

    fun findAll() : Observable<List<UserRepresentation>> {
        return Observable.create({ event ->
            TODO("Implementation")
        })
    }

}