package com.sergey.zhuravlev.mobile.social.ui.search

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.sergey.zhuravlev.mobile.social.domain.enums.RelationshipStatus
import com.sergey.zhuravlev.mobile.social.domain.model.ErrorModel
import com.sergey.zhuravlev.mobile.social.domain.repository.SearchRepository
import com.sergey.zhuravlev.tuples.Tuple7
import com.sergey.zhuravlev.tuples.Tuples

class SearchProfileViewModel(
  private val searchRepository: SearchRepository
) : ViewModel() {

  private val _error = MutableLiveData<ErrorModel>()

  val error: LiveData<ErrorModel> = _error

  // Search profile filters:

  private val _query = MutableLiveData<String>()

  val query: LiveData<String> = _query

  fun setQuery(query: String?) {
    query?.let { _query.postValue(it) }
  }

  private val _country = MutableLiveData<String>()

  val country: LiveData<String> = _country

  fun setCountry(country: String?) {
    country?.let { _country.postValue(it) }
  }

  private val _city = MutableLiveData<String>()

  val city: LiveData<String> = _city

  fun setCity(city: String?) {
    city?.let { _city.postValue(it) }
  }

  private val _relationshipStatus = MutableLiveData<RelationshipStatus>()

  val relationshipStatus: LiveData<RelationshipStatus> = _relationshipStatus

  fun setRelationshipStatus(relationshipStatus: RelationshipStatus?) {
    relationshipStatus?.let { _relationshipStatus.postValue(it) }
  }

  private val _age = MutableLiveData<Int>()

  val age: LiveData<Int> = _age

  fun setAge(age: Int?) {
    age?.let { _age.postValue(it) }
  }

  private val _ageFrom = MutableLiveData<Int>()

  val ageFrom: LiveData<Int> = _ageFrom

  fun setAgeFrom(ageFrom: Int?) {
    ageFrom?.let { _ageFrom.postValue(it) }
  }

  private val _ageTo = MutableLiveData<Int>()

  val ageTo: LiveData<Int> = _ageTo

  fun setAgeTo(ageTo: Int?) {
    ageTo?.let { _ageTo.postValue(it) }
  }

  private val combinedValues =
    MediatorLiveData<Tuple7<String?, String?, String?, RelationshipStatus?, Int?, Int?, Int?>>()
      .apply {
        addSource(_query) { query ->
          value = value?.mapT1 { query }
            ?: Tuples.of(
              query,
              _country.value,
              _city.value,
              _relationshipStatus.value,
              _age.value,
              _ageFrom.value,
              _ageTo.value
            )
        }
        addSource(_country) { country ->
          value = value?.mapT2 { country }
            ?: Tuples.of(
              _query.value,
              country,
              _city.value,
              _relationshipStatus.value,
              _age.value,
              _ageFrom.value,
              _ageTo.value
            )
        }
        addSource(_city) { city ->
          value = value?.mapT3 { city }
            ?: Tuples.of(
              _query.value,
              _country.value,
              city,
              _relationshipStatus.value,
              _age.value,
              _ageFrom.value,
              _ageTo.value
            )
        }
        addSource(_relationshipStatus) { relationshipStatus ->
          value = value?.mapT4 { relationshipStatus }
            ?: Tuples.of(
              _query.value,
              _country.value,
              _city.value,
              relationshipStatus,
              _age.value,
              _ageFrom.value,
              _ageTo.value
            )
        }
        addSource(_age) { age ->
          value = value?.mapT5 { age }
            ?: Tuples.of(
              _query.value,
              _country.value,
              _city.value,
              _relationshipStatus.value,
              age,
              _ageFrom.value,
              _ageTo.value
            )
        }
        addSource(_ageFrom) { ageFrom ->
          value = value?.mapT6 { ageFrom }
            ?: Tuples.of(
              _query.value,
              _country.value,
              _city.value,
              _relationshipStatus.value,
              _age.value,
              ageFrom,
              _ageTo.value
            )
        }
        addSource(_ageTo) { ageTo ->
          value = value?.mapT7 { ageTo }
            ?: Tuples.of(
              _query.value,
              _country.value,
              _city.value,
              _relationshipStatus.value,
              _age.value,
              _ageFrom.value,
              ageTo
            )
        }
      }

  val profiles = Transformations.switchMap(combinedValues) { tuple ->
    searchRepository.searchProfiles(
      tuple.t1,
      tuple.t2,
      tuple.t3,
      tuple.t4,
      tuple.t5,
      tuple.t6,
      tuple.t7,
      3,
      false
    )
      .cachedIn(viewModelScope)
  }
}