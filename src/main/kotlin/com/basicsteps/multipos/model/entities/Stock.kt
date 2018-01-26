package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity

@Entity
data class Stock(@SerializedName("name") var name: String,
                 @SerializedName("address") var address: String,
                 @SerializedName("phone_number") var phoneNumber: String,
                 @SerializedName("pos_id") var accessPosId: MutableList<String>,
                 @SerializedName("establishment_id") var accessEstablishmentId: MutableList<String>) : BaseModel() {
    override fun instance(): Instanceable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}