package com.basicsteps.multipos.verticles.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Company
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class CompanyDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Company>(dbManager, dataStore, Company::class.java)