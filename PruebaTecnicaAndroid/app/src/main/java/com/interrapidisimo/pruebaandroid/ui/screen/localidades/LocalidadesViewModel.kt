package com.interrapidisimo.pruebaandroid.ui.screen.localidades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interrapidisimo.pruebaandroid.data.repository.DatosRepository
import com.interrapidisimo.pruebaandroid.domain.model.Localidad
import com.interrapidisimo.pruebaandroid.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LocalidadesUiState(
    val isLoading: Boolean = false,
    val localidades: List<Localidad> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class LocalidadesViewModel @Inject constructor(
    private val datosRepository: DatosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LocalidadesUiState())
    val uiState: StateFlow<LocalidadesUiState> = _uiState.asStateFlow()

    init {
        cargarLocalidades()
    }

    private fun cargarLocalidades() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            when (val result = datosRepository.getLocalidades()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        localidades = result.data
                    )
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
