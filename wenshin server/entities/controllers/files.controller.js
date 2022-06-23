const crypto = require("crypto");

exports.uploadSketchImage = async (req, res) => {
    try {
        if(!req.files) {
            res.send({
                status: false,
                message: 'No file uploaded'
            });
        } else {
            let image = req.files.image;
            let newName = crypto.randomBytes(16).toString('hex') + '.jpg'
            image.mv('./static/images/sketches/' + newName);

            res.send({
                path: 'sketches/' + newName
            });
        }
    } catch (err) {
        res.status(500).send(err.message);
    }
};