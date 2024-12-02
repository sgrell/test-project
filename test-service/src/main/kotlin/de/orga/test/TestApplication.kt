package de.orga.test

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = [
  "de.orga"
])
class TestApplication

fun main(args: Array<String>) {
  runApplication<TestApplication>(*args)
}
