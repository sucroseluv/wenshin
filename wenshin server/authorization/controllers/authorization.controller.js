const crypto = require("crypto");
const db = require("../../common/services/database.service")

exports.login = (req, res) => {
    try {
        let token = crypto.randomBytes(64).toString('base64');
        let user = req.body.user
        // db.query("DELETE FROM tokens WHERE email=? and role=?", [user.email, user.role], (err, rows, fields) => {
        //     if(err) return res.status(500).send({ error: 'Ошибка удаления старого токена из БД' })
        //     db.query("INSERT INTO tokens (email, token, role, user_id) values (?, ?, ?, ?)", [user.email, token, user.role, user.id],
        //         (err, rows, fields) => {
        //             if(err) return res.status(500).send({ error: 'Ошибка добавления токена в БД [' + user?.email + ", " + token + ", " +  user?.role + ", " +  user?.id + "] - " + err.message })
        //             res.status(201).send({token: token, role: user.role});
        //         })
        // })

        db.query("INSERT INTO tokens (email, token, role, user_id) values (?, ?, ?, ?)", [user.email, token, user.role, user.id],
                (err, rows, fields) => {
                    if(err) return res.status(500).send({ error: 'Ошибка добавления токена в БД [' + user?.email + ", " + token + ", " +  user?.role + ", " +  user?.id + "] - " + err.message })
                    res.status(201).send({token: token, role: user.role});
                })

    }
    catch (err) {
        res.status(500, {error: err})
    }
}