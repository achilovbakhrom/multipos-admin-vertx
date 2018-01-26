package com.basicsteps.multipos.utils

import com.basicsteps.multipos.config.*
import com.basicsteps.multipos.model.entities.Currency
import com.basicsteps.multipos.model.entities.UnitCategoryEntity
import com.basicsteps.multipos.model.entities.UnitEntity
import io.reactivex.Observable
import io.vertx.core.Vertx
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor

/**
 * Created at 20.01.2018
 *
 * @author Achilov Bakhrom
 *
 * Singleton Helper for creating per System data
 * from the resources
 */
object ConfigUtils {

    /**
     * Generating all units from file: resources/units.yaml
     */
    fun getUnitCategories(vertx: Vertx) : Observable<List<UnitCategoryEntity>> {
        return Observable.create({ event ->
            vertx.fileSystem().readFile(SystemConfig.UNITS_PATH, { handler ->
                if (handler.succeeded()) {
                    val result = ArrayList<UnitCategoryEntity>()
                    val yamlString = handler.result().toString()
                    val constructor = Constructor(UnitConfig::class.java)
                    val yaml = Yaml(constructor)
                    val data = yaml.loadAs(yamlString, UnitConfig::class.java)
                    data.units?.keys?.forEach { key ->
                        val unitCategory = UnitCategoryEntity()
                        unitCategory.id = key
                        unitCategory.name = key
                        result.add(unitCategory)
                    }
                    event.onNext(result)
                } else {
                    event.onError(Exception(handler.cause()))
                }
            })
        })
    }

    /**
     *  Generating all units from file: resources/units.yaml
     */
    fun getUnits(vertx: Vertx) : Observable<List<UnitEntity>> {
        return Observable.create({event ->
            vertx.fileSystem().readFile(SystemConfig.UNITS_PATH, { handler ->
                if (handler.succeeded()) {
                    val result = ArrayList<UnitEntity>()
                    val yamlString = handler.result().toString()
                    val constructor = Constructor(UnitConfig::class.java)
                    val yaml = Yaml(constructor)
                    val data = yaml.loadAs(yamlString, UnitConfig::class.java)
                    data.units?.keys?.forEach { key ->
                        data.units?.get(key)?.keys?.forEach { innerKey ->
                            val unit = UnitEntity()
                            unit.id = data.units?.get(key)?.get(innerKey)?.get("id") as String
                            unit.name = data.units?.get(key)?.get(innerKey)?.get("name") as String
                            unit.abbr = data.units?.get(key)?.get(innerKey)?.get("abbr") as String
                            unit.factor = data.units?.get(key)?.get(innerKey)?.get("factor") as Double
                            unit.active = true
                            unit.deleted = false
                            unit.userId = CommonConstants.ANONYMOUS
                            unit.unitCategoryEntityId = key
                            result.add(unit)
                        }
                    }
                    event.onNext(result)
                } else {
                    event.onError(Exception(handler.cause()))
                }
            })
        })
    }

    /**
     * Generating all units from file: resources/currencies.yaml
     */
    fun getCurrencies(vertx: Vertx) : Observable<List<Currency>> {
        return Observable.create({event ->
            vertx.fileSystem().readFile(SystemConfig.CURRENCIES_PATH, { handler ->
                if (handler.succeeded()) {
                    val result = ArrayList<Currency>()
                    val yamlString = handler.result().toString()
                    val constructor = Constructor(CurrencyConfig::class.java)
                    val yaml = Yaml(constructor)
                    val data = yaml.loadAs(yamlString, CurrencyConfig::class.java)
                    data.currencies?.keys?.forEach { key ->
                        val currency = Currency()
                        currency.id = key
                        currency.name = data.currencies?.get(key)?.get("name") as String
                        currency.abbr = data.currencies?.get(key)?.get("abbr") as String
                        currency.main = data.currencies?.get(key)?.get("isMain") as Boolean
                        currency.active = data.currencies?.get(key)?.get("active") as Boolean
                        currency.deleted = false
                        currency.userId = CommonConstants.ANONYMOUS
                        result.add(currency)
                    }
                    event.onNext(result)
                } else {
                    event.onError(Exception(handler.cause()))
                }
            })
        })
    }
}