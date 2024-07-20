package com.group76.doctor.configuration

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "system")
class SystemProperties {
    var idService: String = ""
    val collection: CollectionConfiguration = CollectionConfiguration()

    class CollectionConfiguration{
        var doctor: String = ""
        var medicalSpecialties: String = ""
    }
}

