package io.github.palsergech.userprofile

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@Configuration
@Import(

)
@EnableJdbcRepositories("io.github.palsergech.userprofile.impl.persistence.jdbc")
class Module1Configuration
