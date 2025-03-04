package com.murple.murfy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MurfyApplication

fun main(args: Array<String>) {
    runApplication<MurfyApplication>(*args)
}
