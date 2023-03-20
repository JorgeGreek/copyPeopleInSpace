package com.surrus.peopleinspace

import com.apollographql.apollo3.ApolloClient
import com.surrus.common.di.commonModule
import com.surrus.common.repository.PeopleInSpaceRepositoryInterface
import com.surrus.common.repository.platformModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class PeopleInSpaceTest: KoinTest {
    private val repo : PeopleInSpaceRepositoryInterface by inject()

    @BeforeTest
    fun setUp()  {
        Dispatchers.setMain(StandardTestDispatcher())

        startKoin{
            modules(
                commonModule(true),
                platformModule(),
                module {
                    single { createMockApolloClient("https://peopleinspace-graphql-guhrsfr7ka-uc.a.run.app/graphql") }
                }
            )
        }
    }

    @Test
    fun testGetPeople() = runTest {
        val result = repo.fetchPeople()
        println(result)
        assertTrue(result.isNotEmpty())
    }

    private fun createMockApolloClient(url: String): ApolloClient {
        println("createMockApolloClient, ur = $url")
        return ApolloClient.Builder()
            .serverUrl(url)
            .build()
    }
}
