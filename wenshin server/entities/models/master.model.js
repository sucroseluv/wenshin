const db = require("../../common/services/database.service")

exports.list = () => {
    return new Promise((resolve, reject) => {
        db.query("SELECT id, lastname, firstname, middlename, avatar, price FROM wenshin.master where id!=4;", [], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                resolve(rows)
            })
    })
}

exports.busyness = (masterId) => {
    return new Promise((resolve, reject) => {
        db.query("SELECT date_format(date, \"%Y-%m-%d\") as date,hours FROM wenshin.sessions as s inner join wenshin.user_order as o on s.order_id = o.id where s.status='inProgress' and o.master_id=? order by s.date;", [masterId],
            (err, rows, fields) => {
                if(err) reject(err.message)
                let mapped = rows.map(row => ({ date: row.date, hours: row.hours.split(',').map(v => Number.parseInt(v)) }))
                resolve(mapped)
            })
    })
}
