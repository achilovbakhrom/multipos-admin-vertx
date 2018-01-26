package com.basicsteps.multipos.verticles.handling.dao

import com.basicsteps.multipos.config.CommonConstants
import com.basicsteps.multipos.config.KeycloakConfig
import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.core.dao.DataStoreException
import com.basicsteps.multipos.core.model.exceptions.DeleteDbFailedException
import com.basicsteps.multipos.core.model.exceptions.FieldConflictsException
import com.basicsteps.multipos.core.model.exceptions.NotExistsException
import com.basicsteps.multipos.core.model.exceptions.ReadDbFailedException
import com.basicsteps.multipos.model.sign_up.SignUpMapper
import com.basicsteps.multipos.utils.GenratorUtils
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import java.util.*

class SignUpDao(dbManager: DbManager, mongoDataStore: MongoDataStore?) :
        BaseDao<SignUpMapper>(dbManager, mongoDataStore, SignUpMapper::class.java) {

    fun createUser(signUpMapper: SignUpMapper): Observable<String> {
        return Observable.create({ event ->
            val credential = CredentialRepresentation()
            credential.type = CredentialRepresentation.PASSWORD
            credential.value = signUpMapper.password
            val user = UserRepresentation()
            user.setUsername(signUpMapper.mail)
            user.setFirstName(signUpMapper.firstName)
            user.setLastName(signUpMapper.lastName)
            user.setCredentials(Arrays.asList(credential))
            val attrs = HashMap<String, List<String>>()
            attrs["langueage"] = listOf("en")
            var tenantId = ""
            while (true) {
                tenantId = GenratorUtils.generateTenancyId()
                if (isTenantIdUnique(tenantId)) {
                    break
                }
            }
            attrs.put(CommonConstants.HEADER_TENANT, listOf(tenantId))
            user.attributes = attrs
            user.isEnabled = true
            val client = dbManager?.keycloak!!
            client.realm(KeycloakConfig.REALM)
                    .users()
                    .create(user)

            //TODO Adding role

            //TODO create config files

            event.onNext(client.realm("master").users().search(user.username).get(0).id)
        })
    }


    fun accessCode(mail: String, accessCode: Int): Observable<Boolean> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val findUserQuery = dataStore?.createQuery(SignUpMapper::class.java)
                findUserQuery?.field("mail")?.`is`(mail)
                findUserQuery?.execute({ handler ->
                    if (handler.succeeded()) {
                        if (handler.result().isEmpty) {
                            event.onError(NotExistsException("mail", mail))
                            return@execute
                        }
                        if (handler.result().size() > 1) {
                            event.onError(FieldConflictsException("mail", mail))
                            return@execute
                        }
                        handler.result().iterator().next({item ->
                            val result = item.result()
                            val equal = result.accessCode == accessCode
                            if (equal) {
                                createUser(result).subscribe()
                                removeSignUpMapper(mail).subscribe({}, {error -> event.onError(error) })
                            }
                            event.onNext(equal)
                        })
                    } else {
                        event.onError(ReadDbFailedException())
                    }
                })
            } else {
                event.onError(DataStoreException("${this::class.java.name}: datastore is not set yet..."))
            }
        })
    }

    fun emailUnique(email: String): Observable<Boolean> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val signUpQuery = dataStore?.createQuery(SignUpMapper::class.java)
                signUpQuery?.field("mail")?.`is`(email)
                signUpQuery?.execute({ handler ->
                    if (handler.succeeded()) {
                        var result = handler.result().isEmpty
                        if (result) {
                            val client = dbManager?.keycloak!!
                            val users = client.realm(KeycloakConfig.REALM).users()
                            result = users.search(email).isEmpty()
                        }
                        event.onNext(result)
                    } else {
                        event.onError(ReadDbFailedException())
                    }
                })
            } else {
                event.onError(DataStoreException("${this::class.java.name}: datastore is not set yet..."))
            }
        })
    }


    fun removeSignUpMapper(mail: String): Observable<Boolean> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val signUpQuery = dataStore?.createQuery(SignUpMapper::class.java)
                signUpQuery?.field("mail")?.`is`(mail)
                val signUpMapperDelete = dataStore?.createDelete(SignUpMapper::class.java)
                signUpMapperDelete?.setQuery(signUpQuery)
                signUpMapperDelete?.delete({ deleteResult ->
                    if (deleteResult.failed()) {
                        event.onError(DeleteDbFailedException())
                    } else {
                        event.onNext(true)
                    }
                })
            } else {
                event.onError(DataStoreException("${this::class.java.name}: datastore is not set yet..."))
            }

        })
    }

    fun getSignUpMapperByMail(email: String): Observable<SignUpMapper> {
        return Observable.create({ event ->
            if (dataStore != null) {
                val signUpQuery = dataStore?.createQuery(SignUpMapper::class.java)
                signUpQuery?.field("mail")?.`is`(email)
                signUpQuery?.execute({ handler ->
                    if (handler.succeeded()) {
                        if (handler.result().size() == 1) {
                            handler.result().iterator().next({ item -> event.onNext(item.result()) })
                        } else if (handler.result().isEmpty) {
                            event.onError(NotExistsException("mail", email))
                        } else {
                            event.onError(FieldConflictsException("mail", email))
                        }
                    } else {
                        event.onError(ReadDbFailedException())
                    }
                })
            } else
                event.onError(DataStoreException("${this::class.java.name}: datastore is not set yet..."))
        })
    }

    fun isTenantIdUnique(tenantId: String): Boolean {
        val users = dbManager?.keycloak?.realm(KeycloakConfig.REALM)?.users()?.list()
        if (users == null || users.isEmpty()) return true
        var result = true
        for (user in users) {
            val attrs = user.attributes?.get("X-TENANT-ID")
            if (attrs != null && !attrs.isEmpty() && attrs.get(0).equals(tenantId)) {
                result = false
                break
            }
        }
        return result
    }


}