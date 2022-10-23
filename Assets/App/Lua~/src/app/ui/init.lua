return function()
    local UIManager = require("app.ui.base.UIManager")
    return {
        UIManager = UIManager(), -- create instance
    }
end