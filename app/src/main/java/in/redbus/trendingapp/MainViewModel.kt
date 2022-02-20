package `in`.redbus.trendingapp


import `in`.redbus.trendingapp.model.SampleModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

class MainViewModel(private val networkRepo: Repository) : ViewModel() {


    var job: Job? = null
    val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onError("Exception handled: ${throwable.localizedMessage}")
    }


    private val listDetailsState: MutableLiveData<DataInfoState> = MutableLiveData()
    val listDetails: LiveData<DataInfoState> = listDetailsState

    init {
        listDetailsState.value = DataInfoState()
    }

    fun getAllList() {
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = networkRepo.getAllList()
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    listDetailsState.postValue(listDetailsState.value?.copy(data = response.body(), isLoading = false, isError = false, errorMessage = ""))
                } else {
                    listDetailsState.postValue(
                        listDetailsState.value?.copy(data = null, isLoading = false,
                            isError = true, errorMessage = response.message()
                        )
                    )
                }
            }
        }

    }

    private fun onError(message: String) {
        listDetailsState.postValue(
            listDetailsState.value?.copy(data = null, isLoading = false,
                isError = true, errorMessage = message
            )
        )
    }
    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }

    data class DataInfoState(var data: SampleModel? = null, var isLoading: Boolean = false, var isError: Boolean = false, var errorMessage: String = "")

}