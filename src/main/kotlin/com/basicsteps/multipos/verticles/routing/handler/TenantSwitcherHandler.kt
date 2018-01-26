package com.basicsteps.multipos.verticles.routing.handler

import com.basicsteps.multipos.config.CommonConstants
import io.vertx.core.Vertx
import io.vertx.ext.web.RoutingContext

class TenantSwitcherHandler(val vertx: Vertx) {

    fun switchTenant(routingContext: RoutingContext) {
        val tenantId: String? = routingContext.request().headers().get(CommonConstants.HEADER_TENANT)
        if (tenantId != null || !(tenantId?.equals("")!!)) {
            print("Tenant is: $tenantId\n")
            vertx.eventBus().send(CommonConstants.SWITCH_TENANT, tenantId)
        }
        routingContext.next()
    }

}