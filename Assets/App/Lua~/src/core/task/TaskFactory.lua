local TimeTask = require("core.task.TimeTask")
local M = class("TaskFactory")

function M:initialize()
    if self._initialized then
        return
    end
    self._initialized = true
    ---@type map
    self._taskMap = collections.map()

    self._tickWrapper = CS.MyFramework.Runtime.Utils.UnityTickWrapper.Create('Lua-TaskFactory')
    self._tickWrapper:BindUpdateTick(bind(self.tick, self))
end

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

function M:tick()
    for _, task in self._taskMap:iter() do
        task:tick()
    end
end

return M
