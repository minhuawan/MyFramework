function __G__TRACEBACK__(msg)
    local _msg = debug.traceback(msg, 3)
    return _msg
end

xpcall(function()
    require("core.class")
    require("core.constant.Defined")
    require("ui.splash.SplashPresenter").new()
end, __G__TRACEBACK__)