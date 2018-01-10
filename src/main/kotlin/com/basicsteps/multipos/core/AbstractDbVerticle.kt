package com.basicsteps.multipos.core

import com.basicsteps.multipos.managers.db.DbManager
import io.vertx.core.AbstractVerticle

abstract class AbstractDbVerticle constructor(val dbManager: DbManager) : AbstractVerticle()