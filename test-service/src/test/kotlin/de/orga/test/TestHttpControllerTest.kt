package de.orga.test

import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@SpringBootTest(classes = [TestApplication::class])
class TestHttpControllerTest(
    @Autowired private val webApplicationContext: WebApplicationContext,
    @MockkBean(relaxed = true) private val testService: TestService
) : DescribeSpec({

  lateinit var mockMvc: MockMvc

  beforeTest {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
        .build()
  }

  describe("/test") {

    it("should delegate request body to service") {
      val request = """{"some": "json"}"""
      mockMvc.post("/test") {
        contentType = MediaType.APPLICATION_JSON
        content = request
      }.andExpect { status { isOk() } }

      verify { testService.doNothing() }
    }

  }
})
