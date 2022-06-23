const db = require('../services/database.service')

exports.validateToken = (req, res, next) => {
    try {
        let token = req.headers['authorization']
        if(token) {
            db.query("SELECT * FROM tokens WHERE token=?", [token], (err, rows, fields) => {
                if(err) return res.status(403).send({error: 'Ошибка авторизации'});
                if(rows?.length > 0) {
                    const userInfo = rows[0]
                    req.user = {
                        id: userInfo.user_id,
                        email: userInfo.email,
                        role: userInfo.role
                    }
                }
                next()
            })
        } else {
            next()
        }

    }
    catch (err) {
        console.log('error', err)
    }
}

exports.validTokenNeeded = (req, res, next) => {
    if(req.user && req.user.role) {
        return next()
    }
    if(req.headers['authorization'])
        return res.status(403).send({errors: 'Ошибка авторизации'})
    return res.status(401).send({errors: 'Unauthorized'})
}