import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.Delete
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.File

class MiraiComposeHelper : Plugin<Project> {
    override fun apply(target: Project): Unit = target.run {
        val compileKotlin by tasks.withType<KotlinCompile>()

        val fillBuildConstants by tasks.composeRegister("fillBuildConstants") {
            compileKotlin.source.filter { it.name == "MiraiCompose.kt" }.single()
                .let { file ->
                    file.writeText(
                        file.readText()
                            .replace(
                                Regex("""override val version: SemVersion = SemVersion(\(.*\))""")
                            ) {
                                """override val version: SemVersion = SemVersion("${rootProject.version}")"""
                            }
                    )
                }
        }

        val cleanGenerate by tasks.composeRegister<Delete>("cleanGenerate") {
            delete("data")
            delete("config")
            delete("plugins")
            delete("bot")
        }

//        val autoCleanRun by tasks.composeRegister("autoCleanRun") {
//            dependsOn(tasks.getByName("run"))
//        }
//
//        autoCleanRun.finalizedBy(cleanGenerate)
//        cleanGenerate.mustRunAfter(autoCleanRun)

        compileKotlin.dependsOn(fillBuildConstants)
    }
}

inline fun <reified T : Task> TaskContainer.composeRegister(
    name: String,
    crossinline configurationAction: T.() -> Unit
): TaskProvider<T> = register(name, T::class.java) {
    group = "mirai compose"
    configurationAction(this)
}


internal fun configureUserDir() {
    val run = File(".").resolve("run")
    run.mkdir()
    System.setProperty("user.dir", run.absolutePath)
    println("[Mirai Compose] Set user.dir = ${run.absolutePath}")
}
