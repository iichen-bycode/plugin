package com.gmspace.shubao;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.Delete;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class ShubaoPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.getGradle().settingsEvaluated(settings -> {
            settings.getPluginManagement().repositories(repositories -> {
                repositories.maven(repo -> {
                    repo.setUrl("https://maven.vmos.pro");
                    repo.credentials(credentials -> {
                        credentials.setUsername("hangzhoushubao");
                        credentials.setPassword("j1p4sm5A");
                    });
                });
            });
        });

        project.getGradle().afterProject(p -> {
            p.getPlugins().apply("vmos-build");

            // 创建任务来生成授权文件
            p.getTasks().create("generateAuthFile", task -> {
                task.doLast(t -> {
                    try {
                        File authFile = new File(p.getRootDir(), "auth.txt");
                        FileWriter writer = new FileWriter(authFile);
                        writer.write("# vmos license file\n");
                        writer.write("expiryDate=2024-7-20\n");
                        writer.write("productId=vmos_lite_sdk\n");
                        writer.write("package=com.gmspace.app\n");
                        writer.write("licensee=杭州数爆科技有限公司\n");
                        writer.write("customerID=VMSDK10119\n");
                        writer.write("contact=倪总\n");
                        writer.write("contactEmail=hi@xinstall.com\n");
                        writer.write("publicKey=MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAJtUEGPgkmNaii5HUrsQWnxH1kThuUw0CPhLbasH+CzNyn6zuzK3Y49hOtnn9Q2zmh/ylpuM9k4DKjSbYVBTTo8CAwEAAQ==\n");
                        writer.write("licenseKey=54776165426f742f67584e734a7445367264654866387574662b457556534e75446974506c65454767532f4867335461726b546634797862706b546c657336427a61716a384d6b434e4e37494d4331766d72517268673d3d\n");
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to create auth file", e);
                    }
                });
            });

            // 创建任务来删除授权文件
            p.getTasks().create("deleteAuthFile", Delete.class, task -> {
                task.delete(new File(p.getRootDir(), "auth.txt"));
            });

            // 配置任务依赖，确保在构建之前生成授权文件，在构建完成后删除授权文件
            p.getTasks().getByName("preBuild").dependsOn("generateAuthFile");
            p.getTasks().getByName("clean").dependsOn("deleteAuthFile");
        });
    }
}
