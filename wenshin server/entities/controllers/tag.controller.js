const tag = require('../models/tag.model')

exports.tags = (req, res) => {
    try {
        tag.list()
            .then((rows)=>{
                res.status(200).send(rows)
            })
            .catch((reason)=>res.status(500).send({error: reason}))
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}