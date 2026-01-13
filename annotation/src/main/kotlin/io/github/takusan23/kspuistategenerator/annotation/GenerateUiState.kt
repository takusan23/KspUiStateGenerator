package io.github.takusan23.io.github.takusan23.kspuistategenerator.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.CLASS)
annotation class GenerateUiState

annotation class ClassArg(val clazz: KClass<*>) // KSP ではクラスが存在しないエラーになってしまう