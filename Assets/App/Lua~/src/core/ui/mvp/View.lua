---@class View
local M = class("View")

function M:initialize(model)

end

function M:appearAsync(next)
    next()
end

function M:disappearAsync(next)
    next()
end

return M