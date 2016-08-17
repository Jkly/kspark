project.group = "kspark"
project.version ="v1.0.0"

buildscript {
    repositories {
        mavenCentral()
        gradleScriptKotlin()
    }
    dependencies {
        classpath(kotlinModule("gradle-plugin"))
    }
}

apply {
    plugin("java")
    plugin("kotlin")
    plugin("maven")
}

repositories {
    mavenCentral()
    gradleScriptKotlin()
}

dependencies {
    compile(kotlinModule("stdlib"))
    compile("com.sparkjava:spark-core:2.5")
    compile("com.google.code.gson:gson:2.7")

    testCompile("org.slf4j:slf4j-api:1.7.7")
    testCompile(log4jModule("log4j-api"))
    testCompile(log4jModule("log4j-core"))
    testCompile(log4jModule("log4j-slf4j-impl"))

    testCompile("com.squareup.okhttp3:okhttp:3.4.1")
    testCompile("io.kotlintest:kotlintest:1.3.0")
}

fun log4jModule(module: String): String = "org.jetbrains.kotlin:kotlin-$module:2.6.2"