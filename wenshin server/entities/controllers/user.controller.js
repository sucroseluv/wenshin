const user = require('../models/user.model')

exports.account = (req, res) => {
    try {
        let userId = req.user.id
        user.account(userId)
            .then((rows)=>{
                res.status(200).send(rows)
            })
            .catch((reason)=>res.status(500).send({error: reason}))
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}

exports.changeAccountInfo = (req, res) => {
    try {
        let userId = req.user.id
        let newPassword = req.body.newPassword
        let newPhone = req.body.newPhone 

        
        let firstPromise = newPassword != null ? 
            user.changeAccountPassword(userId, newPassword) : 
            new Promise((resolve, reject) => {
                resolve({})
            })

        let secondPromise = newPhone != null ? 
            user.changeAccountNumber(userId, newPhone) : 
            new Promise((resolve, reject) => {
                resolve({})
            })

        firstPromise
            .then((rows)=>{
                secondPromise
                    .then((rows)=>{
                        res.status(200).redirect('/account')
                    })
                    .catch((reason)=>res.status(500).send({error: reason}))
            })
            .catch((reason)=>res.status(500).send({error: reason}))
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}