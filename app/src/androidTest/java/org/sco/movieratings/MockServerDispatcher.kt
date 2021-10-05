package org.sco.movieratings

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import okio.Buffer
import java.io.InputStreamReader

class MockServerDispatcher {

    internal inner class RequestDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            val path = request.path ?: return MockResponse().setResponseCode(400)
            return with(path) {
                when {
                    contains("movie/top_rated") -> MockResponse().setResponseCode(200).setBody(getJsonContent("toprated.json"))
                    contains("movie/popular") -> MockResponse().setResponseCode(200).setBody(getJsonContent("popular.json"))
                    contains("jpg") -> MockResponse().setResponseCode(200).setBody(getImageContent("sample.jpg")).addHeader("Content-Type", "image/jpeg")
                    else -> MockResponse().setResponseCode(400)
                }
            }
        }
    }

    internal inner class ErrorDispatcher : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            return MockResponse().setResponseCode(400)
        }
    }

    private fun getImageContent(fileName: String): Buffer {
        val buffer = Buffer()
        buffer.readFrom(this.javaClass.classLoader!!.getResourceAsStream(fileName))
        return buffer
    }

    private fun getJsonContent(fileName: String): String {
        return InputStreamReader(this.javaClass.classLoader!!.getResourceAsStream(fileName)).use { it.readText() }
    }
}