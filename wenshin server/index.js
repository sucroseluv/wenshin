const express = require('express')
const config = require('./common/config/env.config')
const db = require('./common/services/database.service')
const app = express()
const jsonParser = express.json()
const validateToken = require('./common/middlewares/auth.validation').validateToken
const authorization = require('./authorization/routes.config')
const sketches = require('./entities/routes.config')
const fileUpload = require('express-fileupload');

app.use(jsonParser)
app.use(validateToken)
app.use(fileUpload({
    createParentPath: true
}));
// app.use((req,res, next) => {
//     if(req.header('jwt') === 'user123') {
//         req.user = {
//             name: 'user228',
//             role: 'user'
//         }
//     }
//     else if(req.header('jwt') === 'master123') {
//         req.user = {
//             name: 'master228',
//             role: 'master'
//         }
//     }
//     else {
//         req.user = {
//             name: undefined,
//             role: 'unauthenticated'
//         }
//     }
//
//     next()
// })

// app.get('/', (req, res) => {
//     //const answer = Array(10).fill(req.header('jwt'))
//     db.query("SELECT * FROM sketch",function (err, rows, fields) {
//         if(err) res.send({ errors: err })
//         //res.send({errors: err})
//         else res.send({ data: rows })
//     })
//
// })

authorization.routesConfig(app)
sketches.routesConfig(app)

app.use(express.static('static'))
app.listen(config.port, () => {
    console.log(`server started on port ${config.port}`)
})
