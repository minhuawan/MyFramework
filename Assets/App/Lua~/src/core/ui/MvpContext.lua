local type_binder = typeof(CS.MyFramework.Runtime.Services.Lua.LuaViewBinder)
local State = {
    Created = 1,
    Initialize = 2,
    Appear = 3,
    Disappear = 4,
    Dispose = 5,
}
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
    self._state = State.Created

    if not self.presenter then
        log.exception("presenter is nil value")
    end
end

function M:moveNextState()
    if self._state == State.Dispose then
        log.exception(
                "move next state error, state are disposed, presenter type {}",
                self.presenter.class.__cname
        )
    end
    self._state = self._state + 1
    if self._state == State.Initialize then
        self.presenter:initialize(self)
    elseif self._state == State.Appear then
        self.presenter:appear()
    elseif self._state == State.Disappear then
        self.presenter:disappear()
    elseif self._state == State.Dispose then
        self:onDispose()
    else
        log.exception("move next state error, state is {}", self._state)
    end
end

function M:onDispose()
    self.presenter:dispose()
    self.view.binder = nil
    CS.UnityEngine.Object.Destroy(self.view.gameObject)
    self.view.gameObject = nil
    self.view = nil
end

function M:createViewAsync(next)
    if not self.presenter then
        log.exception("createViewAsync error, presenter is nil")
    end
    local prefab = self.configuration.prefab
    log.assert(type(prefab) == "string" and prefab ~= "", "prefab is nil or empty")
    -- todo CS load

    ---@type View
    self.view = self.configuration.mvp.view.new()
    local gameObject
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