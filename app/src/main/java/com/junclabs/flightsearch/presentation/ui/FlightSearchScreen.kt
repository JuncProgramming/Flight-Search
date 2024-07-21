package com.junclabs.flightsearch.presentation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.junclabs.flightsearch.R
import com.junclabs.flightsearch.domain.AirportItem
import com.junclabs.flightsearch.domain.FavoriteItem

@Composable
fun FlightSearchResultCard(
    favoriteItem: FavoriteItem,
    onToggleFavorite: (Boolean) -> Unit,
    uiState: UiState,
) {
    val isFavorite = uiState.isFavorite
    Card {
        Row {
            Column {
                Text(text = stringResource(R.string.depart))
                Text(text = favoriteItem.departureCode)
                Text(text = stringResource(R.string.arrive))
                Text(text = favoriteItem.departureCode)
            }
            IconButton(onClick = { onToggleFavorite(isFavorite) }) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun FlightAutoCompleteSearchCard(airport: AirportItem) {
    Card {
        Row {
            Text(text = airport.iataCode)
            Text(text = airport.name)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchScreen(airports: List<AirportItem>, favorites: List<FavoriteItem>, viewModel: FlightSearchScreenViewModel = viewModel(factory = FlightSearchScreenViewModel.Factory)) {
    val focusManager = LocalFocusManager.current
    var query by rememberSaveable { mutableStateOf("") }
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.app_name)) },
        )
    }) { padding ->
        OutlinedTextField(value = query,
            onValueChange = { query = it },
            singleLine = true,
            placeholder = {
                Text(text = stringResource(R.string.search))
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(onSearch = {
                focusManager.clearFocus()
                viewModel.onEvent(FlightsEvent.OnFlightSearch(query))
            }),
            leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                Icon(imageVector = Icons.Filled.Clear,
                    contentDescription = null,
                    Modifier.clickable { query = "" })
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp, top = 8.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = airports) { airport ->
                FlightAutoCompleteSearchCard(airport = airport)
            }
        }
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(padding),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = favorites) { favoriteItem ->
                FlightSearchResultCard(
                    favoriteItem = favoriteItem, onToggleFavorite = viewModel::toggleFavorite, uiState = viewModel.uiState.collectAsState().value
                )
            }
        }
    }
}