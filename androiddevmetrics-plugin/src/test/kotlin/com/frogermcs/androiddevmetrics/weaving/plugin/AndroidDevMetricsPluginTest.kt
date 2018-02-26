package com.frogermcs.androiddevmetrics.weaving.plugin

import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import junit.framework.Assert.assertNotNull
import org.gradle.api.GradleException
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
    fun `plugin should add dependencies when applied`() {
        val project = ProjectBuilder.builder().build()

        with(project) {
            pluginManager.apply(AppPlugin::class.java)
            pluginManager.apply(AndroidDevMetricsPlugin::class.java)

            assertNotNull(project.dependencies.components.all { println(it.id) })

            //TODO
        }
    }
}