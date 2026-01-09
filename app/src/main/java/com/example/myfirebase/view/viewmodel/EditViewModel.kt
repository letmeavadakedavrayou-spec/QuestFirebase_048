package com.example.myfirebase.view.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirebase.modeldata.DetailSiswa
import com.example.myfirebase.modeldata.UIStateSiswa
import com.example.myfirebase.modeldata.toDataSiswa
import com.example.myfirebase.modeldata.toUIStateSiswa
import com.example.myfirebase.repositori.RepositorySiswa
import com.example.myfirebase.view.route.DestinasiEdit
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositorySiswa: RepositorySiswa
) : ViewModel() {
    private val _uiStateSiswa = MutableStateFlow(UIStateSiswa())
    val uiStateSiswa: StateFlow<UIStateSiswa> = _uiStateSiswa.asStateFlow()

    private val itemId: String = savedStateHandle.get<String>(DestinasiEdit.itemIdArg)
        ?: error("ID siswa tidak ditemukan")

    init {
        loadSiswaData()
    }

    private fun loadSiswaData() {
        viewModelScope.launch {
            val siswa = repositorySiswa.getSatuSiswa(itemId.toLong())
            siswa?.let {
                _uiStateSiswa.value = it.toUIStateSiswa(isEntryValid = true)
            }
        }
    }

    fun updateUiState(detailSiswa: DetailSiswa) {
        _uiStateSiswa.value = UIStateSiswa(
            detailSiswa = detailSiswa,
            isEntryValid = validasiInput(detailSiswa)
        )
    }

    private fun validasiInput(detailSiswa: DetailSiswa): Boolean {
        return with(detailSiswa) {
            nama.isNotBlank() && alamat.isNotBlank() && telpon.isNotBlank()
        }
    }

    suspend fun updateSiswa() {
        if (validasiInput(uiStateSiswa.value.detailSiswa)) {
            val siswa = uiStateSiswa.value.detailSiswa.toDataSiswa().copy(id = itemId.toLong())
            repositorySiswa.editSatuSiswa(siswa)
        }
    }
}