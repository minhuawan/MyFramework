local CanvasSortingBaseValue = 0
local type_binder = typeof(CS.MyFramework.Runtime.Services.Lua.LuaViewBinder)
local MvpContextState = require("app.ui.base.MvpContextState")
---@class MvpContext
local M = class("MvpContext")

---@param configuration table
function M:ctor(configuration)
    if not configuration then
        log.exception("[MvpContext] configuration is nil value")
    end
    self.configuration = configuration
    ---@type Presenter
    self.presenter = configuration.mvp.presenter.new()
    if configuration.mvp.model then
        self.model = configuration.mvp.model.new()
        self.model:initialize()
    end
    self._state = MvpContextState.Created
    ---@type list
    self._stateListeners = collections.list()

    if not self.presenter then
        log.exception("[MvpContext] presenter is nil value")
    end

    --log.debug("base canvas order +1")
    CanvasSortingBaseValue = CanvasSortingBaseValue + 1
    if self.configuration.type == 'switchable' then
        self.canvasOrder = CanvasSortingBaseValue
        --log.debug("canvas order = base = {} at {}", self.canvasOrder, self.presenter)
    elseif configuration.type == 'single' then
        self.canvasOrder = CanvasSortingBaseValue + 1000
        --log.debug("canvas order = base + 1000 = {} at {}", self.canvasOrder, self.presenter)
    else
        log.warn('[MvpContext] unknown type {} at {}', self.configuration.type, self.presenter)
        self.canvasOrder = CanvasSortingBaseValue
    end

end

function M:getPrefabPath()
    if self.configuration then
        return self.configuration.prefab or "NIL"
    end
    return "NIL"
end

function M:addStateChangeListener(l)
    assert(type(l) == 'function', 'listener should be a function')
    self._stateListeners:append(l)
end

function M:removeStateChangeListener(l)
    local index = self._stateListeners:find(l)
    if index then
        self._stateListeners:remove(index)
    end
end

function M:moveNextState(from)
    if self._state == MvpContextState.Dispose then
        log.error(
                "[MvpContext] move next state error, state are disposed, presenter type {}",
                self.presenter
        )
        return
    end
    if self._state == MvpContextState.Initialize and from ~= 'create-view-async' then
        -- to avoid `move state` duplicate in async method performing
        log.error('[MvpContext] can not move next, because in `initialize` move without force, target: {}', self.configuration.prefab)
        return
    end
    -- log.verbose("[MvpContext] perform moveNextState, current state {}, self: {}", self._state, self)
    self._state = self._state + 1
    if self._state == MvpContextState.Initialize then
        self.presenter:initialize(self)
    elseif self._state == MvpContextState.Appear then
        self.presenter:appear()
    elseif self._state == MvpContextState.Disappear then
        self.presenter:disappear()
    elseif self._state == MvpContextState.Dispose then
        -- self:dispose()
        -- delay perform after invoke _stateListeners
    else
        log.error("[MvpContext] move next state error, state is {}", self._state)
        return
    end
    for _, l in self._stateListeners:iter() do
        local ok, msg = pcall(l, self, self._state)
        if not ok then
            log.error('[MvpContext] call state listener with error, msg: {}', msg)
        end
    end
    if self._state == MvpContextState.Dispose then
        self:dispose()
    end
    -- log.verbose("[MvpContext] call state listener, name:{}, state: {}", self:getName(), self._state)
end

function M:canSwitch()
    return self._state >= MvpContextState.Appear
end

function M:createViewAsync(next)
    if not self.presenter then
        log.exception("[MvpContext] createViewAsync error, presenter is nil")
    end
    local prefab = self.configuration.prefab
    log.assert(type(prefab) == "string" and prefab ~= "", "prefab is nil or empty")
    -- todo CS load

    local unityObject = CS.MyFramework.Application.LoadGameObject(self.configuration.prefab)
    local gameObject = CS.UnityEngine.Object.Instantiate(unityObject)

    ---@type View
    self.view = self.configuration.mvp.view.new()
    if self.canvasOrder then
        local canvasGameObject = gameObject.transform:Find("Canvas")
        log.assert(canvasGameObject, "[MvpContext] can't find Canvas gameObject in root, view: {}", self.view)
        local canvas = canvasGameObject:GetComponent(typeof(CS.UnityEngine.Canvas))
        log.assert(canvas, "[MvpContext] can't find Canvas component of gameObject, view: {}", self.view)
        canvas.sortingOrder = self.canvasOrder
    end
    gameObject:SetActive(false)
    self.view.gameObject = gameObject
    local binder = gameObject:GetComponent(type_binder)
    if binder then
        self.view.binder = binder
    else
        self.view.binder = nil
    end
    -- for avoid sync
    self._task = App.task.TaskFactory:createTask()
    self._task:bind(bind(next, self.view))
        :delay(0)
        :start()
end

function M:dispose()
    if self.disposed then
        return
    end
    self.disposed = true
    if self._task then
        App.task.TaskFactory:disposeTask(self._task)
        self._task = nil
    end
    if self.view then
        self.view:dispose()
        self.view.binder = nil
        CS.UnityEngine.Object.Destroy(self.view.gameObject)
        self.view.gameObject = nil
        self.view = nil
    end
    self._stateListeners:clear()
    CanvasSortingBaseValue = CanvasSortingBaseValue - 1
    --log.verbose("base canvas order -1 at {}", self.presenter)
    if self.presenter then
        self.presenter:dispose()
        self.presenter = nil
    end
end

return M
