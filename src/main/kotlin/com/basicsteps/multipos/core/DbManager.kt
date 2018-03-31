package com.basicsteps.multipos.core

import com.basicsteps.multipos.config.CommonConstants
import com.basicsteps.multipos.config.SystemConfig
import com.basicsteps.multipos.core.model.exceptions.TenantNotFoundException
import com.basicsteps.multipos.verticles.handling.dao.*
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore
import io.reactivex.Observable
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.MongoClient
import org.keycloak.admin.client.Keycloak
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.admin.client.KeycloakBuilder




class DbManager(vertx: Vertx) {

    //daos
    var signUpDao: SignUpDao? = null
    var unitEntityDao: UnitEntityDao? = null // tenanstore unit dao
    var unitCategoryEntityDao: UnitCategoryEntityDao? = null //tenantstore unit category dao
    var currencyDao: CurrencyDao? = null
    var keycloak: Keycloak? = null // keycloak client
    var accountDao: AccountDao? = null
    var paymentTypeDao: PaymentTypeDao? = null
    var vendorDao: VendorDao? = null
    var productDao: ProductDao? = null
    var subCategoryDao: SubCategoryDao? = null
    var categoryDao: CategoryDao? = null
    var companyDao: CompanyDao? = null
    var posDao: POSDao? = null
    var establishmentDao: EstablishmentDao? = null
    var stockDao: StockDao? = null
    var tenantsDao: TenantsDao? = null
    var userDao: UserDao? = null
    var userCompanyRelDao: UserCompanyRelDao? = null

    private var vertx: Vertx? = vertx

    init {
        //keycloak client
        this.keycloak = KeycloakBuilder.builder() //
                .serverUrl(SystemConfig.KEYCLOAK_AUTH_ENDPOINT) //
                .realm(SystemConfig.REALM)//
                .username(SystemConfig.KEYCLOAK_ADMIN) //
                .password(SystemConfig.KEYCLOAK_PASSWORD) //
                .clientId(SystemConfig.WEB_TOKEN_CLIENT_ID) //
                .clientSecret(SystemConfig.WEB_TOKEN_CLIENT_SECRET)
                .resteasyClient(ResteasyClientBuilder().connectionPoolSize(10).build()) //
                .build()

        // init daos basing on datastores
        //sign up datastore
        signUpDao = SignUpDao(this, getSignUpDataStore())

        //common datastore
        companyDao = CompanyDao(this, getCommonDataStore())
        userCompanyRelDao = UserCompanyRelDao(this, getCommonDataStore())
        tenantsDao = TenantsDao(this, getCommonDataStore())

        //tenant datastore
        unitEntityDao                   = UnitEntityDao(this, null)
        unitCategoryEntityDao           = UnitCategoryEntityDao(this, null)
        currencyDao                     = CurrencyDao(this, null)
        accountDao                      = AccountDao(this, null)
        paymentTypeDao                  = PaymentTypeDao(this, null)
        vendorDao                       = VendorDao(this, null)
        productDao                      = ProductDao(this, null)
        categoryDao                     = CategoryDao(this, null)
        subCategoryDao                  = SubCategoryDao(this, null)
        posDao                          = POSDao(this, null)
        establishmentDao                = EstablishmentDao(this, null)
        stockDao                        = StockDao(this, null)

        //UserDao
        if (keycloak != null) userDao = UserDao(this)
        setMongoClientByTenantId(SystemConfig.COMMON_DB)
    }

    var commonDatastore: MongoDataStore? = null
    private fun getCommonDataStore() : MongoDataStore? {
        if (commonDatastore == null) {
            val commonDatastoreConfig = JsonObject()
                    .put("connection_string", SystemConfig.MONGO_URI)
                    .put("db_name", SystemConfig.COMMON_DB)
            commonDatastore = MongoDataStore(vertx, MongoClient.createNonShared(vertx, commonDatastoreConfig), commonDatastoreConfig)
        }
        return commonDatastore
    }
    
    var signUpdataStore: MongoDataStore? = null
    private fun getSignUpDataStore() : MongoDataStore? {
        if (signUpdataStore == null) {
            val signUpMongoConfig = JsonObject()
                    .put("connection_string", SystemConfig.MONGO_URI)
                    .put("db_name", SystemConfig.SIGN_UP_DB)
            signUpdataStore = MongoDataStore(this.vertx, MongoClient.createNonShared(vertx, signUpMongoConfig), signUpMongoConfig)
        }
        return signUpdataStore
    }

    fun setMongoClientByTenantId(tenantId: String) {
        val tenantMongoConfig = JsonObject()
                .put("connection_string", SystemConfig.MONGO_URI)
                .put("db_name", tenantId)
        val temp = MongoDataStore(vertx, MongoClient.createNonShared(vertx, tenantMongoConfig), tenantMongoConfig)

        //set tenantStore to all daos
        unitEntityDao?.dataStore                = temp
        unitCategoryEntityDao?.dataStore        = temp
        currencyDao?.dataStore                  = temp
        accountDao?.dataStore                   = temp
        paymentTypeDao?.dataStore               = temp
        vendorDao?.dataStore                    = temp
        productDao?.dataStore                   = temp
        posDao?.dataStore                       = temp
        establishmentDao?.dataStore             = temp
        stockDao?.dataStore                     = temp
    }
    fun close() { keycloak?.close() }

    fun setTenantId(tenantId: String) : Observable<Boolean> {
        return Observable.create({ event ->
            setMongoClientByTenantId(tenantId)
            event.onNext(true)
        })
    }
    fun getTenantIdByEmail(mail: String) : Observable<String> {
        return Observable.create({event ->
            val user = keycloak?.realm(SystemConfig.REALM)?.users()?.search(mail)
            if (user != null && !user.isEmpty()) {
                val u = user.get(0)
                val attrs = u.attributes.get(CommonConstants.HEADER_TENANT)
                if (attrs != null && !attrs.isEmpty()) {
                    event.onNext(attrs.get(0))
                    return@create
                } else {
                    event.onError(TenantNotFoundException(mail))
                }

            } else {
                event.onError(TenantNotFoundException(mail))
            }
        })
    }

}