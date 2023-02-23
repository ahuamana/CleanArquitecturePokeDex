package com.paparazziteam.cleanarquitecturepokemon.feature.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.paparazziteam.cleanarquitecturepokemon.domain.Region
import com.paparazziteam.cleanarquitecturepokemon.domain.RegionResponse
import com.paparazziteam.cleanarquitecturepokemon.usecases.GetRegionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import pe.com.tarjetaw.android.client.shared.network.Event
import pe.com.tarjetaw.android.client.shared.network.Resource
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getRegionsUseCase: GetRegionsUseCase
) :ViewModel() {

    private val _eventsRegions = MutableLiveData<Event<RegionsState>>()
    val eventsRegions:LiveData<Event<RegionsState>> get() = _eventsRegions

    private val regions = mutableListOf<Region>()
    private var currectRegionSelected:String = ""

    init {
        getRegions()
    }

    private fun getRegions() = getRegionsUseCase().observeForever {
        when(it.status){
            Resource.Status.SUCCESS -> it.run{
                data?.let { response->
                    handleSuccess(response)
                }
            }
            Resource.Status.ERROR -> it.run{
                message?.let { message->
                    _eventsRegions.value = Event(RegionsState.HideLoading)
                    _eventsRegions.value = Event(RegionsState.Error(message))
                }
            }
            Resource.Status.LOADING -> {
                _eventsRegions.value = Event(RegionsState.ShowLoading)
            }
        }
    }

    private fun handleSuccess(response: RegionResponse) {
        regions.addAll(response.results)
        selectFirstRegion()
        _eventsRegions.value = Event(RegionsState.HideLoading)
        _eventsRegions.value = Event(RegionsState.Success(regions))
    }

    private fun selectFirstRegion() {
        regions.first().isSelected = true
        currectRegionSelected = regions.first().name
    }

    fun updateRegionSelected(region: Region) {
        regions.forEach {
            it.isSelected = it.name == region.name
        }
        _eventsRegions.value = Event(RegionsState.Success(regions))
    }

    sealed class RegionsState {
        object HideLoading : RegionsState()
        object ShowLoading : RegionsState()
        data class Success(val regions: List<Region>) : RegionsState()
        data class Error(val message: String) : RegionsState()
    }

}