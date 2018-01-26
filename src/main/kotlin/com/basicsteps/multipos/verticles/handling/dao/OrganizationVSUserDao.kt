package com.basicsteps.multipos.verticles.handling.dao

import com.basicsteps.multipos.core.DbManager
import com.basicsteps.multipos.core.dao.BaseDao
import com.basicsteps.multipos.model.entities.OrganizationVSUser
import de.braintags.io.vertx.pojomapper.mongo.MongoDataStore

class OrganizationVSUserDao(dbManager: DbManager, dataStore: MongoDataStore?) : BaseDao<OrganizationVSUser>(dbManager, dataStore, OrganizationVSUser::class.java)