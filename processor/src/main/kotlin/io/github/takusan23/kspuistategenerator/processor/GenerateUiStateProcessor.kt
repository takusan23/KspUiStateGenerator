package io.github.takusan23.kspuistategenerator.processor

import com.google.devtools.ksp.processing.*
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.validate
import io.github.takusan23.io.github.takusan23.kspuistategenerator.annotation.GenerateUiState

class GenerateUiStateProcessor(
    options: Map<String, String>,
    logger: KSPLogger,
    private val codeGenerator: CodeGenerator
) : SymbolProcessor {

    /** ファイルが二回作られるのを防ぐ */
    private var invoked = false

    override fun process(resolver: Resolver): List<KSAnnotated> {
        // アノテーションがついているクラスを探す
        val symbols = resolver
            .getSymbolsWithAnnotation(GenerateUiState::class.qualifiedName!!) // implementation(project(":annotation")) したのでアクセス可。名前をべた書きしてもよかったかも・・・
            .filterIsInstance<KSClassDeclaration>() // クラスに限定する、Function とかもあります

        if (!invoked) {
            invoked = true

            // ファイルを作る
            symbols.forEach { symbol ->

                // 自動生成するクラス名
                val generatedClassName = "Generated${symbol.qualifiedName!!.getShortName()}"

                // クラスの引数を val hoge: String にする
                val parameterKotlinCode = symbol
                    .getAllProperties()
                    .joinToString(separator = ", ") {
                        "val ${it.simpleName.getShortName()}: ${it.type.resolve().declaration.qualifiedName!!.asString()}"
                    }

                // ファイルを作る
                // ビルドすると IDEA / AndroidStudio から参照可能になる
                val file = codeGenerator.createNewFile(
                    dependencies = Dependencies(false, *resolver.getAllFiles().toList().toTypedArray()),
                    packageName = "io.github.takusan23.kspuistategenerator",
                    fileName = generatedClassName
                )

                // 自動生成する Kotlin コード
                val generatedKotlinCode = """
                package io.github.takusan23.generateduistate

                sealed interface $generatedClassName {

                    data object Loading : $generatedClassName

                    data class Success(
                        $parameterKotlinCode
                    ) : $generatedClassName

                    data class Error(val throwable: Throwable) : $generatedClassName
                }
                """.trimIndent()

                // 書き込む
                file.write(generatedKotlinCode.toByteArray())
            }

        }

        val unableToProcess = symbols.filterNot { it.validate() }.toList()
        return unableToProcess
    }
}
