import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    jacoco
}

android {
    namespace = "com.example.addressmatcher"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.addressmatcher"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // Without minify is 16242 KB!
            isMinifyEnabled = true
            // With shrink is 1708 KB, without is 2052 KB.
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    testImplementation(libs.json)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}

fun fileTreeToList(fileT: FileTree): List<String> {
    return fileT.mapNotNull{it.absolutePath}.toList();
}

tasks.withType<Test> {
    testLogging {
//        events "passed", "skipped", "failed"
        events("passed", "skipped", "failed", "standardOut", "standardError")
        displayGranularity = 2
        exceptionFormat = TestExceptionFormat.FULL
    }
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.named("testDebugUnitTest"))
    description = "Calcualate coverage"
    sourceDirectories.from("${project.projectDir}/src/main/java")
    classDirectories.setFrom(files("${project.projectDir}/build/tmp/kotlin-classes/debug"))
}

tasks.register<JavaExec>("parseJacocoExec") {
    dependsOn(allprojects.map { it.tasks.named<JacocoReport>("jacocoTestReport") })//('')
    description = "Convert Jacoco binary report to HTML and CSV for easier reading."
    group = "Verification"
    classpath = files("jacococli.jar")
//    val argList = fileTreeToList(fileTree(mapOf("dir" to project.projectDir, "include" to listOf("build/jacoco/*.exec"))))
    val argList = "${project.projectDir}/build/jacoco/testDebugUnitTest.exec"
    // More specific "--classfiles" focus coverage on more testable sub-directories (packages):
    args("report", argList, "--classfiles", "build/tmp/kotlin-classes/debug", "--sourcefiles", "src/main/java", "--html", "build/report", "--csv", "build/report.csv")
    // Then archive and upload ${project.projectDir}\app\build\report or "${project.projectDir}/build/report"
    // java -jar app/jacococli.jar report app/build/jacoco/testDebugUnitTest.exec --classfiles app/build/intermediates/javac/debug --sourcefiles app/src/main/java --html app/build/report --csv app/build/report2.csv
    // java -jar app/jacococli.jar report app/build/jacoco/testDebugUnitTest.exec --classfiles app/build/tmp/kotlin-classes/debug --sourcefiles app/src/main/java --html app/build/report --csv app/build/report2.csv
    doFirst {
        // doFirst/doLast run in the "execution phase". We must assert only inside these blocks
        // or face failure during the "configuration phase". This would break `gradlew tasks`.
        assert(file("build/jacoco/testDebugUnitTest.exec").exists())
    }
}


