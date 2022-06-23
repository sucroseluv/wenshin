
exports.permissionLevelRequired = (permissionLevel) => (req, res, next) => {
    // if(req.user && (req.user.role === permissionLevel))
    //     console.log("permissionLevelRequired", req.body)
    if(req.user && (req.user.role === permissionLevel || req.user.role === 'admin')) {
        return next()
    }
    return res.status(403).send({})
}