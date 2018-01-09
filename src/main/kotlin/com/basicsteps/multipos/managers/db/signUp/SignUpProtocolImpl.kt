package com.basicsteps.multipos.managers.db.signUp

import com.basicsteps.multipos.config.KeycloakConfig
import com.basicsteps.multipos.core.DbException
import com.basicsteps.multipos.core.LMResponse
import com.basicsteps.multipos.managers.db.DbManager
import com.basicsteps.multipos.model.sign_up.ConfirmationMapper
import com.basicsteps.multipos.model.sign_up.SignUpMapper
import com.basicsteps.multipos.utils.GenratorUtils
import io.reactivex.Observable
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import java.util.*

class SignUpProtocolImpl (val dbManager: DbManager) : SignUpProtocol{

    override fun getTenantId(id: String): Observable<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
            user.isEnabled = true
            val client = dbManager.usersClient!!
            client.realm(KeycloakConfig.REALM)
                    .users()
                    .create(user)
            //TODO other settings
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
                        event.onNext(false)
                        return@execute
                    }
                    getSignUpMapperByMail(mail)
                            .subscribe({result ->
                                if (result != null) {
                                    val equal = result.accessCode == accessCode
                                    if (equal) {
                                        createUser(result).subscribe()
                                        removeSignUpMapper(mail).subscribe()
                                    }
                                    event.onNext(equal)
                                }
                            }, { error ->
                                if (error is DbException) {
                                    when (error.code) {
                                        DbException.SUCH_DATA_NOT_EXISTS_CODE -> event.onError(DbException(DbException.SUCH_DATA_NOT_EXISTS_CODE, DbException.SUCH_DATA_NOT_EXISTS_MESSAGE))
                                        DbException.SUCH_DATA_EXISTS_CODE -> event.onError(DbException(DbException.SUCH_DATA_EXISTS_CODE, DbException.SUCH_DATA_EXISTS_MESSAGE))
                                        DbException.DB_FAILED_CODE -> event.onError(DbException(DbException.DB_FAILED_CODE, DbException.DB_FAILED_MESSAGE))
                                    }
                                }
                            })
                } else {
                    event.onError(DbException(DbException.DB_FAILED_CODE, DbException.DB_FAILED_MESSAGE))
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
                    event.onNext(handler.result().isEmpty)
                } else {
                    event.onError(DbException(DbException.DB_FAILED_CODE, DbException.DB_FAILED_MESSAGE))
                }
            })
        })
    }

    override fun createSignUpMapper(signUpMapper: SignUpMapper): Observable<String> {
        return Observable.create({ event ->
            val mail = signUpMapper.mail
            emailUnique(mail).subscribe({ unique ->
                if (!unique) {
                    event.onError(DbException(DbException.SUCH_DATA_EXISTS_CODE, DbException.SUCH_DATA_EXISTS_MESSAGE + ": $mail" ))
                } else {
                    val write = dbManager.signUpStore?.createWrite(SignUpMapper::class.java)!!
                    write.add(signUpMapper)
                    signUpMapper.accessCode = GenratorUtils.generateRandomInt(100000, 999999)
                    write.save({ writeEvent ->
                        if (writeEvent.succeeded()) {
                            event.onNext(signUpMapper.id)
                        } else {
                            event.onError(DbException(DbException.DB_FAILED_CODE, DbException.DB_FAILED_MESSAGE))
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
                        event.onError(DbException(DbException.SUCH_DATA_NOT_EXISTS_CODE, DbException.SUCH_DATA_NOT_EXISTS_MESSAGE))
                    } else {
                        event.onError(DbException(DbException.SUCH_DATA_EXISTS_CODE, DbException.SUCH_DATA_EXISTS_MESSAGE + ": more than twice $email" ))
                    }
                } else {
                    event.onError(DbException(DbException.DB_FAILED_CODE, DbException.DB_FAILED_MESSAGE))
                }
            })
        })
    }



}