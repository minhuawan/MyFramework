local State = {
    Created = 1,
    Initialize = 2,
    Appeared = 3,
    Disposed = 4,
}
---@class MvpContext
local M = class("MvpContext")

function M:ctor()
    self._state = State.Created
end

return M