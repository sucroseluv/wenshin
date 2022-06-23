const e = require('express')
const messageModel = require('../models/message.model')

exports.sendMessage = (req, res) => {
    try {
        let message = req?.body?.message
        let orderId = req?.body?.orderId
        messageModel.add(orderId, message, req.user)
            .then((value)=>{
                res.send(value)
            })
            .catch((reason)=>res.status(500, {error: reason}))

    }
    catch (err) {
        res.status(500, {error: err})
    }
}

exports.messages = (req, res) => {
    try {
        let orderId = req?.query?.orderId
        let messageId = req?.query?.messageId
        if(!messageId) {
            messageModel.list(orderId)
                .then((value)=>{
                    res.send(value)
                })
                .catch((reason)=>res.status(500, {error: reason}))
        } else {
            messageModel.listFromMessage(orderId, messageId)
                .then((value)=>{
                    res.send(value)
                })
                .catch((reason)=>res.status(500, {error: reason}))
        }

    }
    catch (err) {
        res.status(500, {error: err})
    }
}