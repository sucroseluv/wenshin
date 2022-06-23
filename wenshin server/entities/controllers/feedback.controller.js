const feedback = require('../models/feedback.model')

exports.sendFeedback = (req, res) => {
    try {
        let orderId = req?.body?.orderId
        let comment = req?.body?.comment
        let rate = req?.body?.rate

        if(comment?.length > 255) {
            res.status(400).send("Поле комментария содержит больше 255 символов")
            return
        }
        let parsedRate = Number.parseInt(rate)
        if(parsedRate < 1 || parsedRate > 5) {
            res.status(400).send("Оценка должна быть в пределах от 1 до 5")
            return
        }

        feedback.add(orderId, comment, rate)
            .then((value)=>{
                res.send(value)
            })
            .catch((reason)=>res.status(500, {error: reason}))

    }
    catch (err) {
        res.status(500, {error: err})
    }
}