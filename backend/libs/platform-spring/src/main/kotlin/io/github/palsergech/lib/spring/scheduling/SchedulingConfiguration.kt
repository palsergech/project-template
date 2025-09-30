package io.github.palsergech.lib.spring.scheduling

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(ScheduleTasks::class)
class SchedulingConfiguration
