package com.sucroseluvv.wenshin.models.requests

import com.sucroseluvv.wenshin.models.Session

data class ExtraSessionsRequest(val orderId: Int, val sessions: Array<SessionRequest>)
