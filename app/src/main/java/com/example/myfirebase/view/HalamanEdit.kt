package com.example.myfirebase.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myfirebase.R
import com.example.myfirebase.view.route.DestinasiEdit
import com.example.myfirebase.view.viewmodel.EditViewModel
import com.example.myfirebase.view.viewmodel.PenyediaViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditSiswaScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState by viewModel.uiStateSiswa.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SiswaTopAppBar(
                title = stringResource(DestinasiEdit.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack,
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            OutlinedTextField(
                value = uiState.detailSiswa.nama,
                onValueChange = { newNama ->
                    val updatedDetail = uiState.detailSiswa.copy(nama = newNama)
                    viewModel.updateUiState(updatedDetail)
                },
                label = { Text(stringResource(R.string.nama)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
                singleLine = true
            )
            OutlinedTextField(
                value = uiState.detailSiswa.alamat,
                onValueChange = { newAlamat ->
                    val updatedDetail = uiState.detailSiswa.copy(alamat = newAlamat)
                    viewModel.updateUiState(updatedDetail)
                },
                label = { Text(stringResource(R.string.alamat)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
                singleLine = true
            )
            OutlinedTextField(
                value = uiState.detailSiswa.telpon,
                onValueChange = { newTelpon ->
                    val updatedDetail = uiState.detailSiswa.copy(telpon = newTelpon)
                    viewModel.updateUiState(updatedDetail)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                label = { Text(stringResource(R.string.telpon)) },
                modifier = Modifier.fillMaxWidth(),
                enabled = true,
                singleLine = true
            )

            Text(
                text = stringResource(R.string.required_field),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )

            Divider(
                thickness = dimensionResource(R.dimen.padding_small),
                modifier = Modifier.padding(bottom = dimensionResource(R.dimen.padding_medium))
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.updateSiswa()
                        navigateBack()
                    }
                },
                enabled = uiState.isEntryValid,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.btn_submit))
            }
        }
    }
}