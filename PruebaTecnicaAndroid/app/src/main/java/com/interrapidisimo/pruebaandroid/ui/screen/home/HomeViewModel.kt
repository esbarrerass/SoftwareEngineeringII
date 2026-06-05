package com.interrapidisimo.pruebaandroid.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.interrapidisimo.pruebaandroid.data.repository.SeguridadRepository
import com.interrapidisimo.pruebaandroid.domain.model.Usuario
import com.interrapidisimo.pruebaandroid.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val usuario: Usuario? = null,
    val versionMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val seguridadRepository: SeguridadRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        seguridadRepository.getUsuarioLocal()
            .onEach { usuario -> _uiState.value = _uiState.value.copy(usuario = usuario) }
            .launchIn(viewModelScope)

        checkVersion()
    }

    private fun checkVersion() {
        viewModelScope.launch {
            when (val result = seguridadRepository.checkVersion()) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(versionMessage = result.data)
                }
                else -> Unit
            }
        }
    }
}
