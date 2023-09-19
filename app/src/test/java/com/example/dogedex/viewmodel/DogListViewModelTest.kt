package com.example.dogedex.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.dogedex.data.model.response.DogAllResponse
import com.example.dogedex.data.model.response.DogListResponse
import com.example.dogedex.data.repository.DogRepository
import com.example.dogedex.data.repository.mocks.MockRepository
import com.example.dogedex.domain.model.DogModel
import com.example.dogedex.domain.usecase.DogUseCase
import com.example.dogedex.ui.dog.viewmodel.DogListViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class DogListViewModelTest {



    @RelaxedMockK
    private lateinit var dogUseCase: DogUseCase

    lateinit var  dogListViewModel:DogListViewModel

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        dogListViewModel = DogListViewModel( dogUseCase )
    }

    @Test
    fun `When call DogCollection then return sucessfull response `() = runBlocking{

        //Given
        coEvery { dogListViewModel.getdogColection() } returns

        //When
        dogListViewModel.getdogColection()

        //Then
        coVerify(exactly = 1) { dogListViewModel.getdogColection()}

    }


}


