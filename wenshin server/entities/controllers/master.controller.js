const master = require('../models/master.model')

exports.masters = (req, res) => {
    try {
        master.list()
            .then((rows)=>{
                res.status(200).send(rows)
            })
            .catch((reason)=>res.status(500).send({error: reason}))
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}

exports.masterBusyness = (req, res) => {
    try {
        let masterId = Number.parseInt(req?.params?.id)
        master.busyness(masterId)
            .then((rows)=>{
                res.status(200).send(rows)
            })
            .catch((reason)=>res.status(500).send({error: reason}))
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}