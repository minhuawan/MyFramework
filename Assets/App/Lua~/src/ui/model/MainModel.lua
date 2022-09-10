local Model = require("core.ui.mvp.Model")
---@class MainModel : Model
local M = class("MainModel", Model)


function M:getName()
    return 'test'
end

return M