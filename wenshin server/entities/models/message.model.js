const db = require("../../common/services/database.service")

exports.add = (orderId, message, user) => {
    return new Promise((resolve, reject) => {
        db.query("insert into messages (message, author, datetime, order_id) values (?, ?, ?, ?)",
        [message, user.role, new Date(), orderId], 
        (err, rows, fields) => {
            if(err) resolve(err)
            db.query("select * from messages where message=? and author=? and order_id=?",[message, user.role, orderId], 
            (err, rows, fields) => {
                if(err) resolve(err)
                if(rows.length > 0)
                    resolve(rows[0])
                else
                    resolve({})
            })
        })
    })
}

exports.list = (orderId) => {
    return new Promise((resolve, reject) => {
        db.query("select * from messages where order_id=?",[orderId], 
        (err, rows, fields) => {
            if(err) resolve(err)
            resolve(rows)
        })
    })
}

exports.listFromMessage = (orderId, messageId) => {
    return new Promise((resolve, reject) => {
        db.query("select * from messages where order_id=? and id>?",[orderId, messageId], 
        (err, rows, fields) => {
            if(err) resolve(err)
            resolve(rows)
        })
    })
}