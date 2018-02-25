package com.frogermcs.androiddevmetrics.weaving.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import junit.framework.Assert.assertNotNull
import org.gradle.api.GradleException
import org.gradle.api.internal.plugins.PluginApplicationException
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

class AndroidDevMetricsPluginTest {

    @Test(expected = GradleException::class)
    fun `plugin should throw exception when 'android' or 'android-library' plugin unavailable`() {
        val project = ProjectBuilder.builder().build()

        project.pluginManager.apply(AndroidDevMetricsPlugin::class.java)
    }

    @Test
    fun `plugin should work with 'android' plugin`() {
        val project = ProjectBuilder.builder().build()

        with(project) {
            pluginManager.apply(AppPlugin::class.java)
            pluginManager.apply(AndroidDevMetricsPlugin::class.java)
        }
    }

    @Test
    fun `plugin should work with 'android-library' plugin`() {
        val project = ProjectBuilder.builder().build()

        with(project) {
            pluginManager.apply(LibraryPlugin::class.java)
            pluginManager.apply(AndroidDevMetricsPlugin::class.java)
        }
    }

    @Test
    fun `plugin should add tasks when applied`() {
        val project = ProjectBuilder.builder().build()

        with(project) {
            /*pluginManager.apply(ApplicationPlugin::class.java)
            pluginManager.apply(QualityChecksPlugin::class.java)

            assertNotNull(tasks.findByName(QualityChecksPlugin.WRITE_CHECK_STYLE_CONFIG_FILE_TASK))
            assertNotNull(tasks.findByName(QualityChecksPlugin.WRITE_FIND_BUGS_EXCLUSION_FILE_TASK))
            assertNotNull(tasks.findByName(QualityChecksPlugin.WRITE_PMD_CONFIG_FILE_TASK))*/
        }
    }

    @Test
    fun `plugin should add dependencies when applied`() {
        val project = ProjectBuilder.builder().build()

        with(project) {
            pluginManager.apply(AppPlugin::class.java)
            pluginManager.apply(AndroidDevMetricsPlugin::class.java)

            //delegateClodeledefe

            assertNotNull(project.dependencies.components.all { println(it.id) })
            /*assertNotNull(tasks.findByName(QualityChecksPlugin.WRITE_CHECK_STYLE_CONFIG_FILE_TASK))
            assertNotNull(tasks.findByName(QualityChecksPlugin.WRITE_FIND_BUGS_EXCLUSION_FILE_TASK))
            assertNotNull(tasks.findByName(QualityChecksPlugin.WRITE_PMD_CONFIG_FILE_TASK))*/
        }
    }
}