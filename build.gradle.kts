import org.jetbrains.kotlin.gradle.dsl.JvmTarget

buildscript {
  repositories {
    mavenCentral()
    gradlePluginPortal()
  }
}

val javaVersion = 21

val dependencyVersions = listOf(
    libs.annotations,
    libs.checkerEqual,
    libs.classgraph,
    libs.commonsCodec,
    libs.commonsCompress,
    libs.commonsIo,
    libs.commonsLang,
    libs.errorProneAnnotations,
    libs.gson,
    libs.guava,
    libs.http5library,
    libs.httpcore,
    libs.kotlinLoggingOshai,
    libs.kotlinxCorountineBom,
    libs.kotlinxCorountineCore,
    libs.kotlinxCorountineCoreJvm,
    libs.kotlinxCorountineDebug,
    libs.kotlinxCorountineJdk8,
    libs.kotlinxCorountineSlf4j,
    libs.kotlinxCorountineTest,
    libs.kotlinxCorountineTestJvm,
    libs.minidev,
    libs.protobufJava,
    libs.snakeyaml,
    libs.tikaCore,
    libs.reactivestreams
)

val dependencyGroupVersions = mapOf(
    "ch.qos.logback" to libs.logbackClassic.get().version,
    "com.fasterxml.jackson.core" to libs.versions.jackson.get(),
    "com.fasterxml.jackson.dataformat" to libs.versions.jackson.get(),
    "com.fasterxml.jackson.datatype" to libs.versions.jackson.get(),
    "com.fasterxml.jackson.module" to libs.versions.jackson.get(),
    "io.kotest" to libs.versions.kotest.get(),
    "io.ktor" to libs.ktor.get().version,
    "io.micrometer" to libs.micrometer.get().version,
    "io.mockk" to libs.mockk.get().version,
    "io.netty" to libs.netty.get().version,
    "net.bytebuddy" to libs.byteBuddy.get().version,
    "org.apache.groovy" to libs.groovy.get().version,
    "org.jetbrains.kotlin" to libs.versions.kotlin.get(),
    "org.junit.jupiter" to libs.versions.junitJupiter.get(),
    "org.junit.platform" to libs.junitPlatform.get().version,
    "org.slf4j" to libs.versions.slf4j.get(),
    "org.springframework" to libs.versions.spring.get(),
    "org.springframework.boot" to libs.versions.springBoot.get(),
)

plugins {
  id("maven-publish")
  alias(libs.plugins.kotlin)
  alias(libs.plugins.kotlinSpring)
  alias(libs.plugins.kotlinAllopen)
  alias(libs.plugins.springBoot) apply false
}

allprojects {
  group = "de.orga"
}

repositories {
  mavenCentral()
}

subprojects {
  project.apply(plugin = "maven-publish")
  project.apply(plugin = "org.jetbrains.kotlin.jvm")
  project.apply(plugin = "org.jetbrains.kotlin.plugin.spring")
  project.apply(plugin = "org.jetbrains.kotlin.plugin.allopen")

  repositories {
    mavenCentral()
  }

  configurations {
    all {
      exclude(module = "log4j")
      exclude(module = "servlet-api")
      resolutionStrategy {
        failOnVersionConflict()
        force(dependencyVersions)
        eachDependency {
          val forcedVersion = dependencyGroupVersions[requested.group]
          if (forcedVersion != null) {
            useVersion(forcedVersion)
          }
        }
        cacheDynamicVersionsFor(Integer.parseInt(System.getenv("GRADLE_CACHE_DYNAMIC_SECONDS") ?: "0"), "seconds")
      }
    }
  }

  configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
    targetCompatibility = JavaVersion.toVersion(javaVersion)
  }

  tasks {
    withType<Test> {
      useJUnitPlatform()
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
      compilerOptions {
        jvmTarget.set(JvmTarget.valueOf("JVM_$javaVersion"))
        freeCompilerArgs.set(listOf("-Xjsr305=strict"))
      }
    }
  }
}
