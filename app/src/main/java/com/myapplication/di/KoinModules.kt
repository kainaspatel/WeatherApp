package com.myapplication.di



import com.myapplication.adapter.CityAdapter
import com.myapplication.repository.CityRepository
import com.myapplication.viewmodel.CityViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val dbModule = module {

}

val repositoryModule = module {
     single { CityRepository() }

}

val uiModule = module {
    factory { CityAdapter() }

    viewModel { CityViewModel(get()) }

}


