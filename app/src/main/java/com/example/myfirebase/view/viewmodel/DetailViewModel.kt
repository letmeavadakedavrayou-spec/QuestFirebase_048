package com.example.myfirebase.view.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirebase.modeldata.Siswa
import com.example.myfirebase.modeldata.UIStateSiswa
import com.example.myfirebase.repositori.RepositorySiswa
import com.example.myfirebase.view.route.DestinasiDetail
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class StatusUiDetail {
    object Loading : StatusUiDetail()
    data class Success(val siswa: Siswa) : StatusUiDetail()
    data class Error(val message: String) : StatusUiDetail()
}

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositorySiswa: RepositorySiswa
) : ViewModel() {
    private val _statusUiDetail = MutableStateFlow<StatusUiDetail>(StatusUiDetail.Loading)
    val statusUiDetail: StateFlow<StatusUiDetail> = _statusUiDetail.asStateFlow()

    private val itemId: String = savedStateHandle.get<String>(DestinasiDetail.itemIdArg)
        ?: error("ID siswa tidak ditemukan")

    init {
        loadSiswaDetail()
    }

    private fun loadSiswaDetail() {
        viewModelScope.launch {
            _statusUiDetail.value = StatusUiDetail.Loading
            try {
                val siswa = repositorySiswa.getSatuSiswa(itemId.toLong())
                if (siswa != null) {
                    _statusUiDetail.value = StatusUiDetail.Success(siswa)
                } else {
                    _statusUiDetail.value = StatusUiDetail.Error("Data tidak ditemukan")
                }
            } catch (e: Exception) {
                _statusUiDetail.value = StatusUiDetail.Error(e.message ?: "Terjadi kesalahan")
            }
        }
    }

    fun hapusSiswa() {
        viewModelScope.launch {
            try {
                repositorySiswa.hapusSatuSiswa(itemId.toLong())
            } catch (e: Exception) {
                // Handle error
            }
        }
    }
}