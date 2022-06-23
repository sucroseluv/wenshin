const { off } = require("../../common/services/database.service")
const db = require("../../common/services/database.service")

const masterConsultantId = 4

exports.createOrder = (data, user) => {
    let status = "orderInProgress"
    let { masterId, sketchId } = data
    let userId = user.id
    return new Promise((resolve, reject) => {
        db.query("INSERT INTO user_order (status, sketch_id, client_id, master_id) values (?,?,?,?);", 
            [status, sketchId, userId, masterId], 
            (err, rows, fields) => {
                if(err) {
                    reject(err.message)
                }
                db.query("SELECT max(id) as id from user_order where sketch_id=? and client_id=? and master_id=?;", [sketchId, userId, masterId],
                    (err,rows,fields) => {
                        if(err) reject(err.message)
                        resolve(rows[0].id)
                    })
            }
        )
    })
}

exports.createSessions = (orderId, sessions, firstPaid) => {
    let orders = sessions.map((session, i) =>  `('${session.date}', '${session.hours.join(',')}', 'inProgress', ${orderId}, ${i===0&&firstPaid?1:0})`).join(',')
    return new Promise((resolve, reject) => {
        db.query(`INSERT INTO sessions (date, hours, status, order_id, isPaid) values ${orders};`, 
            [], 
            (err, rows, fields) => {
                if(err) {
                    console.log('sessions error: ' + orders , err)
                    reject('sessions error: ' + orders+err.message)
                }
                resolve(rows)
            }
        )
    })
}

exports.createConsultation = (user) => {
    let status = "orderInProgress"
    let userId = user.id
    return new Promise((resolve, reject) => {
        db.query("INSERT INTO user_order (status, sketch_id, client_id, master_id) values (?,?,?,?);", 
            [status, 1, userId, masterConsultantId], 
            (err, rows, fields) => {
                if(err) {
                    reject(err.message)
                }
                db.query("SELECT max(id) as id from user_order where sketch_id=? and client_id=? and master_id=?;", [1, userId, masterConsultantId],
                    (err,rows,fields) => {
                        if(err) reject(err.message)
                        resolve(rows[0].id)
                    })
            }
        )
    })
}

exports.createSession = (orderId, session) => {
    return new Promise((resolve, reject) => {
        db.query(`INSERT INTO sessions (date, hours, status, order_id, isPaid) values (?,?,?,?,?);`, 
            [session.date, session.hours.join(','), 'inProgress', orderId, 1], 
            (err, rows, fields) => {
                if(err) {
                    reject('session create error: ' + err.message)
                }
                resolve(rows)
            }
        )
    })
}

exports.masterList = (masterId) => {
    return new Promise((resolve, reject) => {
        db.query("SELECT * FROM user_order WHERE master_id=? and status in ('orderInProgress', 'sketchRequest', 'seansCancelled')", [masterId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                resolve(rows)
            })
    })
}

exports.userList = (userId) => {
    return new Promise((resolve, reject) => {
        db.query("SELECT * FROM user_order WHERE client_id=? and status in ('orderInProgress', 'sketchRequest', 'seansCancelled')", [userId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                resolve(rows)
            })
    })
}


exports.userOrdersList = (userId) => {
    return new Promise((resolve, reject) => {
        db.query("SELECT o.*, image, name as sketchName, description, width, height, working_hours, concat(lastname, ' ', firstname, ' ',  middlename) as name, avatar, price, working_hours * price as amount FROM user_order as o " +
        "inner join sketch as sk on sk.id = o.sketch_id " +
        "inner join master as m on m.id = o.master_id " +
        "WHERE client_id=? and o.status in ('orderInProgress', 'sketchRequest', 'seansCancelled') order by id desc", [userId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                resolve(rows)
            })
    })
}
exports.userOrderInfo = (orderId) => {
    return new Promise((resolve, reject) => {
        db.query("SELECT o.*, image, name as sketchName, description, width, height, working_hours, concat(lastname, ' ', firstname, ' ',  middlename) as name, avatar, price, working_hours * price as amount FROM user_order as o " +
        "inner join sketch as sk on sk.id = o.sketch_id " +
        "inner join master as m on m.id = o.master_id " +
        "WHERE o.id=? and o.status in ('orderInProgress', 'sketchRequest', 'seansCancelled')", [orderId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                if(rows.length > 0)
                    resolve(rows[0])
                else
                    resolve({})
            })
    })
}

exports.masterOrderInfo = (orderId) => {
    return new Promise((resolve, reject) => {
        db.query("SELECT o.*, image, name as sketchName, description, width, height, working_hours, concat(u.lastname, ' ', u.firstname, ' ',  u.middlename) as name, u.email, u.phone, price, working_hours * price as amount FROM user_order as o " +
        "inner join sketch as sk on sk.id = o.sketch_id " +
        "inner join user as u on u.id = o.client_id " +
        "inner join master as m on m.id = o.master_id " +
        "WHERE o.id=? and o.status in ('orderInProgress', 'sketchRequest', 'seansCancelled')", [orderId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                if(rows && rows.length > 0)
                    resolve(rows[0])
                else
                    resolve({})
            })
    })
}

exports.userSessionsList = (userId) => {
    return new Promise((resolve, reject) => {
        db.query("SELECT s.* FROM wenshin.sessions as s inner join wenshin.user_order as o on s.order_id = o.id " +
        "where o.client_id=? ", [userId],
            (err, rows, fields) => {
                if(err) reject(err.message)
                let mapped = rows.map(row => ({ ...row, hours: row.hours.split(',').map(v => Number.parseInt(v)) }))
                resolve(mapped)
            })
    })
}

exports.sessionsList = () => {
    return new Promise((resolve, reject) => {
        db.query("SELECT s.* FROM wenshin.sessions as s inner join wenshin.user_order as o on s.order_id = o.id ", [],
            (err, rows, fields) => {
                if(err) reject(err.message)
                let mapped = rows.map(row => ({ ...row, hours: row.hours.split(',').map(v => Number.parseInt(v)) }))
                resolve(mapped)
            })
    })
}

exports.orderHistoryUser = (userId) => {
    return new Promise((resolve, reject) => {
        db.query("SELECT o.*, f.id as feedbackId, rate, comment, s.image, s.name FROM user_order as o left join feedback as f on o.id = f.id_user_order inner join sketch as s on s.id = o.sketch_id WHERE client_id=? and status in ('completed') and master_id!=4", [userId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                resolve(rows)
            })
    })
}

exports.orderHistoryInfoUser = (orderId) => {
    return new Promise((resolve, reject) => {
        db.query("SELECT o.*, f.id as feedbackId, rate, comment, s.image, s.working_hours, s.name, concat(m.lastname,' ',m.firstname,' ',m.middlename) as masterName, avatar, price, price * working_hours as amount " +
            "FROM user_order as o left join feedback as f on o.id = f.id_user_order " +
            "inner join master as m on m.id = o.master_id " +
            "inner join sketch as s on s.id = o.sketch_id WHERE o.id=? and status in ('completed')", [orderId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                if(rows?.length > 0)
                    resolve(rows[0])
                else
                    resolve({})
            })
    })
}

exports.masterSchedule = (masterId) => {
    return new Promise((resolve, reject) => {
        db.query("select o.id as orderId, s.id as sessionId, date, hours, concat(u.lastname,' ',u.firstname,' ',u.middlename) as username, image, name as sketchname " +
            "from user_order as o inner join sessions as s on s.order_id = o.id " +
            "inner join sketch as sk on o.sketch_id = sk.id inner join user as u on o.client_id = u.id " +
            "where o.master_id = ? and s.date>=date_sub(now(), interval 1 DAY) and s.date<=date_add(now(), interval 1 MONTH) order by date", 
            [masterId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                rows.forEach((s)=>console.log("zxc"+s.date))
                let mappedHours = rows.map(r=>({...r, hours: r.hours.split(',').map(h=>Number.parseInt(h)), masterId: masterId }))
                resolve(mappedHours)
            })
    })
}

exports.masterOrders = (masterId) => {
    return new Promise((resolve, reject) => {
        db.query("SELECT o.id, image, name, concat(lastname,' ',firstname,' ',middlename) as clientname FROM wenshin.user_order as o inner join wenshin.sketch as s on s.id = o.sketch_id " +
        "inner join wenshin.user as u on u.id = o.client_id " +
        "where o.master_id = ? and o.status not in ('completed') order by id desc;",
            [masterId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                let mapped = rows.map((row)=>({...row, masterId: masterId}))
                resolve(mapped)
            })
    })
}

exports.masterInfo = (masterId) => {
    return new Promise((resolve, reject) => {
        db.query("select concat(lastname,' ',firstname,' ',middlename) as name, email, phone, avatar, price FROM wenshin.master where id=?",
            [masterId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                if(rows && rows.length > 0)
                    resolve(rows[0])
                else resolve({})
            })
    })
}

exports.masterFeedback = (masterId) => {
    return new Promise((resolve, reject) => {
        db.query("SELECT s.name as sketchname, working_hours as hours, image, concat(lastname,' ',firstname,' ',middlename) as name, rate, comment FROM wenshin.feedback as f inner join wenshin.user_order as o on f.id_user_order = o.id " +
        "inner join user as u on u.id = o.client_id " +
        "inner join sketch as s on s.id = o.sketch_id " +
        "where o.master_id=?", 
            [masterId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                resolve(rows)
            })
    })
}

exports.setSessionPaid = (sessionId) => {
    return new Promise((resolve, reject) => {
        db.query("UPDATE sessions SET isPaid = 1 WHERE id = ?", 
            [sessionId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                resolve(rows)
            })
    })
}

exports.setSessionCompleted = (sessionId) => {
    return new Promise((resolve, reject) => {
        db.query("UPDATE sessions SET status = 'completed', isPaid=1 WHERE id=?", 
            [sessionId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                db.query("select order_id from sessions where id=?", 
                [sessionId],
                (err, rows, fields) => {
                    if(err) reject(err.message)
                    let orderId = rows[0].order_id
                    db.query("select s.id as sessionId, hours, working_hours, s.status from user_order as o inner join sessions as s on s.order_id = o.id inner join sketch as sk on sk.id=o.sketch_id where o.id=?", [orderId],
                    (err, rows, fields) => {
                        if(err) reject(err.message)
                        if(rows != null && rows.length != 0) {
                            let allWork = rows[0].working_hours
                            if(rows.filter(r=>r.status=='completed').reduce((acc, r) => acc+r.hours.split(',').length,0) >= allWork) {
                                db.query("UPDATE user_order SET status = 'completed' WHERE id = ?", 
                                    [orderId], 
                                    (err, rows, fields) => {
                                        if(err) reject(err.message)
                                        resolve(rows)
                                    })
                            } else {
                                resolve(rows)
                            }
                        }
                        
                    })
                })
            })
    })
}
exports.setSessionCancelled = (sessionId) => {
    console.log("cancelled", sessionId)
    return new Promise((resolve, reject) => {
        db.query("UPDATE sessions SET status = 'cancelled' WHERE id=?", 
            [sessionId], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                resolve(rows)
                // TODO set user_order status to rejected
            })
    })
}