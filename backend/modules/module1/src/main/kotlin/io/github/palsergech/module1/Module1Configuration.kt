package io.github.palsergech.module1

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@Configuration
@Import(

)
@EnableJdbcRepositories("io.github.palsergech.module1.impl.persistence.jdbc")
class Module1Configuration
