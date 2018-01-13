package com.basicsteps.multipos.managers.db.signUp

import com.basicsteps.multipos.config.KeycloakConfig
import com.basicsteps.multipos.core.DbException
import com.basicsteps.multipos.core.exceptions.FieldConflictsException
import com.basicsteps.multipos.core.exceptions.NotExistsException
import com.basicsteps.multipos.core.exceptions.ReadDbFailedException
import com.basicsteps.multipos.core.exceptions.WriteDbFailedException
import com.basicsteps.multipos.managers.db.DbManager
import com.basicsteps.multipos.model.sign_up.SignUpMapper
import com.basicsteps.multipos.utils.GenratorUtils
import com.sun.deploy.util.GeneralUtil
import io.reactivex.Observable
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import java.util.*
import kotlin.collections.HashMap

class SignUpProtocolImpl (val dbManager: DbManager) : SignUpProtocol{

    override fun isTenantIdUnique(tenantId: String): Boolean {
        val client = dbManager.usersClient!!
        val users = client.realm(KeycloakConfig.REALM).users().list()
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


    override fun createUser(signUpMapper: SignUpMapper): Observable<String> {
        return Observable.create({event ->
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
            attrs.put("X-TENANT-ID", listOf(tenantId))
            user.attributes = attrs
            user.isEnabled = true
            val client = dbManager.usersClient!!
            client.realm(KeycloakConfig.REALM)
                    .users()
                    .create(user)
            //TODO Adding role
            event.onNext(client.realm("master").users().search(user.username).get(0).id)
        })
    }


    override fun accessCode(mail: String, accessCode: Int): Observable<Boolean> {
        return Observable.create({event ->
            val findUserQuery = dbManager.signUpStore?.createQuery(SignUpMapper::class.java)
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
                            createUser(result).subscribe() // TODO handle error
                            removeSignUpMapper(mail).subscribe() // TODO handle error
                        }
                        event.onNext(equal)
                    })
                } else {
                    event.onError(ReadDbFailedException())
                }
            })
        })
    }

    override fun emailUnique(email: String): Observable<Boolean> {
        return Observable.create({ event ->
            val signUpQuery = dbManager.signUpStore?.createQuery(SignUpMapper::class.java)
            signUpQuery?.field("mail")?.`is`(email)
            signUpQuery?.execute({ handler ->
                if (handler.succeeded()) {
                    var result = handler.result().isEmpty
                    if (result) {
                        val client = dbManager.usersClient!!
                        val users = client.realm(KeycloakConfig.REALM).users()
                        result = users.search(email).isEmpty()
                    }
                    event.onNext(result)
                } else {
                    event.onError(ReadDbFailedException())
                }
            })
        })
    }

    override fun createSignUpMapper(signUpMapper: SignUpMapper): Observable<String> {
        return Observable.create({ event ->
            val mail = signUpMapper.mail
            emailUnique(mail).subscribe({ unique ->
                if (!unique) {
                    event.onError(FieldConflictsException("mail", mail))
                } else {
                    val write = dbManager.signUpStore?.createWrite(SignUpMapper::class.java)!!
                    write.add(signUpMapper)
                    signUpMapper.accessCode = GenratorUtils.generateRandomInt(100000, 999999)
                    write.save({ writeEvent ->
                        if (writeEvent.succeeded()) {
                            event.onNext(signUpMapper.id)
                        } else {
                            event.onError(WriteDbFailedException())
                        }
                    })
                }
            })
        })
    }

    override fun removeSignUpMapper(mail: String): Observable<Boolean> {
        return Observable.create({event ->
            val signUpQuery = dbManager.signUpStore?.createQuery(SignUpMapper::class.java)
            signUpQuery?.field("mail")?.`is`(mail)
            val signUpMapperDelete = dbManager.signUpStore?.createDelete(SignUpMapper::class.java)
            signUpMapperDelete?.setQuery(signUpQuery)
            signUpMapperDelete?.delete({ deleteResult ->
                if (deleteResult.failed()) {
                    event.onError(DbException(DbException.DB_FAILED_CODE, DbException.DB_FAILED_MESSAGE))
                } else {
                    event.onNext(true)
                }
            })
        })
    }

    override fun getSignUpMapperByMail(email: String): Observable<SignUpMapper> {
        return Observable.create({ event ->
            val signUpQuery = dbManager.signUpStore?.createQuery(SignUpMapper::class.java)
            signUpQuery?.field("mail")?.`is`(email)
            signUpQuery?.execute({ handler ->
                if (handler.succeeded()) {
                    if (handler.result().size() == 1) {
                        handler.result().iterator().next({item -> event.onNext(item.result())})
                    } else if (handler.result().isEmpty) {
                        event.onError(NotExistsException("mail", email))
                    } else {
                        event.onError(FieldConflictsException("mail", email))
                    }
                } else {
                    event.onError(ReadDbFailedException())
                }
            })
        })
    }



}