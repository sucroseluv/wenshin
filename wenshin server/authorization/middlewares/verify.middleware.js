const crypto = require("crypto");
const db = require("../../common/services/database.service")

exports.isPasswordAndUserMatch = (req, res, next) => {
    let email = req.body?.email
    let password = req.body?.password

    db.query("SELECT * FROM `all_users_and_passwords` where email=? and password=?", [email, password], (err, rows, fields) => {
        if(err) res.status(500).send({error: err})
        else {
            if(rows?.[0]){
                req.body.user = rows[0]
                return next()
            }
            else {
                return res.status(404).send({ error: 'Логин и/или пароль неверны' })
            }

        }
    })
};

exports.hasAuthValidFields = (req, res, next) => {
    let errors = [];

    if (req.body) {
        if (!req.body.email) {
            errors.push('Отсутствует поле почты');
        }
        if (!req.body.password) {
            errors.push('Отсутствует поле пароля');
        }

        if (errors.length) {
            return res.status(400).send({error: errors.length > 1 ? "Отсутствуют поля почты и пароля" : errors[0]});
        } else {
            return next();
        }
    } else {
        return res.status(400).send({error: 'Отсутствуют поля почты и пароля'});
    }
};