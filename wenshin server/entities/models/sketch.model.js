const e = require("express")
const db = require("../../common/services/database.service")

exports.list = (tags, keyword) => {
    return new Promise((resolve, reject) => {
        db.query(`SELECT s.*, group_concat(t.name) as tags FROM wenshin.sketch as s left join wenshin.sketch_tags as st on s.id = st.id_sketch inner join wenshin.tag as t on st.id_tag = t.id where s.is_public=1 and (s.name like '%${keyword || ""}%' or s.description like '%${keyword || ""}%') group by s.id`, 
        [], (err, rows, fields) => {
            if(err) resolve(err)
            let rowsParsedTags = rows.map((val)=>({ ...val, tags: val.tags.split(',')}))
            if(tags){
                if(!Array.isArray(tags)) tags = [tags]
                rowsParsedTags = rowsParsedTags.filter((val)=>val.tags.some(l1=>tags?.some(l2=>l1===l2) ?? false))
            }
            // let rows = rows.map((val) => )
            resolve(rowsParsedTags)
        })
    })
}
exports.get = (id) => {
    return new Promise((resolve, reject) => {
    db.query("SELECT s.*, group_concat(t.name) as tags FROM wenshin.sketch as s left join wenshin.sketch_tags as st on s.id = st.id_sketch inner join wenshin.tag as t on st.id_tag = t.id where s.id=? group by s.id",
    [id], (err, rows, fields) => {
    if(err) resolve(err)
        let rowsParsedTags = rows.map((val)=>({ ...val, tags: val.tags.split(',')}))
        if(rows.length > 0)
            resolve(rowsParsedTags?.[0])
        else resolve({})
    })})
}

exports.favorite = (sketchId, userId) => {
    return new Promise((resolve, reject) => {
        db.query("insert into user_fav_sketches (sketch_id, user_id) values (?, ?)",
        [sketchId, userId], (err, rows, fields) => {
            if(err) resolve(err)
            resolve({ favorite: true })
        })
    })
}

exports.unfavorite = (sketchId, userId) => {
    return new Promise((resolve, reject) => {
        db.query("delete from user_fav_sketches where sketch_id=? and user_id=?",
        [sketchId, userId], (err, rows, fields) => {
            if(err) resolve(err)
            resolve({ favorite: false })
        })
    })
}

exports.favoriteList = (userId) =>  {
    return new Promise((resolve, reject) => {
        db.query("select s.* from " +
            "user as u inner join user_fav_sketches as uf on u.id = uf.user_id " +
            "inner join sketch as s on uf.sketch_id = s.id " +
            "where u.id = ?",
        [userId], (err, rows, fields) => {
            if(err) resolve(err)
            resolve(rows)
        })
    })
}

exports.addSketch = (data, masterId) => {
    return new Promise((resolve, reject) => {
        db.query("insert into sketch (image, name, description, width, height, working_hours, is_public, author) " +
            "values (?, ?, ?, ?, ?, ?, ?, ?)",
        [data.image, data.name, data.description, data.width, data.height, data.workingHours, 1, masterId], 
        (err, rows, fields) => {
            if(err) reject(err)
            db.query("SELECT * FROM wenshin.sketch where image=?;", [data.image], 
            (err, rows, fields) => {
                if(rows.length > 0)
                    resolve(rows[0].id)
                else
                    resolve(-1)
            })
        })
    })
}