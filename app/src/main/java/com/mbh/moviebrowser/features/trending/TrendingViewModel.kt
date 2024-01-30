package com.mbh.moviebrowser.features.trending

import androidx.lifecycle.ViewModel
import com.mbh.moviebrowser.api.TmdbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TrendingViewModel @Inject constructor(
    private val repository: TmdbRepository,
) : ViewModel() {

    private val _state = MutableStateFlow("")
    val state: StateFlow<String> = _state.asStateFlow()

}
