package com.murple.murfy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class MurfyApplication

fun main(args: Array<String>) {
    runApplication<MurfyApplication>(*args)
}
