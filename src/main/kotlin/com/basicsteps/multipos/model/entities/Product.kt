package com.basicsteps.multipos.model.entities

import com.basicsteps.multipos.core.model.BaseModel
import com.basicsteps.multipos.core.model.Instanceable
import com.google.gson.annotations.SerializedName
import de.braintags.io.vertx.pojomapper.annotation.Entity
import de.braintags.io.vertx.pojomapper.annotation.field.Id

@Entity
data class Product(@SerializedName("subcategory") var subcategoryId: String,
                   @SerializedName("name") var name: String,
                   @SerializedName("unitcategory_id") var unitCategoryId: String,
                   @SerializedName("unit_id") var unitId: String,
                   @SerializedName("product_class_id") var productClassId: String,
                   @SerializedName("description") var description: String,
                   @SerializedName("is_ingridient") var isIngridient: Boolean,
                   @SerializedName("has_ingridient") var hasIngridient: Boolean,
                   @SerializedName("ingridients") var ingridients: List<String>,
                   @SerializedName("country") var country: String) : BaseModel() {
    override fun instance(): Instanceable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}