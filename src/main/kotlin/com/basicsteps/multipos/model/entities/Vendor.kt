package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class Vendor(@SerializedName("status") var name: String,
                  @SerializedName("type") var address: Int) : BaseModel() {
    override fun instance(): Instanceable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}