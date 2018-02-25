package com.frogermcs.androiddevmetrics.weaving.plugin

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import com.android.build.gradle.api.BaseVariant
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.DomainObjectSet
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.AbstractCompile

class AndroidDevMetricsPlugin: Plugin<Project> {

    override fun apply(project: Project) {
        with(project) {
            val hasApp = project.plugins.withType(AppPlugin::class.java).isNotEmpty()
            val hasLib = project.plugins.withType(LibraryPlugin::class.java).isNotEmpty()
            if (!hasApp && !hasLib) {
                throw IllegalStateException("'android' or 'android-library' plugin required.")
            }

            //TODO can we do it as closure?
            project.dependencies.add("releaseCompile", "com.frogermcs.androiddevmetrics:androiddevmetrics-runtime-noop:0.6")
            project.dependencies.add("debugCompile", "com.frogermcs.androiddevmetrics:androiddevmetrics-runtime:0.6")
            project.dependencies.add("debugCompile", "org.aspectj:aspectjrt:1.8.8")
            project.dependencies.add("compile", "com.android.support:support-v4:26.1.0")

            val log = project.logger
            val variants: DomainObjectSet<BaseVariant>
            if (hasApp) {
                variants = (project.extensions.getByName("android") as AppExtension).applicationVariants as DomainObjectSet<BaseVariant>
            } else {
                variants = (project.extensions.getByName("android") as LibraryExtension).libraryVariants as DomainObjectSet<BaseVariant>
            }

            variants.all { variant ->
                if (!variant.buildType.isDebuggable) {
                    log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
                    return@all
                }

                val javaCompiler = variant.javaCompiler as AbstractCompile
                javaCompiler.doLast({
                    /*val args = [
                            "-showWeaveInfo",
                            "-1.5",
                            "-inpath", javaCompiler.destinationDir.toString(),
                            "-aspectpath", javaCompiler.classpath.asPath,
                            "-d", javaCompiler.destinationDir.toString(),
                            "-classpath", javaCompiler.classpath.asPath
                            //"-bootclasspath", (project.extensions.getByName("android") as BaseExtension).bootClasspath.(File.pathSeparator)
                            ]*/
                    val args = arrayOf(
                            "-showWeaveInfo",
                            "-1.5",
                            "-inpath", javaCompiler.destinationDir.toString(),
                            "-aspectpath", javaCompiler.classpath.asPath,
                            "-d", javaCompiler.destinationDir.toString(),
                            "-classpath", javaCompiler.classpath.asPath
                            //"-bootclasspath", (project.extensions.getByName("android") as BaseExtension).bootClasspath.(File.pathSeparator)
                    )
                    log.debug("ajc args: %s".format(args))

                    val handler = MessageHandler(true)
                    Main().run(args, handler)

                    handler.getMessages(null, true).forEach {
                        when(it.kind) {
                            IMessage.ABORT, IMessage.ERROR, IMessage.FAIL -> log.error(it.message, it.thrown)
                            IMessage.WARNING -> log.warn(it.message, it.thrown)
                            IMessage.INFO -> log.info(it.message, it.thrown)
                            IMessage.DEBUG -> log.debug(it.message, it.thrown)
                        }
                    }
                })
            }
        }
    }
}

/**
 * package com.frogermcs.androiddevmetrics.weaving.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.compile.JavaCompile

public class AndroidDevMetricsPlugin implements Plugin<Project> {
@Override void apply(Project project) {
def hasApp = project.plugins.withType(AppPlugin)
def hasLib = project.plugins.withType(LibraryPlugin)
if (!hasApp && !hasLib) {
throw new IllegalStateException("'android' or 'android-library' plugin required.")
}

final def log = project.logger
final def variants
if (hasApp) {
variants = project.android.applicationVariants
} else {
variants = project.android.libraryVariants
}

project.dependencies {
releaseCompile 'com.frogermcs.androiddevmetrics:androiddevmetrics-runtime-noop:0.6'
debugCompile 'com.frogermcs.androiddevmetrics:androiddevmetrics-runtime:0.6'
debugCompile 'org.aspectj:aspectjrt:1.8.8'
compile 'com.android.support:support-v4:26.1.0'
}

variants.all { variant ->
if (!variant.buildType.isDebuggable()) {
log.debug("Skipping non-debuggable build type '${variant.buildType.name}'.")
return;
}

JavaCompile javaCompile = variant.javaCompile
javaCompile.doLast {
String[] args = [
"-showWeaveInfo",
"-1.5",
"-inpath", javaCompile.destinationDir.toString(),
"-aspectpath", javaCompile.classpath.asPath,
"-d", javaCompile.destinationDir.toString(),
"-classpath", javaCompile.classpath.asPath,
"-bootclasspath", project.android.bootClasspath.join(File.pathSeparator)
]
log.debug "ajc args: " + Arrays.toString(args)

MessageHandler handler = new MessageHandler(true);
new Main().run(args, handler);
for (IMessage message : handler.getMessages(null, true)) {
switch (message.getKind()) {
case IMessage.ABORT:
case IMessage.ERROR:
case IMessage.FAIL:
log.error message.message, message.thrown
break;
case IMessage.WARNING:
log.warn message.message, message.thrown
break;
case IMessage.INFO:
log.info message.message, message.thrown
break;
case IMessage.DEBUG:
log.debug message.message, message.thrown
break;
}
}
}
}
}
}

 * */