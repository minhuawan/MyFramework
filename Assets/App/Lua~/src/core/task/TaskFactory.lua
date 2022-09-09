local TimeTask = require("core.task.TimeTask")
---@class TaskFactory
local M = class("TaskFactory")

function M:initialize()
    if self._initialized then
        return
    end
    self._initialized = true
    ---@type map
    self._taskMap = collections.map()

    self._tickWrapper = CS.MyFramework.Runtime.Utils.UnityTickWrapper.Create('Lua-TaskFactory')
    self._tickWrapper:BindUpdateTick(bind(self.tick, self), 60)
end

---@return TimeTask
function M:createTask()
    self:initialize()
    local task = TimeTask.new()
    self._taskMap:set(tostring(task), task)
    return task
end

function M:releaseTask(task)
    local key = tostring(task)
    if self._taskMap:has(key) then
        ---@type TimeTask
        local task = self._taskMap:get(key)
        task:release()
        self._taskMap:remove(key)
    end
end

function M:tick(deltaTime)
    if self._taskMap:count() == 0 then
        return
    end
    ---@param task TimeTask
    for key, task in self._taskMap:iter() do
        if not task:tick() then
            self._taskMap:remove(key)
        end
    end
end

function M:disposeFactory()
    if self._initialized then
        self._tickWrapper = nil
        self._initialized = false;
    end
end

return M
