const VerifyUserMiddleware = require('../common/middlewares/auth.validation');
// const AuthorizationController = require('./controllers/authorization.controller');
const PermissionMiddleware = require('../common/middlewares/auth.permission')

const SketchesController = require('./controllers/sketch.controller')
const OrderController = require('./controllers/order.controller')
const MessageController = require('./controllers/message.controller')
const FeedbackController = require('./controllers/feedback.controller')
const MasterController = require('./controllers/master.controller')
const TagsController = require('./controllers/tag.controller')
const UserController = require('./controllers/user.controller')
const FilesController = require('./controllers/files.controller')

exports.routesConfig = function (app) {
    app.get('/sketches', [
        SketchesController.sketches
    ]);
    app.get('/sketch/:id', [
        SketchesController.sketch
    ])
    app.post('/uploadSketchImage', [
        PermissionMiddleware.permissionLevelRequired('master'),
        FilesController.uploadSketchImage
    ])
    app.post('/createSketch', [
        PermissionMiddleware.permissionLevelRequired('master'),
        SketchesController.createSketch
    ])
    
    app.get('/favSketches', [
        VerifyUserMiddleware.validTokenNeeded,
        PermissionMiddleware.permissionLevelRequired('user'),
        SketchesController.favSketches
    ]);
    app.post('/favorite', [
        VerifyUserMiddleware.validTokenNeeded,
        PermissionMiddleware.permissionLevelRequired('user'),
        SketchesController.setFavSketch
    ])

    app.get('/masters', [
        MasterController.masters
    ])
    app.get('/masterBusyness/:id', [
        MasterController.masterBusyness
    ])

    app.get('/tags', [
        TagsController.tags
    ])

    app.get('/orders', [
        VerifyUserMiddleware.validTokenNeeded,
        OrderController.orders
    ])
    
    app.get('/order/:id', [
        VerifyUserMiddleware.validTokenNeeded,
        OrderController.orderInfo
    ])
    
    app.get('/orderHistory', [
        VerifyUserMiddleware.validTokenNeeded,
        OrderController.orderHistory
    ])
    app.get('/orderHistory/:id', [
        OrderController.orderHistoryInfo
    ])

    app.post('/setSessionCompleted', [
        VerifyUserMiddleware.validTokenNeeded,
        PermissionMiddleware.permissionLevelRequired('master'),
        OrderController.setSessionCompleted
    ])
    app.post('/setSessionCancelled', [
        VerifyUserMiddleware.validTokenNeeded,
        PermissionMiddleware.permissionLevelRequired('master'),
        OrderController.setSessionCancelled
    ])
    app.post('/setSessionPaid', [
        VerifyUserMiddleware.validTokenNeeded,
        OrderController.setSessionPaid
    ])

    // need to test
    app.get('/schedule', [
        VerifyUserMiddleware.validTokenNeeded,
        PermissionMiddleware.permissionLevelRequired('master'),
        OrderController.masterSchedule
    ])
    app.get('/masterInfo/:id', [
        OrderController.masterInfo
    ])
    app.get('/masterOrders', [
        VerifyUserMiddleware.validTokenNeeded,
        PermissionMiddleware.permissionLevelRequired('master'),
        OrderController.orders
    ])
    app.post('/createOrder', [
        VerifyUserMiddleware.validTokenNeeded,
        PermissionMiddleware.permissionLevelRequired('user'),
        OrderController.createOrder
    ])
    app.post('/createSessions', [
        VerifyUserMiddleware.validTokenNeeded,
        PermissionMiddleware.permissionLevelRequired('user'),
        OrderController.createSessions
    ])
    app.post('/createConsultation', [
        VerifyUserMiddleware.validTokenNeeded,
        PermissionMiddleware.permissionLevelRequired('user'),
        OrderController.createConsultation
    ])

    app.get('/messages', [
        VerifyUserMiddleware.validTokenNeeded,
        MessageController.messages
    ])
    app.post('/sendMessage', [
        VerifyUserMiddleware.validTokenNeeded,
        MessageController.sendMessage
    ])
    
    app.post('/sendFeedback', [
        VerifyUserMiddleware.validTokenNeeded,
        PermissionMiddleware.permissionLevelRequired('user'),
        FeedbackController.sendFeedback
    ])

    app.get('/account', [
        VerifyUserMiddleware.validTokenNeeded,
        PermissionMiddleware.permissionLevelRequired('user'),
        UserController.account
    ])

    app.post('/changeAccountInfo', [
        VerifyUserMiddleware.validTokenNeeded,
        PermissionMiddleware.permissionLevelRequired('user'),
        UserController.changeAccountInfo
    ])
};