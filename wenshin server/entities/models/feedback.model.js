const db = require("../../common/services/database.service")

exports.list = (userId) => {
    return new Promise((resolve, reject) => {
        db.query("SELECT id,lastname,firtname,middlename,avatar,price FROM wenshin.master", [], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                resolve(rows)
            })
    })
}

exports.add = (orderId, comment, rate) => {
    return new Promise((resolve, reject) => {
        db.query("INSERT INTO feedback (rate, comment, id_user_order) values (?,?,?)", [rate, comment, orderId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                db.query("SELECT rate, comment FROM feedback where id_user_order=?", [orderId], 
                (err, rows, fields) => {
                    if(err) reject(err.message)
                    resolve(rows[0])
                })
            })
    })
}