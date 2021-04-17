package com.youngerhousea.miraicompose.future

import com.arkivanov.decompose.ComponentContext
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier

/**
 * Decompose Koin extensions for Routing class
 *
 */

/**
 * inject lazily given dependency
 * @param qualifier - bean name / optional
 * @param parameters
 */
inline fun <reified T : Any> ComponentContext.inject(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
) =
    lazy { get<T>(qualifier, parameters) }

/**
 * Retrieve given dependency for ComponentContext
 * @param qualifier - bean name / optional
 * @param parameters
 */
inline fun <reified T : Any> ComponentContext.get(
    qualifier: Qualifier? = null,
    noinline parameters: ParametersDefinition? = null
) =
    getKoin().get<T>(qualifier, parameters)

/**
 * Retrieve given property for ComponentContext
 * @param key - key property
 */
fun <T: Any> ComponentContext.getProperty(key: String) =
    getKoin().getProperty<T>(key)

/**
 * Retrieve given property for ComponentContext
 * give a default value if property is missing
 *
 * @param key - key property
 * @param defaultValue - default value if property is missing
 *
 */
fun ComponentContext.getProperty(key: String, defaultValue: String) =
    getKoin().getProperty(key) ?: defaultValue

/**
 * Help work on ModuleDefinition
 */
fun ComponentContext.getKoin() = GlobalContext.get()