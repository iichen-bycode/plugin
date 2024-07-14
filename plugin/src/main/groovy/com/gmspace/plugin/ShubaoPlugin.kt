package com.gmspace.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project


class ShubaoPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.afterEvaluate {
            val plugins = project.pluginManager
            if (!plugins.hasPlugin("com.android.application")) {
                throw IllegalArgumentException("Android Application plugin required");
            }
            plugins.apply("vmos-build")
        }
    }
}
