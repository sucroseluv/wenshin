const sketch = require('../models/sketch.model')
const tag = require('../models/tag.model')

exports.sketches = (req, res) => {
    try {
        let tags = req?.query?.tags
        let keyword = req?.query?.keyword
        sketch.list(tags, keyword)
            .then((value)=>{
                res.send(value)
            })
            .catch((reason)=>res.status(500, {error: reason}))

    }
    catch (err) {
        res.status(500, {error: err})
    }
}

exports.sketch = (req, res) => {
    try {
        let id = req?.params?.id
        sketch.get(id)
            .then((value)=>{
                res.send(value)
            })
            .catch((reason)=>res.status(500, {error: reason}))

    }
    catch (err) {
        res.status(500, {error: err})
    }
}

exports.favSketches = (req, res) => {
    try {
        let id = req.user.id
        sketch.favoriteList(id)
            .then((value)=>{
                res.send(value)
            })
            .catch((reason)=>res.status(500, {error: reason}))
    }
    catch (err) {
        res.status(500, {error: err})
    }
}

exports.setFavSketch = (req, res) => {
    try {
        let userId = req.user.id
        let sketchId = req.body.sketchId
        let favorite = req.body.favorite
        if(!sketchId){
            res.status(400).send("Укажите sketchId")
            return
        }
        if(favorite)
            sketch.favorite(sketchId, userId)
                .then((value)=>{
                    res.send(value)
                })
                .catch((reason)=>res.status(500, {error: reason}))
        else
            sketch.unfavorite(sketchId, userId)
                .then((value)=>{
                    res.send(value)
                })
                .catch((reason)=>res.status(500, {error: reason}))
    }
    catch (err) {
        res.status(500, {error: err})
    }
}

exports.createSketch = (req, res) => {
    try {
        sketch.addSketch(req.body, req.user.id)
            .then((sketchId)=>{
                tag.addTagsToSketch(req.body.tags, sketchId)
                    .then(()=>{
                        res.status(201).send({id: sketchId})
                    })
                    .catch((reason)=>res.status(500).send({error: reason, place: "addTagsToSketch"}))
            })
            .catch((reason)=>res.status(500).send({error: reason, place: "createSketch"}))
    }
    catch (err) {
        res.status(500).send({error: err.message})
    }
}
