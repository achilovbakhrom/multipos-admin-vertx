package com.basicsteps.multipos.verticles.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.Stock
import com.basicsteps.multipos.model.entities.Vendor
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class VendorDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<Vendor>(dbManager, dataStore, Vendor::class.java)