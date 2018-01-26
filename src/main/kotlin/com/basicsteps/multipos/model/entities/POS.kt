package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Id
import java.io.Serializable

@Entity
data class POS(@SerializedName("name") var name: String,
               @SerializedName("address") var address: String,
               @SerializedName("phone_number") var phoneNumber: String,
               @SerializedName("login") var login: String,
               @SerializedName("name") var password: String,
               @SerializedName("stock_ids") var stockId: MutableList<String>,
               @SerializedName("stock_ids") var owner_company_id: String) : BaseModel() {

    @SerializedName("stock") var stock: Stock? = null

    override fun instance(): Instanceable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}