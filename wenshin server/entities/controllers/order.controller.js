const order = require('../models/order.model')

exports.createOrder = (req, res) => {
    try {
        order.createOrder(req.body, req.user)
            .then((orderId)=>{
                order.createSessions(orderId, req.body.sessions, true)
                    .then((sessions)=>{
                        res.status(201).send({id: orderId})
                    })
                    .catch((reason)=>res.status(500).send({error: reason, place: "createSessions"}))
            })
            .catch((reason)=>res.status(500).send({error: reason, place: "createOrder"}))
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}

exports.createSessions = (req, res) => {
    try {
        order.createSessions(req.body.orderId, req.body.sessions)
            .then((sessions)=>{
                res.status(201).send({id: req.body.orderId})
            })
            .catch((reason)=>res.status(500).send({error: reason, place: "createSessions"}))
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}



exports.createConsultation = (req, res) => {
    try {
        order.createConsultation(req.user)
            .then((orderId)=>{
                order.createSession(orderId, req.body)
                    .then((session)=>{
                        res.status(201).send({id: orderId})
                    })
                    .catch((reason)=>res.status(500).send({error: reason, place: "createSessions"}))
            })
            .catch((reason)=>res.status(500).send({error: reason, place: "createOrder"}))
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}

exports.orders = (req, res) => {
    try {
        if(req.user.role === 'master') {
            order.masterOrders(req.user.id)
                .then((rows)=>{
                    res.status(200).send(rows)
                })
                .catch((reason)=>res.status(500).send({error: reason}))
        }
        else {
            order.userOrdersList(req.user.id)
                .then((orders)=>{
                    order.userSessionsList(req.user.id)
                        .then((sessions)=>{
                            let result = orders.map((val)=>({...val, sessions: sessions.filter((v)=>v.order_id===val.id)}))
                            res.status(200).send(result)
                        })
                        .catch((reason)=>res.status(500).send({error: reason}))
                    //res.status(200).send(rows)
                })
                .catch((reason)=>res.status(500).send({error: reason}))
        }
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}

exports.orderInfo = (req, res) => {
    try {
        if(req.user.role === 'user') {
            order.userOrderInfo(req.params.id)
                .then((o)=>{
                    order.sessionsList()
                        .then((sessions)=>{
                            let orderSessions = { ...o, sessions: sessions.filter((v)=>v.order_id===o.id)}
                            res.status(200).send(orderSessions)
                        })
                        .catch((reason)=>res.status(500).send({error: reason}))
                })
                .catch((reason)=>res.status(500).send({error: reason}))
        } else {
            order.masterOrderInfo(req.params.id)
                .then((o)=>{
                    order.sessionsList()
                        .then((sessions)=>{
                            let orderSessions = { ...o, sessions: sessions.filter((v)=>v.order_id===o.id)}
                            res.status(200).send(orderSessions)
                        })
                        .catch((reason)=>res.status(500).send({error: reason}))
                })
                .catch((reason)=>res.status(500).send({error: reason}))
        }
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}

exports.orderHistory = (req, res) => {
    try {
        if(req.user.role === 'user') {
            order.orderHistoryUser(req.user.id)
                .then((orders)=>{
                    res.status(200).send(orders)
                })
                .catch((reason)=>res.status(500).send({error: reason}))
        }
        else {
            
        }
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}

exports.orderHistoryInfo = (req, res) => {
    try {
        let id = req.params.id
        if(req.user.role === 'user') {
            order.orderHistoryInfoUser(id)
                .then((orders)=>{
                    res.status(200).send(orders)
                })
                .catch((reason)=>res.status(500).send({error: reason}))
        }
        else {
            
        }
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}

exports.masterInfo = (req, res) => {
    try {
        order.masterInfo(req.params.id)
            .then((info)=>{
                order.masterFeedback(req.params.id)
                    .then((feedbacks)=>{
                        res.status(200).send({...info, feedbacks: feedbacks})
                    })
                    .catch((reason)=>res.status(500).send({error: reason}))
            })
            .catch((reason)=>res.status(500).send({error: reason}))
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}

exports.masterSchedule = (req, res) => {
    try {
        order.masterSchedule(req.user.id)
            .then((rows)=>{
                res.status(200).send(rows)
            })
            .catch((reason)=>res.status(500).send({error: reason}))
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}

exports.setSessionPaid = (req, res) => {
    try {
        order.setSessionPaid(req.body.sessionId)
            .then((rows)=>{
                res.status(200).send(rows)
            })
            .catch((reason)=>res.status(500).send({error: reason}))
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}
exports.setSessionCompleted = (req, res) => {
    try {
        order.setSessionCompleted(req.body.sessionId)
            .then((rows)=>{
                res.status(200).send(rows)
            })
            .catch((reason)=>res.status(500).send({error: reason}))
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}
exports.setSessionCancelled = (req, res) => {
    try {
        order.setSessionCancelled(req.body.sessionId)
            .then((rows)=>{
                res.status(200).send(rows)
            })
            .catch((reason)=>res.status(500).send({error: reason}))
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}