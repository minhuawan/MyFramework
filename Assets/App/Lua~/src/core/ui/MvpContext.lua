local CanvasSortingBaseValue = 0
local TaskFactory = require("core.task.TaskFactory")
local type_binder = typeof(CS.MyFramework.Runtime.Services.Lua.LuaViewBinder)
local MvpContextState = require("core.ui.MvpContextState")
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
    end
    self._state = MvpContextState.Created

    if not self.presenter then
        log.exception("[MvpContext] presenter is nil value")
    end

    CanvasSortingBaseValue = CanvasSortingBaseValue + 1
    if self.configuration.type == 'switchable' then
        self.canvasOrder = 1000 + CanvasSortingBaseValue
    else
        self.canvasOrder = CanvasSortingBaseValue
    end
end

function M:getName()
    if self.configuration then
        return self.configuration.prefab or "NIL"
    end
    return "NIL"
end

function M:addStateChangeListener(l)
    assert(type(l) == 'function', 'listener should be a function')
    if not self._stateListeners then
        self._stateListeners = {}
    end
    table.insert(self._stateListeners, l)
end

function M:removeStateChangeListener(l)
    if type(l ~= "function") or not self._stateListeners then
        return
    end
    local index
    for i, listener in ipairs(self._stateListeners) do
        if listener == i then
            index = i
            break
        end
    end
    if index then
        table.remove(self._stateListeners, index)
    end
end

function M:moveNextState()
    if self._state == MvpContextState.Dispose then
        log.error(
                "[MvpContext] move next state error, state are disposed, presenter type {}",
                self.presenter
        )
        return
    end
    log.verbose("[MvpContext] perform moveNextState, current state {}, self: {}", self._state, self)
    self._state = self._state + 1
    if self._state == MvpContextState.Initialize then
        self.presenter:initialize(self)
    elseif self._state == MvpContextState.Appear then
        self.presenter:appear()
    elseif self._state == MvpContextState.Disappear then
        self.presenter:disappear()
    elseif self._state == MvpContextState.Dispose then
        self:dispose()
    else
        log.exception("[MvpContext] move next state error, state is {}", self._state)
    end
    if self._stateListeners then
        -- log.verbose("[MvpContext] call state listener, name:{}, state: {}", self:getName(), self._state)
        for _, l in ipairs(self._stateListeners) do
            l(self, self._state)
        end
    end
end

function M:canSwitch()
    return self._state >= MvpContextState.Appear
end

function M:dispose()
    self.presenter:dispose()
    if self.view then
        self.view:dispose()
        self.view.binder = nil
        CS.UnityEngine.Object.Destroy(self.view.gameObject)
        self.view.gameObject = nil
        self.view = nil
    end
    self._stateListeners = nil
    CanvasSortingBaseValue = CanvasSortingBaseValue - 1
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
        local canvas = gameObject.transform:Find("Canvas"):GetComponent(typeof(CS.UnityEngine.Canvas))
        if not canvas then
            log.error("[MvpContext] can't find canvas object in {}", self.view.class.__cname)
        end
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
    TaskFactory:createTask()
               :bind(bind(next, self.view))
               :delay(1)
               :start()
end

return M
