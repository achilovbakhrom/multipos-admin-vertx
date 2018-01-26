package com.basicsteps.multipos.core

import io.vertx.core.Vertx

abstract class BaseHandler(val vertx : Vertx, val dbManager: DbManager)