package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.util.DummyStories
import com.dicoding.picodiploma.loginwithanimation.util.MainDispatcherRule
import com.dicoding.picodiploma.loginwithanimation.util.getOrAwaitValue
import com.dicoding.picodiploma.loginwithanimation.view.adapter.StoriesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var userRepository: UserRepository

    @Mock
    lateinit var storyRepository: StoryRepository

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = MainViewModel(userRepository, storyRepository)
    }

    @Test
    fun `when getAllStories returns stories, it should return non-null data and correct size`() = runTest {
        // Membuat data dummy untuk PagingData
        val dummyStories = DummyStories.generateDummyStoryResponse()  // Sesuaikan dengan format ListStoryItem
        val pagingData = PagingData.from(dummyStories)
        val liveDataPaging = MutableLiveData<PagingData<ListStoryItem>>()
        liveDataPaging.value = pagingData

        val authToken = "dummy_token"
        Mockito.`when`(storyRepository.getAllStories(authToken)).thenReturn(liveDataPaging)

        // Ambil data PagingData dari ViewModel
        val actualPagingData = viewModel.getAllStories(authToken).getOrAwaitValue()

        // Verifikasi bahwa PagingData yang diterima bukan null dan data sesuai dengan yang diharapkan
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        // Submit data PagingData yang diterima ke AsyncPagingDataDiffer
        differ.submitData(actualPagingData)

        // Periksa bahwa snapshot PagingData sesuai dengan data dummy
        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertEquals(dummyStories[0], differ.snapshot()[0])
    }

    @Test
    fun `when getAllStories returns no stories, it should return empty list`() = runTest {
        // Membuat PagingData kosong
        val emptyPagingData = PagingData.from(emptyList<ListStoryItem>())
        val emptyLiveData = MutableLiveData<PagingData<ListStoryItem>>()
        emptyLiveData.value = emptyPagingData

        val authToken = "dummy_token"
        Mockito.`when`(storyRepository.getAllStories(authToken)).thenReturn(emptyLiveData)

        // Ambil data PagingData dari ViewModel (gunakan getOrAwaitValue)
        val actualPagingData = viewModel.getAllStories("dummy_token").getOrAwaitValue()

        // Verifikasi bahwa PagingData kosong
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        // Submit data PagingData kosong
        differ.submitData(actualPagingData)

        // Periksa bahwa snapshot PagingData kosong
        assertEquals(0, differ.snapshot().size)
    }

    val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}