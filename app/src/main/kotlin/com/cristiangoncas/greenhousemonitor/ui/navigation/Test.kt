package com.cristiangoncas.greenhousemonitor.ui.navigation

val softwareEngineer = SoftwareEngineer()


data class SoftwareEngineer(
    val softSkills: SoftSkills =
        SoftSkills(
            problemSolving = true,
            adaptability = true,
            communicationSkills = true,
            attentionToDetail = true,
            leadership = true
        ),
    val technicalSkills: TechnicalSkills =
        TechnicalSkills(
            kotlin = true,
            jetpackCompose = true,
            sdkDevelopment = true,
            apiIntegration = true,
            testing = true,
            architecture = true,
        )
)


data class SoftSkills(
    val problemSolving: Boolean,
    val adaptability: Boolean,
    val communicationSkills: Boolean,
    val attentionToDetail: Boolean,
    val leadership: Boolean
)

data class TechnicalSkills(
    val kotlin: Boolean,
    val jetpackCompose: Boolean,
    val sdkDevelopment: Boolean,
    val apiIntegration: Boolean,
    val testing: Boolean,
    val architecture: Boolean
)


















