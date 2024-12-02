package de.orga.test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod.GET
import org.springframework.web.bind.annotation.RequestMethod.POST
import org.springframework.web.bind.annotation.RestController

@RestController
class TestHttpController @Autowired constructor(private val testService: TestService) {

  @RequestMapping(value = ["/test"], method = [GET], produces = [APPLICATION_JSON_VALUE])
  fun fooGet(): Map<String, String> {
    return mapOf("foo" to "bar")
  }

  @RequestMapping(value = ["/test"], method = [POST], produces = [APPLICATION_JSON_VALUE])
  fun fooPost(@RequestBody request: Map<String, String>): Map<String, String> {
    testService.doNothing()
    println("request: $request")
    return mapOf("foo" to "bar")
  }
}
