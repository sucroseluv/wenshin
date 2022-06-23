const db = require("../../common/services/database.service")

exports.list = () => {
    return new Promise((resolve, reject) => {
        db.query("SELECT * FROM wenshin.tag", [], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                let mapped = rows.map(row => row.name)
                resolve(mapped)
            })
    })
}

exports.addTagsToSketch = (tags, sketchId) => {
    let tagstr = tags.map((t) => `(${sketchId},${t})`).join(',')
    return new Promise((resolve, reject) => {
        if(tags?.length === 0) resolve(1)
        db.query("INSERT INTO sketch_tags (id_sketch, id_tag) values " + tagstr, [], 
            (err, rows, fields) => {
                if(err) reject(err.message)
                resolve(rows)
            })
    })
}