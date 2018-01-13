package com.basicsteps.multipos.managers.db.signUp

import com.basicsteps.multipos.model.sign_up.SignUpMapper
import io.reactivex.Observable


interface SignUpProtocol {
    fun createSignUpMapper(signUpMapper: SignUpMapper) : Observable<String>
    fun removeSignUpMapper(id: String) : Observable<Boolean>
    fun getSignUpMapperByMail(email: String) : Observable<SignUpMapper>
    fun emailUnique(email: String) : Observable<Boolean>
    fun accessCode(mail: String, accessCode: Int) : Observable<Boolean>
    fun createUser(signUpMapper: SignUpMapper) : Observable<String>
    fun isTenantIdUnique(tenantId: String) : Boolean
}