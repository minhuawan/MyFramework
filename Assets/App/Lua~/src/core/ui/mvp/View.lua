---@class View
local M = class("View")

function M:initialize(model)
end

function M:appearAsync(next)
    self.gameObject:SetActive(true)
    next()
end

function M:disappearAsync(next)
    self.gameObject:SetActive(false)
    next()
end

function M:dispose()
end


return M