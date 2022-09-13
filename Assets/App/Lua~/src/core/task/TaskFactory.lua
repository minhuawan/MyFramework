local TimeTask = require("core.task.TimeTask")
---@class TaskFactory
local M = class("TaskFactory")

function M:initialize()
    if self._initialized then
        return
    end
    self._realtimeSinceStartup = CS.UnityEngine.Time.realtimeSinceStartup
    self._initialized = true
    ---@type map
    self._taskMap = collections.map()

    self._tickWrapper = CS.MyFramework.Runtime.Utils.UnityTickWrapper.Create('Lua-TaskFactory')
    self._tickWrapper:BindUpdateTick(bind(self.tick, self), 60)

    self._removeTick = bind(self.removeTick, self)
end

---@return TimeTask
function M:createTask()
    self:initialize()
    local task = TimeTask.new(self._realtimeSinceStartup)
    self._taskMap:set(tostring(task), task)
    return task
end

---@param task TimeTask
function M:releaseTask(task)
    self:initialize()
    local key = tostring(task)
    if self._taskMap:has(key) then
        task = self._taskMap:get(key)
        task:release()
        -- self._taskMap:remove(key)
        -- remove at next tick
    end
end

function M:tick(realtimeSinceStartup, deltaTime)
    self._realtimeSinceStartup = realtimeSinceStartup
    if self._taskMap:count() == 0 then
        return
    end

    self._taskMap:remove_if(self._removeTick)
end

---@private
---@param task TimeTask
function M:removeTick(_, task)
    local alive = task:tick(self._realtimeSinceStartup)
    -- log.debug('task: {}, alive: {}', task, alive)
    return not alive
end

function M:disposeFactory()
    if self._initialized then
        self._tickWrapper = nil
        self._initialized = false;
    end
end

return M
