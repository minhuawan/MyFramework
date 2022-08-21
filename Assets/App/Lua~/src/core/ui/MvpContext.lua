local type_binder = typeof(CS.MyFramework.Runtime.Services.Lua.LuaViewBinder)
local MvpContextState = require("core.ui.MvpContextState")
---@class MvpContext
local M = class("MvpContext")

---@param configuration table
function M:ctor(configuration)
    if not configuration then
        log.exception("configuration is nil value")
    end
    self.configuration = configuration
    ---@type Presenter
    self.presenter = configuration.mvp.presenter.new()
    if configuration.mvp.model then
        self.model = configuration.mvp.model.new()
    end
    self._state = MvpContextState.Created

    if not self.presenter then
        log.exception("presenter is nil value")
    end
end

function M:getName()
    if self.configuration then
        return self.configuration.prefab or "NIL"
    end
    return "NIL"
end

function M:addStateChangeListener(l)
    log.verbose("addStateChangeListener")
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

function M:moveNextState(from)
    log.debug("moveNextState start from {}", from or "NIL")
    if self._state == MvpContextState.Dispose then
        log.exception(
                "move next state error, state are disposed, presenter type {}",
                self.presenter.class.__cname
        )
    end
    log.verbose("move next state, current {}, next {}", self._state, self._state + 1)
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
        log.exception("move next state error, state is {}", self._state)
    end
    if self._stateListeners then
        log.verbose("call state listener @{}, name:{}, state:{}, length: {}", tostring(self), self:getName(), self._state, #self._stateListeners)
        for _, l in ipairs(self._stateListeners) do
            l(self, self._state)
        end
    end
    log.debug("moveNextState end from {}", from or "NIL")
end

function M:canSwitch()
    return self._state >= MvpContextState.Appear
end

function M:dispose()
    self.presenter:dispose()
    self.view:dispose()
    self.view.binder = nil
    CS.UnityEngine.Object.Destroy(self.view.gameObject)
    self.view.gameObject = nil
    self.view = nil
    self._stateListeners = nil
end

function M:createViewAsync(next)
    if not self.presenter then
        log.exception("createViewAsync error, presenter is nil")
    end
    local prefab = self.configuration.prefab
    log.assert(type(prefab) == "string" and prefab ~= "", "prefab is nil or empty")
    -- todo CS load

    local unityObject = CS.MyFramework.Application.LoadGameObject(self.configuration.prefab)
    local gameObject = CS.UnityEngine.Object.Instantiate(unityObject)

    ---@type View
    self.view = self.configuration.mvp.view.new()
    gameObject:SetActive(false)
    self.view.gameObject = gameObject
    local binder = gameObject:GetComponent(type_binder)
    if binder then
        self.view.binder = binder
    else
        self.view.binder = nil
    end
    next(self.view)
end

return M