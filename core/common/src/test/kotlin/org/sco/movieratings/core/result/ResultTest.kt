package org.sco.movieratings.core.result

import app.cash.turbine.test
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class ResultTest {

    @Test
    fun `when error result catches`() = runTest {
        flow {
            emit(1)
            throw Exception("Test")
        }
            .asResult()
            .test {
                assertEquals(Result.Loading, awaitItem())
                assertEquals(Result.Success(1), awaitItem())

                when (val errorResult = awaitItem()) {
                    is Result.Error -> assertEquals(
                        "Test",
                        errorResult.exception.message
                    )
                    Result.Loading,
                    is Result.Success,
                    -> throw IllegalStateException(
                        "The flow should have been Error Result"
                    )
                }

                awaitComplete()
            }
    }
}