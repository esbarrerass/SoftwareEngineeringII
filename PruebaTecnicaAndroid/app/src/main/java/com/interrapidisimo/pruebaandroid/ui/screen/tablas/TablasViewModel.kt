package com.interrapidisimo.pruebaandroid.ui.screen.tablas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interrapidisimo.pruebaandroid.data.repository.DatosRepository
import com.interrapidisimo.pruebaandroid.domain.model.Tabla
import com.interrapidisimo.pruebaandroid.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TablasUiState(
    val isLoading: Boolean = false,
    val tablas: List<Tabla> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class TablasViewModel @Inject constructor(
    private val datosRepository: DatosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TablasUiState())
    val uiState: StateFlow<TablasUiState> = _uiState.asStateFlow()

    init {
        observeTablas()
        sincronizar()
    }

    private fun observeTablas() {
        datosRepository.getTablasLocal()
            .onEach { tablas ->
                _uiState.value = _uiState.value.copy(tablas = tablas)
            }
            .launchIn(viewModelScope)
    }

    private fun sincronizar() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = datosRepository.sincronizarTablas()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
                Resource.Loading -> Unit
            }
        }
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
