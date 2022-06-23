package com.sucroseluvv.wenshin.network.services

import com.sucroseluvv.wenshin.models.Master
import com.sucroseluvv.wenshin.models.MasterBusyness
import com.sucroseluvv.wenshin.models.Sketch
import com.sucroseluvv.wenshin.models.responses.MasterInfo
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MasterService {
    @GET("/masters")
    fun fetchMasters(
    ) : Single<Response<Array<Master>>>

    @GET("/masterBusyness/{id}")
    fun fetchMasterBusyness(
        @Path("id") masterId: Int
    ) : Single<Response<Array<MasterBusyness>>>

    @GET("/masterInfo/{id}")
    fun fetchMasterInfo(
        @Path("id") masterId: Int
    ) : Single<Response<MasterInfo>>

}