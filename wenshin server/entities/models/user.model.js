const db = require("../../common/services/database.service")

exports.account = (userId) => {
    return new Promise((resolve, reject) => {
        db.query("SELECT phone FROM user where id=?", [userId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                if(rows.length > 0)
                    resolve(rows[0])
                else
                    reject("Нет такого пользователя")
            })
    })
}

exports.changeAccountPassword = (userId, newPassword) => {
    return new Promise((resolve, reject) => {
        db.query("UPDATE user SET password = ? WHERE id = ?", [newPassword, userId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                    resolve(rows)
            })
    })
}
exports.changeAccountNumber = (userId, newNumber) => {
    return new Promise((resolve, reject) => {
        db.query("UPDATE user SET phone = ? WHERE id = ?", [newNumber, userId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                    resolve(rows)
            })
    })
}