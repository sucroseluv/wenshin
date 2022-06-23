const VerifyUserMiddleware = require('./middlewares/verify.middleware');
const AuthorizationController = require('./controllers/authorization.controller');
const RegisterUserMiddleware = require('./middlewares/register.middleware');
const RegisterController = require('./controllers/register.controller');

exports.routesConfig = function (app) {
    app.post('/auth', [
        VerifyUserMiddleware.hasAuthValidFields,
        VerifyUserMiddleware.isPasswordAndUserMatch,
        AuthorizationController.login
    ]);
    app.post('/register', [
        RegisterUserMiddleware.isEmailNew,
        RegisterController.register
    ]);
    app.get('/', [
        (req, res) => {
            var ip = req.headers['x-forwarded-for'] ||
                req.socket.remoteAddress ||
                null;
            console.log(`ping ip: ${ip}`)
            res.status(200).send("pong")
        }
    ]);
};