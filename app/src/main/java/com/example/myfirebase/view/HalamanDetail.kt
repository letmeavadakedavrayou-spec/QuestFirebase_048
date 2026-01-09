package com.example.myfirebase.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myfirebase.R
import com.example.myfirebase.view.route.DestinasiDetail
import com.example.myfirebase.view.viewmodel.DetailViewModel
import com.example.myfirebase.view.viewmodel.PenyediaViewModel
import com.example.myfirebase.view.viewmodel.StatusUiDetail

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSiswaScreen(
    navigateToEditItem: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    Scaffold(
        topBar = {
            SiswaTopAppBar(
                title = stringResource(DestinasiDetail.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToEditItem,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_siswa)
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        val statusUiDetail by viewModel.statusUiDetail.collectAsState()
        val coroutineScope = rememberCoroutineScope()

        BodyDetailDataSiswa(
            statusUiDetail = statusUiDetail,
            onDelete = {
                viewModel.hapusSiswa()
                navigateBack()
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
private fun BodyDetailDataSiswa(
    statusUiDetail: StatusUiDetail,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        var deleteConfirmationRequired by remember { mutableStateOf(false) }

        when (statusUiDetail) {
            is StatusUiDetail.Success -> DetailDataSiswa(
                siswa = statusUiDetail.siswa,
                modifier = Modifier.fillMaxWidth()
            )
            is StatusUiDetail.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is StatusUiDetail.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = statusUiDetail.message)
                }
            }
        }

        Button(
            onClick = { deleteConfirmationRequired = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.delete))
        }

        if (deleteConfirmationRequired) {
            AlertDialog(
                onDismissRequest = { deleteConfirmationRequired = false },
                title = { Text(stringResource(R.string.attention)) },
                text = { Text(stringResource(R.string.confirm_delete)) },
                confirmButton = {
                    TextButton(onClick = {
                        deleteConfirmationRequired = false
                        onDelete()
                    }) {
                        Text(stringResource(R.string.yes))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deleteConfirmationRequired = false }) {
                        Text(stringResource(R.string.no))
                    }
                }
            )
        }
    }
}

@Composable
fun DetailDataSiswa(
    siswa: com.example.myfirebase.modeldata.Siswa,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            BarisDetailData(
                label = stringResource(R.string.nama),
                value = siswa.nama
            )
            BarisDetailData(
                label = stringResource(R.string.alamat),
                value = siswa.alamat
            )
            BarisDetailData(
                label = stringResource(R.string.telpon),
                value = siswa.telpon
            )
        }
    }
}

@Composable
private fun BarisDetailData(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Text(text = label, modifier = Modifier.weight(1f))
        Text(text = value, fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
    }
}